package com.qd.welfare.fragment.shop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.entity.CreateOrderInfo;
import com.qd.welfare.entity.GoodsDetailInfo;
import com.qd.welfare.utils.AppUtils;
import com.qd.welfare.utils.PriceUtil;
import com.qd.welfare.utils.SharedPreferencesUtil;
import com.qd.welfare.utils.ToastUtils;

public class ConfirmOrderPopupWindow extends PopupWindow {

    private Context context;

    private SelectableRoundedImageView goodsImage;
    private TextView goodsPrice;
    private TextView goodsName;
    private TextView number;
    private EditText receiverName;
    private EditText receiverPhone;
    private EditText receiverAddress;

    private OnClickConfirmPayListener onClickConfirmPayListener;

    private int payWayType = AppConfig.DEFAULT_PAY_WAY;
    private int buyNumber = 1;


    public ConfirmOrderPopupWindow(final Context context, final GoodsDetailInfo info) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.popupwindow_comfirm_order, null);

        goodsImage = mView.findViewById(R.id.goods_image);
        goodsName = mView.findViewById(R.id.goods_name);
        goodsPrice = mView.findViewById(R.id.goods_price);
        receiverName = mView.findViewById(R.id.receiver_name);
        receiverPhone = mView.findViewById(R.id.receiver_phone);
        receiverAddress = mView.findViewById(R.id.receiver_address);
        final RadioGroup radioGroup = mView.findViewById(R.id.radio_group);
        number = mView.findViewById(R.id.number);

        mView.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrderPopupWindow.this.dismiss();
            }
        });
        mView.findViewById(R.id.confirm_pay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String strReceiverName = receiverName.getText().toString().trim();
                if (TextUtils.isEmpty(strReceiverName)) {
                    ToastUtils.getInstance(context).showToast("请输入收货人");
                    return;
                }
                String strReceiverPhone = receiverPhone.getText().toString().trim();
                if (TextUtils.isEmpty(strReceiverPhone)) {
                    ToastUtils.getInstance(context).showToast("请输入联系电话");
                    return;
                }
                if (!AppUtils.isMobileNO(strReceiverPhone)) {
                    ToastUtils.getInstance(context).showToast("请输入正确的手机号");
                    return;
                }
                String strReceiverAddress = receiverAddress.getText().toString().trim();
                if (TextUtils.isEmpty(strReceiverAddress)) {
                    ToastUtils.getInstance(context).showToast("请输入收货地址");
                    return;
                }
                if (onClickConfirmPayListener != null) {
                    CreateOrderInfo createOrderInfo = new CreateOrderInfo(info.getId(), App.userInfo.getId(),
                            payWayType, buyNumber, strReceiverAddress, strReceiverName, strReceiverPhone);
                    onClickConfirmPayListener.onClickConfirmPay(createOrderInfo);
                }
            }
        });

        if (payWayType == AppConfig.PAY_TYPE_WECHAT) {
            radioGroup.check(R.id.type_wechat);
        } else {
            radioGroup.check(R.id.type_alipay);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payWayType = checkedId == R.id.type_wechat ? AppConfig.PAY_TYPE_WECHAT : AppConfig.PAY_TYPE_ALPAY;
            }
        });
        mView.findViewById(R.id.number_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNumber++;
                number.setText(String.valueOf(buyNumber));
            }
        });
        mView.findViewById(R.id.number_less).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyNumber > 1) {
                    buyNumber--;
                    number.setText(String.valueOf(buyNumber));
                }
            }
        });

        bindGoodsInfo(info);
        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //this.setHeight((int) (ScreenUtils.instance(context).getScreenHeight() * 1.0));
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_animation);
        //设置SelectPicPopupWindow弹出窗体的背景
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }

    public void setOnClickConfirmPayListener(OnClickConfirmPayListener onClickConfirmPayListener) {
        this.onClickConfirmPayListener = onClickConfirmPayListener;
    }

    private void bindGoodsInfo(GoodsDetailInfo info) {
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(goodsImage);
        goodsName.setText(info.getName());
        goodsPrice.setText("￥" + PriceUtil.getPrice(info.getPrice()));

        String strReceiverName = SharedPreferencesUtil.getString(context, "username", "");
        String strReceiverPhone = SharedPreferencesUtil.getString(context, "phone", "");
        String strReceiverAddress = SharedPreferencesUtil.getString(context, "address", "");

        receiverName.setText(strReceiverName);
        receiverPhone.setText(strReceiverPhone);
        receiverAddress.setText(strReceiverAddress);
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1.0f);
        super.dismiss();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.5f);
        super.showAtLocation(parent, gravity, x, y);
    }


    public interface OnClickConfirmPayListener {
        void onClickConfirmPay(CreateOrderInfo createOrderInfo);
    }
}