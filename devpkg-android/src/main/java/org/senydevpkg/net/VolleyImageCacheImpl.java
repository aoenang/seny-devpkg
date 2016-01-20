package org.senydevpkg.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import org.senydevpkg.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Volley图片缓存实现类
 * <p/>
 * 内存缓存LruCache+磁盘缓存DiskLruCache
 *
 * @author Seny
 */
public class VolleyImageCacheImpl implements ImageCache {

    //磁盘缓存大小
    private static final int DISK_MAX_SIZE = 10 * 1024 * 1024;//默认10M
    //缓存类
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;

    public VolleyImageCacheImpl(Context context) {
        // 获取应用可占内存的1/8作为缓存
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 实例化LruCache对象
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //测量bitmap的大小
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        try {
            // 获取DiskLruCahce对象
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context, "bitmaps"), getAppVersion(context), 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        File externalCacheDir = context.getExternalCacheDir();
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) && externalCacheDir != null) {
            cachePath = externalCacheDir.getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 从缓存（内存缓存，磁盘缓存）中获取Bitmap
     */
    @Override
    public Bitmap getBitmap(String url) {
        if (mLruCache.get(url) != null) {
            // 从LruCache缓存中取
            return mLruCache.get(url);
        } else {
            String key = MD5Utils.encode(url);
            try {
                if (mDiskLruCache != null && mDiskLruCache.get(key) != null) {
                    // 从DiskLruCahce取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if (snapshot != null) {
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        // 存入LruCache缓存
                        mLruCache.put(url, bitmap);
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // 存入LruCache缓存
        mLruCache.put(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Utils.encode(url);
        try {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}