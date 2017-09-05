package com.qd.welfare.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.base.BaseBackFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 用户协议
 * Created by scene on 2017/9/5.
 */

public class UserAgreementFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.content)
    TextView content;
    Unbinder unbinder;

    public static UserAgreementFragment newInstance() {
        Bundle args = new Bundle();
        UserAgreementFragment fragment = new UserAgreementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_agreement, container, false);
        unbinder = ButterKnife.bind(this, (view));
        return attachToSwipeBack(view);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        toolbarTitle.setText("用户协议");
        initToolbarNav(toolbar);
        content.setText(App.commonInfo.getUser_agreement());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
