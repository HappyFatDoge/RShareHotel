package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.HotelSelectedInfoModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.HotelSelectedInfoActivity;

@ActivityScope
@Component(modules = HotelSelectedInfoModule.class, dependencies = AppComponent.class)
public interface HotelSelectedInfoComponent {
    void inject(HotelSelectedInfoActivity activity);
}