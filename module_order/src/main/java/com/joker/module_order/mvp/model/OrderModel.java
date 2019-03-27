package com.joker.module_order.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.joker.module_order.mvp.contract.OrderContract;


@FragmentScope
public class OrderModel extends BaseModel implements OrderContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public OrderModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}