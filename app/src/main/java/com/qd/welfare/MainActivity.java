package com.qd.welfare;

import android.os.Bundle;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.config.AppConfig;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;

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
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(ApiUtil.UPLOAD_POSITION_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.UPLOAD_USER_INFO_TAG);
        super.onDestroy();
    }

    /**
     * Case By:上传使用信息每隔10s
     * Author: scene on 2017/4/20 10:25
     */
    private Thread thread;
    private boolean isWork = true;

    private void startUpLoad() {
        thread = new Thread(new Runnable() {
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
        OkGo.<LzyResponse<String>>get(ApiUtil.API_PRE + ApiUtil.UPLOAD_USER_INFO)
                .tag(ApiUtil.UPLOAD_USER_INFO_TAG)
                .execute(new JsonCallback<LzyResponse<String>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<String>> response) {
                        AppConfig.DEFAULT_PAY_WAY = AppConfig.PAY_TYPE_ALPAY;
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
}
