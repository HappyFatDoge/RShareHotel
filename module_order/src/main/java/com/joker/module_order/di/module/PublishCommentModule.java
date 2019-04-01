package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.PublishCommentContract;
import com.joker.module_order.mvp.model.PublishCommentModel;


@Module
public class PublishCommentModule {
    private PublishCommentContract.View view;

    /**
     * 构建PublishCommentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PublishCommentModule(PublishCommentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PublishCommentContract.View providePublishCommentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PublishCommentContract.Model providePublishCommentModel(PublishCommentModel model) {
        return model;
    }
}