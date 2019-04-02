package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.AboutUsModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.AboutUsActivity;

@ActivityScope
@Component(modules = AboutUsModule.class, dependencies = AppComponent.class)
public interface AboutUsComponent {
    void inject(AboutUsActivity activity);
}