package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.FindModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.FindActivity;

@ActivityScope
@Component(modules = FindModule.class, dependencies = AppComponent.class)
public interface FindComponent {
    void inject(FindActivity activity);
}