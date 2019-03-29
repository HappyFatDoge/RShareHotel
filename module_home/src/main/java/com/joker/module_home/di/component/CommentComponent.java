package com.joker.module_home.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.CommentModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.CommentActivity;

@ActivityScope
@Component(modules = CommentModule.class, dependencies = AppComponent.class)
public interface CommentComponent {
    void inject(CommentActivity activity);
}