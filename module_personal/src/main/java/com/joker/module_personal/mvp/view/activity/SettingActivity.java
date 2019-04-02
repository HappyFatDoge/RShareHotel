package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.utils.CheckVersion;
import com.example.commonres.utils.ClearDataManagerUtil;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerSettingComponent;
import com.joker.module_personal.di.module.SettingModule;
import com.joker.module_personal.mvp.contract.SettingContract;
import com.joker.module_personal.mvp.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 设置
 */
@Route(path = RouterHub.PERSONAL_SETTINGACTIVITY)
public class SettingActivity extends BaseActivity<SettingPresenter>
    implements SettingContract.View {

    @BindView(R2.id.cache_tv)
    TextView cacheTextView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent
            .builder()
            .appComponent(appComponent)
            .settingModule(new SettingModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        try {
            cacheTextView.setText(ClearDataManagerUtil.getTotalCacheSize(this) + "");//显示缓存大小
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @OnClick({R2.id.back,R2.id.about_us_linear_layout,
        R2.id.ll_change_password,R2.id.check_up_update_linear_layout,
        R2.id.recommentd_to_friend_linear_layout,R2.id.clear_data_linear_layout,
        R2.id.login_out})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//关闭界面
            killMyself();
        else if (viewId == R.id.about_us_linear_layout)//关于我们
            Utils.navigation(this, RouterHub.PERSONAL_ABOUTUSACTIVITY);
        else if (viewId == R.id.ll_change_password){//修改密码
            if (LoginUtil.getInstance().isLogin()) {
                Utils.navigation(this, RouterHub.PERSONAL_MODIFYPSSSWORDACTIVITY);
                killMyself();
            }
            else
                ToastUtil.makeText(this, "请先登录");
        }else if (viewId == R.id.check_up_update_linear_layout){//检查更新
            ToastUtil.makeText(this, "检查更新中......");
            CheckVersion checkVersion = new CheckVersion(this);
            new Thread(checkVersion).start();
        }else if (viewId == R.id.recommentd_to_friend_linear_layout){//共享软件

        }else if (viewId == R.id.clear_data_linear_layout){//清理缓存
            ClearDataManagerUtil.clearAllCache(this);
            cacheTextView.setText("OK");
            ToastUtil.makeText(this, "清理完成");
        }else if (viewId == R.id.login_out){//退出登录
            SharedPreferences.Editor editor = getSharedPreferences("login",MODE_PRIVATE).edit();
            editor.putBoolean("isLogin", false);
            editor.commit();
            Intent back = new Intent();
            back.putExtra("Logout", true);
            setResult(2,back);
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
