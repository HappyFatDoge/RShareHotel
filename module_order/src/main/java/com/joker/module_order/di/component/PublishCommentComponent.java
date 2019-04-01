package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.PublishCommentModule;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.PublishCommentActivity;

@ActivityScope
@Component(modules = PublishCommentModule.class, dependencies = AppComponent.class)
public interface PublishCommentComponent {
    void inject(PublishCommentActivity activity);
}