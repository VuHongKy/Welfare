package com.qd.welfare.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.CateGroyInfo;
import com.qd.welfare.widgets.RatioImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CateGroyViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<CateGroyInfo> mDrawableResIdList;

    private OnCateGoryItemClickListener onCateGoryItemClickListener;

    public CateGroyViewPagerAdapter(Context context, List<CateGroyInfo> resIdList) {
        super();
        mContext = context;
        mDrawableResIdList = resIdList;
    }


    public void setOnCateGoryItemClickListener(OnCateGoryItemClickListener onCateGoryItemClickListener) {
        this.onCateGoryItemClickListener = onCateGoryItemClickListener;
    }

    @Override
    public int getCount() {
        if (mDrawableResIdList != null) {
            return mDrawableResIdList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object != null && mDrawableResIdList != null) {
            Integer resId = (Integer) ((ImageView) object).getTag();
            if (resId != null) {
                for (int i = 0; i < mDrawableResIdList.size(); i++) {
                    if (resId.equals(mDrawableResIdList.get(i))) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View container, final int position) {
        if (mDrawableResIdList != null && position < mDrawableResIdList.size()) {
            CateGroyInfo info = mDrawableResIdList.get(position);
            String filePath = info.getThumb();
            if (filePath != null) {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_categroy_item, null);
                RatioImageView imageView = itemView.findViewById(R.id.image);
                ImageView backImage = itemView.findViewById(R.id.backImage);
                TextView click_number = itemView.findViewById(R.id.click_number);
                TextView update_number = itemView.findViewById(R.id.update_number);
                TextView name = itemView.findViewById(R.id.name);

                click_number.setText(info.getView_times() + "人点击");
                name.setText(info.getTitle());
                update_number.setText("更新至" + info.getUpdate_to() + "部");

                Glide.with(mContext).load(App.commonInfo.getFile_domain() + filePath).centerCrop().into(imageView);
                Glide.with(mContext).load(App.commonInfo.getFile_domain() + filePath).bitmapTransform(new BlurTransformation(mContext)).into(backImage);
                itemView.setTag(filePath);

                ((ViewPager) container).addView(itemView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCateGoryItemClickListener != null) {
                            onCateGoryItemClickListener.onCateGoryItemClick(mDrawableResIdList.get(position));
                        }
                    }
                });
                return itemView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public void startUpdate(View container) {
    }

    @Override
    public void finishUpdate(View container) {
    }

    public void updateData(List<CateGroyInfo> filePath) {
        if (filePath == null) {
            return;
        }
        mDrawableResIdList = filePath;
        this.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDrawableResIdList.get(position).getTitle();
    }

    public interface OnCateGoryItemClickListener {
        void onCateGoryItemClick(CateGroyInfo info);
    }
}