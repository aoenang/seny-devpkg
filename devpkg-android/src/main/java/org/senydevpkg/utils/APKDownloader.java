package org.senydevpkg.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import org.springframework.util.Assert;

import java.util.ArrayList;
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
 * Created by Seny on 2015/12/2.
 * <p/>
 * APK下载器，调用系统DownloadManager发起下载任务。下载成功自动发起安装请求。
 */
public class APKDownloader {


    /**
     * 下载完成的广播接收者
     */
    protected static DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
    private static APKDownloader mInstance;
    /**
     * 保存已下载的任务
     */
    private static List<Long> sIds = new ArrayList<>();
    /**
     * 系统下载服务
     */
    private static DownloadManager mDownloadManager;


    private APKDownloader() {
    }

    /**
     * 获取APKDownloader实例，同时注册DOWNLOAD_COMPLETE
     *
     * @return APKDownloader对象
     */
    public static APKDownloader getInstance(Context context) {
        Assert.notNull(context);
        if (mInstance == null) {
            mInstance = new APKDownloader();
            mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            IntentFilter intentfilter = new IntentFilter();
            intentfilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
            context.registerReceiver(receiver, intentfilter);
        }
        return mInstance;
    }


    /**
     * 执行下载任务
     *
     * @param apkUri  apk的下载地址
     * @param apkName 要保存的apk名字
     * @return 下载任务的ID
     */
    public long downloadAPK(String apkUri, String apkName) {
        Assert.notNull(apkUri);
        Assert.notNull(apkName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUri));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        long id = mDownloadManager.enqueue(request);
        sIds.add(id);
        return id;
    }

    /*
     * 接受下载完成后的intent
     */
    static class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (sIds.contains(id)) {//如果本应用发起的下载请求完成了才发起安装请求
                sIds.remove(id);//删除已保存的ID
                ALog.d(intent.toString());
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = mDownloadManager.getUriForDownloadedFile(id);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);

            }
        }
    }
}
