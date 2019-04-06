package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.utils.DateUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerPostHouseComponent;
import com.joker.module_personal.di.module.PostHouseModule;
import com.joker.module_personal.mvp.contract.PostHouseContract;
import com.joker.module_personal.mvp.presenter.PostHousePresenter;

import com.joker.module_personal.R;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 发布房子
 */
@Route(path = RouterHub.PERSONAL_POSTHOUSEACTIVITY)
public class PostHouseActivity extends BaseActivity<PostHousePresenter>
        implements PostHouseContract.View {

    @BindView(R2.id.et_posthouse_name)
    EditText houseName;
    @BindView(R2.id.et_lock_address)
    EditText lockAddress;
    @BindView(R2.id.et_posthouse_detail_address)
    EditText houseAddress;
    @BindView(R2.id.et_posthouse_area)
    EditText houseArea;
    @BindView(R2.id.et_posthouse_price)
    EditText housePrice;
    @BindView(R2.id.et_posthouse_desc)
    EditText houseDesc;
    @BindView(R2.id.tv_posthouse_num)
    TextView housePhotosNum;
    @BindView(R2.id.tv_posthouse_start_date)
    TextView startDate;
    @BindView(R2.id.tv_posthouse_end_date)
    TextView endDate;
    @BindView(R2.id.tv_posthouse_city)
    TextView city;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPostHouseComponent
            .builder()
            .appComponent(appComponent)
            .postHouseModule(new PostHouseModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_post_house;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        startDate.setText(DateUtil.getTomorrow());
        endDate.setText(DateUtil.getAcquired());
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
