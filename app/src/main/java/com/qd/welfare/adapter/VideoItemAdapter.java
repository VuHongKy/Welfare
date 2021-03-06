package com.qd.welfare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haozhang.lib.SlantedTextView;
import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.VideoInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * 视屏横图
 * Created by scene on 2017/9/1.
 */

public class VideoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<VideoInfo> list;
    private LayoutInflater inflater;

    private OnChildItemClickListener onChildItemClickListener;

    public VideoItemAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_video_item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        VideoInfo info = list.get(position);
        viewHolder.videoName.setText(info.getTitle());
        viewHolder.videoPlayCount.setText("播放：" + info.getPlay_times());
        viewHolder.tagLayout.setTags(info.getTags());
        if (info.getThumb_shu().endsWith("gif")) {
            Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb_shu())
                    .asGif().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.image);
        } else {
            Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb_shu())
                    .centerCrop().into(viewHolder.image);
        }
        viewHolder.tagText.setText(info.getType() == 1 ? "免费试看" : "会员尊享");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onChildItemClickListener != null) {
                    onChildItemClickListener.onChildItemClick(position);
                }
            }
        });
        if (info.getType() > 1) {
            viewHolder.tagText.setSlantedBackgroundColor(Color.parseColor("#d462ff"));
        } else {
            viewHolder.tagText.setSlantedBackgroundColor(Color.parseColor("#E60012"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.videoName)
        TextView videoName;
        @BindView(R.id.videoPlayCount)
        TextView videoPlayCount;
        @BindView(R.id.tag_layout)
        TagContainerLayout tagLayout;
        @BindView(R.id.tag_text)
        SlantedTextView tagText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnChildItemClickListener {
        void onChildItemClick(int position);
    }
}
