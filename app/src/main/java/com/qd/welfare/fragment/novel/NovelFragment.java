package com.qd.welfare.fragment.novel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.R;
import com.qd.welfare.adapter.NovelAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.entity.NovelInfo;
import com.qd.welfare.entity.NovelResultInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.SpacesItemDecoration;
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
 * 小说首页
 * Created by scene on 2017/9/4.
 */

public class NovelFragment extends BaseMainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;

    Unbinder unbinder;

    private List<NovelInfo> list = new ArrayList<>();
    private NovelAdapter adapter;
    private int page = 1;

    public static NovelFragment newInstance() {
        Bundle args = new Bundle();
        NovelFragment fragment = new NovelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
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
                getData(false, page + 1);
            }
        });

        adapter = new NovelAdapter(getContext(), list);
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.designedDP2px(1)));
        recyclerView.setAdapter(mAdapter);
        ptrLayout.setLoadMoreEnable(true);
    }

    private void getData(final boolean isFirst, final int currentPage) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            OkGo.<LzyResponse<NovelResultInfo>>get(ApiUtil.API_PRE + ApiUtil.NOVEL)
                    .tag(ApiUtil.NOVEL_TAG)
                    .execute(new JsonCallback<LzyResponse<NovelResultInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<NovelResultInfo>> response) {
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                ptrLayout.loadMoreComplete(response.body().data.getInfo().getPage_total() > currentPage);
                                page = currentPage;
                                if (currentPage == 1) {
                                    list.clear();
                                }
                                list.addAll(response.body().data.getData());
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<NovelResultInfo>> response) {
                            super.onError(response);
                            try {
                                if (isFirst) {
                                    statusLayout.showFailed(retryListener);
                                } else {
                                    if (response.getException() != null && !TextUtils.isEmpty(response.getException().getMessage())) {
                                        ToastUtils.getInstance(getContext()).showToast(response.getException().getMessage());
                                    } else {
                                        ToastUtils.getInstance(getContext()).showToast(response.message());
                                    }
                                    ptrLayout.refreshComplete();
                                    ptrLayout.loadMoreComplete(currentPage > 1 ? true : false);
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
        OkGo.getInstance().cancelTag(ApiUtil.NOVEL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
