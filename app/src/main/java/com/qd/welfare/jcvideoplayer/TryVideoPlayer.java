package com.qd.welfare.jcvideoplayer;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.DanmuInfo;
import com.qd.welfare.event.ToastEvent;
import com.qd.welfare.event.VideoOpenVipEvent;
import com.qd.welfare.fragment.video.VideoDetailActivity;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.ToastUtils;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 试看使用这个来播放
 * Created by scene on 2017/10/9.
 */

public class TryVideoPlayer extends JCVideoPlayerStandard {
    public TryVideoPlayer(Context context) {
        super(context);
        EventBus.getDefault().register(this);
    }

    public TryVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        return false;
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onEvent(JCUserActionStandard.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.bottom_seek_progress) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
                    mTouchingProgressBar = false;

                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    mChangeBrightness = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                        if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                                cancelProgressTimer();
                                if (absDeltaX >= THRESHOLD) {
                                    // 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
                                    // 否则会因为mediaplayer的状态非法导致App Crash
                                    if (currentState != CURRENT_STATE_ERROR) {
                                        mChangePosition = false;
                                        mGestureDownPosition = getCurrentPositionWhenPlaying();
                                    }
                                } else {
                                    //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                                    if (mDownX < mScreenWidth * 0.5f) {//左侧改变亮度
                                        mChangeBrightness = true;
                                        WindowManager.LayoutParams lp = JCUtils.getAppCompActivity(getContext()).getWindow().getAttributes();
                                        if (lp.screenBrightness < 0) {
                                            try {
                                                mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                                                Log.i(TAG, "current system brightness: " + mGestureDownBrightness);
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mGestureDownBrightness = lp.screenBrightness * 255;
                                            Log.i(TAG, "current activity brightness: " + mGestureDownBrightness);
                                        }
                                    } else {//右侧改变声音
                                        mChangeVolume = true;
                                        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    }
                                }
                            }
                        }
                    }
                    if (mChangePosition) {
                        return false;
                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        //dialog中显示百分比
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                        showVolumeDialog(-deltaY, volumePercent);
                    }

                    if (mChangeBrightness) {
                        deltaY = -deltaY;
                        int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
                        WindowManager.LayoutParams params = JCUtils.getAppCompActivity(getContext()).getWindow().getAttributes();
                        if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
                            params.screenBrightness = 1;
                        } else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
                            params.screenBrightness = 0.01f;
                        } else {
                            params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
                        }
                        JCUtils.getAppCompActivity(getContext()).getWindow().setAttributes(params);
                        //dialog中显示百分比
                        int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
                        showBrightnessDialog(brightnessPercent);
//                        mDownY = y;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
                    mTouchingProgressBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    dismissBrightnessDialog();
                    if (mChangePosition) {
                        return false;
                    }
                    if (mChangeVolume) {
                        onEvent(JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME);
                    }
                    startProgressTimer();
                    break;
            }
        }
        return false;
    }

    @Override
    public void init(final Context context) {
        super.init(context);
        SAVE_PROGRESS = false;
        if (App.userInfo == null || App.userInfo.getRole() == 1) {
            getDanmuData();
            ToastUtils.getInstance(context).showLongToast("您当前非会员只能体验视频片段");
        }
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
                ToastUtils.getInstance(context).showToast("该功能为会员专享，请先开通会员");
            }
        });
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
        try {


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
                                        EventBus.getDefault().post(new VideoOpenVipEvent());
                                        backPress();
                                        dialog.cancel();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
        progressBar.setProgress(progress / 20);
    }

    @Subscribe
    public void showNoticeToast(ToastEvent event) {
        try {
            toastContent.setText(event.message);
            toastContent.setVisibility(View.VISIBLE);
            toastContent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getContext() instanceof VideoDetailActivity)
                        ((VideoDetailActivity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    toastContent.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
