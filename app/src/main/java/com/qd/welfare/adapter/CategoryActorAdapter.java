package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.CateGroyActorResultInfo;
import com.qd.welfare.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类内页
 * Created by scene on 17-8-29.
 */

public class CategoryActorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CateGroyActorResultInfo.CateGroyActorInfo> list;

    public CategoryActorAdapter(Context context, List<CateGroyActorResultInfo.CateGroyActorInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryActorViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_category_actor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CateGroyActorResultInfo.CateGroyActorInfo info = list.get(position);
        CategoryActorViewHolder viewHolder = (CategoryActorViewHolder) holder;
        viewHolder.title.setText(info.getName());
        //Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).into(viewHolder.image);
        GlideUtils.loadIntoUseFitWidth(context, App.commonInfo.getFile_domain() + info.getThumb(), viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryActorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SelectableRoundedImageView image;
        @BindView(R.id.title)
        TextView title;

        CategoryActorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
