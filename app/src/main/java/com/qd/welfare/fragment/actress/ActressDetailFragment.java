package com.qd.welfare.fragment.actress;

import android.os.Bundle;

import com.qd.welfare.base.BaseBackFragment;

/**
 * 女友详情
 * Created by scene on 17-8-29.
 */

public class ActressDetailFragment extends BaseBackFragment {
    public static ActressDetailFragment newInstance() {
        Bundle args = new Bundle();
        ActressDetailFragment fragment = new ActressDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
