package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.AboutUsContract;
import com.joker.module_personal.mvp.model.AboutUsModel;


@Module
public class AboutUsModule {
    private AboutUsContract.View view;

    /**
     * 构建AboutUsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AboutUsModule(AboutUsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AboutUsContract.View provideAboutUsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AboutUsContract.Model provideAboutUsModel(AboutUsModel model) {
        return model;
    }
}