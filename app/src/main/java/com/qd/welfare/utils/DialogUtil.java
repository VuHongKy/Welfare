package com.qd.welfare.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.pay.OpenVipDialog;
import com.zhl.cbdialog.CBDialogBuilder;

/**
 * Created by scene on 2017/9/4.
 */

public class DialogUtil {

    public static void showOpenViewDialog(Context context, final int positionId, final int dataId) {
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
                                DialogUtil.showVipDialog(context,positionId,dataId);
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }

    public static void showOpenVipSuccess(Context context) {
        cancelDialog();
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle("恭喜你成功开通会员")
                .setMessage("")
                .setConfirmButtonText("确定")
                .setCancelButtonText("取消")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .create().show();
    }

    private static OpenVipDialog dialog;

    public static void showVipDialog(Context context, int positionId, int dataId) {
        OpenVipDialog.Builder builder = new OpenVipDialog.Builder(context, positionId, dataId);
        dialog = builder.create();
        dialog.show();
    }

    public static void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }
}