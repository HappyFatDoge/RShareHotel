package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosChooseDeviceWorkWiFiContract;
import com.joker.module_order.mvp.model.GosChooseDeviceWorkWiFiModel;


@Module
public class GosChooseDeviceWorkWiFiModule {
    private GosChooseDeviceWorkWiFiContract.View view;

    /**
     * 构建GosChooseDeviceWorkWiFiModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosChooseDeviceWorkWiFiModule(GosChooseDeviceWorkWiFiContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosChooseDeviceWorkWiFiContract.View provideGosChooseDeviceWorkWiFiView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosChooseDeviceWorkWiFiContract.Model provideGosChooseDeviceWorkWiFiModel(GosChooseDeviceWorkWiFiModel model) {
        return model;
    }
}