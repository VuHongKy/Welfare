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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.adapter.OrderDetailAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.entity.LogisticInfo;
import com.qd.welfare.entity.OrderDetailInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.PriceUtil;
import com.qd.welfare.utils.ToastUtils;
import com.qd.welfare.widgets.RatioImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 订单详情
 * Created by scene on 2017/9/7.
 */

public class OrderDetailFragment extends BaseBackFragment {
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

    private RatioImageView image;
    private TextView order_number;
    private TextView order_status;
    private TextView goods_name;
    private TextView goods_price;
    private TextView goods_number;
    private TextView total_number;
    private TextView total_price;
    private TextView yunfei;
    private TextView receiver_name;
    private TextView receiver_phone;
    private TextView receiver_address;
    private LinearLayout layout_wuliu;
    private TextView kuaidoCode;

    private int id;

    private List<LogisticInfo.DataBean> list = new ArrayList<>();
    private OrderDetailAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

    public static OrderDetailFragment newInstance(int orderId) {
        Bundle args = new Bundle();
        args.putInt("id", orderId);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText("订单详情");
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
        adapter = new OrderDetailAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        initHeader();
    }

    private void initHeader() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order_detail_header, null);

        image = headerView.findViewById(R.id.image);
        order_number = headerView.findViewById(R.id.order_number);
        order_status = headerView.findViewById(R.id.order_status);
        goods_name = headerView.findViewById(R.id.goods_name);
        goods_price = headerView.findViewById(R.id.goods_price);
        goods_number = headerView.findViewById(R.id.goods_number);
        total_number = headerView.findViewById(R.id.total_number);
        total_price = headerView.findViewById(R.id.total_price);
        yunfei = headerView.findViewById(R.id.yunfei);
        receiver_name = headerView.findViewById(R.id.receiver_name);
        receiver_phone = headerView.findViewById(R.id.receiver_phone);
        receiver_address = headerView.findViewById(R.id.receiver_address);
        layout_wuliu = headerView.findViewById(R.id.layout_wuliu);
        kuaidoCode = headerView.findViewById(R.id.kuaidoCode);
        mAdapter.addHeader(headerView);
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            params.put("user_id", App.userInfo.getId());
            params.put("order_id", id);
            OkGo.<LzyResponse<OrderDetailInfo>>get(ApiUtil.API_PRE + ApiUtil.ORDER_DETAIL)
                    .tag(ApiUtil.ORDER_DETAIL_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<OrderDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<OrderDetailInfo>> response) {
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                bindHeader(response.body().data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<OrderDetailInfo>> response) {
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

    private void getWuliuData(String shipperCode, String logisticCode) {
        HttpParams params = new HttpParams();
        params.put("type", shipperCode);
        params.put("postid", logisticCode);
        OkGo.<String>get(ApiUtil.GET_LOGISTICS)
                .tag(ApiUtil.GET_LOGISTICS_TAG)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            LogisticInfo logisticInfo = JSON.parseObject(response.body(), LogisticInfo.class);
                            list.clear();
                            list.addAll(logisticInfo.getData());
                            if (list != null && list.size() > 0) {
                                Collections.reverse(list);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    private void bindHeader(OrderDetailInfo info) {
        goods_name.setText(info.getGoods_name());
        goods_number.setText("x" + info.getNumber());
        order_number.setText("订单编号：" + info.getOrder_id());
        switch (info.getStatus()) {
            case 1:
                order_status.setText("待支付");
                break;
            case 2:
                order_status.setText("待发货");
                break;
            case 3:
                order_status.setText("未支付");
                break;
            case 4:
                order_status.setText("已发货");
                break;
        }
        total_number.setText("共" + info.getNumber() + "件商品");
        total_price.setText("￥" + PriceUtil.getPrice(info.getMoney()));
        goods_price.setText("￥" + PriceUtil.getPrice(info.getPrice()));
        yunfei.setText("(含运费￥" + info.getDelivery_money() + ")");

        Glide.with(this).load(App.commonInfo.getFile_domain() + info.getThumb())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), PtrLocalDisplay.dp2px(5), 0)).into(image);

        receiver_name.setText(info.getUsername());
        receiver_address.setText(info.getAddress());
        receiver_phone.setText(info.getPhone());
        if (info.getStatus() == 4) {
            layout_wuliu.setVisibility(View.VISIBLE);
            getWuliuData(info.getDelivery_code(), info.getDelivery_no());
            kuaidoCode.setText(info.getDelivery_name() + "（" + info.getDelivery_code() + ":" + info.getDelivery_no() + "）");
        } else {
            layout_wuliu.setVisibility(View.GONE);
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
        OkGo.getInstance().cancelTag(ApiUtil.GET_LOGISTICS_TAG);
        OkGo.getInstance().cancelTag(ApiUtil.ORDER_DETAIL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
