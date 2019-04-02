package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.WalletModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.WalletActivity;

@ActivityScope
@Component(modules = WalletModule.class, dependencies = AppComponent.class)
public interface WalletComponent {
    void inject(WalletActivity activity);
}