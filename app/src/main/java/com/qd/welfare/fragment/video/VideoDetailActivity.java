package com.qd.welfare.fragment.video;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.VideoRecommendAdapter;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.PayResultInfo;
import com.qd.welfare.entity.VideoDetailInfo;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.event.OpenVipSuccessEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.jcvideoplayer.JCVideoPlayer;
import com.qd.welfare.jcvideoplayer.MyJCVideoPlayerStandard;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.LoadingDialog;
import com.qd.welfare.widgets.RatioImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 视屏详情
 * Created by scene on 2017/9/1.
 */

public class VideoDetailActivity extends SwipeBackActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    private RatioImageView image;
    private TextView openVip;
    private ImageView playVideo;


    private int videoId;

    private List<VideoInfo> list = new ArrayList<>();
    private VideoRecommendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_detail);
        unbinder = ButterKnife.bind(this);
        videoId = getIntent().getIntExtra("id", 0);
        initToolbar();
        initView();
        MainActivity.upLoadPageInfo(PageConfig.VIDEO_DETAIL_TRY, 0);
    }

    private void initToolbar() {
        toolbarTitle.setText("详情");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        adapter = new VideoRecommendAdapter(VideoDetailActivity.this, list);
        initHeaderView();
        listView.setAdapter(adapter);
        getVideoData();
    }

    private void initHeaderView() {
        View headerView = LayoutInflater.from(VideoDetailActivity.this).inflate(R.layout.fragment_video_detail_header, null);
        listView.addHeaderView(headerView);
        image = headerView.findViewById(R.id.image);
        openVip = headerView.findViewById(R.id.openVip);
        playVideo = headerView.findViewById(R.id.play_video);
        openVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.userInfo.getRole() <= 1) {
                    DialogUtil.showVipDialog(VideoDetailActivity.this, PageConfig.VIDEO_DETAIL_TRY, videoId);
                }
            }
        });
    }

    private void bindHeaderView(VideoDetailInfo info) {
        Glide.with(VideoDetailActivity.this).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(image);
        if (App.userInfo.getRole() > 1) {
            openVip.setVisibility(View.GONE);
        } else {
            openVip.setVisibility(View.VISIBLE);
        }
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                MyJCVideoPlayerStandard.startFullscreen(VideoDetailActivity.this, MyJCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "嫂子辛苦了");
            }
        });
    }

    private void getVideoData() {
        if (NetWorkUtils.isNetworkConnected(VideoDetailActivity.this)) {
            statusLayout.showLoading();
            HttpParams params = new HttpParams();
            params.put("video_id", videoId);
            OkGo.<LzyResponse<VideoDetailInfo>>get(ApiUtil.API_PRE + ApiUtil.VIDEO_DETAIL)
                    .tag(ApiUtil.VIDEO_DETAIL_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<VideoDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<VideoDetailInfo>> response) {
                            bindHeaderView(response.body().data);
                            getRecommendData();
                        }

                        @Override
                        public void onError(Response<LzyResponse<VideoDetailInfo>> response) {
                            super.onError(response);
                            statusLayout.showFailed(retryListener);
                        }
                    });
        } else {
            statusLayout.showNetError(retryListener);
        }
    }

    private void getRecommendData() {
        HttpParams params = new HttpParams();
        params.put("video_id", videoId);
        OkGo.<LzyResponse<List<VideoInfo>>>get(ApiUtil.API_PRE + ApiUtil.RECOMMEND)
                .tag(ApiUtil.RECOMMEND_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<List<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<VideoInfo>>> response) {
                        list.clear();
                        list.addAll(response.body().data);
                        adapter.notifyDataSetChanged();
                        statusLayout.showContent();
                    }

                    @Override
                    public void onError(Response<LzyResponse<List<VideoInfo>>> response) {
                        super.onError(response);
                        statusLayout.showFailed(retryListener);
                    }
                });
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getVideoData();
        }
    };

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(ApiUtil.RECOMMEND_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.VIDEO_DETAIL_TAG);
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressedSupport() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressedSupport();
    }

    @Override
    protected void onResume() {
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

    private LoadingDialog loadingDialog;

    private void checkOrder() {
        loadingDialog = LoadingDialog.getInstance(VideoDetailActivity.this);
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
                                DialogUtil.showOpenVipSuccess(VideoDetailActivity.this);
                                App.userInfo.setRole(response.body().data.getRole());
                                EventBus.getDefault().post(new OpenVipSuccessEvent());
                                if (App.userInfo.getRole() > 1) {
                                    openVip.setVisibility(View.GONE);
                                }
                            } else {
                                ToastUtils.getInstance(VideoDetailActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.getInstance(VideoDetailActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PayResultInfo>> response) {
                        super.onError(response);
                        try {
                            ToastUtils.getInstance(VideoDetailActivity.this).showToast("如遇微信不能支付，请使用支付宝支付");
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
