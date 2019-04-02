package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.PostHouseModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.PostHouseActivity;

@ActivityScope
@Component(modules = PostHouseModule.class, dependencies = AppComponent.class)
public interface PostHouseComponent {
    void inject(PostHouseActivity activity);
}