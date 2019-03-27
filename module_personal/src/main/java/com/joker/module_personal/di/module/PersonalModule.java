package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.PersonalContract;
import com.joker.module_personal.mvp.model.PersonalModel;


@Module
public class PersonalModule {
    private PersonalContract.View view;

    /**
     * 构建PersonalModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PersonalModule(PersonalContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    PersonalContract.View providePersonalView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    PersonalContract.Model providePersonalModel(PersonalModel model) {
        return model;
    }
}