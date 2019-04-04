package com.example.commonres.utils;

import android.content.Context;
import android.widget.Toast;

import com.jess.arms.utils.ArmsUtils;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/27.
 */
public class ToastUtil {


    /**
     * 单例 toast
     * @param context
     * @param string
     */
    public static void makeText(Context context, String string) {
        Toast mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        mToast.setText(string);
        mToast.show();
    }


    /**
     * 单例 toast
     * @param context
     * @param resId
     */
    public static void makeText(Context context, int resId) {
        String messing = context.getResources().getString(resId);
        ArmsUtils.makeText(context,messing);
    }
}
