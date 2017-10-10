package com.qd.welfare.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        if (path instanceof String && ((String) path).endsWith("gif")) {
            Glide.with(context).load(path).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else {
            Glide.with(context).load(path).into(imageView);
        }
    }
}