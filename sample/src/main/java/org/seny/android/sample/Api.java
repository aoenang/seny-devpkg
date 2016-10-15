package org.seny.android.sample;

import android.content.Context;

import com.android.volley.Request;

import org.seny.android.sample.resp.NewsResponse;
import org.seny.android.sample.resp.WeatherResponse;
import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.springframework.util.Assert;

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
 * <p>定义app中的所有网络请求接口
 * Created by Seny on 2016/6/28.
 */
public class Api {

    private static Api sInstance;
    private HttpLoader mLoader;


    private Api(Context context) {
        this.mLoader = HttpLoader.getInstance(context);
    }

    private Api() {
    }

    /**
     * 返回单例对象
     *
     * @param context   上下文
     * @return  返回单例对象
     */
    public static synchronized Api getInstance(Context context) {
        if (sInstance == null) {
            Assert.notNull(context);
            sInstance = new Api(context);
        }
        return sInstance;
    }


    /**
     * 生成一个HttpParams 对象，默认包含通用参数信息
     *
     * @return  返回生成的HttpParams对象
     */
    public HttpParams generateHttpParams() {
        HttpParams params = new HttpParams();
        params.addHeader("myHeader1", "xxx");//根据实际需求，在这里初始化通用Header设置参数
        params.addHeader("myHeader2", "xxx");
        params.addHeader("myHeader3", "xxx");
        params.addHeader("myHeader4", "xxx");

        return params;
    }

    /**
     * 取消请求，提供给UI层
     *
     * @param tag 请求TAG
     */

    public void cancelRequest(Object tag) {
        if (mLoader != null) {
            mLoader.cancelRequest(tag);
        }
    }

    /**
     * Test:请求新闻
     *
     * @param listener  处理结果的监听器
     * @param tag  用来标识当前Request,用于{@link #cancelRequest(Object tag)}
     * @return  Request对象
     */
    public Request postNews(HttpLoader.HttpListener listener, Object tag) {
        HttpParams params = generateHttpParams();//设置参数
        params.put("pd", "newsplus")
                .put("sv", "5910")
                .put("did", "774B771B3E8B365FCA12C19537331BA2")
                .put("cuid", "774B771B3E8B365FCA12C19537331BA2|000000000000000")
                .put("ver", "8")
                .put("type", "tag");
        params.addHeader("xxx", "xxxx");//设置特定Header
        return mLoader.post(Constants.API_POST, params, NewsResponse.class, Constants.REQUEST_CODE_GETUSERDATA, listener).setTag(tag);//发送POST请求并返回Request对象
    }

    /**
     * Test:请求天气
     *
     * @param city  城市
     * @param listener   处理结果的监听器
     * @param tag   用来标识当前Request,用于{@link #cancelRequest(Object tag)}
     * @return  Request对象
     */
    public Request getWeather(String city, HttpLoader.HttpListener listener, Object tag) {
        HttpParams paramsGet = generateHttpParams()
                .put("city", city)
                .put("id", "0")
                .put("cuid", "774B771B3E8B365FCA12C19537331BA2|000000000000000");
        return mLoader.get(Constants.API_GET, paramsGet, WeatherResponse.class, Constants.REQUEST_CODE_WEATHER, listener,true).setTag(tag);//发送Get请求并返回Request对象
    }


}
