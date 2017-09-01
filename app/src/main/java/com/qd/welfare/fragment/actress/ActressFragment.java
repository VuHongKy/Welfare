package com.qd.welfare.fragment.actress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.ActressAdapter;
import com.qd.welfare.base.BaseMainFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.ActressInfo;
import com.qd.welfare.event.StartBrotherEvent;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.itemDecoration.SpacesItemDecoration;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;
import wiki.scene.loadmore.recyclerview.RecyclerAdapterWithHF;
import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressFragment extends BaseMainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;

    private ToastUtils toastUtils;

    private List<ActressInfo> list = new ArrayList<>();
    private ActressAdapter adapter;


    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;

    public static ActressFragment newInstance() {
        Bundle args = new Bundle();
        ActressFragment fragment = new ActressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actress, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.ACTOR_LIST, 0);
        initView();
    }

    private void initView() {
        toastUtils = ToastUtils.getInstance(_mActivity);

        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });

        adapter = new ActressAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(PtrLocalDisplay.designedDP2px(1)));
        RecyclerAdapterWithHF mAdapter = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(mAdapter);
        getData(true);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                EventBus.getDefault().post(new StartBrotherEvent(ActorDetailFragment.newInstance(list.get(position).getId(), list.get(position).getName())));
            }
        });
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_actress_header, null);
        layout1 = headerView.findViewById(R.id.layout1);
        layout2 = headerView.findViewById(R.id.layout2);
        layout3 = headerView.findViewById(R.id.layout3);
        name1 = headerView.findViewById(R.id.name1);
        name2 = headerView.findViewById(R.id.name2);
        name3 = headerView.findViewById(R.id.name3);
        image1 = headerView.findViewById(R.id.image1);
        image2 = headerView.findViewById(R.id.image2);
        image3 = headerView.findViewById(R.id.image3);
        mAdapter.addHeader(headerView);
    }

    private void bindHeaderView(List<ActressInfo> headerList) {
        layout1.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);
        switch (headerList.size()) {
            case 3:
                layout3.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(App.commonInfo.getFile_domain() + headerList.get(2).getThumb())
                        .bitmapTransform(new CropCircleTransformation(getContext())).into(image3);
                name3.setText(headerList.get(2).getName());
            case 2:
                layout2.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(App.commonInfo.getFile_domain() + headerList.get(1).getThumb())
                        .bitmapTransform(new CropCircleTransformation(getContext())).into(image2);
                name2.setText(headerList.get(1).getName());
            case 1:
                layout1.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(App.commonInfo.getFile_domain() + headerList.get(0).getThumb())
                        .bitmapTransform(new CropCircleTransformation(getContext())).into(image1);
                name1.setText(headerList.get(0).getName());
                break;
        }
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            OkGo.<LzyResponse<List<ActressInfo>>>get(ApiUtil.API_PRE + ApiUtil.ACTRESS)
                    .tag(ApiUtil.ACTRESS_TAG)
                    .execute(new JsonCallback<LzyResponse<List<ActressInfo>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<ActressInfo>>> response) {
                            if (isFirst) {
                                statusLayout.showContent();
                            } else {
                                ptrLayout.refreshComplete();
                            }
                            if (response.body().data == null || response.body().data.size() == 0) {
                                statusLayout.showNone(retryListener);
                            } else {
                                list.clear();
                                List<ActressInfo> headerList = new ArrayList<ActressInfo>();
                                for (int i = 0; i < response.body().data.size(); i++) {
                                    if (i < 3) {
                                        headerList.add(response.body().data.get(i));
                                    } else {
                                        list.add(response.body().data.get(i));
                                    }
                                }
                                bindHeaderView(headerList);
                                adapter.notifyDataSetChanged();
                            }


                        }

                        @Override
                        public void onError(Response<LzyResponse<List<ActressInfo>>> response) {
                            super.onError(response);
                            if (isFirst) {
                                statusLayout.showNetError(retryListener);
                            } else {
                                ptrLayout.refreshComplete();
                                ToastUtils.getInstance(getContext()).showToast("请检查网络连接");
                            }
                        }
                    });

        } else {
            if (isFirst) {
                statusLayout.showNetError();
            } else {
                toastUtils.showToast("请检查网络链接");
            }
        }
    }


    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.ACTRESS_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
