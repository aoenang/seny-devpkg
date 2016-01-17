package org.senydevpkg.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 创建一个简单的异步的任务 去执行异步的操作.
 *
 * @author Seny
 */
public abstract class SimpleAsyncTask {


    private final MyHandler mHandler = new MyHandler(this);

    /**
     * 耗时任务执行之前调用的方法，可选重写
     */
    public void onPreExecute() {

    }

    /**
     * 耗时任务执行后调用的方法. 可选重写
     */
    public void onPostExecute() {

    }

    /**
     * 在后台运行的一个耗时的任务.
     */
    public abstract void doInBackground();

    /**
     * 开启一个异步的任务.
     */
    public void execute() {
        onPreExecute();
        new Thread() {
            public void run() {
                doInBackground();
                mHandler.sendEmptyMessage(0);
            }

        }.start();

    }


    /**
     * safe handler
     */
    static class MyHandler extends Handler {

        private WeakReference<SimpleAsyncTask> mTarget;

        public MyHandler(SimpleAsyncTask target) {

            this.mTarget = new WeakReference<>(target);
        }


        public void handleMessage(Message msg) {
            SimpleAsyncTask instance = mTarget.get();
            if (instance != null) {
                instance.onPostExecute();
            }
        }
    }
}
