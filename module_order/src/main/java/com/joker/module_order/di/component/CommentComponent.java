package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.CommentModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_order.mvp.view.fragment.CommentFragment;

@FragmentScope
@Component(modules = CommentModule.class, dependencies = AppComponent.class)
public interface CommentComponent {
    void inject(CommentFragment fragment);
}