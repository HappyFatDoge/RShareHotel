package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosChooseDeviceWorkWiFiModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosChooseDeviceWorkWiFiActivity;

@ActivityScope
@Component(modules = GosChooseDeviceWorkWiFiModule.class, dependencies = AppComponent.class)
public interface GosChooseDeviceWorkWiFiComponent {
    void inject(GosChooseDeviceWorkWiFiActivity activity);
}