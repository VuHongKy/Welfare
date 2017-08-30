package com.qd.welfare.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import wiki.scene.loadmore.utils.PtrLocalDisplay;

/**
 * Case By: 夺宝的分割线
 * package:wiki.scene.shop.itemDecoration
 * Author：scene on 2017/6/28 17:28
 */
public class ActorDetailSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing = PtrLocalDisplay.designedDP2px(8);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position > 0) {
            if (position % 2 == 1) {
                outRect.right = spacing / 2;
            } else {
                outRect.left = spacing / 2;
            }
            if (position > 2) {
                outRect.top = spacing;
            }
        }
    }
}