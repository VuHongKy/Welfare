package com.qd.welfare;

import android.os.Bundle;

import com.qd.welfare.base.BaseFragment;

/**
 * 主界面
 * Created by scene on 17-8-25.
 */

public class MainFragment extends BaseFragment {
    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
