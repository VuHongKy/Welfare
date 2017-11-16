package com.qd.welfare.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.RecommendAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.event.OpenVipSuccessEvent;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.fragment.video.VideoDetailActivity;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.GlideCacheUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.CustomeGridView;
import com.qd.welfare.widgets.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 * Created by scene on 17-8-29.
 */

public class Mine2Fragment extends BaseBackFragment {
    @BindView(R.id.gridView)
    CustomeGridView gridView;
    @BindView(R.id.vip_type)
    TextView vipType;
    @BindView(R.id.userId)
    TextView userId;
    @BindView(R.id.openVip_layout)
    LinearLayout openVipLayout;

    Unbinder unbinder;

    LoadingDialog loadingDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.back)
    TextView back;

    public static Mine2Fragment newInstance() {
        Bundle args = new Bundle();
        Mine2Fragment fragment = new Mine2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initToolbarNav(toolbar);
        init();
    }

    private void init() {
        try {
            if (App.userInfo != null && App.userInfo.getRole() > 1) {
                openVipLayout.setVisibility(View.GONE);
                vipType.setVisibility(View.VISIBLE);
                if (App.userInfo.getRole() == 2) {
                    //包月
                    vipType.setText("包月会员");
                } else if (App.userInfo.getRole() == 3) {
                    //包年
                    vipType.setText("包年会员");
                }
            } else {
                openVipLayout.setVisibility(View.VISIBLE);
                vipType.setVisibility(View.GONE);
            }
            userId.setText(String.valueOf(App.userInfo.getId()));
            MainActivity.upLoadPageInfo(PageConfig.MINE, 0);
            getData();
        } catch (Exception e) {
            if (_mActivity != null) {
                startActivity(new Intent(_mActivity, MainActivity.class));
                _mActivity.finish();
            }
            e.printStackTrace();
        }
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        onBackPressedSupport();
    }

    private void getData() {
        HttpParams params = new HttpParams();
        params.put("video_id", 0);
        OkGo.<LzyResponse<List<VideoInfo>>>get(ApiUtil.API_PRE + ApiUtil.RECOMMEND)
                .tag(ApiUtil.RECOMMEND_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<List<VideoInfo>>>() {
                    @Override
                    public void onSuccess(final Response<LzyResponse<List<VideoInfo>>> response) {
                        try {
                            RecommendAdapter adapter = new RecommendAdapter(getContext(), response.body().data);
                            gridView.setAdapter(adapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (App.userInfo.getRole() <= 1 && response.body().data.get(i).getType() == 2) {
                                        DialogUtil.showOpenViewDialog(getContext(), "该视频为会员专享，请先开通会员", PageConfig.VIDEO_TRY, response.body().data.get(i).getId());
                                    } else {
                                        Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                                        intent.putExtra("id", response.body().data.get(i).getId());
                                        startActivity(intent);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        DialogUtil.cancelDialog();
        OkGo.getInstance().cancelTag(ApiUtil.GET_PAY_INFO_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.RECOMMEND_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.my_order)
    public void onClickMyOrder() {
        EventBus.getDefault().post(new StartBrotherEvent(OrderFragment.newInstance()));
    }

    @OnClick(R.id.openVip)
    public void onClickOpenVip() {
        if (App.userInfo == null || App.userInfo.getRole() <= 1) {
            DialogUtil.showVipDialog(getContext(), PageConfig.VIDEO_TRY, 0);
        } else {
            ToastUtils.getInstance(getContext()).showToast("您已经是会员了，不需要再次开通");
        }
    }

    @OnClick(R.id.user_agreement)
    public void onClickUserAgreement() {
        //EventBus.getDefault().post(new StartBrotherEvent(UserAgreementActivity.newInstance()));
        Intent intent = new Intent(_mActivity, UserAgreementActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.update_version)
    public void onClickUpdateVersion() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getUpdateVersion(true);
        }
    }

    @OnClick(R.id.clear_cache)
    public void onClickClearCache() {
        loadingDialog = LoadingDialog.getInstance(getContext());
        loadingDialog.showLoadingDialog("正在清理缓存...");
        GlideCacheUtil.getInstance().cleanCacheDisk(_mActivity);
        GlideCacheUtil.getInstance().clearCacheDiskSelf(_mActivity);
        userId.postDelayed(new Runnable() {
            @Override
            public void run() {
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadingDialog.cancelLoadingDialog();
                            ToastUtils.getInstance(_mActivity).showToast("缓存清理成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 2000);

    }

    @OnClick(R.id.service_center)
    public void onClickServiceCenter() {
        EventBus.getDefault().post(new StartBrotherEvent(ServiceCenterFragment.newInstance()));
    }

    @OnClick(R.id.ic_back)
    public void onClickTopBack() {
        _mActivity.onBackPressed();
    }

    @Subscribe
    public void openVipSuccess(OpenVipSuccessEvent event) {
        init();
    }
}
