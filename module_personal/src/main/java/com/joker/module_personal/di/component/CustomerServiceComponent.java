package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.CustomerServiceModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.CustomerServiceActivity;

@ActivityScope
@Component(modules = CustomerServiceModule.class, dependencies = AppComponent.class)
public interface CustomerServiceComponent {
    void inject(CustomerServiceActivity activity);
}