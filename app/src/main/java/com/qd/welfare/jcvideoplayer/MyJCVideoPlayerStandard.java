package com.qd.welfare.jcvideoplayer;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.event.VideoOpenVipEvent;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * 自定义播放器
 * Created by scene on 2017/9/1.
 */

public class MyJCVideoPlayerStandard extends JCVideoPlayerStandard {

    public MyJCVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyJCVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onCurrentPositionListener(int position) {
        super.onCurrentPositionListener(position);
        Log.e("当前播放的时长：", "---------->" + position + "");
    }

    @Override
    public void onAutoCompletion() {
        if (App.userInfo.getRole() <= 1) {
            onEvent(JCUserAction.ON_CLICK_PAUSE);
            JCMediaManager.instance().mediaPlayer.pause();
            onStatePause();
            CBDialogBuilder builder = new CBDialogBuilder(getContext());
            TextView titleView = builder.getView(R.id.dialog_title);
            titleView.setSingleLine(false);
            builder.setTouchOutSideCancelable(false)
                    .showCancelButton(true)
                    .setTitle("非会员只能试看体验")
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
                                case BUTTON_CANCEL:
                                    backPress();
                                    dialog.cancel();
                                    EventBus.getDefault().post(new VideoOpenVipEvent());
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .create().show();
        } else {
            super.onAutoCompletion();
        }

    }

}
