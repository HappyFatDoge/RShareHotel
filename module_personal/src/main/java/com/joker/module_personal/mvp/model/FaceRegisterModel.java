package com.joker.module_personal.mvp.model;

import android.app.Application;

import com.example.commonres.beans.FaceRegisterBean;
import com.example.commonres.utils.RetrofitFactory;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.joker.module_personal.mvp.contract.FaceRegisterContract;

import io.reactivex.Flowable;


@ActivityScope
public class FaceRegisterModel extends BaseModel implements FaceRegisterContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FaceRegisterModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     * 人脸注册
     * @param accessToken
     * @param image
     * @param imageType
     * @param groupId
     * @param userId
     * @param userInfo
     * @param qualityControl
     * @param livenessControl
     * @return
     */
    @Override
    public Flowable<FaceRegisterBean> registerFace(String accessToken, String image,
                                                   String imageType,String groupId,
                                                   String userId,String userInfo,
                                                   String qualityControl,String livenessControl) {

        return RetrofitFactory.getServiceInterface()
                .registerFace(accessToken,image, imageType,
                        groupId,userId,userInfo,qualityControl,livenessControl);

    }
}