package com.joker.module_order.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.OrderModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_order.mvp.view.fragment.OrderFragment;

@FragmentScope
@Component(modules = OrderModule.class, dependencies = AppComponent.class)
public interface OrderComponent {
    void inject(OrderFragment fragment);
}