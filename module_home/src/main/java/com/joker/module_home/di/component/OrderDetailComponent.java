package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.OrderDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.OrderDetailActivity;

@ActivityScope
@Component(modules = OrderDetailModule.class, dependencies = AppComponent.class)
public interface OrderDetailComponent {
    void inject(OrderDetailActivity activity);
}