package com.joker.module_home.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_home.mvp.contract.FilterHotelContract;
import com.joker.module_home.mvp.model.FilterHotelModel;


@Module
public class FilterHotelModule {
    private FilterHotelContract.View view;

    /**
     * 构建FilterHotelModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FilterHotelModule(FilterHotelContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FilterHotelContract.View provideFilterHotelView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FilterHotelContract.Model provideFilterHotelModel(FilterHotelModel model) {
        return model;
    }
}