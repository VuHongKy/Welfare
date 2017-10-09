package com.qd.welfare.jcvideoplayer;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.DanmuInfo;
import com.qd.welfare.event.VideoOpenVipEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.ToastUtils;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    public void init(Context context) {
        super.init(context);
        SAVE_PROGRESS = false;
        if (App.userInfo == null || App.userInfo.getRole() == 1) {
            getDanmuData();
            ToastUtils.getInstance(context).showLongToast("您当前非会员只能体验视频片段");
        }
    }

    private void getDanmuData() {
        OkGo.<LzyResponse<List<DanmuInfo>>>get(ApiUtil.API_PRE + ApiUtil.DANMU)
                .tag(ApiUtil.DANMU_TAG)
                .execute(new JsonCallback<LzyResponse<List<DanmuInfo>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<DanmuInfo>>> response) {
                        try {
                            addDanmakus(response.body().data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onAutoCompletion() {
        if (danMuView != null) {
            danMuView.hideAllDanMuView(true);
        }
        if (App.userInfo == null || App.userInfo.getRole() <= 1) {
            onEvent(JCUserAction.ON_CLICK_PAUSE);
            JCMediaManager.instance().mediaPlayer.pause();
            onStatePause();
            CBDialogBuilder builder = new CBDialogBuilder(getContext());
            TextView titleView = builder.getView(R.id.dialog_title);
            titleView.setSingleLine(false);
            builder.setTouchOutSideCancelable(false)
                    .showCancelButton(true)
                    .setTitle("非会员只能试看，请开通会员继续观看")
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
