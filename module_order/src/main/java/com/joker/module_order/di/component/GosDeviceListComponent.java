package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosDeviceListModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosDeviceListActivity;

@ActivityScope
@Component(modules = GosDeviceListModule.class, dependencies = AppComponent.class)
public interface GosDeviceListComponent {
    void inject(GosDeviceListActivity activity);
}