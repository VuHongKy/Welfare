package com.qd.welfare.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import com.qd.welfare.R;
import com.qd.welfare.utils.ViewUtils;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import wiki.scene.loadmore.utils.PtrLocalDisplay;


public class DownLoadDialog extends Dialog {

    public DownLoadDialog(Context context) {
        super(context);
    }

    public DownLoadDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private MaterialProgressBar progressBar;

        public Builder(Context context) {
            this.context = context;
        }

        public MaterialProgressBar getProgressBar() {
            return progressBar;
        }

        public DownLoadDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final DownLoadDialog dialog = new DownLoadDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_download, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ViewUtils.setViewHeightByViewGroup(layout, (int) (PtrLocalDisplay.SCREEN_WIDTH_PIXELS * 0.9f));
            progressBar = (MaterialProgressBar) layout.findViewById(R.id.progressBar);
            progressBar.setProgress(0);
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}