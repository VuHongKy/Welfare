package com.qd.welfare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门推荐
 * Created by scene on 17-8-29.
 */

public class RecommendAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    public RecommendAdapter(Context context, List<VideoInfo> list) {
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
        VideoInfo info = list.get(position);
        viewHolder.title.setText(info.getTitle());
        if(info.getThumb_shu().endsWith("gif")){
            Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb_shu()).asGif().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.image);
        }else{
            Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb_shu()).centerCrop().into(viewHolder.image);
        }
        viewHolder.starLayout.setVisibility(View.GONE);
        viewHolder.title.setVisibility(View.VISIBLE);
        return view;
    }

    static class ActressItemViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.star_layout)
        LinearLayout starLayout;

        ActressItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
