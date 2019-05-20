package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosConfigFailedContract;
import com.joker.module_order.mvp.model.GosConfigFailedModel;


@Module
public class GosConfigFailedModule {
    private GosConfigFailedContract.View view;

    /**
     * 构建GosConfigFailedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosConfigFailedModule(GosConfigFailedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosConfigFailedContract.View provideGosConfigFailedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosConfigFailedContract.Model provideGosConfigFailedModel(GosConfigFailedModel model) {
        return model;
    }
}