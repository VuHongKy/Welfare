package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.CateGroyInfo;
import com.qd.welfare.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类
 * Created by scene on 2017/9/8.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CateGroyInfo> list;

    public CategoryAdapter(Context context, List<CateGroyInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NovelIndexViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_category_new_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            CateGroyInfo info = list.get(position);
            NovelIndexViewHolder viewHolde = (NovelIndexViewHolder) holder;
            viewHolde.name.setText(info.getTitle());
            viewHolde.number.setText(+info.getView_times() + "次点击");
            GlideUtils.loadIntoUseFitWidth(context, App.commonInfo.getFile_domain() + info.getThumb(), viewHolde.image);
        }catch (Exception e){
            e.printStackTrace();
        }
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
