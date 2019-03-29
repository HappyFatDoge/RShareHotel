package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.MapModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.MapActivity;

@ActivityScope
@Component(modules = MapModule.class, dependencies = AppComponent.class)
public interface MapComponent {
    void inject(MapActivity activity);
}