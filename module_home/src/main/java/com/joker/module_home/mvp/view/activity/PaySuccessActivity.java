package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerPaySuccessComponent;
import com.joker.module_home.mvp.contract.PaySuccessContract;
import com.joker.module_home.mvp.presenter.PaySuccessPresenter;

import com.joker.module_home.R;


import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/30/2019 22:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

@Route(path = RouterHub.HOME_PAYSUCCESSACTIVITY)
public class PaySuccessActivity extends BaseActivity<PaySuccessPresenter> implements PaySuccessContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPaySuccessComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick(R2.id.bt_pay_success)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.bt_pay_success){
            Utils.navigation(this, RouterHub.APP_HOMEACTIVITY);
            killMyself();
        }
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
