package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.HistoryOrderModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_order.mvp.view.fragment.HistoryOrderFragment;

@FragmentScope
@Component(modules = HistoryOrderModule.class, dependencies = AppComponent.class)
public interface HistoryOrderComponent {
    void inject(HistoryOrderFragment fragment);
}