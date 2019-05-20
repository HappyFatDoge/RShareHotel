package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.GosModeListContract;
import com.joker.module_order.mvp.model.GosModeListModel;


@Module
public class GosModeListModule {
    private GosModeListContract.View view;

    /**
     * 构建GosModeListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GosModeListModule(GosModeListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GosModeListContract.View provideGosModeListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GosModeListContract.Model provideGosModeListModel(GosModeListModel model) {
        return model;
    }
}