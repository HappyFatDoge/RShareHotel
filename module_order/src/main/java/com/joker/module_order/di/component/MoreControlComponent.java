package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.MoreControlModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.MoreControlActivity;

@ActivityScope
@Component(modules = MoreControlModule.class, dependencies = AppComponent.class)
public interface MoreControlComponent {
    void inject(MoreControlActivity activity);
}