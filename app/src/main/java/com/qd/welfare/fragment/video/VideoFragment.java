package com.qd.welfare.fragment.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.R;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.entity.VideoResultInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 视频首页
 * Created by scene on 17-8-29.
 */

public class VideoFragment extends BaseMainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getData(true);
    }

    private void initView() {

    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            OkGo.<LzyResponse<VideoResultInfo>>get(ApiUtil.API_PRE + ApiUtil.VIDEO)
                    .tag(ApiUtil.VIDEO_TAG)
                    .execute(new JsonCallback<LzyResponse<VideoResultInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<VideoResultInfo>> response) {
                            if (isFirst) {
                                statusLayout.showContent();
                            } else {
                                ptrLayout.refreshComplete();
                            }


                        }

                        @Override
                        public void onError(Response<LzyResponse<VideoResultInfo>> response) {
                            super.onError(response);
                            if (isFirst) {
                                statusLayout.showFailed(retryListener);
                            } else {
                                ptrLayout.refreshComplete();
                            }
                        }
                    });
        } else {
            if (isFirst) {
                statusLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
                ToastUtils.getInstance(getContext()).showToast("请检查网络连接");
            }
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.VIDEO_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
