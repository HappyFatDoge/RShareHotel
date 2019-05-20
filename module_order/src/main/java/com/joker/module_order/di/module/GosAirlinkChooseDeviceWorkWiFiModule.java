package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosAirlinkChooseDeviceWorkWiFiContract;
import com.joker.module_order.mvp.model.GosAirlinkChooseDeviceWorkWiFiModel;


@Module
public class GosAirlinkChooseDeviceWorkWiFiModule {
    private GosAirlinkChooseDeviceWorkWiFiContract.View view;

    /**
     * 构建GosAirlinkChooseDeviceWorkWiFiModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosAirlinkChooseDeviceWorkWiFiModule(GosAirlinkChooseDeviceWorkWiFiContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosAirlinkChooseDeviceWorkWiFiContract.View provideGosAirlinkChooseDeviceWorkWiFiView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosAirlinkChooseDeviceWorkWiFiContract.Model provideGosAirlinkChooseDeviceWorkWiFiModel(GosAirlinkChooseDeviceWorkWiFiModel model) {
        return model;
    }
}