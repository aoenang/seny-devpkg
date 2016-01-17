package org.senydevpkg.utils;

import org.springframework.util.Assert;

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
 * Created by Seny on 2015/11/30.
 */
public class PrintUtil {

    public static String printList(List list) {
        Assert.notEmpty(list);
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ ");
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o != null) {
                buffer.append(o.toString()+" ,");
            }
        }

        buffer.append(" ]");

        return buffer.toString();
    }
}
