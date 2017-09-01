package com.qd.welfare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.widgets.drawableratingbar.DrawableRatingBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * 视频热门推荐
 * Created by scene on 2017/9/1.
 */

public class VideoRecommendAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    public VideoRecommendAdapter(Context context, List<VideoInfo> list) {
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
        VideoDetailViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_video_detail_item, viewGroup, false);
            viewHolder = new VideoDetailViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (VideoDetailViewHolder) view.getTag();
        }
        VideoInfo info = list.get(position);
        viewHolder.videoName.setText(info.getTitle());
        viewHolder.ratingBar.setRating((int) info.getStar());
        viewHolder.tagLayout.setTags(info.getTags());
        viewHolder.videoPlayCount.setText("播放：" + info.getPlay_times());
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(viewHolder.image);
        return view;
    }

    static class VideoDetailViewHolder {
        @BindView(R.id.image)
        SelectableRoundedImageView image;
        @BindView(R.id.videoName)
        TextView videoName;
        @BindView(R.id.videoPlayCount)
        TextView videoPlayCount;
        @BindView(R.id.ratingBar)
        DrawableRatingBar ratingBar;
        @BindView(R.id.tag_layout)
        TagContainerLayout tagLayout;

        VideoDetailViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
