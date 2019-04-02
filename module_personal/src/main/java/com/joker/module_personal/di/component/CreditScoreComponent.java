package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.CreditScoreModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.CreditScoreActivity;

@ActivityScope
@Component(modules = CreditScoreModule.class, dependencies = AppComponent.class)
public interface CreditScoreComponent {
    void inject(CreditScoreActivity activity);
}