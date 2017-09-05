package com.qd.welfare.pay;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.utils.ViewUtils;

import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * Case By:开通VIP
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class OpenVipDialog extends Dialog {
    public OpenVipDialog(@NonNull Context context) {
        super(context);
    }

    public OpenVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected OpenVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (PtrLocalDisplay.SCREEN_WIDTH_PIXELS * 0.95f); // 宽度
        dialogWindow.setAttributes(lp);
        super.show();
    }

    public static class Builder {
        private Context context;

        private RelativeLayout layoutYear;
        private RelativeLayout layoutMonth;
        private ImageView rdYear;
        private ImageView rdMonth;
        private RadioGroup rg;
        private TextView pay;


        private int dataId;
        private int positionId;

        private int payWayType = AppConfig.DEFAULT_PAY_WAY;
        private int vipType = AppConfig.VIP_YEAR;

        public Builder(Context context, int positionId, int dataId) {
            this.context = context;
            this.positionId = positionId;
            this.dataId = dataId;
        }

        public OpenVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final OpenVipDialog dialog = new OpenVipDialog(context, R.style.Dialog);
            View view = inflater.inflate(R.layout.dialog_open_vip, null);
            dialog.addContentView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutYear = view.findViewById(R.id.layout_year);
            layoutMonth = view.findViewById(R.id.layout_month);
            rdYear = view.findViewById(R.id.rd_year);
            rdMonth = view.findViewById(R.id.rd_month);
            rg = view.findViewById(R.id.rg);
            pay = view.findViewById(R.id.pay);

            if (payWayType == AppConfig.PAY_TYPE_WECHAT) {
                rg.check(R.id.rd_pay_wechat);
            } else if (payWayType == AppConfig.PAY_TYPE_ALPAY) {
                rg.check(R.id.rd_pay_alipay);
            }

            layoutYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rdMonth.setBackgroundResource(R.drawable.ic_rd_d);
                    rdYear.setBackgroundResource(R.drawable.ic_rd_s);
                    vipType = AppConfig.VIP_YEAR;
                }
            });

            layoutMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rdMonth.setBackgroundResource(R.drawable.ic_rd_s);
                    rdYear.setBackgroundResource(R.drawable.ic_rd_d);
                    vipType = AppConfig.VIP_MONTH;
                }
            });

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                    if (checkId == R.id.rd_pay_wechat) {
                        payWayType = AppConfig.PAY_TYPE_WECHAT;
                    } else if (checkId == R.id.rd_pay_alipay) {
                        payWayType = AppConfig.PAY_TYPE_ALPAY;
                    }
                }
            });

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayUtil.getPayInfo(context, vipType, payWayType, positionId, dataId);
                }
            });

            ViewUtils.setViewHeightByViewGroup(view, (int) (PtrLocalDisplay.SCREEN_HEIGHT_PIXELS * 0.9f));
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
