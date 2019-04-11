package com.joker.module_personal.mvp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Hotel;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerMyHouseComponent;
import com.joker.module_personal.di.module.MyHouseModule;
import com.joker.module_personal.mvp.contract.MyHouseContract;
import com.joker.module_personal.mvp.presenter.MyHousePresenter;
import com.joker.module_personal.mvp.view.adapter.MyHouseFragmentAdapter;
import com.joker.module_personal.mvp.view.adapter.MyHouseListAdapter;
import com.joker.module_personal.mvp.view.fragment.CleanHouseFragment;
import com.joker.module_personal.mvp.view.fragment.RentHouseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 我的房子
 */
@Route(path = RouterHub.PERSONAL_MYHOUSEACTIVITY)
public class MyHouseActivity extends BaseActivity<MyHousePresenter>
    implements MyHouseContract.View {

    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyHouseComponent
            .builder()
            .appComponent(appComponent)
            .myHouseModule(new MyHouseModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_house;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<Fragment> list = new ArrayList<>();
        list.add(RentHouseFragment.newInstance());
        list.add(CleanHouseFragment.newInstance());

        viewPager.setAdapter(new MyHouseFragmentAdapter(getSupportFragmentManager(), list));
        viewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("出租的房子");
        tabLayout.getTabAt(1).setText("发布清洁的房子");
    }


    @OnClick(R2.id.back)
    public void onClick(View view){
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
