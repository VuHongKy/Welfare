package com.qd.welfare.fragment.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.CategoryActorAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.CateGroyActorResultInfo;
import com.qd.welfare.entity.CateGroyInfo;
import com.qd.welfare.fragment.actress.ActorGalleryFragment;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.GridSpacingItemDecoration;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;

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
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 分类进入的界面
 * Created by scene on 17-8-29.
 */

public class CategoryActorFragment extends BaseBackFragment {
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

    private CateGroyInfo info;

    private ToastUtils toastUtils;


    private List<CateGroyActorResultInfo.CateGroyActorInfo> list = new ArrayList<>();
    private int page = 1;
    private CategoryActorAdapter adapter;

    public static CategoryActorFragment newInstance(CateGroyInfo info) {
        Bundle args = new Bundle();
        args.putSerializable("ARG", info);
        CategoryActorFragment fragment = new CategoryActorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = (CateGroyInfo) getArguments().getSerializable("ARG");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_actor, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.CATEGORY_ACTOR, 0);
        initView();
        toolbarTitle.setText(info.getTitle());
        initToolbarNav(toolbar);
    }

    private void initView() {
        toastUtils = ToastUtils.getInstance(_mActivity);
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
                getData(false, page + 1);
            }
        });

        adapter = new CategoryActorAdapter(getContext(), list);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, PtrLocalDisplay.dp2px(10), true));
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        mAdapter.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_GRID);
        recyclerView.setAdapter(mAdapter);
        ptrLayout.setLoadMoreEnable(true);
        getData(true, 1);

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                start(ActorGalleryFragment.newInstance(list.get(position).getId(), list.get(position).getName()));
            }
        });
    }

    private void getData(final boolean isFirst, final int currentPage) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            params.put("cate_id", info.getId());
            params.put("page", currentPage);
            OkGo.<LzyResponse<CateGroyActorResultInfo>>get(ApiUtil.API_PRE + ApiUtil.CATEGORY_ACTOR)
                    .tag(ApiUtil.CATEGORY_ACTOR_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<CateGroyActorResultInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<CateGroyActorResultInfo>> response) {
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                ptrLayout.loadMoreComplete(currentPage < response.body().data.getInfo().getPage_total());
                                if (currentPage == 1) {
                                    list.clear();
                                }
                                page = currentPage;
                                list.addAll(response.body().data.getData());
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<CateGroyActorResultInfo>> response) {
                            super.onError(response);
                            try {
                                if (isFirst) {
                                    statusLayout.showFailed(retryListener);
                                } else {
                                    ptrLayout.refreshComplete();
                                    ptrLayout.loadMoreComplete(true);
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
            getData(true, 1);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
