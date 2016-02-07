package org.seny.android.sample.resp;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

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
 * 测试接口：http://mobile.weather.com.cn/data/zsM/101010100.html?_=1381891661502
 * Created by Seny on 2016/2/7.
 */
public class WeatherResponse implements IResponse {


    /**
     * date : 20131012
     * zs : [{"name":"空调开启指数","hint":"较少开启","des":"您将感到很舒适，一般不需要开启空调。"},{"name":"息斯敏过敏指数","hint":"不易发","des":"天气条件不易诱发过敏，可放心外出，除特殊体质外，无需担心过敏问题。"},{"name":"晨练指数","hint":"适宜","des":"天气不错，空气清新，是您晨练的大好时机，建议不同年龄段的人们积极参加户外健身活动。"},{"name":"旅游指数","hint":"适宜","des":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"},{"name":"紫外线强度指数","hint":"弱","des":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"}]
     */

    public String date;
    /**
     * name : 空调开启指数
     * hint : 较少开启
     * des : 您将感到很舒适，一般不需要开启空调。
     */

    public List<ZsEntity> zs;

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "date='" + date + '\'' +
                ", zs=" + zs +
                '}';
    }

    public static class ZsEntity {
        public String name;
        public String hint;
        public String des;

        @Override
        public String toString() {
            return "ZsEntity{" +
                    "name='" + name + '\'' +
                    ", hint='" + hint + '\'' +
                    ", des='" + des + '\'' +
                    '}';
        }
    }
}
