package com.qd.welfare.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.entity.CateGroyInfo;
import com.qd.welfare.entity.NovelCateGoryInfo;
import com.qd.welfare.fragment.category.CateGroyItemFragment;
import com.qd.welfare.fragment.novel.NovelIndexNewItemFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 分类的Adapter
 * Created by scene on 2017/9/22.
 */

public class NovelIndexPageAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private Context context;
    private List<NovelCateGoryInfo> list;
    private LayoutInflater inflate;

    public NovelIndexPageAdapter(FragmentManager fragmentManager, Context context, List<NovelCateGoryInfo> list) {
        super(fragmentManager);
        this.context = context;
        this.list = list;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(list.get(position).getTitle());
        int padding = PtrLocalDisplay.dp2px(10);
        textView.setPadding(padding, 0, padding, 0);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        return NovelIndexNewItemFragment.newInstance(list.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
