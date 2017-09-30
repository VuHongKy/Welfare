package com.qd.welfare.pay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.entity.PayInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.LoadingDialog;

import java.util.List;

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
                                WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, payInfo.getQr_url());
                                WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                                wxQRCodePayDialog.show();
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_SCAN) {
                                //公众号扫码
                                WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, payInfo.getQr_url());
                                WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                                wxQRCodePayDialog.show();
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_CHANGE) {
                                //公众号跳转
                                if (isWeixinAvilible(context)) {
                                    //打开微信
                                    Intent intent1 = new Intent();
                                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                    intent1.setAction(Intent.ACTION_MAIN);
                                    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.setComponent(cmp);
                                    context.startActivity(intent1);
                                    SystemClock.sleep(6000);
                                    //跳转支付
                                    Intent intent = new Intent(context, WechatPayActivity.class);
                                    intent.putExtra(WechatPayActivity.WECHAT_PAY_URL, payInfo.getUrl());
                                    context.startActivity(intent);
                                } else {
                                    ToastUtils.getInstance(context).showToast("请先安装微信");
                                }
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_ALIPAY_SCAN) {
                                Intent intent = new Intent(context, AliPayActivity.class);
                                intent.putExtra(AliPayActivity.ALIPAY_URL, payInfo.getUrl());
                                context.startActivity(intent);
                            }
                            App.isNeedCheckOrder = true;
                            App.orderIdInt = payInfo.getOrder_id();
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


    /**
     * 商品支付
     */
    public static void createOrder(final Context context, HttpParams params) {
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(context);
        loadingDialog.showLoadingDialog("正在创建订单...");
        OkGo.<LzyResponse<PayInfo>>post(ApiUtil.API_PRE + ApiUtil.CREATE_ORDER)
                .tag(ApiUtil.CREATE_ORDER_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<PayInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayInfo>> response) {
                        try {
                            PayInfo payInfo = response.body().data;
                            if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_SCAN) {
                                //扫码
                                WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, payInfo.getQr_url());
                                WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                                wxQRCodePayDialog.show();
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_SCAN) {
                                //公众号扫码
                                WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, payInfo.getQr_url());
                                WxQRCodePayDialog wxQRCodePayDialog = builder.create();
                                wxQRCodePayDialog.show();
                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_WX_GZH_CHANGE) {
                                //公众号跳转
                                if(isWeixinAvilible(context)){
                                    //打开微信
                                    Intent intent1 = new Intent();
                                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                    intent1.setAction(Intent.ACTION_MAIN);
                                    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.setComponent(cmp);
                                    context.startActivity(intent1);
                                    SystemClock.sleep(6000);
                                    Intent intent = new Intent(context, WechatPayActivity.class);
                                    intent.putExtra(WechatPayActivity.WECHAT_PAY_URL, payInfo.getUrl());
                                    context.startActivity(intent);
                                }else{
                                    ToastUtils.getInstance(context).showToast("请先安装微信");
                                }

                            } else if (payInfo.getApi_type() == AppConfig.API_TYPE_ALIPAY_SCAN) {
                                Intent intent = new Intent(context, AliPayActivity.class);
                                intent.putExtra(AliPayActivity.ALIPAY_URL, payInfo.getUrl());
                                context.startActivity(intent);
                            }
                            App.isNeedCheckGoodsOrder = true;
                            App.goodsOrderId = payInfo.getOrder_id();
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

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}
