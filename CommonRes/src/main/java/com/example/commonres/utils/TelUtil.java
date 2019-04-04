package com.example.commonres.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/4.
 */
public class TelUtil {

    /**
     * 打电话
     */
    public static void callPhone(String tel, Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            activity.startActivity(callIntent);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            activity.startActivity(callIntent);
        }
    }
}
