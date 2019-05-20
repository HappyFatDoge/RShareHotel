package com.joker.module_order.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.joker.module_order.mvp.contract.GosAirlinkChooseDeviceWorkWiFiContract;


@ActivityScope
public class GosAirlinkChooseDeviceWorkWiFiModel extends BaseModel implements GosAirlinkChooseDeviceWorkWiFiContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public GosAirlinkChooseDeviceWorkWiFiModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}