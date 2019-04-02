package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.di.component.DaggerLoginComponent;
import com.joker.module_personal.di.module.LoginModule;
import com.joker.module_personal.mvp.contract.LoginContract;
import com.joker.module_personal.mvp.presenter.LoginPresenter;

import com.joker.module_personal.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
            .builder()
            .appComponent(appComponent)
            .loginModule(new LoginModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
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
