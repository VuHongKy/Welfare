package com.qd.welfare.fragment.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.VideoAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.entity.VideoResultInfo;
import com.qd.welfare.event.OpenVipSuccessEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.SpacesItemDecoration;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.GlideImageLoader;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.SharedPreferencesUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.utils.ViewUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 视频首页
 * Created by scene on 17-8-29.
 */

public class VideoFragment extends BaseMainFragment implements VideoAdapter.OnVideoItemClickListener {
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    private Banner banner;
    private List<String> bannerImageUrls = new ArrayList<>();
    List<String> bannerTitles = new ArrayList<>();

    private List<VideoResultInfo.VideoIndexInfo> list = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        getData(true);
        MainActivity.upLoadPageInfo(PageConfig.VIDEO_TRY, 0);
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
        mAdapter = new RecyclerAdapterWithHF(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.dp2px(1)));
        listView.setAdapter(mAdapter);
        adapter.setOnVideoItemClickListener(this);
        initBanner();
        if (App.userInfo.getRole() <= 1) {
            footerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_footer, null);
            mAdapter.addFooter(footerView);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.showVipDialog(getContext(), PageConfig.VIDEO_TRY, 0);
                }
            });
        }
    }

    private void initBanner() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_header, null);
        mAdapter.addHeader(headerView);
        banner = headerView.findViewById(R.id.banner);
        //设置banner高度
        ViewUtils.setViewHeightByViewGroup(banner, (int) (PtrLocalDisplay.SCREEN_WIDTH_PIXELS * 7f / 15f));
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setDelayTime(2000);
        banner.setBannerTitles(bannerTitles);
        banner.setImages(bannerImageUrls);
        banner.start();
    }

    private void bindBanner(final List<VideoInfo> bannerList) {
        bannerImageUrls.clear();
        bannerTitles.clear();
        for (VideoInfo info : bannerList) {
            bannerImageUrls.add(App.commonInfo.getFile_domain() + info.getThumb());
            bannerTitles.add(info.getTitle());
        }
        banner.setImages(bannerImageUrls);
        banner.setBannerTitles(bannerTitles);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerList.get(position).getType() == 2 && App.userInfo.getRole() <= 1) {
                    DialogUtil.showOpenViewDialog(getContext(), "该视频为会员专享，请先开通会员", PageConfig.VIDEO_TRY, bannerList.get(position).getId());
                } else {
                    Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                    intent.putExtra("id", bannerList.get(position).getId());
                    startActivity(intent);
                }

            }
        });
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
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                list.clear();
                                list.addAll(response.body().data.getTry_other());
                                list.addAll(response.body().data.getVip_other());
                                if (App.userInfo.getRole() <= 1) {
                                    for (VideoResultInfo.VideoIndexInfo info : list) {
                                        if (info.getShow() == 2) {
                                            list.remove(info);
                                        }
                                    }
                                    if (mAdapter.getFootSize() == 0) {
                                        mAdapter.addFooter(footerView);
                                    }
                                } else {
                                    try {
                                        mAdapter.removeFooter(footerView);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                bindBanner(response.body().data.getTry_banner().getVideo());
                                if (list.size() > 0) {
                                    SharedPreferencesUtil.putString(_mActivity, "NOTIFY_DATA", JSON.toJSONString(list.get(0)));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Response<LzyResponse<VideoResultInfo>> response) {
                            super.onError(response);
                            try {
                                if (isFirst) {
                                    statusLayout.showFailed(retryListener);
                                } else {
                                    ptrLayout.refreshComplete();
                                    ToastUtils.getInstance(getContext()).showToast("请检查网络连接");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(ApiUtil.VIDEO_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onVideoItemClick(VideoInfo info) {

        if (App.userInfo.getRole() <= 1 && info.getType() == 2) {
            DialogUtil.showOpenViewDialog(getContext(), "该视频为会员专享，请先开通会员", PageConfig.VIDEO_TRY, info.getId());
        } else {
            Intent intent = new Intent(getContext(), VideoDetailActivity.class);
            intent.putExtra("id", info.getId());
            startActivity(intent);
        }


    }

    @Subscribe
    public void openVipSuccess(OpenVipSuccessEvent event) {
        ptrLayout.autoRefresh();
    }
}
