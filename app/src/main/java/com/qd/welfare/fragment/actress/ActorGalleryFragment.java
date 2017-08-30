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
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.entity.GalleryInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 查看大图
 * Created by scene on 17-8-29.
 */

public class ActorGalleryFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.page)
    TextView page;
    Unbinder unbinder;

    private int actorId;
    private String title;
    private List<GalleryInfo> list = new ArrayList<>();

    public static ActorGalleryFragment newInstance(int actorId, String title) {
        Bundle args = new Bundle();
        args.putInt("id", actorId);
        args.putString("title", title);
        ActorGalleryFragment fragment = new ActorGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actorId = getArguments().getInt("id");
            title = getArguments().getString("title");
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
        toolbarTitle.setText(title);
        initToolbarNav(toolbar);
        initView();
    }

    private void initView() {
        getData();
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
                Glide.with(getContext()).load(App.commonInfo.getFile_domain() + list.get(position).getThumb()).into(view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        changePageText();


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
    }

    private void changePageText() {
        page.setText((viewPager.getCurrentItem() + 1) + "/" + list.size());
    }

    private void getData() {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            statusLayout.showLoading();
            HttpParams params = new HttpParams();
            params.put("actor_id", actorId);
            OkGo.<LzyResponse<List<GalleryInfo>>>get(ApiUtil.API_PRE + ApiUtil.GALLERY)
                    .tag(ApiUtil.GALLERY_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<List<GalleryInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<GalleryInfo>>> response) {
                            statusLayout.showContent();
                            list.addAll(response.body().data);
                            initViewPager();
                        }

                        @Override
                        public void onError(Response<LzyResponse<List<GalleryInfo>>> response) {
                            super.onError(response);
                            statusLayout.showFailed(retryListener);
                        }
                    });

        } else {
            statusLayout.showNetError(retryListener);
        }

    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData();
        }
    };

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.GALLERY_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
