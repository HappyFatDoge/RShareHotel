package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosConfigCountdownContract;
import com.joker.module_order.mvp.model.GosConfigCountdownModel;


@Module
public class GosConfigCountdownModule {
    private GosConfigCountdownContract.View view;

    /**
     * 构建GosConfigCountdownModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosConfigCountdownModule(GosConfigCountdownContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosConfigCountdownContract.View provideGosConfigCountdownView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosConfigCountdownContract.Model provideGosConfigCountdownModel(GosConfigCountdownModel model) {
        return model;
    }
}