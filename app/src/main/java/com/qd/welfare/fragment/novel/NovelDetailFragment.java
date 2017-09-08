package com.qd.welfare.fragment.novel;

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
import com.qd.welfare.base.BaseBackFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.NovelDetailInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 小说详情
 * Created by scene on 2017/9/6.
 */

public class NovelDetailFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    Unbinder unbinder;
    private int chapterId;
    private String chapterName;

    public static NovelDetailFragment newInstance(int chapterId, String chapterName) {
        Bundle args = new Bundle();
        args.putInt("id", chapterId);
        args.putString("name", chapterName);
        NovelDetailFragment fragment = new NovelDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getInt("id", 0);
            chapterName = getArguments().getString("name");
        } else {
            onBackPressedSupport();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText(chapterName);
        initToolbarNav(toolbar);
        MainActivity.upLoadPageInfo(PageConfig.NOVEL_DETAIL, chapterId);
        getData();
    }

    private void getData() {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            statusLayout.showLoading();
            HttpParams params = new HttpParams();
            params.put("chapter_id", chapterId);
            OkGo.<LzyResponse<NovelDetailInfo>>get(ApiUtil.API_PRE + ApiUtil.NOVEL_DETAIL)
                    .tag(ApiUtil.NOVEL_DETAIL_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<NovelDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<NovelDetailInfo>> response) {
                            try {
                                statusLayout.showContent();
                                content.setText(response.body().data.getContent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<NovelDetailInfo>> response) {
                            super.onError(response);
                            try {
                                statusLayout.showFailed(retryListener);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
        OkGo.getInstance().cancelTag(ApiUtil.NOVEL_DETAIL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
