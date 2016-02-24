package org.seny.android.sample.protocol;

import com.android.volley.Request;

import org.seny.android.sample.Constants;
import org.seny.android.sample.resp.WeatherResponse;
import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.protocol.IProtocol;

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
 * <p/>
 * Created by Seny on 2016/2/22.
 */
public class WeatherProtocol implements IProtocol {

    @Override
    public Request doRequest(HttpLoader loader, HttpLoader.HttpListener listener) {
        HttpParams paramsGet = new HttpParams()
                .put("city", "北京")
                .put("id", "0")
                .put("cuid", "774B771B3E8B365FCA12C19537331BA2|000000000000000");
        return loader.get(Constants.API_GET, paramsGet, WeatherResponse.class, Constants.REQUEST_CODE_WEATHER, listener);//发送Get请求并返回Request对象
    }
}
