package com.qd.welfare.fragment.novel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.qd.welfare.adapter.NovelAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.NovelInfo;
import com.qd.welfare.entity.NovelResultInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.SpacesItemDecoration;
import com.qd.welfare.utils.DialogUtil;
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

public class NovelListFragment extends BaseBackFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    Unbinder unbinder;

    private List<NovelInfo> list = new ArrayList<>();
    private NovelAdapter adapter;
    private int page = 1;

    private int novelCateId;
    private String novelCateName;

    public static NovelListFragment newInstance(int novelCateId, String novelCateName) {
        Bundle args = new Bundle();
        args.putInt("id", novelCateId);
        args.putString("name", novelCateName);
        NovelListFragment fragment = new NovelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            novelCateId = getArguments().getInt("id", 0);
            novelCateName = getArguments().getString("name", "");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText(novelCateName);
        initToolbarNav(toolbar);
        initView();
        getData(true, 1);
        MainActivity.upLoadPageInfo(PageConfig.NOVEL_INDEX, 0);
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

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                NovelInfo info = list.get(position);
                if (App.userInfo.getRole() > 1) {
                    if (info.getChapters_id() == 0) {
                        start(NovelChapterFragment.newInstance(info.getId(), info.getTitle()));
                    } else {
                        start(NovelDetailFragment.newInstance(info.getChapters_id(), info.getTitle()));
                    }
                } else {
                    DialogUtil.showOpenViewDialog(getContext(), PageConfig.NOVEL_INDEX, info.getId());
                }

            }
        });
    }

    private void getData(final boolean isFirst, final int currentPage) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            params.put("page", currentPage);
            params.put("category_id", novelCateId);
            OkGo.<LzyResponse<NovelResultInfo>>get(ApiUtil.API_PRE + ApiUtil.NOVEL)
                    .tag(ApiUtil.NOVEL_TAG)
                    .params(params)
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
        OkGo.getInstance().cancelTag(ApiUtil.NOVEL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
