package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.FaceRegisterBean;
import com.example.commonres.utils.Base64Util;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.joker.module_personal.mvp.contract.FaceRegisterContract;
import com.trello.rxlifecycle2.RxLifecycle;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class FaceRegisterPresenter extends BasePresenter<FaceRegisterContract.Model, FaceRegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public FaceRegisterPresenter(FaceRegisterContract.Model model, FaceRegisterContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 人脸注册
     * @param accessToken
     * @param imageByte
     * @param imageType
     * @param groupId
     * @param userId
     * @param userInfo
     * @param qualityControl
     * @param livenessControl
     */
    public void registerFace(String accessToken, byte[] imageByte,
                             String imageType,String groupId,
                             String userId,String userInfo,
                             String qualityControl,String livenessControl) {
        mModel.registerFace(accessToken, Base64Util.encode(imageByte),imageType,
                groupId,userId,userInfo,qualityControl,livenessControl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new Consumer<FaceRegisterBean>() {
                    @Override
                    public void accept(FaceRegisterBean faceRegisterBean) throws Exception {
                        if (faceRegisterBean.getError_code() == 0 &&
                                faceRegisterBean.getError_msg().equals("SUCCESS"))
                            mRootView.faceRegisterResult(true);
                        else
                            mRootView.faceRegisterResult(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.faceRegisterResult(false);
                    }
                });


    }
}
