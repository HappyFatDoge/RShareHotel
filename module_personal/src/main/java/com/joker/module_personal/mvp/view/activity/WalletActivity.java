package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerWalletComponent;
import com.joker.module_personal.di.module.WalletModule;
import com.joker.module_personal.mvp.contract.WalletContract;
import com.joker.module_personal.mvp.presenter.WalletPresenter;

import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 我的钱包
 */
@Route(path = RouterHub.PERSONAL_WALLETACTIVITY)
public class WalletActivity extends BaseActivity<WalletPresenter> implements WalletContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWalletComponent
            .builder()
            .appComponent(appComponent)
            .walletModule(new WalletModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wallet;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }


    @OnClick(R2.id.back)
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
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
