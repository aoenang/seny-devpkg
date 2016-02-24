package org.seny.android.sample.protocol;

import com.android.volley.Request;

import org.seny.android.sample.Constants;
import org.seny.android.sample.resp.NewsResponse;
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
 * <p/>http://api.baiyue.baidu.com/sn/api/getuserdata
 * pd=newsplus&sv=5910&did=774B771B3E8B365FCA12C19537331BA2&cuid=774B771B3E8B365FCA12C19537331BA2|000000000000000&ver=8&type=tag&bduss=&
 * <p/>
 * Created by Seny on 2016/2/22.
 */
public class NewsProtocol implements IProtocol {


    @Override
    public Request doRequest(HttpLoader loader, HttpLoader.HttpListener listener) {
        HttpParams paramsPost = new HttpParams();//设置参数
        paramsPost.put("pd", "newsplus")
                .put("sv", "5910")
                .put("did", "774B771B3E8B365FCA12C19537331BA2")
                .put("cuid", "774B771B3E8B365FCA12C19537331BA2|000000000000000")
                .put("ver", "8")
                .put("type", "tag");
        return loader.post(Constants.API_POST, paramsPost, NewsResponse.class, Constants.REQUEST_CODE_GETUSERDATA, listener);//发送POST请求并返回Request对象
    }
}
