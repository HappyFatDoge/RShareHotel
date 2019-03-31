package com.joker.module_order.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_order.mvp.contract.HousingContract;
import com.joker.module_order.mvp.model.HousingModel;


@Module
public class HousingModule {
    private HousingContract.View view;

    /**
     * 构建HousingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HousingModule(HousingContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    HousingContract.View provideHousingView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    HousingContract.Model provideHousingModel(HousingModel model) {
        return model;
    }
}