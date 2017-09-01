package com.qd.welfare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.entity.VideoResultInfo;
import com.qd.welfare.widgets.CustomeGridView;
import com.qd.welfare.widgets.drawableratingbar.DrawableRatingBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;

/**
 * 视频
 * Created by scene on 2017/9/1.
 */

public class VideoAdapter extends BaseAdapter {
    private static final int TYPE_IMAGE_1 = 0;
    private static final int TYPE_IMAGE_2 = 1;

    private Context context;
    private List<VideoResultInfo.VideoIndexInfo> list;
    private LayoutInflater inflater;

    private OnVideoItemClickListener onVideoItemClickListener;

    public VideoAdapter(Context context, List<VideoResultInfo.VideoIndexInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setOnVideoItemClickListener(OnVideoItemClickListener onVideoItemClickListener) {
        this.onVideoItemClickListener = onVideoItemClickListener;
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
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(position);

        if (view == null) {
            if (type == TYPE_IMAGE_1) {
                view = inflater.inflate(R.layout.fragment_video_item1, viewGroup, false);
                holder1 = new ViewHolder1(view);
                view.setTag(holder1);
            } else {
                view = inflater.inflate(R.layout.fragment_video_item2, viewGroup, false);
                holder2 = new ViewHolder2(view);
                view.setTag(holder2);
            }
        } else {
            if (type == TYPE_IMAGE_1) {
                holder1 = (ViewHolder1) view.getTag();
            } else {
                holder2 = (ViewHolder2) view.getTag();
            }
        }

        final VideoResultInfo.VideoIndexInfo info = list.get(position);

        if (type == TYPE_IMAGE_1) {
            holder1.title.setText(info.getTitle());
            if (info.getVideo().size() > 0) {
                holder1.videoName.setText(info.getVideo().get(0).getTitle());
                holder1.videoPlayCount.setText("播放：" + info.getVideo().get(0).getPlay_times());
                holder1.ratingBar.setMax(5);
                holder1.ratingBar.setRating((int) info.getVideo().get(0).getStar());
                Glide.with(context).load(App.commonInfo.getFile_domain() + info.getVideo().get(0).getThumb())
                        .centerCrop().into(holder1.image);
                holder1.tagLayout.setTags(info.getVideo().get(0).getTags());
                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onVideoItemClickListener != null) {
                            onVideoItemClickListener.onVideoItemClick(info.getVideo().get(0));
                        }
                    }
                });
            }
        } else {
            holder2.title.setText(info.getTitle());
            VideoItemAdapter adapter = new VideoItemAdapter(context, info.getVideo());
            holder2.itemGridView.setAdapter(adapter);
            holder2.itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (onVideoItemClickListener != null) {
                        onVideoItemClickListener.onVideoItemClick(info.getVideo().get(i));
                    }
                }
            });
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getShow_type() == 1 ? TYPE_IMAGE_1 : TYPE_IMAGE_2;
    }

    static class ViewHolder1 {
        @BindView(R.id.itemView)
        LinearLayout itemView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.videoName)
        TextView videoName;
        @BindView(R.id.videoPlayCount)
        TextView videoPlayCount;
        @BindView(R.id.ratingBar)
        DrawableRatingBar ratingBar;
        @BindView(R.id.image)
        SelectableRoundedImageView image;
        @BindView(R.id.tag_layout)
        TagContainerLayout tagLayout;

        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.itemGridView)
        CustomeGridView itemGridView;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnVideoItemClickListener {
        void onVideoItemClick(VideoInfo info);
    }
}
