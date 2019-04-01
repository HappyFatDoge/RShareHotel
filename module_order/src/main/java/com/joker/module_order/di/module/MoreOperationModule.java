package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.MoreOperationContract;
import com.joker.module_order.mvp.model.MoreOperationModel;


@Module
public class MoreOperationModule {
    private MoreOperationContract.View view;

    /**
     * 构建MoreOperationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MoreOperationModule(MoreOperationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MoreOperationContract.View provideMoreOperationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MoreOperationContract.Model provideMoreOperationModel(MoreOperationModel model) {
        return model;
    }
}