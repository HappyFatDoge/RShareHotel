package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.CreditScoreContract;
import com.joker.module_personal.mvp.model.CreditScoreModel;


@Module
public class CreditScoreModule {
    private CreditScoreContract.View view;

    /**
     * 构建CreditScoreModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CreditScoreModule(CreditScoreContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CreditScoreContract.View provideCreditScoreView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CreditScoreContract.Model provideCreditScoreModel(CreditScoreModel model) {
        return model;
    }
}