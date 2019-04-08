package com.joker.module_home.mvp.presenter;

import android.app.Application;
import android.util.Log;

import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_home.mvp.contract.FindContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class FindPresenter extends BasePresenter<FindContract.Model, FindContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public FindPresenter(FindContract.Model model, FindContract.View rootView) {
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
     * 获取得到指定时间段内，以及其他搜索条件的hotel列表
     * @param city
     * @param condition
     * @param searchMode
     * @param queryList
     */
    public void searchHotel(String city, String condition, String searchMode, List<BmobQuery> queryList){

        Log.i("FindActivity", "获取数据");
        Integer type = getSearchTpe(searchMode);
        BmobQuery<Hotel> city_query = new BmobQuery<Hotel>("Hotel");
        city_query.addWhereEqualTo("city", city);
        BmobQuery<Hotel> avaiable_query = new BmobQuery<>("Hotel");
        avaiable_query.addWhereEqualTo("available", 1);
        List<BmobQuery> list_query = new ArrayList<>();
        if (queryList != null) {
            list_query.addAll(queryList);
        }
        list_query.add(city_query);
        list_query.add(avaiable_query);
        if (type == 1) {
            BmobQuery<Hotel> type_query = new BmobQuery<>("Hotel");
            type_query.addWhereEqualTo("mode", "民宿");
            list_query.add(type_query);
        }
        if (type == 2) {
            BmobQuery<Hotel> type_query = new BmobQuery<>("Hotel");
            type_query.addWhereEqualTo("mode", "酒店公寓");
            list_query.add(type_query);
        }

        BmobQuery final_query = new BmobQuery<>("Hotel");
        Log.i("城市", city);
        if (condition.equals("")) {
            //没有输入条件查询
            final_query.and(list_query);
            executeQuery(final_query);
        } else {
            BmobQuery<Hotel> con_query = new BmobQuery<>();
            con_query.addWhereEqualTo("district", condition);
            list_query.add(con_query);
            final_query.and(list_query);
            executeQuery(final_query);//符合查询 即根据城市很输入的关键词搜索
            Log.i("区", condition);
        }
    }


    /**
     * 执行查询
     * @param query
     */
    private void executeQuery(BmobQuery<Hotel> query) {
        //query.addQueryKeys("type,name,address,grade,price,comment,url");
        query.setLimit(20);
        query.addWhereNotEqualTo("host", LoginUtil.getInstance().getUser());
        query.include("host");
        query.findObjects(new FindListener<Hotel>() {
            @Override
            public void done(List<Hotel> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        Log.i("FindActivity", "搜索成功" + list.size());
                        mRootView.searchResult(true,list,"");
                    } else
                        mRootView.searchResult(false,null,"没有符合条件内容");
                } else {
                    Log.i("错误", e.toString());
                    mRootView.searchResult(false,null,"获取失败");
                }
            }
        });

    }

    /**
     * hotel 类型获取
     * @param searchMode
     * @return
     */
    private Integer getSearchTpe(String searchMode) {
        if (searchMode.equals("HotelApartment"))
            return 2;
        else if (searchMode.equals("House"))
            return 1;
        else
            return 0;
    }

}
