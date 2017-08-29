package com.qd.welfare.fragment.actress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qd.welfare.R;
import com.qd.welfare.base.BaseMainFragment;

/**
 * 女优百科
 * Created by scene on 17-8-29.
 */

public class ActressFragment extends BaseMainFragment {
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
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }
}
