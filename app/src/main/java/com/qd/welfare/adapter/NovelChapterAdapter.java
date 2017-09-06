package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.entity.NovelChapterInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 章节列表
 * Created by scene on 2017/9/6.
 */

public class NovelChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NovelChapterInfo> list;

    public NovelChapterAdapter(Context context, List<NovelChapterInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChapterViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_novel_chapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NovelChapterInfo info = list.get(position);
        ChapterViewHolder viewHolder = (ChapterViewHolder) holder;
        viewHolder.title.setText(info.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        ChapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
