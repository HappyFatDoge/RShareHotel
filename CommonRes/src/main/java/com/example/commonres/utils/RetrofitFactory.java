package com.example.commonres.utils;

import com.example.commonres.CommonService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class RetrofitFactory {

    private static String baseUrl = "https://aip.baidubce.com/rest/2.0/face/v3/";

    public static CommonService getServiceInterface(){

        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CommonService.class);
    }

}
