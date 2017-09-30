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
import com.qd.welfare.entity.NovelInfo;
import com.qd.welfare.widgets.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * 小说
 * Created by scene on 2017/9/6.
 */

public class NovelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NovelInfo> list;
    private String novelCategory;

    public NovelAdapter(Context context, List<NovelInfo> list, String novelCategory) {
        this.context = context;
        this.list = list;
        this.novelCategory = novelCategory;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NovelViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_novel_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NovelInfo info = list.get(position);
        NovelViewHolder viewHolder = (NovelViewHolder) holder;
        viewHolder.title.setText(info.getTitle());
        viewHolder.description.setText(info.getDescription());
        viewHolder.actor.setText(info.getAuthor());
        viewHolder.imageTag.setText(novelCategory);
        List<String> tags = new ArrayList<>();
        tags.addAll(info.getTags());
        tags.add(info.getStatus());
        viewHolder.tagLayout.setTags(tags);
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NovelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.actor)
        TextView actor;
        @BindView(R.id.tag_layout)
        TagContainerLayout tagLayout;
        @BindView(R.id.image_tag)
        TextView imageTag;

        NovelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
