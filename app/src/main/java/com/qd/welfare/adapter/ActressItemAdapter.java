package com.qd.welfare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.ActressInfo;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressItemAdapter extends BaseAdapter {
    private Context context;
    private List<ActressInfo.WorksBean> list;
    private LayoutInflater inflater;

    public ActressItemAdapter(Context context, List<ActressInfo.WorksBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ActressItemViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_actress_item_item, viewGroup, false);
            viewHolder = new ActressItemViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ActressItemViewHolder) view.getTag();
        }
        ActressInfo.WorksBean info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(viewHolder.image);
        switch (position) {
            case 0:
                viewHolder.star1.setVisibility(View.VISIBLE);
                viewHolder.star2.setVisibility(View.VISIBLE);
                viewHolder.star3.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewHolder.star1.setVisibility(View.VISIBLE);
                viewHolder.star2.setVisibility(View.VISIBLE);
                viewHolder.star3.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.star1.setVisibility(View.VISIBLE);
                viewHolder.star2.setVisibility(View.VISIBLE);
                viewHolder.star3.setVisibility(View.GONE);
                break;
        }
        return view;
    }

    static class ActressItemViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.star1)
        ImageView star1;
        @BindView(R.id.star2)
        ImageView star2;
        @BindView(R.id.star3)
        ImageView star3;

        ActressItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
