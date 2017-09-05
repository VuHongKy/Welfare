package com.qd.welfare;

import android.os.Bundle;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.entity.DefaultPayTypeInfo;
import com.qd.welfare.entity.PayResultInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.LoadingDialog;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
        startUpLoad();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    public void onResume() {
        super.onResume();
        try {
            if (App.isNeedCheckOrder && App.orderIdInt != 0) {
                checkOrder();
            } else {
                App.isNeedCheckOrder = false;
                App.orderIdInt = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isWork = false;
        OkGo.getInstance().cancelTag(ApiUtil.UPLOAD_POSITION_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.UPLOAD_USER_INFO_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.ChECK_ORDER_GOODS_TAG);
        super.onDestroy();
    }

    private boolean isWork = true;

    /*
        Case By:上传使用信息每隔10s
        Author: scene on 2017/4/20 10:25
       */
    private void startUpLoad() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isWork) {
                        uploadUseInfo();
                        Thread.sleep(30000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    /**
     * 上报用户信息
     */
    private void uploadUseInfo() {
        OkGo.<LzyResponse<DefaultPayTypeInfo>>get(ApiUtil.API_PRE + ApiUtil.UPLOAD_USER_INFO)
                .tag(ApiUtil.UPLOAD_USER_INFO_TAG)
                .execute(new JsonCallback<LzyResponse<DefaultPayTypeInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<DefaultPayTypeInfo>> response) {
                        try {
                            AppConfig.DEFAULT_PAY_WAY = response.body().data.getDefault_pay_type();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 上传使用页面信息
     */
    public static void upLoadPageInfo(int positionId, int dataId) {
        HttpParams params = new HttpParams();
        params.put("position_id", positionId);
        if (dataId != 0) {
            params.put("data_id", dataId);
        }
        OkGo.<LzyResponse<String>>get(ApiUtil.API_PRE + ApiUtil.UPLOAD_POSITION).tag(ApiUtil.UPLOAD_POSITION_TAG).params(params).execute(new JsonCallback<LzyResponse<String>>() {
            @Override
            public void onSuccess(Response<LzyResponse<String>> response) {

            }
        });
    }

    private LoadingDialog loadingDialog;

    private void checkOrder() {
        loadingDialog = LoadingDialog.getInstance(MainActivity.this);
        loadingDialog.showLoadingDialog("正在获取支付结果...");
        HttpParams params = new HttpParams();
        params.put("order_id", App.orderIdInt);
        OkGo.<LzyResponse<PayResultInfo>>get(ApiUtil.API_PRE + ApiUtil.CHECK_ORDER_STATUS)
                .tag(ApiUtil.ChECK_ORDER_GOODS_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<PayResultInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayResultInfo>> response) {
                        try {
                            if (response.body().data.isPay_success()) {
                                DialogUtil.showOpenVipSuccess(MainActivity.this);
                            } else {
                                ToastUtils.getInstance(MainActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.getInstance(MainActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PayResultInfo>> response) {
                        super.onError(response);
                        try {
                            ToastUtils.getInstance(MainActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        try {
                            loadingDialog.cancelLoadingDialog();
                            App.isNeedCheckOrder = false;
                            App.orderIdInt = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
