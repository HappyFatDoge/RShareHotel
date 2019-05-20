package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosAirlinkChooseDeviceWorkWiFiModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosAirlinkChooseDeviceWorkWiFiActivity;

@ActivityScope
@Component(modules = GosAirlinkChooseDeviceWorkWiFiModule.class, dependencies = AppComponent.class)
public interface GosAirlinkChooseDeviceWorkWiFiComponent {
    void inject(GosAirlinkChooseDeviceWorkWiFiActivity activity);
}