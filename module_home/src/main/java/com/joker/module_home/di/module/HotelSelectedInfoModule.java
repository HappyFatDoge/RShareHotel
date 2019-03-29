package com.joker.module_home.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.HotelSelectedInfoContract;
import com.joker.module_home.mvp.model.HotelSelectedInfoModel;


@Module
public class HotelSelectedInfoModule {
    private HotelSelectedInfoContract.View view;

    /**
     * 构建HotelSelectedInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HotelSelectedInfoModule(HotelSelectedInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HotelSelectedInfoContract.View provideHotelSelectedInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HotelSelectedInfoContract.Model provideHotelSelectedInfoModel(HotelSelectedInfoModel model) {
        return model;
    }
}