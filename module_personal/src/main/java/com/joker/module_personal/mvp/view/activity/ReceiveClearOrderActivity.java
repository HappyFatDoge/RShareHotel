package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerReceiveClearOrderComponent;
import com.joker.module_personal.mvp.contract.ReceiveClearOrderContract;
import com.joker.module_personal.mvp.presenter.ReceiveClearOrderPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.adapter.ReceiveClearOrderAdapter;
import com.joker.module_personal.mvp.view.fragment.MyCleanOrderFragment;
import com.joker.module_personal.mvp.view.fragment.PostCleanOrderListFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

/**
 * 个人中心Fragment -> 清洁接单
 */
@Route(path = RouterHub.PERSONAL_RECEIVECLEARORDERACTIVITY)
public class ReceiveClearOrderActivity extends BaseActivity<ReceiveClearOrderPresenter>
        implements ReceiveClearOrderContract.View {

    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerReceiveClearOrderComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_receive_clear_order;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<Fragment> list = new ArrayList<>();
        list.add(PostCleanOrderListFragment.newInstance());
        list.add(MyCleanOrderFragment.newInstance());

        viewPager.setAdapter(new ReceiveClearOrderAdapter(getSupportFragmentManager(), list));
        viewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("被发布待清洁的房子");
        tabLayout.getTabAt(1).setText("我接的清洁订单");
    }

    @OnClick(R2.id.back)
    public void back(){
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
