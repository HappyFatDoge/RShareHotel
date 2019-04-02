package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.SettingModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.SettingActivity;

@ActivityScope
@Component(modules = SettingModule.class, dependencies = AppComponent.class)
public interface SettingComponent {
    void inject(SettingActivity activity);
}