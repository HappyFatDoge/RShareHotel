package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.UnConfirmModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_order.mvp.view.fragment.UnConfirmFragment;

@FragmentScope
@Component(modules = UnConfirmModule.class, dependencies = AppComponent.class)
public interface UnConfirmComponent {
    void inject(UnConfirmFragment fragment);
}