package com.qd.welfare.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.adapter.ShopPagerFragmentAdapter;
import com.qd.welfare.base.BaseMainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 商城
 * Created by scene on 17-8-29.
 */

public class ShopFragment extends BaseMainFragment {
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;

    public static ShopFragment newInstance() {
        Bundle args = new Bundle();
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        String tabTitle[] = {"男性", "女性"};
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(GoodsDetailFragment.newInstance(1));
        fragmentList.add(GoodsDetailFragment.newInstance(2));
        tab.addTab(tab.newTab().setText(tabTitle[0]));
        tab.addTab(tab.newTab().setText(tabTitle[1]));
        ShopPagerFragmentAdapter adapter = new ShopPagerFragmentAdapter(getChildFragmentManager(), tabTitle, fragmentList);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);
//        for (int i = 0; i < adapter.getCount(); i++) {
//            TabLayout.Tab tabItem = tab.getTabAt(i);//获得每一个tab
//            tabItem.setCustomView(R.layout.layout_custom_tab);//给每一个tab设置view
//            if (i == 0) {
//                // 设置第一个tab的TextView是被选择的样式
//                tabItem.getCustomView().findViewById(R.id.tab_text).setSelected(true);//第一个tab被选中
//            }
//            TextView textView = (TextView) tabItem.getCustomView().findViewById(R.id.tab_text);
//            textView.setText(tabTitle[i]);//设置tab上的文字
//        }
//        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
