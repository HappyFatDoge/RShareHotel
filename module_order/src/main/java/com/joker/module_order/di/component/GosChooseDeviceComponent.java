package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosChooseDeviceModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosChooseDeviceActivity;

@ActivityScope
@Component(modules = GosChooseDeviceModule.class, dependencies = AppComponent.class)
public interface GosChooseDeviceComponent {
    void inject(GosChooseDeviceActivity activity);
}