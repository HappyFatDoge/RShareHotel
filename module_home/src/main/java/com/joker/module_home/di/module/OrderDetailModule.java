package com.joker.module_home.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.OrderDetailContract;
import com.joker.module_home.mvp.model.OrderDetailModel;


@Module
public class OrderDetailModule {
    private OrderDetailContract.View view;

    /**
     * 构建OrderDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OrderDetailModule(OrderDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OrderDetailContract.View provideOrderDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    OrderDetailContract.Model provideOrderDetailModel(OrderDetailModel model) {
        return model;
    }
}