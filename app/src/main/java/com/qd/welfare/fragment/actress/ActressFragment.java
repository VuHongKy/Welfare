package com.qd.welfare.fragment.actress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.R;
import com.qd.welfare.adapter.ActressAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.entity.ActressInfo;
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
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressFragment extends BaseMainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    private ToastUtils toastUtils;

    private List<ActressInfo> list = new ArrayList<>();
    private ActressAdapter adapter;

    public static ActressFragment newInstance() {
        Bundle args = new Bundle();
        ActressFragment fragment = new ActressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actress, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
    }

    private void initView() {
        toastUtils = ToastUtils.getInstance(_mActivity);

        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });

        adapter = new ActressAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.designedDP2px(1)));
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        getData(true);
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            OkGo.<LzyResponse<List<ActressInfo>>>get(ApiUtil.API_PRE + ApiUtil.ACTRESS)
                    .tag(ApiUtil.ACTRESS_TAG)
                    .execute(new JsonCallback<LzyResponse<List<ActressInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<ActressInfo>>> response) {
                            if (isFirst) {
                                statusLayout.showContent();
                            } else {
                                ptrLayout.refreshComplete();
                            }
                            list.clear();
                            list.addAll(response.body().data);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Response<LzyResponse<List<ActressInfo>>> response) {
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
                statusLayout.showNetError();
            } else {
                toastUtils.showToast("请检查网络链接");
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
        OkGo.getInstance().cancelTag(ApiUtil.ACTRESS_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
