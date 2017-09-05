package com.qd.welfare.fragment.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.CateGroyViewPagerAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.CateGroyInfo;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.DialogUtil;
import com.qd.welfare.utils.NetWorkUtils;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 正妹分类
 * Created by scene on 17-8-29.
 */

public class CategoryFragment extends BaseMainFragment {
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.pre_step)
    ImageView preStep;
    @BindView(R.id.next_step)
    ImageView nextStep;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
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

    private void initView() {
        MainActivity.upLoadPageInfo(PageConfig.CATEGORY, 0);
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
            OkGo.<LzyResponse<List<CateGroyInfo>>>get(ApiUtil.API_PRE + ApiUtil.CATEGORY)
                    .tag(ApiUtil.CATEGORY_TAG)
                    .execute(new JsonCallback<LzyResponse<List<CateGroyInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<CateGroyInfo>>> response) {
                            if (response.body().data == null || response.body().data.size() == 0) {
                                statusLayout.showNone();
                            } else {
                                statusLayout.showContent();
                                CateGroyViewPagerAdapter adapter = new CateGroyViewPagerAdapter(getContext(), response.body().data);
                                viewPager.setAdapter(adapter);
                                tab.setupWithViewPager(viewPager);
                                adapter.setOnCateGoryItemClickListener(new CateGroyViewPagerAdapter.OnCateGoryItemClickListener() {
                                    @Override
                                    public void onCateGoryItemClick(CateGroyInfo info) {
                                        if (App.userInfo.getRole() > 1) {
                                            EventBus.getDefault().post(new StartBrotherEvent(CategoryActorFragment.newInstance(info)));
                                        } else {
                                            DialogUtil.showOpenViewDialog(getContext(), PageConfig.CATEGORY, info.getId());
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<List<CateGroyInfo>>> response) {
                            super.onError(response);
                            statusLayout.showFailed(retryListener);
                        }
                    });
        } else {
            statusLayout.showNetError();
        }

    }

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.CATEGORY_TAG);
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
