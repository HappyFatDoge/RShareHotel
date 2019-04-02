package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.MyHouseModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.MyHouseActivity;

@ActivityScope
@Component(modules = MyHouseModule.class, dependencies = AppComponent.class)
public interface MyHouseComponent {
    void inject(MyHouseActivity activity);
}