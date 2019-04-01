package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.di.component.DaggerFaceRegisterComponent;
import com.joker.module_personal.di.module.FaceRegisterModule;
import com.joker.module_personal.mvp.contract.FaceRegisterContract;
import com.joker.module_personal.mvp.presenter.FaceRegisterPresenter;

import com.joker.module_personal.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.PERSONAL_FACEREGISTERACTIVITY)
public class FaceRegisterActivity extends BaseActivity<FaceRegisterPresenter> implements FaceRegisterContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceRegisterComponent
            .builder()
            .appComponent(appComponent)
            .faceRegisterModule(new FaceRegisterModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_register;
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
