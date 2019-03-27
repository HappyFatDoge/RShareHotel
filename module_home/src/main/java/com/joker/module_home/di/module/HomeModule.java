package com.joker.module_home.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.HomeContract;
import com.joker.module_home.mvp.model.HomeModel;


@Module
public class HomeModule {
    private HomeContract.View view;

    /**
     * 构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    HomeContract.View provideHomeView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model) {
        return model;
    }
}