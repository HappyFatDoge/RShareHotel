package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.flyco.tablayout.SegmentTabLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerCouponComponent;
import com.joker.module_personal.di.module.CouponModule;
import com.joker.module_personal.mvp.contract.CouponContract;
import com.joker.module_personal.mvp.presenter.CouponPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.fragment.AllCouponFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 优惠券
 */
@Route(path = RouterHub.PERSONAL_COUPONACTIVITY)
public class CouponActivity extends BaseActivity<CouponPresenter> implements CouponContract.View {

    @BindView(R2.id.tab_layout)
    SegmentTabLayout tabLayout;

    private String[] tabText = {"全部", "可用", "已过期"};

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCouponComponent
            .builder()
            .appComponent(appComponent)
            .couponModule(new CouponModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_coupon;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(AllCouponFragment.newInstance());
        fragments.add(AllCouponFragment.newInstance());
        fragments.add(AllCouponFragment.newInstance());
        tabLayout.setTabData(tabText,this,R.id.frame_layout,fragments);
    }

    @OnClick({R2.id.back,R2.id.right})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
        else if (viewId == R.id.right){ //优惠券使用说明

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
