package com.qd.welfare.fragment.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.adapter.VideoAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.entity.VideoResultInfo;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.GlideImageLoader;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.utils.ViewUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 视频首页
 * Created by scene on 17-8-29.
 */

public class VideoFragment extends BaseMainFragment implements VideoAdapter.OnVideoItemClickListener {
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    private Banner banner;
    private List<String> bannerImageUrls = new ArrayList<>();

    private List<VideoResultInfo.VideoIndexInfo> list = new ArrayList<>();
    private VideoAdapter adapter;


    private View footerView;

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
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
        adapter = new VideoAdapter(getContext(), list);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_footer, null);
        if (App.userInfo.getRole() == 1) {
            listView.addFooterView(footerView);
        }
        initBanner();
        listView.setAdapter(adapter);
        adapter.setOnVideoItemClickListener(this);
    }

    private void initBanner() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_header, null);
        listView.addHeaderView(headerView);
        banner = headerView.findViewById(R.id.banner);
        //设置banner高度
        ViewUtils.setViewHeightByViewGroup(banner, (int) (PtrLocalDisplay.SCREEN_WIDTH_PIXELS * 7f / 15f));
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setDelayTime(2000);
        banner.setImages(bannerImageUrls);
        banner.start();
    }

    void bindBanner(List<VideoInfo> bannerList) {
        bannerImageUrls.clear();
        for (VideoInfo info : bannerList) {
            bannerImageUrls.add(App.commonInfo.getFile_domain() + info.getThumb());
        }
        banner.setImages(bannerImageUrls);
        banner.start();
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
                            list.clear();
                            list.addAll(response.body().data.getTry_other());
                            if (App.userInfo.getRole() > 1) {
                                footerView.setVisibility(View.GONE);
                                footerView.setPadding(0, -footerView.getHeight(), 0, 0);
                                list.addAll(response.body().data.getVip_other());
                            } else {
                                footerView.setVisibility(View.VISIBLE);
                                footerView.setPadding(0, 0, 0, 0);
                            }
                            adapter.notifyDataSetChanged();
                            bindBanner(response.body().data.getTry_banner().getVideo());
                        }

                        @Override
                        public void onError(Response<LzyResponse<VideoResultInfo>> response) {
                            super.onError(response);
                            if (isFirst) {
                                statusLayout.showFailed(retryListener);
                            } else {
                                ptrLayout.refreshComplete();
                                ToastUtils.getInstance(getContext()).showToast("请检查网络连接");
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

    @Override
    public void onVideoItemClick(VideoInfo info) {
        EventBus.getDefault().post(new StartBrotherEvent(VideoDetailFragment.newInstance(info.getId())));
    }
}
