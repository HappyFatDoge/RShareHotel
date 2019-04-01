package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.MoreOperationModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.MoreOperationActivity;

@ActivityScope
@Component(modules = MoreOperationModule.class, dependencies = AppComponent.class)
public interface MoreOperationComponent {
    void inject(MoreOperationActivity activity);
}