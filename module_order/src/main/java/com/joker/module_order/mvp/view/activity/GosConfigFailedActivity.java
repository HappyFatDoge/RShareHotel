package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.commonres.utils.ToolUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosConfigFailedComponent;
import com.joker.module_order.di.module.GosConfigFailedModule;
import com.joker.module_order.mvp.contract.GosConfigFailedContract;
import com.joker.module_order.mvp.presenter.GosConfigFailedPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosConfigFailedActivity
    extends GosConfigModuleBaseActivity<GosConfigFailedPresenter>
    implements GosConfigFailedContract.View {

    /**
     * The btn Again
     */
    @BindView(R2.id.btnAgain)
    Button btnAgain;

    private boolean isAirLink;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosConfigFailedComponent
            .builder()
            .appComponent(appComponent)
            .gosConfigFailedModule(new GosConfigFailedModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_config_failed;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        /**   判断是否是从一键配置界面传过去的  */
        isAirLink = getIntent().getBooleanExtra("isAirLink", false);
    }

    @OnClick({R2.id.back,R2.id.btnAgain})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back) {
            Intent intent = new Intent(GosConfigFailedActivity.this, GosDeviceListActivity.class);
            quitAlert(GosConfigFailedActivity.this, intent);
        }else if (viewId == R.id.btnAgain){
            if (ToolUtils.noDoubleClick()) {
                if (isAirLink) {
                    //config_airlink-false-start
                    Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
                    startActivity(intent);
                    //config_airlink-false-end
                } else {
                    //  config_softap-false-start
                    Intent intent = new Intent(this, GosChooseDeviceWorkWiFiActivity.class);
                    startActivity(intent);
                    //  config_softap-false-end
                }

            }
        }
    }

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GosConfigFailedActivity.this, GosDeviceListActivity.class);
            quitAlert(this, intent);
            return true;
        }
        return false;
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
