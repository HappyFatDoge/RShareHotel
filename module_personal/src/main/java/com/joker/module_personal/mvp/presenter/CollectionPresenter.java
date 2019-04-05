package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Collection;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.CollectionContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CollectionPresenter extends BasePresenter<CollectionContract.Model, CollectionContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CollectionPresenter(CollectionContract.Model model, CollectionContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    /**
     * 加载收藏列表
     */
    public void getCollections(){
        BmobQuery<Collection> query = new BmobQuery<>("Collection");
        query.include("user,hotel");
        query.addWhereEqualTo("user", LoginUtil.getInstance().getUser());
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null){
                    if (list.size() > 0)
                        mRootView.getCollectionsResult(true, "加载成功", list);
                    else
                        mRootView.getCollectionsResult(true,"没有收藏", new ArrayList<>());
                }else
                    mRootView.getCollectionsResult(false, "收藏加载失败", new ArrayList<>());
            }
        });
    }


    /**
     * 取消收藏
     * @param collection
     */
    public void cancelCollection(Collection collection){
        collection.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    mRootView.cancelCollectionResult(true, "取消收藏成功", collection);
                else
                    mRootView.cancelCollectionResult(false, "取消收藏失败", null);
            }
        });
    }
}
