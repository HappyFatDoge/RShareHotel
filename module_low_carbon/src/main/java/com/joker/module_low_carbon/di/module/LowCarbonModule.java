package com.joker.module_low_carbon.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_low_carbon.mvp.contract.LowCarbonContract;
import com.joker.module_low_carbon.mvp.model.LowCarbonModel;


@Module
public class LowCarbonModule {
    private LowCarbonContract.View view;

    /**
     * 构建LowCarbonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LowCarbonModule(LowCarbonContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    LowCarbonContract.View provideLowCarbonView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    LowCarbonContract.Model provideLowCarbonModel(LowCarbonModel model) {
        return model;
    }
}