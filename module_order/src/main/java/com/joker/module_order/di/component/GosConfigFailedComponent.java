package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosConfigFailedModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosConfigFailedActivity;

@ActivityScope
@Component(modules = GosConfigFailedModule.class, dependencies = AppComponent.class)
public interface GosConfigFailedComponent {
    void inject(GosConfigFailedActivity activity);
}