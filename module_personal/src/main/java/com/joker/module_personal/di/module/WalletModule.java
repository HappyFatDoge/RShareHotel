package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.WalletContract;
import com.joker.module_personal.mvp.model.WalletModel;


@Module
public class WalletModule {
    private WalletContract.View view;

    /**
     * 构建WalletModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WalletModule(WalletContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WalletContract.View provideWalletView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WalletContract.Model provideWalletModel(WalletModel model) {
        return model;
    }
}