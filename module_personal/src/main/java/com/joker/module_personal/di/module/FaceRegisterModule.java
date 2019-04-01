package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.FaceRegisterContract;
import com.joker.module_personal.mvp.model.FaceRegisterModel;


@Module
public class FaceRegisterModule {
    private FaceRegisterContract.View view;

    /**
     * 构建FaceRegisterModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceRegisterModule(FaceRegisterContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceRegisterContract.View provideFaceRegisterView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceRegisterContract.Model provideFaceRegisterModel(FaceRegisterModel model) {
        return model;
    }
}