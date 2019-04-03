package com.joker.module_personal.mvp.presenter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.commonres.beans.FaceRegisterBean;
import com.example.commonres.beans.User;
import com.example.commonres.utils.Base64Util;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.joker.module_personal.mvp.contract.FaceRegisterContract;
import com.trello.rxlifecycle2.RxLifecycle;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
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
                                faceRegisterBean.getError_msg().equals("SUCCESS")){
                            //更新数据，设置人脸注册boolean
                            User mUser = LoginUtil.getInstance().getUser();
                            mUser.setFaceRegister(true);
                            mUser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null)
                                        mRootView.faceRegisterResult(true, "人脸注册成功");
                                    else{
                                        Log.d("data",e.getMessage());
                                        mRootView.faceRegisterResult(false, "人脸注册失败，请重新注册");
                                    }
                                }
                            });
                        }else
                            mRootView.faceRegisterResult(false, "人脸注册失败，请重新注册");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.faceRegisterResult(false, "人脸注册失败，请重新注册");
                    }
                });


    }
}
