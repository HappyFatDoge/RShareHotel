package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.ConfirmOrderModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.ConfirmOrderActivity;

@ActivityScope
@Component(modules = ConfirmOrderModule.class, dependencies = AppComponent.class)
public interface ConfirmOrderComponent {
    void inject(ConfirmOrderActivity activity);
}