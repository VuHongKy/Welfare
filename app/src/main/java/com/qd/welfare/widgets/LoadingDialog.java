package com.qd.welfare.widgets;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.zhl.cbdialog.CBDialogBuilder;

/**
 * Case By:加载对话框
 * package:wiki.scene.shop.widgets
 * Author：scene on 2017/7/17 10:34
 */

public class LoadingDialog {
    private Context context;
    private Dialog dialog;
    private CBDialogBuilder dialogBuilder;

    private static LoadingDialog instance;

    private LoadingDialog(Context context) {
        this.context = context;
    }

    public static LoadingDialog getInstance(Context context) {    //对获取实例的方法进行同步
        instance = new LoadingDialog(context);
        return instance;
    }

    public void showLoadingDialog(String message) {
        cancelLoadingDialog();
        if (dialogBuilder == null) {
            dialogBuilder = new CBDialogBuilder(context, CBDialogBuilder.DIALOG_STYLE_PROGRESS, 0.5f)
                    .showCancelButton(false)
                    .showConfirmButton(false)
                    .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                    .setOnProgressOutTimeListener(10000, new CBDialogBuilder.onProgressOutTimeListener() {
                        @Override
                        public void onProgressOutTime(Dialog dialog, TextView dialogMsgTextView) {
                            cancelLoadingDialog();
                        }
                    });
        }
        dialogBuilder.setMessage(message);
        if (dialog == null) {
            dialog = dialogBuilder.create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancelLoadingDialog() {
        if (dialog != null && dialogBuilder != null && dialog.isShowing()) {
            dialog.cancel();
//            dialog = null;
//            dialogBuilder = null;
        }
    }
}
