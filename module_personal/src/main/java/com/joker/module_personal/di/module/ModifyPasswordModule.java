package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.ModifyPasswordContract;
import com.joker.module_personal.mvp.model.ModifyPasswordModel;


@Module
public class ModifyPasswordModule {
    private ModifyPasswordContract.View view;

    /**
     * 构建ModifyPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ModifyPasswordModule(ModifyPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ModifyPasswordContract.View provideModifyPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ModifyPasswordContract.Model provideModifyPasswordModel(ModifyPasswordModel model) {
        return model;
    }
}