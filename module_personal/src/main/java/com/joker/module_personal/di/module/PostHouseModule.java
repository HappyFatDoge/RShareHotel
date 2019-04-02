package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.PostHouseContract;
import com.joker.module_personal.mvp.model.PostHouseModel;


@Module
public class PostHouseModule {
    private PostHouseContract.View view;

    /**
     * 构建PostHouseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PostHouseModule(PostHouseContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PostHouseContract.View providePostHouseView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PostHouseContract.Model providePostHouseModel(PostHouseModel model) {
        return model;
    }
}