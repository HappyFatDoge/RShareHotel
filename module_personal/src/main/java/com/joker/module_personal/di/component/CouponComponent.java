package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.CouponModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.CouponActivity;

@ActivityScope
@Component(modules = CouponModule.class, dependencies = AppComponent.class)
public interface CouponComponent {
    void inject(CouponActivity activity);
}