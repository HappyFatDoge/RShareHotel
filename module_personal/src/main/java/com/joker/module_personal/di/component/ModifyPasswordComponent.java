package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.ModifyPasswordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.ModifyPasswordActivity;

@ActivityScope
@Component(modules = ModifyPasswordModule.class, dependencies = AppComponent.class)
public interface ModifyPasswordComponent {
    void inject(ModifyPasswordActivity activity);
}