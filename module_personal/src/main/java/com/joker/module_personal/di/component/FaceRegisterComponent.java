package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.FaceRegisterModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.FaceRegisterActivity;

@ActivityScope
@Component(modules = FaceRegisterModule.class, dependencies = AppComponent.class)
public interface FaceRegisterComponent {
    void inject(FaceRegisterActivity activity);
}