package com.qd.welfare.utils;

import android.content.Context;
import android.os.Looper;

import com.bumptech.glide.Glide;

import java.io.File;
import java.math.BigDecimal;

/**
 * Case By: Glide缓存
 * package:wiki.scene.shop.utils
 * Author：scene on 2017/6/30 14:41
 */
public class GlideCacheUtil {
    private static GlideCacheUtil instance;

    public static GlideCacheUtil getInstance() {
        if (null == instance) {
            instance = new GlideCacheUtil();
        }
        return instance;
    }

    // 获取Glide磁盘缓存大小
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(Glide.getPhotoCacheDir(context)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    public boolean cleanCacheDisk(Context context) {
        return deleteFolderFile(Glide.getPhotoCacheDir(context).getAbsolutePath(), true);
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    public boolean clearCacheDiskSelf(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清除Glide内存缓存
    public boolean clearCacheMemory(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 获取指定文件夹内所有文件大小的和
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 格式化单位
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    // 按目录删除文件夹文件方法
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}