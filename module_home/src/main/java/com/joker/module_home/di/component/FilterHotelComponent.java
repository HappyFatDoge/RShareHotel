package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.FilterHotelModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.FilterHotelActivity;

@ActivityScope
@Component(modules = FilterHotelModule.class, dependencies = AppComponent.class)
public interface FilterHotelComponent {
    void inject(FilterHotelActivity activity);
}