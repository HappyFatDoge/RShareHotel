package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosAirlinkReadyContract;
import com.joker.module_order.mvp.model.GosAirlinkReadyModel;


@Module
public class GosAirlinkReadyModule {
    private GosAirlinkReadyContract.View view;

    /**
     * 构建GosAirlinkReadyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosAirlinkReadyModule(GosAirlinkReadyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosAirlinkReadyContract.View provideGosAirlinkReadyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosAirlinkReadyContract.Model provideGosAirlinkReadyModel(GosAirlinkReadyModel model) {
        return model;
    }
}