package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.ConfirmOrderContract;
import com.joker.module_personal.mvp.model.ConfirmOrderModel;


@Module
public class ConfirmOrderModule {
    private ConfirmOrderContract.View view;

    /**
     * 构建ConfirmOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConfirmOrderModule(ConfirmOrderContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConfirmOrderContract.View provideConfirmOrderView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConfirmOrderContract.Model provideConfirmOrderModel(ConfirmOrderModel model) {
        return model;
    }
}