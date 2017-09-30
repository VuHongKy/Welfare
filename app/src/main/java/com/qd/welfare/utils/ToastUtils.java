package com.qd.welfare.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * FileName:com.scene.baseframe.util.ToastUtils.java
 * 功能描述：防止Toast重复显示
 * author: scene
 * date: 2016-07-19 17:05
 */
public class ToastUtils {

    private static ToastUtils singleton;  //静态变量
    private Context mContext;

    private Toast mToast;


    private ToastUtils(Context context) {
        this.mContext = context;
    }

    public static ToastUtils getInstance(Context context) {
        if (singleton == null) {  //第一层校验
            synchronized (ToastUtils.class) {
                if (singleton == null) {  //第二层校验
                    singleton = new ToastUtils(context);
                }
            }
        }
        return singleton;
    }

    public void showToast(String text) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showLongToast(String text) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_LONG);
            }
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showToast(int resId) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
