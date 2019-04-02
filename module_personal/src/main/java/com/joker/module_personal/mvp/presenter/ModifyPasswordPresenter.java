package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.User;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.ModifyPasswordContract;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordContract.Model, ModifyPasswordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ModifyPasswordPresenter(ModifyPasswordContract.Model model, ModifyPasswordContract.View rootView) {
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
     * 修改密码
     * @param orignPaw
     * @param newPaw
     * @param confirmPaw
     */
    public void modifyPassword(String orignPaw, String newPaw, String confirmPaw){
        User user = LoginUtil.getInstance().getUser();
        if (!orignPaw.equals(user.getPassword()))
            mRootView.modifyPasswordResult(false,"原密码错误");
        else {
            if (!newPaw.equals("") && newPaw.equals(confirmPaw)) {
                user.setPassword(newPaw);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null)
                            mRootView.modifyPasswordResult(true,"修改密码成功");
                        else
                            mRootView.modifyPasswordResult(false,"修改密码错误");
                    }
                });
            } else
                mRootView.modifyPasswordResult(false,"两次密码不一样");
        }
    }
}
