package com.qd.welfare.fragment.mine;

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
import com.qd.welfare.R;
import com.qd.welfare.adapter.OrderAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.entity.OrderInfo;
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
 * 我的订单
 * Created by scene on 2017/9/7.
 */

public class OrderFragment extends BaseBackFragment {
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

    private List<OrderInfo> list = new ArrayList<>();
    private OrderAdapter adapter;

    public static OrderFragment newInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText("我的订单");
        initToolbarNav(toolbar);
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
        adapter = new OrderAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.dp2px(8)));
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                start(OrderDetailFragment.newInstance(list.get(position).getId()));
            }
        });
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            if (App.userInfo == null) {
                _mActivity.onBackPressed();
                return;
            }
            params.put("user_id", App.userInfo.getId());
            OkGo.<LzyResponse<List<OrderInfo>>>get(ApiUtil.API_PRE + ApiUtil.ORDER_LIST)
                    .tag(ApiUtil.ORDER_LIST_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<List<OrderInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<OrderInfo>>> response) {
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
                        public void onError(Response<LzyResponse<List<OrderInfo>>> response) {
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
        OkGo.getInstance().cancelTag(ApiUtil.ORDER_LIST_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
