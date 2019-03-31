package com.joker.module_order.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.CommentContract;
import com.joker.module_order.mvp.model.CommentModel;


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

    @FragmentScope
    @Provides
    CommentContract.View provideCommentView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    CommentContract.Model provideCommentModel(CommentModel model) {
        return model;
    }
}