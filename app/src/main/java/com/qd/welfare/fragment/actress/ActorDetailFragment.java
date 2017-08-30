package com.qd.welfare.fragment.actress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.adapter.ActressDetailAdapter;
import com.qd.welfare.adapter.ActressDetailHeaderAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.entity.ActorDetailResultInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.ActorDetailSpacingItemDecoration;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.widgets.CustomeGridView;
import com.qd.welfare.widgets.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.loadmore.OnLoadMoreListener;
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;

/**
 * 女优详情
 * Created by scene on 17-8-30.
 */

public class ActorDetailFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;

    Unbinder unbinder;

    private RatioImageView actorAvater;
    private TextView tvActorName;
    private TextView tvActorBirthday;
    private TextView tvActorDescription;

    private List<ActorDetailResultInfo.ActorBean.WorksBean> headerList = new ArrayList<>();
    private ActressDetailHeaderAdapter headerAdapter;

    private int actorId;
    private String actorName;

    private int page = 1;
    private List<ActorDetailResultInfo.GalleryBean.DataBean> list = new ArrayList<>();
    private ActressDetailAdapter detailAdapter;
    private RecyclerAdapterWithHF mAdapter;

    public static ActorDetailFragment newInstance(int actorId, String actorName) {
        Bundle args = new Bundle();
        args.putInt("id", actorId);
        args.putString("name", actorName);
        ActorDetailFragment fragment = new ActorDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actorId = getArguments().getInt("id");
            actorName = getArguments().getString("name");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText(actorName);
        initToolbarNav(toolbar);
        initView();
        getData(true, 1);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false, 1);
            }
        });
        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                getData(false, page);
            }
        });
        detailAdapter = new ActressDetailAdapter(getContext(), list);
        mAdapter = new RecyclerAdapterWithHF(detailAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == recyclerView.getAdapter().getItemCount() - 1 || position == 0) ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ActorDetailSpacingItemDecoration());
        recyclerView.setAdapter(mAdapter);
        initHeaderView();
    }

    private void initHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_actor_detail_header, null);
        actorAvater = headerView.findViewById(R.id.actor_avater);
        tvActorName = headerView.findViewById(R.id.actor_name);
        tvActorBirthday = headerView.findViewById(R.id.actor_birthday);
        tvActorDescription = headerView.findViewById(R.id.actor_description);
        CustomeGridView headerGridView = headerView.findViewById(R.id.header_gridView);
        headerAdapter = new ActressDetailHeaderAdapter(getContext(), headerList);
        headerGridView.setAdapter(headerAdapter);
        mAdapter.addHeader(headerView);
    }

    private void bindHeaderView(ActorDetailResultInfo.ActorBean actorBean) {
        tvActorName.setText(actorBean.getName());
        tvActorBirthday.setText("年龄：" + String.valueOf(actorBean.getAge()));
        tvActorDescription.setText(actorBean.getDescription());
        Glide.with(getContext()).load(App.commonInfo.getFile_domain() + actorBean.getThumb()).centerCrop().into(actorAvater);
        headerList.clear();
        headerList.addAll(actorBean.getWorks());
        headerAdapter.notifyDataSetChanged();
    }

    private void getData(final boolean isFirst, final int curPage) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            params.put("actor_id", actorId);
            params.put("page", curPage);
            OkGo.<LzyResponse<ActorDetailResultInfo>>get(ApiUtil.API_PRE + ApiUtil.ACTOR_DETAIL)
                    .tag(ApiUtil.ACTOR_DETAIL_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<ActorDetailResultInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<ActorDetailResultInfo>> response) {
                            page = curPage;
                            if (curPage == 1) {
                                list.clear();
                            }
                            list.addAll(response.body().data.getGallery().getData());
                            detailAdapter.notifyDataSetChanged();
                            if (isFirst) {
                                statusLayout.showContent();
                            } else {
                                ptrLayout.refreshComplete();
                            }
                            boolean hasMore = page < response.body().data.getGallery().getInfo().getPage_total();
                            ptrLayout.setLoadMoreEnable(hasMore);
                            if (ptrLayout.isLoading()) {
                                ptrLayout.loadMoreComplete(hasMore);
                            }
                            bindHeaderView(response.body().data.getActor());
                        }

                        @Override
                        public void onError(Response<LzyResponse<ActorDetailResultInfo>> response) {
                            super.onError(response);
                            if (isFirst) {
                                statusLayout.showFailed(retryListener);
                            } else {
                                ptrLayout.refreshComplete();
                                ptrLayout.loadMoreComplete(true);
                            }
                        }
                    });

        } else {
            statusLayout.showNetError(retryListener);
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData(true, 1);
        }
    };

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.ACTOR_DETAIL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
