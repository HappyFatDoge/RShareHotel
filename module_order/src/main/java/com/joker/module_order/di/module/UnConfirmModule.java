package com.joker.module_order.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.UnConfirmContract;
import com.joker.module_order.mvp.model.UnConfirmModel;


@Module
public class UnConfirmModule {
    private UnConfirmContract.View view;

    /**
     * 构建UnConfirmModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UnConfirmModule(UnConfirmContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    UnConfirmContract.View provideUnConfirmView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    UnConfirmContract.Model provideUnConfirmModel(UnConfirmModel model) {
        return model;
    }
}