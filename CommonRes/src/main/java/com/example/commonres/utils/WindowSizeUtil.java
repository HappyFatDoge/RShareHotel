package com.example.commonres.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class WindowSizeUtil {

    public static int getWidth(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static int getHeight(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    public static float getProportion(Context context){
        int width = getWidth(context);
        int height = getHeight(context);
        return height * 1.0f / width * 1.0f;
    }


}
