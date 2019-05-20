package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosChooseDeviceContract;
import com.joker.module_order.mvp.model.GosChooseDeviceModel;


@Module
public class GosChooseDeviceModule {
    private GosChooseDeviceContract.View view;

    /**
     * 构建GosChooseDeviceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosChooseDeviceModule(GosChooseDeviceContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosChooseDeviceContract.View provideGosChooseDeviceView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosChooseDeviceContract.Model provideGosChooseDeviceModel(GosChooseDeviceModel model) {
        return model;
    }
}