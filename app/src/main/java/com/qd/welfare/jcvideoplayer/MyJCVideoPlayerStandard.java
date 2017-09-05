package com.qd.welfare.jcvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
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
}
