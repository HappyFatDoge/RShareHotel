package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosChooseModuleHelpModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosChooseModuleHelpActivity;

@ActivityScope
@Component(modules = GosChooseModuleHelpModule.class, dependencies = AppComponent.class)
public interface GosChooseModuleHelpComponent {
    void inject(GosChooseModuleHelpActivity activity);
}