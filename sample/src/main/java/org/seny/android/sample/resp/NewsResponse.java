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
 * Created by Seny on 2016/2/24.
 */
public class NewsResponse implements IResponse {
    /**
     * errno : 0
     * data : {"tag":[{"dim":"","id":"","count":0,"type":"chosen","ishot":"","name":"精选"},{"dim":"","id":"478","count":0,"type":"news","ishot":"","name":"百家"},{"dim":"","id":"","count":0,"type":"local","ishot":"","name":"本地"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"科技"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"娱乐"},{"type":"image","name":"图片","id":"1"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"互联网"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"财经"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"体育"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"军事"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"社会"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"汽车"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"搞笑"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"国内"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"国际"},{"type":"radio","name":"电台","id":"1"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"生活"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"时尚"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"女人"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"游戏"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"教育"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"房产"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"创意"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"人文"},{"dim":"","id":"","count":0,"type":"info","ishot":"","name":"旅游"},{"dim":"1","id":"14773","count":0,"type":"senti","ishot":"0","name":"互联网+"}],"push":[]}
     */

    public int errno;
    public DataEntity data;

    public static class DataEntity {
        /**
         * dim :
         * id :
         * count : 0
         * type : chosen
         * ishot :
         * name : 精选
         */

        public List<TagEntity> tag;

        public static class TagEntity {
            public String dim;
            public String id;
            public int count;
            public String type;
            public String ishot;
            public String name;
        }
    }
}
