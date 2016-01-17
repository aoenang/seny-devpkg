package org.senydevpkg;

import android.app.Application;

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
 * Created by Seny on 2016/1/17.
 * <p/>
 * 工具包的初始化类
 */
public class DevPkg {
    /**
     * 全局的Application context
     */
    public static Application application;
    /**
     * 是否初始化过工具包
     */
    public static boolean isInit;


    /**
     * 初始化工具包
     *
     * @param application 全局的Context
     */
    public static void init(Application application) {
        if (!isInit) {
            DevPkg.application = application;
            isInit = true;
        }
    }

}
