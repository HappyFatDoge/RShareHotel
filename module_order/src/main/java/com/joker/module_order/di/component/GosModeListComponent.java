package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.GosModeListModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.GosModeListActivity;

@ActivityScope
@Component(modules = GosModeListModule.class, dependencies = AppComponent.class)
public interface GosModeListComponent {
    void inject(GosModeListActivity activity);
}