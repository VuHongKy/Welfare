package com.qd.welfare.jcvideoplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.anbetter.danmuku.model.DanMuModel;
import com.anbetter.danmuku.model.utils.DimensionUtil;
import com.anbetter.danmuku.view.IDanMuParent;
import com.anbetter.danmuku.view.OnDanMuTouchCallBackListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qd.welfare.App;
import com.qd.welfare.R;
import com.qd.welfare.entity.DanmuInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 弹幕库使用帮助类
 * <p>
 * 建议凡是弹幕中涉及到的图片，大小控制在50kb以内，尺寸控制在100x100以内（单位像素）
 * <p>
 * Created by android_ls on 2016/12/18.
 */
public final class DanMuHelper {

    private ArrayList<WeakReference<IDanMuParent>> mDanMuViewParents;
    private Context mContext;

    public DanMuHelper(Context context) {
        this.mContext = context.getApplicationContext();
        this.mDanMuViewParents = new ArrayList<>();
    }

    public void release() {
        if (mDanMuViewParents != null) {
            for (WeakReference<IDanMuParent> danMuViewParentsRef : mDanMuViewParents) {
                if (danMuViewParentsRef != null) {
                    IDanMuParent danMuParent = danMuViewParentsRef.get();
                    if (danMuParent != null)
                        danMuParent.release();
                }
            }
            mDanMuViewParents.clear();
            mDanMuViewParents = null;
        }

        mContext = null;
    }

    public void add(final IDanMuParent danMuViewParent) {
        if (danMuViewParent != null) {
            danMuViewParent.clear();
        }

        if (mDanMuViewParents != null) {
            mDanMuViewParents.add(new WeakReference<>(danMuViewParent));
        }
    }

    public void addDanMu(DanmuInfo danmuInfo, boolean broadcast) {
        if (mDanMuViewParents != null) {
            WeakReference<IDanMuParent> danMuViewParent = mDanMuViewParents.get(0);
            if (!broadcast) {
                danMuViewParent = mDanMuViewParents.get(0);
            }

            DanMuModel danMuView = createDanMuView(danmuInfo);
            if (danMuViewParent != null && danMuView != null && danMuViewParent.get() != null) {
                danMuViewParent.get().add(danMuView);
            }
        }
    }

    private DanMuModel createDanMuView(final DanmuInfo entity) {
        final DanMuModel danMuView = new DanMuModel();
        danMuView.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
        danMuView.setPriority(DanMuModel.NORMAL);
        danMuView.marginLeft = DimensionUtil.dpToPx(mContext, 30);
        // 图像
        int avatarSize = DimensionUtil.dpToPx(mContext, 30);
        danMuView.avatarWidth = avatarSize;
        danMuView.avatarHeight = avatarSize;

        String avatarImageUrl = entity.getAvatar();
        Glide.with(mContext).load(App.commonInfo.getFile_domain() + avatarImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                danMuView.avatar = CircleBitmapTransform.transform(resource);
            }
        });
        // 显示的文本内容
        String content = entity.getContent();
        danMuView.textSize = DimensionUtil.spToPx(mContext, 14);
        danMuView.textColor = ContextCompat.getColor(mContext, R.color.white);
        danMuView.textMarginLeft = DimensionUtil.dpToPx(mContext, 5);
        danMuView.text = content;

        // 弹幕文本背景
        danMuView.textBackground = ContextCompat.getDrawable(mContext, R.drawable.corners_danmu);
        danMuView.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 15);
        danMuView.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 3);
        danMuView.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 3);
        danMuView.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 15);

        danMuView.enableTouch(true);
        danMuView.setOnTouchCallBackListener(new OnDanMuTouchCallBackListener() {

            @Override
            public void callBack(DanMuModel danMuView) {

            }
        });
        return danMuView;
    }

}