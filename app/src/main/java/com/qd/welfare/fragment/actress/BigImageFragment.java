package com.qd.welfare.fragment.actress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.http.api.ApiUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 查看大图
 * Created by scene on 17-8-29.
 */

public class BigImageFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.page)
    TextView page;
    Unbinder unbinder;

    private ArrayList<String> list = new ArrayList<>();
    private int position = 0;

    public static BigImageFragment newInstance(ArrayList<String> urls, int position) {
        Bundle args = new Bundle();
        args.putStringArrayList("urls", urls);
        args.putInt("position", position);
        BigImageFragment fragment = new BigImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getStringArrayList("urls") != null) {
            list.clear();
            list.addAll(getArguments().getStringArrayList("urls"));
            position = getArguments().getInt("position");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_gallery, container, false);
        unbinder = ButterKnife.bind(this, (view));
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initToolbarNav(toolbar);
        initView();
    }

    private void initView() {
        statusLayout.showContent();
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(getContext());
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(getContext()).load(App.commonInfo.getFile_domain() + list.get(position)).into(view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changePageText();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position);
        changePageText();
    }

    private void changePageText() {
        page.setText((viewPager.getCurrentItem() + 1) + "/" + list.size());
    }

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.GALLERY_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
