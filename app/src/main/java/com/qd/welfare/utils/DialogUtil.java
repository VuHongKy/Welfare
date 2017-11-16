package com.qd.welfare.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.qd.welfare.LauncherActivity;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.fragment.theatre.TheatreFragment;
import com.qd.welfare.pay.OpenVipDialog;
import com.qd.welfare.pay.YouhuiOpenVipDialog;
import com.qd.welfare.widgets.OpenVipSuccessDialog;
import com.zhl.cbdialog.CBDialogBuilder;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * dialog相关
 * Created by scene on 2017/9/4.
 */

public class DialogUtil {

    public static void showOpenViewDialog(Context context, final int positionId, final int dataId) {
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle(context.getString(R.string.open_vip_notice))
                .setMessage("")
                .setCustomIcon(0)
                .setConfirmButtonText("确定")
                .setCancelButtonText("再看看")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case BUTTON_CONFIRM:
                                DialogUtil.showVipDialog(context, positionId, dataId);
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }

    public static void showOpenViewDialog(Context context, String message, final int positionId, final int dataId) {
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle(message)
                .setMessage("")
                .setCustomIcon(0)
                .setConfirmButtonText("确定")
                .setCancelButtonText("再看看")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case BUTTON_CONFIRM:
                                DialogUtil.showVipDialog(context, positionId, dataId);
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }

    public static void showOpenVipSuccess(final Activity activity) {
        final long openVipTime = System.currentTimeMillis();
        SharedPreferencesUtil.putLong(activity, "open_vip_time", openVipTime);
        cancelDialog();
        final OpenVipSuccessDialog.Builder builder = new OpenVipSuccessDialog.Builder(activity);
        builder.setOnClickButtonListener(new OpenVipSuccessDialog.OnClickButtonListener() {
            @Override
            public void onClickButton() {
                Intent intent = new Intent(activity, LauncherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                System.exit(0);
            }
        });
        OpenVipSuccessDialog dialog = builder.create();
        dialog.show();
        try {
            threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 1);
            scheduledFuture = threadPoolUtils.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                long leaveTime = TheatreFragment.WAIT_TIME - (System.currentTimeMillis() - openVipTime);
                                builder.getCountDownView().setText(DateFormatUtils.getHours(leaveTime));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 1, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static ThreadPoolUtils threadPoolUtils;
    static ScheduledFuture scheduledFuture;

    private void cancelDownTimer() {
        try {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            if (threadPoolUtils != null) {
                threadPoolUtils.shutDownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static OpenVipDialog dialog;
    private static YouhuiOpenVipDialog youhuiDialog;

    public static void showVipDialog(Context context, int positionId, int dataId) {
        MainActivity.upLoadPageInfo(PageConfig.OPEN_VIP, dataId);
        OpenVipDialog.Builder builder = new OpenVipDialog.Builder(context, positionId, dataId);
        dialog = builder.create();
        dialog.show();
    }

    public static void showYouhuiVipDialog(Context context, int positionId, int dataId) {
        MainActivity.upLoadPageInfo(PageConfig.OPEN_VIP_YOUHUI, dataId);
        YouhuiOpenVipDialog.Builder builder = new YouhuiOpenVipDialog.Builder(context, positionId, dataId);
        youhuiDialog = builder.create();
        youhuiDialog.show();
    }

    public static void cancelDialog() {
        try {
            if (dialog != null) {
                dialog.cancel();
            }
            if (youhuiDialog != null) {
                youhuiDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context, String message) {
        cancelDialog();
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle(message)
                .setMessage("")
                .setConfirmButtonText("确定")
                .setCancelButtonText("取消")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .create().show();
    }
}
