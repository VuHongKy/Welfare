package com.qd.welfare.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.qd.welfare.R;
import com.zhl.cbdialog.CBDialogBuilder;

/**
 * Created by scene on 2017/9/4.
 */

public class DialogUtil {

    public static void showOpenViewDialog(Context context) {
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(true)
                .setTitle(context.getString(R.string.open_vip_notice))
                .setMessage("")
                .setCustomIcon(0)
                .setConfirmButtonText("确定")
                .setCancelButtonText("取消")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case BUTTON_CONFIRM:
                                ToastUtils.getInstance(context).showToast("点击了确定按钮");
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }
}
