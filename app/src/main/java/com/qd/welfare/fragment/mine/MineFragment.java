package com.qd.welfare.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.adapter.RecommendAdapter;
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.VideoInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.widgets.CustomeGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的
 * Created by scene on 17-8-29.
 */

public class MineFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.gridView)
    CustomeGridView gridView;

    Unbinder unbinder;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
        MainActivity.upLoadPageInfo(PageConfig.MINE, 0);
        getData();
    }

    private void getData() {
        HttpParams params = new HttpParams();
        params.put("video_id", 0);
        OkGo.<LzyResponse<List<VideoInfo>>>get(ApiUtil.API_PRE + ApiUtil.RECOMMEND)
                .tag(ApiUtil.RECOMMEND_TAG)
                .params(params)
                .execute(new JsonCallback<LzyResponse<List<VideoInfo>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<VideoInfo>>> response) {
                        RecommendAdapter adapter = new RecommendAdapter(getContext(), response.body().data);
                        gridView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.RECOMMEND_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
