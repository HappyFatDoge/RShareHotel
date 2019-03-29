package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerFilterHotelComponent;
import com.joker.module_home.di.module.FilterHotelModule;
import com.joker.module_home.mvp.contract.FilterHotelContract;
import com.joker.module_home.mvp.presenter.FilterHotelPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 主页-.搜索->右上角筛选
 */
@Route(path = RouterHub.HOME_FILTERHOTELACTIVITY)
public class FilterHotelActivity extends BaseActivity<FilterHotelPresenter> implements FilterHotelContract.View {

    @BindView(R2.id.first_tv)
    TextView firstPrice;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFilterHotelComponent
            .builder()
            .appComponent(appComponent)
            .filterHotelModule(new FilterHotelModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_filter_hotel;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();//初始化控件
    }


    private void initView(){
        firstPrice.callOnClick();
    }

    @OnClick(R2.id.back)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            finish();
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
