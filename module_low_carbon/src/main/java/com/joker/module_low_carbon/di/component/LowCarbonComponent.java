package com.joker.module_low_carbon.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_low_carbon.di.module.LowCarbonModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_low_carbon.mvp.view.fragment.LowCarbonFragment;

@FragmentScope
@Component(modules = LowCarbonModule.class, dependencies = AppComponent.class)
public interface LowCarbonComponent {
    void inject(LowCarbonFragment fragment);
}