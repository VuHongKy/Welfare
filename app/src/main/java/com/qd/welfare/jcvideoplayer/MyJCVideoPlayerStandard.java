package com.qd.welfare.jcvideoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.qd.welfare.event.ToastEvent;
import com.qd.welfare.fragment.video.VideoDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 自定义播放器
 * Created by scene on 2017/9/1.
 */

public class MyJCVideoPlayerStandard extends JCVideoPlayerStandard {

    public MyJCVideoPlayerStandard(Context context) {
        super(context);
        EventBus.getDefault().register(this);
    }

    public MyJCVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCurrentPositionListener(int position) {
        super.onCurrentPositionListener(position);
    }

    @Subscribe
    public void showNoticeToast(ToastEvent event) {
        try {
            toastContent.setText(event.message);
            toastContent.setVisibility(View.VISIBLE);
            toastContent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (getContext() instanceof VideoDetailActivity) {
                            ((VideoDetailActivity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toastContent.setVisibility(View.GONE);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
