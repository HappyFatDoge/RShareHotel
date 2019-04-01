package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.FaceVerificationModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.FaceVerificationActivity;

@ActivityScope
@Component(modules = FaceVerificationModule.class, dependencies = AppComponent.class)
public interface FaceVerificationComponent {
    void inject(FaceVerificationActivity activity);
}