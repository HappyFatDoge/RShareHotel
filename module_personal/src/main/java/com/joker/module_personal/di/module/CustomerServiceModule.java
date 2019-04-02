package com.joker.module_personal.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_personal.mvp.contract.CustomerServiceContract;
import com.joker.module_personal.mvp.model.CustomerServiceModel;


@Module
public class CustomerServiceModule {
    private CustomerServiceContract.View view;

    /**
     * 构建CustomerServiceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CustomerServiceModule(CustomerServiceContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CustomerServiceContract.View provideCustomerServiceView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CustomerServiceContract.Model provideCustomerServiceModel(CustomerServiceModel model) {
        return model;
    }
}