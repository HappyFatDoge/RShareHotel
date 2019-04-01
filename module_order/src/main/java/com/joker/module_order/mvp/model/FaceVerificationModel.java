package com.joker.module_order.mvp.model;

import android.app.Application;

import com.example.commonres.beans.FaceVerificationBean;
import com.example.commonres.utils.RetrofitFactory;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.joker.module_order.mvp.contract.FaceVerificationContract;

import javax.inject.Inject;

import io.reactivex.Flowable;


@ActivityScope
public class FaceVerificationModel extends BaseModel implements FaceVerificationContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FaceVerificationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     * 人脸验证
     * @param accessToken
     * @param image
     * @param imageType
     * @param groupList
     * @param qualityControl
     * @param livenessControl
     * @param userId
     * @param maxUserNum
     * @return
     */
    @Override
    public Flowable<FaceVerificationBean> verification(String accessToken, String image,
                                                       String imageType, String groupList,
                                                       String qualityControl, String livenessControl,
                                                       String userId, int maxUserNum) {
        return RetrofitFactory.getServiceInterface().verification(accessToken,image,
            imageType,groupList, qualityControl,livenessControl,userId,maxUserNum);
    }
}