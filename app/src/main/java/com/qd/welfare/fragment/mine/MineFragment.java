package com.qd.welfare.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.GlideCacheUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.CustomeGridView;
import com.qd.welfare.widgets.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 * Created by scene on 17-8-29.
 */

public class MineFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.gridView)
    CustomeGridView gridView;
    @BindView(R.id.vip_type)
    TextView vipType;
    @BindView(R.id.openVip)
    TextView openVip;
    @BindView(R.id.userId)
    TextView userId;

    Unbinder unbinder;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
        if (App.userInfo.getRole() > 1) {
            openVip.setVisibility(View.GONE);
            vipType.setVisibility(View.VISIBLE);
            if (App.userInfo.getRole() == 2) {
                //包月
                vipType.setText("包月会员");
            } else if (App.userInfo.getRole() == 3) {
                //包年
                vipType.setText("包年会员");
            }
        } else {
            openVip.setVisibility(View.VISIBLE);
            vipType.setVisibility(View.GONE);
        }
        userId.setText(String.valueOf(App.userInfo.getId()));
        MainActivity.upLoadPageInfo(PageConfig.MINE, 0);
        getData();
    }

    private void getData() {
        HttpParams params = new HttpParams();
        params.put("video_id", 0);
        OkGo.<LzyResponse<List<VideoInfo>>>get(ApiUtil.API_PRE + ApiUtil.RECOMMEND)
                .tag(ApiUtil.RECOMMEND_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<List<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<VideoInfo>>> response) {
                        try {
                            RecommendAdapter adapter = new RecommendAdapter(getContext(), response.body().data);
                            gridView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
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
        if (App.userInfo.getRole() <= 1) {
            DialogUtil.showVipDialog(getContext(), PageConfig.VIDEO_TRY, 0);
        } else {
            ToastUtils.getInstance(getContext()).showToast("您已经是会员了，不需要再次开通");
        }
    }

    @OnClick(R.id.user_agreement)
    public void onClickUserAgreement() {
        EventBus.getDefault().post(new StartBrotherEvent(UserAgreementFragment.newInstance()));
    }

    @OnClick(R.id.update_version)
    public void onClickUpdateVersion() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getUpdateVersion(true);
        }
    }

    @OnClick(R.id.clear_cache)
    public void onClickClearCache() {
        LoadingDialog loadingDialog = LoadingDialog.getInstance(getContext());
        loadingDialog.showLoadingDialog("正在清理缓存...");
        GlideCacheUtil.getInstance().cleanCacheDisk(_mActivity);
        GlideCacheUtil.getInstance().clearCacheDiskSelf(_mActivity);
        loadingDialog.cancelLoadingDialog();
        ToastUtils.getInstance(_mActivity).showToast("缓存清理成功");
    }

    @OnClick(R.id.service_center)
    public void onClickServiceCenter() {
        EventBus.getDefault().post(new StartBrotherEvent(ServiceCenterFragment.newInstance()));
    }

}
