package org.senydevpkg.net.protocol;

import com.android.volley.Request;

import org.senydevpkg.net.HttpLoader;

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
 * <p/> 网络请求Bean。封装请求参数信息，提供请求方法。
 * Created by Seny on 2016/2/22.
 */
public interface IProtocol {

    /**
     * 封装参数,发起网络请求
     *
     * @param loader   发起请求的HttpLoader对象
     * @param listener 处理请求的监听
     * @return 返回 Request对象
     */
    Request doRequest(HttpLoader loader, HttpLoader.HttpListener listener);
}
