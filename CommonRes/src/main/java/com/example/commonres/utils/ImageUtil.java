package com.example.commonres.utils;

import android.graphics.Bitmap;

import com.example.commonres.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class ImageUtil {

    public static DisplayImageOptions createOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.image_loading)
            .showImageForEmptyUri(R.mipmap.image_loading)
            .showImageOnFail(R.mipmap.image_loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            // .delayBeforeLoading(100)
            .build();
        return options;
    }
}
