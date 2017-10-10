package com.qd.welfare.fragment.novel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.NovelIndexPageAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.NovelCateGoryInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.DrawableBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 小说首页第二种
 * Created by scene on 17-8-29.
 */

public class NovelIndexNewFragment extends BaseMainFragment {
    @BindView(R.id.tab)
    ScrollIndicatorView tab;
    @BindView(R.id.pre_step)
    ImageView preStep;
    @BindView(R.id.next_step)
    ImageView nextStep;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;
    private IndicatorViewPager indicatorViewPager;
    private List<NovelCateGoryInfo> list = new ArrayList<>();

    public static NovelIndexNewFragment newInstance() {
        Bundle args = new Bundle();
        NovelIndexNewFragment fragment = new NovelIndexNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
    }

    private void initTabLayout() {
        tab.setBackgroundColor(Color.BLACK);
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
        indicatorViewPager = new IndicatorViewPager(tab, viewPager);
        indicatorViewPager.setAdapter(new NovelIndexPageAdapter(getChildFragmentManager(), getContext(), list));
    }

    private void initView() {
        MainActivity.upLoadPageInfo(PageConfig.CATEGORY, 0);
        initTabLayout();
        getData();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    preStep.setImageResource(R.drawable.ic_arrow_right_d);
                } else {
                    preStep.setImageResource(R.drawable.ic_arrow_right_s);
                }
                if (position == viewPager.getAdapter().getCount() - 1) {
                    nextStep.setImageResource(R.drawable.ic_arrow_left_d);
                } else {
                    nextStep.setImageResource(R.drawable.ic_arrow_left_s);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void getData() {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            statusLayout.showLoading();
            OkGo.<LzyResponse<List<NovelCateGoryInfo>>>get(ApiUtil.API_PRE + ApiUtil.NOVEL_CATEGORY)
                    .tag(ApiUtil.NOVEL_CATEGORY_TAG)
                    .execute(new JsonCallback<LzyResponse<List<NovelCateGoryInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<NovelCateGoryInfo>>> response) {
                            try {
                                if (response.body().data == null || response.body().data.size() == 0) {
                                    statusLayout.showNone();
                                } else {
                                    statusLayout.showContent();
                                    list.clear();
                                    list.addAll(response.body().data);
                                    indicatorViewPager.getAdapter().notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Response<LzyResponse<List<NovelCateGoryInfo>>> response) {
                            super.onError(response);
                            try {
                                statusLayout.showFailed(retryListener);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            statusLayout.showNetError();
        }

    }

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.NOVEL_CATEGORY_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData();
        }
    };

    @OnClick(R.id.next_step)
    public void onClickNextStep() {
        if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.pre_step)
    public void onClickPreStep() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
}
