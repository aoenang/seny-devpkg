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
    private static boolean mIsInit;


    /**
     * 初始化工具包
     *
     * @param application 全局的Context
     */
    public static void init(Application application) {
        DevPkg.application = application;
        mIsInit = true;
    }

    /**
     * 校验是否初始化过
     */
    public static void checkInit() {
        if (!mIsInit) {
            throw new IllegalStateException("Please invoke DevPkg.init(Application application) at Application.onCreate() !!");
        }
    }

}
