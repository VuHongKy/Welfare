package com.qd.welfare.fragment.novel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.NovelIndexAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.NovelCateGoryInfo;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.GridSpacingItemDecoration;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;

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
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 分类首页
 * Created by scene on 2017/9/8.
 */

public class NovelIndexFragment extends BaseMainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;

    Unbinder unbinder;

    private List<NovelCateGoryInfo> list = new ArrayList<>();
    private NovelIndexAdapter adapter;


    public static NovelIndexFragment newInstance() {
        Bundle args = new Bundle();
        NovelIndexFragment fragment = new NovelIndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel_index, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.NOVEL_CATEGORY, 0);
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
        adapter = new NovelIndexAdapter(getContext(), list);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, PtrLocalDisplay.dp2px(10), true));
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                if (App.userInfo != null && App.userInfo.getRole() > 1) {
                    EventBus.getDefault().post(new StartBrotherEvent(NovelListFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
                } else {
                    DialogUtil.showOpenViewDialog(getContext(), PageConfig.NOVEL_CATEGORY, 0);
                }
            }
        });
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            OkGo.<LzyResponse<List<NovelCateGoryInfo>>>get(ApiUtil.API_PRE + ApiUtil.NOVEL_CATEGORY)
                    .tag(ApiUtil.NOVEL_CATEGORY_TAG)
                    .execute(new JsonCallback<LzyResponse<List<NovelCateGoryInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<NovelCateGoryInfo>>> response) {
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                list.clear();
                                list.addAll(response.body().data);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<List<NovelCateGoryInfo>>> response) {
                            super.onError(response);
                            try {
                                if (isFirst) {
                                    statusLayout.showFailed(retryListener);
                                } else {
                                    ptrLayout.refreshComplete();
                                    if (response.getException() != null && !TextUtils.isEmpty(response.getException().getMessage())) {
                                        ToastUtils.getInstance(_mActivity).showToast(response.getException().getMessage());
                                    } else {
                                        ToastUtils.getInstance(_mActivity).showToast(response.message());
                                    }
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
                ToastUtils.getInstance(_mActivity).showToast("请检查网络连接");
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
        OkGo.getInstance().cancelTag(ApiUtil.NOVEL_CATEGORY_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
