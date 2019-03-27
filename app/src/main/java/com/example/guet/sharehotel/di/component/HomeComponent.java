package com.example.guet.sharehotel.di.component;

import dagger.Component;

import com.example.guet.sharehotel.mvp.view.activity.HomeActivity;
import com.jess.arms.di.component.AppComponent;

import com.example.guet.sharehotel.di.module.HomeModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}