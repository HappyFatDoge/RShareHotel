package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.MyHouseContract;
import com.joker.module_personal.mvp.model.MyHouseModel;


@Module
public class MyHouseModule {
    private MyHouseContract.View view;

    /**
     * 构建MyHouseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyHouseModule(MyHouseContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyHouseContract.View provideMyHouseView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyHouseContract.Model provideMyHouseModel(MyHouseModel model) {
        return model;
    }
}