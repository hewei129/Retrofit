package com.hw.net.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hw.net.R


/**
 * @author hewei(David)
 * @date 2020/6/5  10:00 AM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */


val options =   RequestOptions().centerCrop().placeholder(R.drawable.ic_default).error(R.drawable.ic_default)

fun imgLoad(context: Context, url: String, iv: ImageView){
    Glide.with(context)
        .load(url)
        .apply(options)
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
        .apply(RequestOptions.skipMemoryCacheOf(false))
        .thumbnail(0.1f)
        .into(iv)
}


fun imgLoad(context: Context, url: String, iv: ImageView, resourceId: Int){
    val options  =   RequestOptions().centerCrop().placeholder(resourceId).error(resourceId)

    Glide.with(context)
        .load(url)
        .apply(options)
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
        .apply(RequestOptions.skipMemoryCacheOf(false))
        .thumbnail(0.1f)
        .into(iv)
}

