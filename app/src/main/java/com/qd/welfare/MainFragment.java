package com.qd.welfare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qd.welfare.base.BaseFragment;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.event.TabSelectedEvent;
import com.qd.welfare.fragment.actress.ActressFragment;
import com.qd.welfare.fragment.category.CategoryNewFragment;
import com.qd.welfare.fragment.mine.MineFragment;
import com.qd.welfare.fragment.novel.NovelIndexNewFragment;
import com.qd.welfare.fragment.shop.ShopFragment;
import com.qd.welfare.fragment.video.VideoFragment;
import com.qd.welfare.view.BottomBar;
import com.qd.welfare.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;
import wiki.scene.loadmore.utils.SceneLogUtil;

/**
 * 主界面
 * Created by scene on 17-8-25.
 */

public class MainFragment extends BaseFragment {
    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;
    public static final int FIVE = 4;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.toolbarLayout)
    RelativeLayout toolbarLayout;

    private SupportFragment[] mFragments = new SupportFragment[5];
    private List<String> tabNames = new ArrayList<>();

    Unbinder unbinder;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(VideoFragment.class);
        tabNames.add(getString(R.string.tab_name_video));
        tabNames.add(getString(R.string.tab_name_category));
        tabNames.add(getString(R.string.tab_name_actress));
        tabNames.add(getString(R.string.tab_name_novel));
        tabNames.add(getString(R.string.tab_name_mine));
        if (firstFragment == null) {
            mFragments[FIRST] = VideoFragment.newInstance();
            mFragments[SECOND] = CategoryNewFragment.newInstance();
            mFragments[THIRD] = ActressFragment.newInstance();
            mFragments[FOUR] = NovelIndexNewFragment.newInstance();
            mFragments[FIVE] = MineFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR],
                    mFragments[FIVE]);
        } else {
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.findFragmentByTag自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(CategoryNewFragment.class);
            mFragments[THIRD] = findChildFragment(ActressFragment.class);
            mFragments[FOUR] = findChildFragment(NovelIndexNewFragment.class);
            mFragments[FIVE] = findChildFragment(MineFragment.class);
        }
        try {
            toolbarTitle.setText(tabNames.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_video_d, R.drawable.ic_tab_video_s, tabNames.get(FIRST)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_category_d, R.drawable.ic_tab_category_s, tabNames.get(SECOND)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_actress_d, R.drawable.ic_tab_actress_s, tabNames.get(THIRD)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_novel_d, R.drawable.ic_tab_novel_s, tabNames.get(FOUR)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_mine_d, R.drawable.ic_tab_mine_s, tabNames.get(FIVE)));


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                toolbarTitle.setText(tabNames.get(position));
                toolbarLayout.setVisibility(position == 4 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {
            SceneLogUtil.e("返回");
        }
    }

    @OnClick(R.id.toolbar_mine)
    public void onClickToolbarMine() {
        start(ShopFragment.newInstance());
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Subscribe
    public void toIndexPage(TabSelectedEvent event) {
        mBottomBar.setCurrentItem(event.position);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }
}
