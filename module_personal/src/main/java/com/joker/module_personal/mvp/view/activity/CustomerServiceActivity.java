package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.utils.TelUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerCustomerServiceComponent;
import com.joker.module_personal.di.module.CustomerServiceModule;
import com.joker.module_personal.mvp.contract.CustomerServiceContract;
import com.joker.module_personal.mvp.presenter.CustomerServicePresenter;

import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 客服
 */
@Route(path = RouterHub.PERSONAL_CUSTOMERSERVICEACTIVITY)
public class CustomerServiceActivity extends BaseActivity<CustomerServicePresenter> implements CustomerServiceContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCustomerServiceComponent
            .builder()
            .appComponent(appComponent)
            .customerServiceModule(new CustomerServiceModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_customer_service;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }


    @OnClick({R2.id.back,R2.id.tel_domestic,R2.id.tel_foreign,R2.id.chat})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            killMyself();
        else if (viewId == R.id.tel_domestic)//国内拨打客服
            TelUtil.callPhone("888-888-888",this);
        else if (viewId == R.id.tel_foreign)//国外拨打客服
            TelUtil.callPhone("99-88-888888", this);
        else if (viewId == R.id.chat){//24小时人工在线客服
            ToastUtil.makeText(this, "24小时人工在线客服");
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
