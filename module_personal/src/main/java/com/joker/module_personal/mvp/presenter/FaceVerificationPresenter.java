package com.joker.module_personal.mvp.presenter;

import android.app.Application;
import android.util.Log;

import com.example.commonres.beans.FaceUser;
import com.example.commonres.beans.FaceVerificationBean;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.Base64Util;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.joker.module_personal.mvp.contract.FaceVerificationContract;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 16:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class FaceVerificationPresenter extends BasePresenter<FaceVerificationContract.Model, FaceVerificationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public FaceVerificationPresenter(FaceVerificationContract.Model model, FaceVerificationContract.View rootView) {
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
     * 退房操作
     * @param mOrder
     */
    public void checkOut(Order mOrder){
        mOrder.setState(4);
        mOrder.setCurrentOrder(0);
        mOrder.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Hotel hotel = mOrder.getHotel();
                    hotel.setAvailable(1);
                    hotel.setType(1);
                    hotel.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mRootView.checkOutResult(true,"验证成功，退房成功");
                                Log.i("FaceVerification", "退房成功");
                            } else {
                                mRootView.checkOutResult(false,"验证成功，退房失败");
                                Log.i("FaceVerification", "退房失败" + e.toString());
                            }
                        }
                    });
                } else {
                    mRootView.checkOutResult(false,"验证成功，退房失败");
                    Log.i("FaceVerification", "退房失败" + e.toString());
                }
            }
        });
    }


    /**
     * 人脸验证
     * @param accessToken
     * @param imageByte
     * @param imageType
     * @param groupList
     * @param qualityControl
     * @param livenessControl
     * @param userId
     * @param maxUserNum
     */
    public void verification(String accessToken, byte[] imageByte,
                             String imageType, String groupList,
                             String qualityControl, String livenessControl,
                             String userId, int maxUserNum) {


        mModel.verification(accessToken, Base64Util.encode(imageByte),
                imageType,groupList,qualityControl,livenessControl,userId,maxUserNum)
                .map(new Function<FaceVerificationBean, List<FaceUser>>() {
                    @Override
                    public List<FaceUser> apply(FaceVerificationBean faceVerificationBean) throws Exception {
                        if (faceVerificationBean.getError_code() == 0 &&
                                faceVerificationBean.getError_msg().equals("SUCCESS"))
                            return faceVerificationBean.getResult().getUser_list();
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new Consumer<List<FaceUser>>() {
                    @Override
                    public void accept(List<FaceUser> faceUsers) throws Exception {
                        if (faceUsers == null)
                            mRootView.verificationResult(false);
                        else {
                            for (FaceUser user: faceUsers){
                                if (user.getScore() >= 80){
                                    Log.d("data", user.getUser_id());
                                    mRootView.verificationResult(true);
                                    return;
                                }
                            }
                            mRootView.verificationResult(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.verificationResult(false);
                    }
                });
    }
}
