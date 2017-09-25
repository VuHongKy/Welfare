package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.ActorDetailResultInfo;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ActorDetailResultInfo.GalleryBean.DataBean> list;
    private LayoutInflater inflater;

    public ActressDetailAdapter(Context context, List<ActorDetailResultInfo.GalleryBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActressItemViewHolder(inflater.inflate(R.layout.fragment_actress_item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActressItemViewHolder viewHolder = (ActressItemViewHolder) holder;
        ActorDetailResultInfo.GalleryBean.DataBean info = list.get(position);
        viewHolder.title.setVisibility(View.GONE);
        viewHolder.starLayout.setVisibility(View.INVISIBLE);
        Glide.with(context).load(App.commonInfo.getFile_domain() + info.getThumb()).centerCrop().into(viewHolder.image);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ActressItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        RatioImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.star_layout)
        LinearLayout starLayout;

        ActressItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
