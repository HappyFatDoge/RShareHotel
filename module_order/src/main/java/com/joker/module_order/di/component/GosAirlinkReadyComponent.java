package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosAirlinkReadyModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosAirlinkReadyActivity;

@ActivityScope
@Component(modules = GosAirlinkReadyModule.class, dependencies = AppComponent.class)
public interface GosAirlinkReadyComponent {
    void inject(GosAirlinkReadyActivity activity);
}