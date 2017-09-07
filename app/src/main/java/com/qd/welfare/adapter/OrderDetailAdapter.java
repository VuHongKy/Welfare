package com.qd.welfare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.entity.LogisticInfo;
import com.qd.welfare.widgets.timelineview.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 物流信息
 * Created by scene on 2017/9/7.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<LogisticInfo.DataBean> list;

    public OrderDetailAdapter(Context context, List<LogisticInfo.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_order_detail_item, parent, false), viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderDetailViewHolder viewHolder = (OrderDetailViewHolder) holder;

        LogisticInfo.DataBean timeLineModel = list.get(position);
        viewHolder.time.setText(timeLineModel.getTime());
        viewHolder.content.setText(timeLineModel.getContext());
        if (position != 0) {
            viewHolder.timeMarker.setMarker(ContextCompat.getDrawable(context, R.drawable.marker), Color.parseColor("#999999"));
            viewHolder.content.setTextColor(Color.parseColor("#999999"));
            viewHolder.time.setTextColor(Color.parseColor("#999999"));
        } else {
            viewHolder.timeMarker.setMarker(ContextCompat.getDrawable(context, R.drawable.ic_time_line_mark));
            viewHolder.content.setTextColor(Color.parseColor("#F85788"));
            viewHolder.time.setTextColor(Color.parseColor("#F85788"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time_marker)
        TimelineView timeMarker;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.time)
        TextView time;

        OrderDetailViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            timeMarker.initLine(viewType);
        }
    }
}
