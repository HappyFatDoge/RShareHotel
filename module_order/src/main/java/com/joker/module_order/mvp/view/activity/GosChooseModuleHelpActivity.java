package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.commonres.utils.AssetsUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosChooseModuleHelpComponent;
import com.joker.module_order.di.module.GosChooseModuleHelpModule;
import com.joker.module_order.mvp.contract.GosChooseModuleHelpContract;
import com.joker.module_order.mvp.presenter.GosChooseModuleHelpPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosChooseModuleHelpActivity
    extends GosConfigModuleBaseActivity<GosChooseModuleHelpPresenter>
    implements GosChooseModuleHelpContract.View {

    @BindView(R2.id.webHelp)
    WebView webHelp;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosChooseModuleHelpComponent
            .builder()
            .appComponent(appComponent)
            .gosChooseModuleHelpModule(new GosChooseModuleHelpModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_choose_module_help;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        WebSettings wSet = webHelp.getSettings();
        wSet.setJavaScriptEnabled(true);
        if (AssetsUtils.isZh(this)) {
            webHelp.loadUrl("file:///android_asset/moduleTypeInfo.html");
        } else {
            webHelp.loadUrl("file:///android_asset/moduleTypeInfoEnglish.html");
        }
    }

    @OnClick(R2.id.back)
    public void onClick(View v){
        int viewId = v.getId();
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
