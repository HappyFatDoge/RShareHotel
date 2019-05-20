package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosDeviceReadyContract;
import com.joker.module_order.mvp.model.GosDeviceReadyModel;


@Module
public class GosDeviceReadyModule {
    private GosDeviceReadyContract.View view;

    /**
     * 构建GosDeviceReadyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosDeviceReadyModule(GosDeviceReadyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosDeviceReadyContract.View provideGosDeviceReadyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosDeviceReadyContract.Model provideGosDeviceReadyModel(GosDeviceReadyModel model) {
        return model;
    }
}