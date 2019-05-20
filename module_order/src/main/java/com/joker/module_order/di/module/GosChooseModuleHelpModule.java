package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosChooseModuleHelpContract;
import com.joker.module_order.mvp.model.GosChooseModuleHelpModel;


@Module
public class GosChooseModuleHelpModule {
    private GosChooseModuleHelpContract.View view;

    /**
     * 构建GosChooseModuleHelpModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosChooseModuleHelpModule(GosChooseModuleHelpContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosChooseModuleHelpContract.View provideGosChooseModuleHelpView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosChooseModuleHelpContract.Model provideGosChooseModuleHelpModel(GosChooseModuleHelpModel model) {
        return model;
    }
}