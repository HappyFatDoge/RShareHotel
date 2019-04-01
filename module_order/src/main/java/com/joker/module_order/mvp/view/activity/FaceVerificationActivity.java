package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_order.di.component.DaggerFaceVerificationComponent;
import com.joker.module_order.di.module.FaceVerificationModule;
import com.joker.module_order.mvp.contract.FaceVerificationContract;
import com.joker.module_order.mvp.presenter.FaceVerificationPresenter;

import com.joker.module_order.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 订单Fragment -> 待入住订单Fragment -> 人脸验证
 */
@Route(path = RouterHub.ORDER_FACEVERIFICATIONACTIVITY)
public class FaceVerificationActivity extends BaseActivity<FaceVerificationPresenter> implements FaceVerificationContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceVerificationComponent
            .builder()
            .appComponent(appComponent)
            .faceVerificationModule(new FaceVerificationModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_verification;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
