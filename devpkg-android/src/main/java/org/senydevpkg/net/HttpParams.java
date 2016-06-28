package org.senydevpkg.net;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
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
 * Created by Seny on 2016/1/17.<p/>
 * 1. http request 参数的封装。可以是get，也可以是post
 * <p>
 * 2. http header参数的封装
 */
public class HttpParams {

    /**
     * 封装http request params
     */
    private final Map<String, String> mRequestParams = new HashMap<>();
    /**
     * 封装http headers
     */
    private final Map<String, String> mHeaderParams = new HashMap<>();

    /**
     * 获取request params中某个key对应的value
     *
     * @return 返回某个key对应的value
     */
    public String get(String key) {

        return mRequestParams.get(key);
    }


    /**
     * 设置一个key=value的http 参数
     *
     * @param key   参数的key
     * @param value 参数的value
     * @return 返回HttpParams本身，便于链式编程
     */
    public HttpParams put(String key, String value) {
        mRequestParams.put(key, value);
        return this;
    }


    /**
     * 获取request params中某个key对应的value
     *
     * @return 返回某个key对应的value
     */
    public String getHeader(String key) {

        return mHeaderParams.get(key);
    }


    /**
     * 设置一个key=value的http 参数
     *
     * @param key   参数的key
     * @param value 参数的value
     * @return 返回HttpParams本身，便于链式编程
     */
    public HttpParams addHeader(String key, String value) {
        mHeaderParams.put(key, value);
        return this;
    }

    /**
     * 返回一个get请求格式的字符串,如:?age=18&name=seny
     *
     * @return get请求的字符串结构
     */
    public String toGetParams() {

        StringBuilder buffer = new StringBuilder();
        if (!mRequestParams.isEmpty()) {
            buffer.append("?");
            for (Map.Entry<String, String> entry : mRequestParams.entrySet()) {
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
     * 返回封装了http request params的Map集合
     *
     * @return
     */
    public Map<String, String> getParams() {
        return mRequestParams;
    }

    /**
     * 返回封装了http headers的Map集合
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        return mHeaderParams;
    }
}
