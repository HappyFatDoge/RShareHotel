package com.joker.module_message.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_message.di.module.MessageModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_message.mvp.view.fragment.MessageFragment;

@FragmentScope
@Component(modules = MessageModule.class, dependencies = AppComponent.class)
public interface MessageComponent {
    void inject(MessageFragment fragment);
}