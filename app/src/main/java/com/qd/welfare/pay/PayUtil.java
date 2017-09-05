package com.qd.welfare.pay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.entity.PayInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.LoadingDialog;

/**
 * 支付
 * Created by scene on 2017/9/5.
 */

public class PayUtil {

    public static void getPayInfo(final Context context, int vipType, int payType, int positionId, int dataId) {
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(context);
        loadingDialog.showLoadingDialog("正在获取支付信息...");
        HttpParams params = new HttpParams();
        params.put("vip_type", vipType);
        params.put("pay_type", payType);
        params.put("position_id", positionId);
        params.put("data_id", dataId);
        OkGo.<LzyResponse<PayInfo>>post(ApiUtil.API_PRE + ApiUtil.GET_PAY_INFO)
                .tag(ApiUtil.GET_PAY_INFO_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<PayInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayInfo>> response) {
                        try {
                            PayInfo payInfo = response.body().data;
                            if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_SCAN) {
                                //扫码

                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_SCAN) {
                                //公众号扫码

                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_CHANGE) {
                                //公众号跳转
                                Intent intent = new Intent(context, WechatPayActivity.class);
                                intent.putExtra(WechatPayActivity.WECHAT_PAY_URL, payInfo.getUrl());
                                context.startActivity(intent);
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_ALIPAY_SCAN) {
                                Intent intent = new Intent(context, AliPayActivity.class);
                                intent.putExtra(AliPayActivity.ALIPAY_URL, payInfo.getUrl());
                                context.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PayInfo>> response) {
                        super.onError(response);
                        try {
                            if (response.getException() != null && !TextUtils.isEmpty(response.getException().getMessage())) {
                                ToastUtils.getInstance(context).showToast(response.getException().getMessage());
                            } else {
                                ToastUtils.getInstance(context).showToast("支付信息获取失败请重试，或更换支付方式");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        try {
                            loadingDialog.cancelLoadingDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
