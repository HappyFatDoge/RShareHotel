package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.HomeModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_home.mvp.view.fragment.HomeFragment;

@FragmentScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeFragment fragment);
}