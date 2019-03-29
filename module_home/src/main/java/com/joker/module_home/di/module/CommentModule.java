package com.joker.module_home.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.CommentContract;
import com.joker.module_home.mvp.model.CommentModel;


@Module
public class CommentModule {
    private CommentContract.View view;

    /**
     * 构建CommentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CommentModule(CommentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CommentContract.View provideCommentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CommentContract.Model provideCommentModel(CommentModel model) {
        return model;
    }
}