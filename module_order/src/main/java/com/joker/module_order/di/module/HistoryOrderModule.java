package com.joker.module_order.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.HistoryOrderContract;
import com.joker.module_order.mvp.model.HistoryOrderModel;


@Module
public class HistoryOrderModule {
    private HistoryOrderContract.View view;

    /**
     * 构建HistoryOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HistoryOrderModule(HistoryOrderContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    HistoryOrderContract.View provideHistoryOrderView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    HistoryOrderContract.Model provideHistoryOrderModel(HistoryOrderModel model) {
        return model;
    }
}