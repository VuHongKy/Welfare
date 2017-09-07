package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.OrderInfo;
import com.qd.welfare.utils.PriceUtil;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 我的订单
 * Created by scene on 2017/9/7.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<OrderInfo> list;

    public OrderAdapter(Context context, List<OrderInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder viewHolder = (OrderViewHolder) holder;
        OrderInfo info = list.get(position);
        viewHolder.goodsName.setText(info.getGoods_name());
        viewHolder.goodsNumber.setText("x" + info.getNumber());
        viewHolder.orderNumber.setText("订单编号：" + info.getOrder_id());
        switch (info.getStatus()) {
            case 1:
                viewHolder.orderStatus.setText("待支付");
                break;
            case 2:
                viewHolder.orderStatus.setText("待发货");
                break;
            case 3:
                viewHolder.orderStatus.setText("未支付");
                break;
            case 4:
                viewHolder.orderStatus.setText("已发货");
                break;
        }
        viewHolder.totalNumber.setText("共" + info.getNumber() + "件商品");
        viewHolder.totalPrice.setText("￥" + PriceUtil.getPrice(info.getMoney()));
        viewHolder.goodsPrice.setText("￥" + PriceUtil.getPrice(info.getPrice()));
        viewHolder.yunfei.setText("(含运费￥" + info.getDelivery_money() + ")");

        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb())
                .bitmapTransform(new RoundedCornersTransformation(context, PtrLocalDisplay.dp2px(5), 0)).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.order_number)
        TextView orderNumber;
        @BindView(R.id.order_status)
        TextView orderStatus;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.goods_number)
        TextView goodsNumber;
        @BindView(R.id.total_number)
        TextView totalNumber;
        @BindView(R.id.total_price)
        TextView totalPrice;
        @BindView(R.id.yunfei)
        TextView yunfei;

        OrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
