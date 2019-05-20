package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosAirlinkConfigCountdownContract;
import com.joker.module_order.mvp.model.GosAirlinkConfigCountdownModel;


@Module
public class GosAirlinkConfigCountdownModule {
    private GosAirlinkConfigCountdownContract.View view;

    /**
     * 构建GosAirlinkConfigCountdownModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosAirlinkConfigCountdownModule(GosAirlinkConfigCountdownContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosAirlinkConfigCountdownContract.View provideGosAirlinkConfigCountdownView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosAirlinkConfigCountdownContract.Model provideGosAirlinkConfigCountdownModel(GosAirlinkConfigCountdownModel model) {
        return model;
    }
}