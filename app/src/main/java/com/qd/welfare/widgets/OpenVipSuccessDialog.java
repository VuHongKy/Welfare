package com.qd.welfare.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.qd.welfare.R;


public class OpenVipSuccessDialog extends Dialog {

    public OpenVipSuccessDialog(Context context) {
        super(context);
    }

    public OpenVipSuccessDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private TextView btn;
        private TextView countDownView;
        private OnClickButtonListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public void setOnClickButtonListener(OnClickButtonListener listener) {
            this.listener = listener;
        }

        public TextView getCountDownView() {
            return countDownView;
        }

        public OpenVipSuccessDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final OpenVipSuccessDialog dialog = new OpenVipSuccessDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_open_vip_success, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            //ViewUtils.setViewHeightByViewGroup(layout, (int) (PtrLocalDisplay.SCREEN_WIDTH_PIXELS * 0.9f));
            btn = (TextView) layout.findViewById(R.id.btn);
            countDownView = layout.findViewById(R.id.countDownView);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    if (listener != null) {
                        listener.onClickButton();
                    }
                }
            });
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

    public interface OnClickButtonListener {
        void onClickButton();
    }
}