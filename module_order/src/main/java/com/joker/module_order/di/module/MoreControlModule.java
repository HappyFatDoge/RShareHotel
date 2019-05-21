package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.MoreControlContract;
import com.joker.module_order.mvp.model.MoreControlModel;


@Module
public class MoreControlModule {
    private MoreControlContract.View view;

    /**
     * 构建MoreControlModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MoreControlModule(MoreControlContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MoreControlContract.View provideMoreControlView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MoreControlContract.Model provideMoreControlModel(MoreControlModel model) {
        return model;
    }
}