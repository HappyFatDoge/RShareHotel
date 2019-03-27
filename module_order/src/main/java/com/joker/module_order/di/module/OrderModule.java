package com.joker.module_order.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.OrderContract;
import com.joker.module_order.mvp.model.OrderModel;


@Module
public class OrderModule {
    private OrderContract.View view;

    /**
     * 构建MessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OrderModule(OrderContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    OrderContract.View provideMessageView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    OrderContract.Model provideMessageModel(OrderModel model) {
        return model;
    }
}