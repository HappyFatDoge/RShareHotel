package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.CollectionModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.CollectionActivity;

@ActivityScope
@Component(modules = CollectionModule.class, dependencies = AppComponent.class)
public interface CollectionComponent {
    void inject(CollectionActivity activity);
}