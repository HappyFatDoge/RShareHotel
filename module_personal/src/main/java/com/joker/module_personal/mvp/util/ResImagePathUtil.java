package com.joker.module_personal.mvp.util;

import android.content.ContentResolver;
import android.content.res.Resources;


/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/3.
 * 获取res中图片的Path
 */
public class ResImagePathUtil {

    public static String getPath(Resources resource, int resId){
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resource.getResourcePackageName(resId) + "/" +
                resource.getResourceTypeName(resId) + "/" +
                resource.getResourceEntryName(resId);
    }

}
