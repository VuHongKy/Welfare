package com.qd.welfare;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.entity.DefaultPayTypeInfo;
import com.qd.welfare.entity.PayResultInfo;
import com.qd.welfare.entity.UpdateVersionInfo;
import com.qd.welfare.event.OpenVipSuccessEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.service.ChatHeadService;
import com.qd.welfare.utils.AppUtils;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.DownLoadDialog;
import com.qd.welfare.widgets.LoadingDialog;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

public class MainActivity extends SupportActivity {
    private final Handler mHandler = new MyHandler(this);
    private Toast toast;
    private TextView toastContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
        startUpLoad();
        getUpdateVersion(false);
        startService(new Intent(MainActivity.this, ChatHeadService.class));

        Timer mTimer = new Timer();
        mTimer.schedule(timerTask, 30 * 1000, 30 * 1000);
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

            if (App.isNeedCheckGoodsOrder && App.goodsOrderId != 0) {
                checkGoodsOrder();
            } else {
                App.isNeedCheckGoodsOrder = false;
                App.goodsOrderId = 0;
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
        OkGo.getInstance().cancelTag(ApiUtil.UPDATE_VERSION_TAG);
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
                                App.userInfo.setRole(response.body().data.getRole());
                                EventBus.getDefault().post(new OpenVipSuccessEvent());
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

    private void checkGoodsOrder() {
        loadingDialog = LoadingDialog.getInstance(MainActivity.this);
        loadingDialog.showLoadingDialog("正在获取支付结果...");
        HttpParams params = new HttpParams();
        params.put("order_id", App.goodsOrderId);
        params.put("user_id", App.userInfo.getId());
        OkGo.<LzyResponse<PayResultInfo>>get(ApiUtil.API_PRE + ApiUtil.CHECK_GOODS_ORDER)
                .tag(ApiUtil.CHECK_GOODS_ORDER_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<PayResultInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PayResultInfo>> response) {
                        try {
                            if (response.body().data.isPay_success()) {
                                DialogUtil.showDialog(MainActivity.this, "恭喜您成功购买商品");
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
                            App.isNeedCheckGoodsOrder = false;
                            App.goodsOrderId = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getUpdateVersion(final boolean isMine) {
        OkGo.<LzyResponse<UpdateVersionInfo>>get(ApiUtil.API_PRE + ApiUtil.UPDATE_VERSION)
                .tag(ApiUtil.UPDATE_VERSION_TAG)
                .execute(new JsonCallback<LzyResponse<UpdateVersionInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<UpdateVersionInfo>> response) {
                        try {
                            UpdateVersionInfo updateInfo = response.body().data;
                            int versionCode = AppUtils.getVersion(MainActivity.this);
                            if (versionCode != 0 && updateInfo.getVersion() > versionCode) {
                                showUpdateDialog(MainActivity.this, updateInfo.getUrl());
                            } else {
                                if (isMine) {
                                    ToastUtils.getInstance(MainActivity.this).showToast("当前已经是最新版本");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showUpdateDialog(Context context, final String apkUrl) {
        CBDialogBuilder builder = new CBDialogBuilder(context);
        TextView titleView = builder.getView(R.id.dialog_title);
        titleView.setSingleLine(false);
        builder.setTouchOutSideCancelable(false)
                .showCancelButton(false)
                .setTitle("检测到应用有新版本，立即更新")
                .setMessage("")
                .setConfirmButtonText("确定")
                .setCancelButtonText("取消")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
                    @Override
                    public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                        switch (whichBtn) {
                            case BUTTON_CONFIRM:
                                showUploadDialog(apkUrl);
                                break;
                            case BUTTON_CANCEL:
                                //退出APP
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create().show();
    }

    /**
     * Case By:提示更新的对话框
     * Author: scene on 2017/4/27 16:52
     */
    private DownLoadDialog downLoadDialog;

    private void showUploadDialog(final String url) {
        DownLoadDialog.Builder downLoadDialogBuilder = new DownLoadDialog.Builder(MainActivity.this);
        downLoadDialog = downLoadDialogBuilder.create();
        downLoadDialog.show();
        downLoadFile(url);
        downLoadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

    }

    private void downLoadFile(String url) {
        OkGo.<File>get(url).tag("DOWNLOAD_APK").execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                try {
                    installAPK(MainActivity.this, response.body().getAbsolutePath());
                    if (downLoadDialog != null) {
                        downLoadDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                if (downLoadDialog != null) {
                    downLoadDialog.dismiss();
                }
            }
        });
    }

    /**
     * 安装app
     *
     * @param mContext
     * @param fileName
     */
    private static void installAPK(Context mContext, String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            String topActivity = tasks.get(0).baseActivity.getPackageName();
            return !topActivity.equals(context.getPackageName());
        }
        return false;
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(10);
        }
    };

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (!isApplicationBroughtToBackground(MainActivity.this)) {
                showNoticeToast("xxxxxxxx");
                OkGo.<String>get(ApiUtil.API_PRE + ApiUtil.GET_PAY_SUCCESS_INFO)
                        .tag(ApiUtil.GET_PAY_SUCCESS_INFO_TAG)
                        .execute(new JsonCallback<String>() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                showNoticeToast("xxxxxxxx");
                            }
                        });
            }
        }
    }

    private void showNoticeToast(String message) {
        if (toast == null) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_toast, null);
            toastContent = (TextView) v.findViewById(R.id.content);
            toast = new Toast(MainActivity.this);
            toast.setView(v);
            toast.setGravity(Gravity.TOP, 0, PtrLocalDisplay.dp2px(80));
            toast.setDuration(3000);
        }
        toastContent.setText(message);
        toast.show();
    }
}
