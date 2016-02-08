package org.senydevpkg.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.springframework.util.Assert;

/**
 * Toast 工具类.简化Toast的使用，并且不用关心线程的问题。
 * Toast的初始化会创建默认构造Handler。Handler默认构造会使用当前线程Looper。如果没有，抛异常。
 * 调用了Looper.prepare和Looper.loop以后Toast才可以生效。但是子线程调用了loop方法就阻塞了。
 * 所以选择抛到主线程执行。
 *
 * @author Seny
 */
public class MyToast {
    private static Toast mToast;

    /**
     * show a short toast
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        Assert.notNull(context, "Context cant be NULL!");
        safeShow(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * show a long toast
     *
     * @param context
     * @param text    string text
     */
    public static void showLong(Context context, String text) {
        Assert.notNull(context, "Context cant be NULL!");
        safeShow(context, text, Toast.LENGTH_LONG);
    }


    /**
     * show a short toast
     *
     * @param context
     * @param resId   string id
     */
    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    public static void showLong(Context context, int resId) {
        showLong(context, context.getString(resId));
    }


    /**
     * 安全弹出Toast。处理线程的问题。
     *
     * @param context
     * @param text
     * @param lengthShort
     */
    private static void safeShow(final Context context, final String text, final int lengthShort) {
        if (Looper.myLooper() != Looper.getMainLooper()) {//如果不是在主线程弹出吐司，那么抛到主线程弹
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            showToast(context, text, lengthShort);
                        }
                    }
            );
        } else {
            showToast(context, text, lengthShort);
        }
    }

    /**
     * 弹出Toast，处理单例的问题。
     *
     * @param context
     * @param text
     * @param lengthShort
     */
    private static void showToast(Context context, String text, int lengthShort) {
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(lengthShort);
        mToast.setText(text);
        mToast.show();
    }


}
