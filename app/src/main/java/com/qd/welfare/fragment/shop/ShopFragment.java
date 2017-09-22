package com.qd.welfare.fragment.shop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qd.welfare.R;
import com.qd.welfare.adapter.ShopPageAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.DrawableBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 商城
 * Created by scene on 17-8-29.
 */

public class ShopFragment extends BaseMainFragment {
    @BindView(R.id.tab)
    ScrollIndicatorView tab;
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
        List<String> list = new ArrayList<>();
        list.add("男性");
        list.add("女性");
        tab.setScrollBar(new DrawableBar(getContext(), R.drawable.round_border_white_selector, ScrollBar.Gravity.CENTENT_BACKGROUND) {
            @Override
            public int getHeight(int tabHeight) {
                return tabHeight - PtrLocalDisplay.dp2px(12);
            }

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - PtrLocalDisplay.dp2px(12);
            }
        });
        // 设置滚动监听
        tab.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.parseColor("#F85788"), Color.WHITE));
        viewPager.setOffscreenPageLimit(2);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(tab, viewPager);
        indicatorViewPager.setAdapter(new ShopPageAdapter(getChildFragmentManager(), getContext(), list));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
