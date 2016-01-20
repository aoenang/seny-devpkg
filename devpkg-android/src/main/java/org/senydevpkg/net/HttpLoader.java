package org.senydevpkg.net;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.senydevpkg.net.req.IReq;
import org.senydevpkg.net.resp.ErrorResp;
import org.senydevpkg.net.resp.IResp;
import org.senydevpkg.utils.ALog;
import org.senydevpkg.utils.MD5Utils;
import org.senydevpkg.utils.MyToast;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the XYY protecting━━━
 * <p>
 * Created by Seny on 2015/12/1.
 * <p>
 * 网络请求核心类。负责封装get，set请求，初始化RequestQueue及ImageLoader
 */
public class HttpLoader {

    /**
     * 过滤重复请求。保存当前正在消息队列中执行的Request.key为对应的requestCode.
     */
    private static final HashMap<Integer, Request> sInFlightRequests =
            new HashMap<>();
    private static HttpLoader mInstance;
    /**
     * 消息队列，全局使用一个
     */
    private RequestQueue sRequestQueue;
    /**
     * 图片加载工具，自定义缓存机制
     */
    private ImageLoader sImageLoader;
    private Context mContext;

    private HttpLoader(Context context) {
        mContext = context;
        sRequestQueue = Volley.newRequestQueue(context);
        sImageLoader = new ImageLoader(sRequestQueue, new VolleyImageCacheImpl(context));
    }


    /**
     * 返回HttpLoader 单例对象
     *
     * @param context
     * @return
     */
    public static synchronized HttpLoader getInstance(Context context) {
        if (mInstance == null) {
            Assert.notNull(context);
            mInstance = new HttpLoader(context);
        }
        return mInstance;
    }

    /**
     * 添加一个请求到请求队列
     *
     * @param requestCode 请求的唯一标识码
     * @return 返回该Request，方便链式编程
     */
    public Request addRequest(Request<?> request, int requestCode) {
        if (sRequestQueue != null && request != null) {
            sRequestQueue.add(request);
        }
        sInFlightRequests.put(requestCode, request);
        return request;//添加到正在处理请求中
    }

    /**
     * 取消请求
     *
     * @param tag 请求TAG
     */

    public void cancelRequest(Object tag) {
        if (sRequestQueue != null) {
            sRequestQueue.cancelAll(tag);//从请求队列中取消对应的任务
        }
        //同时在mInFlightRequests删除保存所有TAG匹配的Request
        Iterator<Map.Entry<Integer, Request>> it = sInFlightRequests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Request> entry = it.next();
            Object rTag = entry.getValue().getTag();
            if (rTag != null && rTag.equals(tag)) {
                it.remove();
            }
        }
    }

    /**
     * 获取一个ImageLoader对象用于加载图片
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        return sImageLoader;
    }

    /**
     * 从Map集合中构建一个get请求参数字符串
     *
     * @param param get请求map集合
     * @return get请求的字符串结构
     */
    private String buildGetParam(Map<String, String> param) {

        StringBuilder buffer = new StringBuilder();
        if (param != null) {
            buffer.append("?");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    buffer.append(URLEncoder.encode(key, "UTF-8"));
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(value, "UTF-8"));
                    buffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        String str = buffer.toString();
        //去掉最后的&
        if (str.length() > 1 && str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 发送GsonRequest请求
     *
     * @param method      请求方式
     * @param url         请求地址
     * @param params      请求参数,可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果
     */

    private Request request(int method, String url, IReq params, Class<? extends IResp> clazz, final int requestCode, final HttpListener listener, boolean isCache) {
        Request request = sInFlightRequests.get(requestCode);
        if (request == null) {
            request = makeGsonRequest(method, url, params, clazz, requestCode, listener, isCache);
            //如果是GET请求，则首先尝试解析本地缓存供界面显示，然后再发起网络请求
            if (method == Request.Method.GET) {
                tryLoadCacheResponse(request, requestCode, listener);
            }
            ALog.d("Handle request by network!");
            return addRequest(request, requestCode);
        } else {
            ALog.i("Hi guy,the request (RequestCode is " + requestCode + ")  is already in-flight , So Ignore!");
            return request;
        }
    }

    /**
     * 发送get方式的GsonRequest请求,默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public Request get(String url, IReq params, Class<? extends IResp> clazz, final int requestCode, final HttpListener listener) {
        return request(Request.Method.GET, url, params, clazz, requestCode, listener, true);
    }

    /**
     * 发送get方式的GsonRequest请求
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public Request get(String url, IReq params, Class<? extends IResp> clazz, final int requestCode, final HttpListener listener, boolean isCache) {
        return request(Request.Method.GET, url, params, clazz, requestCode, listener, isCache);
    }

    /**
     * 发送post方式的GsonRequest请求，默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public Request post(String url, IReq params, Class<? extends IResp> clazz, final int requestCode, final HttpListener listener) {
        return request(Request.Method.POST, url, params, clazz, requestCode, listener, false);//POST请求不缓存
    }

    /**
     * 初始化一个GsonRequest
     *
     * @param method      请求方法
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param clazz       Clazz类型，用于GSON解析json字符串封装数据
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    监听器用来响应结果
     * @return 返回一个GsonRequest对象
     */
    private GsonRequest makeGsonRequest(int method, String url, IReq params, Class<? extends IResp> clazz, int requestCode, HttpListener listener, boolean isCache) {
        ResponseListener responseListener = new ResponseListener(requestCode, listener);
        Map<String, String> paramsMap = null;//默认为null
        if (params != null) {//如果有参数，则构建参数
            if (method == Request.Method.GET) {
                url = url + buildGetParam(params.getParams());//如果是get请求，则把参数拼在url后面
            } else {
                paramsMap = params.getParams();//如果不是get请求，取出IReq中的Map参数集合。
            }
        }
        GsonRequest request = new GsonRequest<>(method, url, paramsMap, clazz, responseListener, responseListener, isCache, mContext);
        request.setRetryPolicy(new DefaultRetryPolicy());//设置超时时间，重试次数，重试因子（1,1*2,2*2,4*2）等
        return request;
    }

    /**
     * 尝试从缓存中读取json数据
     *
     * @param request 要寻找缓存的request
     */
    private void tryLoadCacheResponse(Request request, int requestCode, HttpListener listener) {
        ALog.d("Try to  load cache response first !");
        if (listener != null && request != null) {
            try {
                //获取缓存文件
                File cacheFile = new File(mContext.getCacheDir(), "" + MD5Utils.encode(request.getUrl()));
                StringWriter sw = new StringWriter();
                //读取缓存文件
                FileCopyUtils.copy(new FileReader(cacheFile), sw);
                if (request instanceof GsonRequest) {
                    //如果是GsonRequest，那么解析出本地缓存的json数据为GsonRequest
                    GsonRequest gr = (GsonRequest) request;
                    IResp response = (IResp) gr.gson.fromJson(sw.toString(), gr.getClazz());
                    //传给onResponse，让前面的人用缓存数据
                    listener.onGetResponseSuccess(requestCode, response);
                    ALog.d("Load cache response success !");
                }
            } catch (Exception e) {
                ALog.w("No cache response ! " + e.getMessage());
            }
        }

    }


    /**
     * 成功获取到服务器响应结果的监听，供UI层注册
     */
    public interface HttpListener {
        /**
         * 当成功获取到服务器响应结果的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onGetResponseSuccess(int requestCode, IResp response);

        /**
         * 网络请求失败，做一些释放性的操作，比如关闭对话框
         *
         * @param requestCode 请求码
         * @param error       异常详情
         */
        void onGetResponseError(int requestCode, VolleyError error);
    }

    /**
     * ResponseListener，封装了Volley错误和成功的回调监，并执行一些默认处理，同时会将事件通过HttpListener抛到UI层
     */
    private class ResponseListener implements Response.ErrorListener, Response.Listener<IResp> {

        private HttpListener listener;
        private int requestCode;

        public ResponseListener(int requestCode, HttpListener listener) {
            this.requestCode = requestCode;
            this.listener = listener;
        }


        @Override
        public void onErrorResponse(VolleyError error) {
            ALog.w("Request error from network!");
            error.printStackTrace();
            sInFlightRequests.remove(requestCode);//请求错误，从正在飞的集合中删除该请求
            if (listener != null) {
                listener.onGetResponseError(requestCode, error);
            }

        }


        @Override
        public void onResponse(IResp response) {
            sInFlightRequests.remove(requestCode);//请求成功，从正在飞的集合中删除该请求
            if (response != null) {
                //执行通用处理，如果是服务器返回的ErrorResponse，直接提示错误信息并返回
                if (response instanceof ErrorResp) {
                    ErrorResp errorRes = (ErrorResp) response;
                    MyToast.show(mContext, errorRes.getErrorMsg());
                    return;
                }

                ALog.i("Request success from network!");
                if (listener != null) {
                    listener.onGetResponseSuccess(requestCode, response);
                }
            }
        }


    }
}
