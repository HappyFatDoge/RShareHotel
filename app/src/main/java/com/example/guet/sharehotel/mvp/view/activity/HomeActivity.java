package com.example.guet.sharehotel.mvp.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.User;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.guet.sharehotel.R;
import com.example.guet.sharehotel.di.component.DaggerHomeComponent;
import com.example.guet.sharehotel.di.module.HomeModule;
import com.example.guet.sharehotel.mvp.contract.HomeContract;
import com.example.guet.sharehotel.mvp.presenter.HomePresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.functions.Consumer;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.APP_HOMEACTIVITY)
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    private long mPressedTime;

    private String account;
    private boolean isLogin;

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
        //请求权限
        requestPermissions();
    }

    //自动登录
    private void automaticLogin() {
        final SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        account = sp.getString("Account", "");
        isLogin = sp.getBoolean("isLogin",false);
        if (account != ""){
            //获取User
            BmobQuery<User> query = new BmobQuery<>();
            //查询Bmob中username字段叫account的数据
            query.addWhereEqualTo("account", account);
            //返回1条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(1);
            //执行查询方法
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        if (list.size() == 0) { //没有创建账号
                            Log.i("MainActivity", "账号不存在");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Account","");
                            editor.putBoolean("isLogin", false);
                            editor.commit();
                        } else {
                            LoginUtil loginUtil = LoginUtil.getInstance();
                            loginUtil.setUser(list.get(0));
                            loginUtil.setLogin(isLogin);
                        }
                    } else {
                        Log.i("MainActivity", e.toString());
                    }
                }
            });
        }
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
