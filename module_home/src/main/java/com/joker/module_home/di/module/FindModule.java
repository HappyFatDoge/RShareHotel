package com.joker.module_home.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.FindContract;
import com.joker.module_home.mvp.model.FindModel;


@Module
public class FindModule {
    private FindContract.View view;

    /**
     * 构建FindModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FindModule(FindContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FindContract.View provideFindView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FindContract.Model provideFindModel(FindModel model) {
        return model;
    }
}