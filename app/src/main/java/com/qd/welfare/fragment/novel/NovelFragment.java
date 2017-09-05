package com.qd.welfare.fragment.novel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qd.welfare.R;
import com.qd.welfare.base.BaseMainFragment;

/**
 * 小说首页
 * Created by scene on 2017/9/4.
 */

public class NovelFragment extends BaseMainFragment {
    public static NovelFragment newInstance() {
        Bundle args = new Bundle();
        NovelFragment fragment = new NovelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }
}
