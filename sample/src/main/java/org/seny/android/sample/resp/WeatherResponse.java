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
     * errno : 0
     * data : {"weather":[{"time":"周一(实时：8℃)","weather":"晴","temperature":"8 ~ -4℃","date":"2016-02-22","pm25":{"value":"","level":"","levelnum":""}},{"time":"周二","weather":"晴","temperature":"4 ~ -6℃","date":"2016-02-23"},{"time":"周三","weather":"晴转多云","temperature":"7 ~ -4℃","date":"2016-02-24"}],"image":"http://f.hiphotos.baidu.com/news/pic/item/a1ec08fa513d2697d296987357fbb2fb4316d853.jpg"}
     */

    public int errno;
    /**
     * weather : [{"time":"周一(实时：8℃)","weather":"晴","temperature":"8 ~ -4℃","date":"2016-02-22","pm25":{"value":"","level":"","levelnum":""}},{"time":"周二","weather":"晴","temperature":"4 ~ -6℃","date":"2016-02-23"},{"time":"周三","weather":"晴转多云","temperature":"7 ~ -4℃","date":"2016-02-24"}]
     * image : http://f.hiphotos.baidu.com/news/pic/item/a1ec08fa513d2697d296987357fbb2fb4316d853.jpg
     */

    public DataEntity data;

    public static class DataEntity {
        public String image;
        /**
         * time : 周一(实时：8℃)
         * weather : 晴
         * temperature : 8 ~ -4℃
         * date : 2016-02-22
         * pm25 : {"value":"","level":"","levelnum":""}
         */

        public List<WeatherEntity> weather;

        public static class WeatherEntity {
            public String time;
            public String weather;
            public String temperature;
            public String date;
            /**
             * value :
             * level :
             * levelnum :
             */

            public Pm25Entity pm25;

            public static class Pm25Entity {
                public String value;
                public String level;
                public String levelnum;
            }
        }
    }
}
