package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.CollectionContract;
import com.joker.module_personal.mvp.model.CollectionModel;


@Module
public class CollectionModule {
    private CollectionContract.View view;

    /**
     * 构建CollectionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CollectionModule(CollectionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CollectionContract.View provideCollectionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CollectionContract.Model provideCollectionModel(CollectionModel model) {
        return model;
    }
}