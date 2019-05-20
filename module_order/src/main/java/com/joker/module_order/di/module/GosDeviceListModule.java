package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosDeviceListContract;
import com.joker.module_order.mvp.model.GosDeviceListModel;


@Module
public class GosDeviceListModule {
    private GosDeviceListContract.View view;

    /**
     * 构建GosDeviceListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosDeviceListModule(GosDeviceListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosDeviceListContract.View provideGosDeviceListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosDeviceListContract.Model provideGosDeviceListModel(GosDeviceListModel model) {
        return model;
    }
}