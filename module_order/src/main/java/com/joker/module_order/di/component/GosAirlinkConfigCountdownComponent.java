package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosAirlinkConfigCountdownModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosAirlinkConfigCountdownActivity;

@ActivityScope
@Component(modules = GosAirlinkConfigCountdownModule.class, dependencies = AppComponent.class)
public interface GosAirlinkConfigCountdownComponent {
    void inject(GosAirlinkConfigCountdownActivity activity);
}