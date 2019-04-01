package com.joker.module_order.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.FaceVerificationContract;
import com.joker.module_order.mvp.model.FaceVerificationModel;


@Module
public class FaceVerificationModule {
    private FaceVerificationContract.View view;

    /**
     * 构建FaceVerificationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceVerificationModule(FaceVerificationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceVerificationContract.View provideFaceVerificationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceVerificationContract.Model provideFaceVerificationModel(FaceVerificationModel model) {
        return model;
    }
}