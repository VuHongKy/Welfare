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
import com.qd.welfare.entity.NovelCateGoryInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 小说分类
 * Created by scene on 2017/9/8.
 */

public class NovelIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NovelCateGoryInfo> list;

    public NovelIndexAdapter(Context context, List<NovelCateGoryInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NovelIndexViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_novel_index_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NovelCateGoryInfo info = list.get(position);
        NovelIndexViewHolder viewHolde = (NovelIndexViewHolder) holder;
        viewHolde.name.setText(info.getTitle());
        viewHolde.number.setText("共" + info.getTotal() + "本作品");
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getIcon()).centerCrop().into(viewHolde.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class NovelIndexViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SelectableRoundedImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.number)
        TextView number;

        NovelIndexViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
