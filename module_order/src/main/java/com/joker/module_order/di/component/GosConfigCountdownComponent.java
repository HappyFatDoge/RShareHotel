package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosConfigCountdownModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosConfigCountdownActivity;

@ActivityScope
@Component(modules = GosConfigCountdownModule.class, dependencies = AppComponent.class)
public interface GosConfigCountdownComponent {
    void inject(GosConfigCountdownActivity activity);
}