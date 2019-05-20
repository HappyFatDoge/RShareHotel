package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.commonres.utils.AssetsUtils;
import com.example.commonres.utils.ToolUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosDeviceReadyComponent;
import com.joker.module_order.di.module.GosDeviceReadyModule;
import com.joker.module_order.mvp.contract.GosDeviceReadyContract;
import com.joker.module_order.mvp.presenter.GosDeviceReadyPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosDeviceReadyActivity
    extends GosConfigModuleBaseActivity<GosDeviceReadyPresenter>
    implements GosDeviceReadyContract.View {

    /**
     * The tv Ready
     */
    @BindView(R2.id.tvReady)
    TextView tvReady;

    private int sum = 0;

    boolean isAirLink = false;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosDeviceReadyComponent
            .builder()
            .appComponent(appComponent)
            .gosDeviceReadyModule(new GosDeviceReadyModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_device_ready;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        /**   判断是否是从一键配置界面传过去的  */
        isAirLink = getIntent().getBooleanExtra("isAirLink", false);
        initView();
    }

    private void initView(){
        SpannableString spannableString = new SpannableString(getString(R.string.common_ready_message));
        if (AssetsUtils.isZh(this)) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9500")), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9500")), 28, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvReady.setText(spannableString);
    }


    @OnClick({R2.id.back,R2.id.btnNext})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back){
            sum = 0;
            if (isAirLink) {
                Intent intent = new Intent(GosDeviceReadyActivity.this, GosDeviceListActivity.class);
                startActivity(intent);
            } else
                killMyself();
        } else if (viewId == R.id.btnNext){
            if (ToolUtils.noDoubleClick()) {
                sum = 0;
                Intent intent2 = new Intent(GosDeviceReadyActivity.this, GosChooseDeviceActivity.class);
                startActivity(intent2);
            }
        }
    }


    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sum = 0;
            if (isAirLink) {
                Intent intent = new Intent(GosDeviceReadyActivity.this, GosDeviceListActivity.class);
                startActivity(intent);
            } else {
                finish();
            }
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
