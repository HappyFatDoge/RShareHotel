package com.example.guet.sharehotel.mvp.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.guet.sharehotel.R;
import com.example.guet.sharehotel.di.component.DaggerHomeComponent;
import com.example.guet.sharehotel.di.module.HomeModule;
import com.example.guet.sharehotel.mvp.contract.HomeContract;
import com.example.guet.sharehotel.mvp.presenter.HomePresenter;
import com.example.guet.sharehotel.mvp.view.adapter.HomeViewPagerAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.mvp.view.fragment.HomeFragment;
import com.joker.module_message.mvp.view.fragment.MessageFragment;
import com.joker.module_order.mvp.view.fragment.OrderFragment;
import com.joker.module_personal.mvp.view.fragment.PersonalFragment;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.APP_HOMEACTIVITY)
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;


    private long mPressedTime;
    private List<Fragment> mFragmentList;
    private HomeViewPagerAdapter mHomeViewPagerAdapter;
    private int[] tabRes = {R.layout.home_tab_layout,R.layout.message_tab_layout,
        R.layout.order_tab_layout,R.layout.personal_tab_layout};

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
            .builder()
            .appComponent(appComponent)
            .homeModule(new HomeModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //初始化布局
        initView();
        //请求权限
        requestPermissions();
    }

    private void initView(){
        //fragment列表
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(MessageFragment.newInstance());
        mFragmentList.add(OrderFragment.newInstance());
        mFragmentList.add(PersonalFragment.newInstance());

        mHomeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mHomeViewPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        setTabItems();
    }


    /**
     * 设置tab item的view
     */
    private void setTabItems(){
        for (int i = 0; i < 4; ++i)
            mTabLayout.getTabAt(i).setCustomView(tabRes[i]);
    }


    /**
     * 请求权限
     */
    private void requestPermissions(){
        new RxPermissions(this)
            .requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted)
                        Log.d("permission",permission.name + " is accept");
                    else
                        Log.d("permission" , permission.name + " is reject");
                }
            });
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


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mPressedTime > 2000){
            mPressedTime = currentTime;
            ToastUtil.makeText(this,R.string.public_exit_tips);
        }else
            super.onBackPressed();
    }
}
