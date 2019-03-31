package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.HousingModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_order.mvp.view.fragment.HousingFragment;

@FragmentScope
@Component(modules = HousingModule.class, dependencies = AppComponent.class)
public interface HousingComponent {
    void inject(HousingFragment fragment);
}