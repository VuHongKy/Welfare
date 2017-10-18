package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haozhang.lib.SlantedTextView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.ActressInfo;
import com.qd.welfare.widgets.CustomeGridView;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ActressInfo> list;

    public ActressAdapter(Context context, List<ActressInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ActressViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_actress_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            ActressViewHolder viewHolder = (ActressViewHolder) holder;
            ActressInfo info = list.get(position);
            viewHolder.name.setText(info.getName());
            viewHolder.sanwei.setText("三围：" + info.getSanwei());
            viewHolder.rank.setText("TOP " + (position + 4));
            if (info.getThumb().endsWith("gif")) {
                Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).asGif().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.image);
            } else {
                Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(viewHolder.image);
            }
            ActressItemAdapter itemAdapter = new ActressItemAdapter(context, info.getWorks());
            viewHolder.gridView.setAdapter(itemAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ActressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.rank)
        SlantedTextView rank;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.see)
        TextView see;
        @BindView(R.id.sanwei)
        TextView sanwei;
        @BindView(R.id.gridView)
        CustomeGridView gridView;

        ActressViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
