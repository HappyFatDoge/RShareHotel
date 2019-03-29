package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_home.di.component.DaggerOrderDetailComponent;
import com.joker.module_home.di.module.OrderDetailModule;
import com.joker.module_home.mvp.contract.OrderDetailContract;
import com.joker.module_home.mvp.presenter.OrderDetailPresenter;

import com.joker.module_home.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.HOME_ORDERDETAILACTIVITY)
public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter> implements OrderDetailContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOrderDetailComponent
            .builder()
            .appComponent(appComponent)
            .orderDetailModule(new OrderDetailModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_order_detail;
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
