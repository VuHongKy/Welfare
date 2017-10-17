package com.qd.welfare.fragment.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.R;
import com.qd.welfare.base.BaseBackFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 客服中心
 * Created by scene on 2017/9/7.
 */

public class ServiceCenterFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    Unbinder unbinder;
    @BindView(R.id.service_qq)
    TextView serviceQq;

    public static ServiceCenterFragment newInstance() {
        Bundle args = new Bundle();
        ServiceCenterFragment fragment = new ServiceCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_center, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        try {
            toolbarTitle.setText("客服中心");
            initToolbarNav(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                serviceQq.setText(Html.fromHtml("<u>170059106</u>", Html.FROM_HTML_MODE_COMPACT));
            } else {
                serviceQq.setText(Html.fromHtml("<u>170059106</u>"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.service_qq)
    public void onClickServiceQQ() {
        try {
            String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=170059106&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
