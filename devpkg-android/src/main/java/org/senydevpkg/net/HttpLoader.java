package org.senydevpkg.net;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.senydevpkg.net.resp.IResponse;
import org.senydevpkg.utils.ALog;
import org.senydevpkg.utils.MD5Utils;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
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
 * 网络请求核心类。负责封装get，post请求(GsonRequest)，支持添加自定义Request。初始化RequestQueue及ImageLoader
 */
public class HttpLoader {

    private static HttpLoader sInstance;
    /**
     * 保存ImageView上正在发起的网络请求
     */
    private final Map<ImageView, ImageLoader.ImageContainer> mImageContainers = new HashMap<>();
    /**
     * 过滤重复请求。保存当前正在消息队列中执行的Request.key为对应的requestCode.
     */
    private final HashMap<Integer, Request<?>> mInFlightRequests =
            new HashMap<>();
    /**
     * 消息队列，全局使用一个
     */
    private RequestQueue mRequestQueue;
    /**
     * 图片加载工具，自定义缓存机制
     */
    private ImageLoader mImageLoader;
    private Context mContext;

    private HttpLoader(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = new ImageLoader(mRequestQueue, new VolleyImageCacheImpl(mContext));
    }


    /**
     * 返回HttpLoader 单例对象
     *
     * @param context
     * @return
     */
    public static synchronized HttpLoader getInstance(Context context) {
        if (sInstance == null) {
            Assert.notNull(context);
            sInstance = new HttpLoader(context);
        }
        return sInstance;
    }


    /**
     * 添加一个请求到请求队列.支持任意Request
     *
     * @return 返回该Request，方便链式编程
     */
    public Request<?> addRequest(Request<?> request) {
        if (mRequestQueue != null && request != null) {
            mRequestQueue.add(request);
        }
        return request;

    }


    /**
     * 添加一个HttpLoader管理和创建的的Request请求到请求队列.
     *
     * @param requestCode 请求的唯一标识码
     * @return 返回该Request，方便链式编程
     */
    private Request<?> addRequest(Request<?> request, int requestCode) {
        if (mRequestQueue != null && request != null) {
            mRequestQueue.add(request);
            mInFlightRequests.put(requestCode, request);//添加到正在处理请求中
        }
        return request;

    }

    /**
     * 取消请求
     *
     * @param tag 请求TAG
     */

    public void cancelRequest(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);//从请求队列中取消对应的任务
        }
        //同时在mInFlightRequests删除保存所有TAG匹配的Request
        Iterator<Map.Entry<Integer, Request<?>>> it = mInFlightRequests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Request<?>> entry = it.next();
            Object rTag = entry.getValue().getTag();
            if (rTag != null && rTag.equals(tag)) {
                it.remove();
            }
        }
    }

    /**
     * 返回已初始化过的ImageLoader类。
     *
     * @return ImageLoader 对象
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 请求网络图片并设置给ImageView
     *
     * @param view       The imageView
     * @param requestUrl The URL of the image to be loaded.
     */
    public void display(ImageView view, String requestUrl) {
        display(view, requestUrl, 0, 0);
    }

    /**
     * 请求网络图片并设置给ImageView，可以设置默认显示图片和加载错误显示图片
     *
     * @param view              The imageView
     * @param requestUrl        The image url to request
     * @param defaultImageResId Default image resource ID to use, or 0 if it doesn't exist.
     * @param errorImageResId   Error image resource ID to use, or 0 if it doesn't exist.
     */
    public void display(ImageView view, String requestUrl, int defaultImageResId, int errorImageResId) {
        display(view, requestUrl, defaultImageResId, errorImageResId, view.getWidth(), view.getHeight(), ImageView.ScaleType.FIT_XY);
    }

    /**
     * 发起图片网络请求
     *
     * @param requestUrl The url of the remote image
     * @param maxWidth   The maximum width of the returned image.
     * @param maxHeight  The maximum height of the returned image.
     * @param scaleType  The ImageViews ScaleType used to calculate the needed image size.
     * @return A container object that contains all of the properties of the request, as well as
     * the currently available image (default if remote is not loaded).
     */
    public void display(final ImageView view, String requestUrl, final int defaultImageResId, final int errorImageResId, int maxWidth, int maxHeight, ImageView.ScaleType scaleType) {
        if (mImageContainers.containsKey(view)) {//如果已经在给该View请求一张网络图片
            mImageContainers.get(view).cancelRequest();//那么就把之前的取消掉，保证一个ImageView身上只有一个任务。
        }
        ImageLoader.ImageContainer imageContainer = mImageLoader.get(requestUrl, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(800).start();//渐变动画
                }
                mImageContainers.remove(view);//请求失败，移除
            }

            @Override
            public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(800).start();//渐变动画
                    mImageContainers.remove(view);//请求成功，移除
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        }, maxWidth, maxHeight, scaleType);
        mImageContainers.put(view, imageContainer);//将View身上的请求任务进行保存
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

    private Request<?> request(int method, String url, HttpParams params, Class<? extends IResponse> clazz, final int requestCode, final HttpListener listener, boolean isCache) {
        ALog.d("Request URL:" + url);
        Request request = mInFlightRequests.get(requestCode);
        if (request == null) {
            request = makeGsonRequest(method, url, params, clazz, requestCode, listener, isCache);
            //如果是GET请求，则首先尝试解析本地缓存供界面显示，然后再发起网络请求
            if (method == Request.Method.GET) {
                tryLoadCacheResponse(request, requestCode, listener);
            }
            ALog.i("Handle request by network!");
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
    public Request<?> get(String url, HttpParams params, Class<? extends IResponse> clazz, final int requestCode, final HttpListener listener) {
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
    public Request<?> get(String url, HttpParams params, Class<? extends IResponse> clazz, final int requestCode, final HttpListener listener, boolean isCache) {
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
    public Request<?> post(String url, HttpParams params, Class<? extends IResponse> clazz, final int requestCode, final HttpListener listener) {
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
    private GsonRequest<IResponse> makeGsonRequest(int method, String url, final HttpParams params, Class<? extends IResponse> clazz, int requestCode, HttpListener listener, boolean isCache) {
        ResponseListener responseListener = new ResponseListener(requestCode, listener);
        Map<String, String> paramsMap = null;//默认为null
        Map<String, String> headerMap = null;//默认为null
        if (params != null) {//如果有参数，则构建参数
            if (method == Request.Method.GET) {
                url = url + params.toGetParams();//如果是get请求，则把参数拼在url后面
            } else {
                paramsMap = params.getParams();//如果不是get请求，取出HttpParams中的Map参数集合。
            }
            headerMap = params.getHeaders();//获取设置的header信息
        }
        GsonRequest<IResponse> request = new GsonRequest<>(method, url, paramsMap, headerMap, clazz, responseListener, responseListener, isCache, mContext);
        request.setRetryPolicy(new DefaultRetryPolicy());//设置超时时间，重试次数，重试因子（1,1*2,2*2,4*2）等
        return request;
    }

    /**
     * 尝试从缓存中读取json数据
     *
     * @param request 要寻找缓存的request
     */
    private void tryLoadCacheResponse(Request request, int requestCode, HttpListener listener) {
        ALog.i("Try to  load cache response first !");
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
                    IResponse response = (IResponse) gr.gson.fromJson(sw.toString(), gr.getClazz());
                    //传给onResponse，让前面的人用缓存数据
                    listener.onGetResponseSuccess(requestCode, response);
                    ALog.i("Load cache response success !");
                }
            } catch (Exception e) {
                ALog.w("No cache response ! " + e.getMessage());
            }
        }

    }


    /**
     * 成功获取到服务器响应结果的监听，供UI层注册.
     */
    public interface HttpListener {
        /**
         * 当成功获取到服务器响应结果的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onGetResponseSuccess(int requestCode, IResponse response);

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
    private class ResponseListener implements Response.ErrorListener, Response.Listener<IResponse> {

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
            mInFlightRequests.remove(requestCode);//请求错误，从正在飞的集合中删除该请求
            if (listener != null) {
                listener.onGetResponseError(requestCode, error);
            }

        }


        @Override
        public void onResponse(IResponse response) {
            mInFlightRequests.remove(requestCode);//请求成功，从正在飞的集合中删除该请求
            if (response != null) {
                ALog.i("Request success from network!");
                if (listener != null) {
                    listener.onGetResponseSuccess(requestCode, response);
                }
            }
        }


    }
}
