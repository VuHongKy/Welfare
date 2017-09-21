package com.qd.welfare.base;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qd.welfare.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;


/**
 * Created by scene on 17/3/13.
 */
public class BaseBackFragment extends SwipeBackFragment {

    /**
     * 销毁butterknife
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}
