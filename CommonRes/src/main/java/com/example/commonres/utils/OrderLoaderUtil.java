package com.example.commonres.utils;


import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.beans.User;

import cn.bmob.v3.BmobQuery;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class OrderLoaderUtil {


    /**
     * 获取订单
     * @param orderState
     * @param account
     * @return
     */
    public static BmobQuery<Order> getOrder(Integer orderState, String account) {

        BmobQuery<Order> query = new BmobQuery<Order>("Order");
        query.addWhereEqualTo("state", orderState);
        BmobQuery<User> innerQuery = new BmobQuery<User>("User");
        innerQuery.addWhereEqualTo("account", account);
        query.addWhereMatchesQuery("user", "User", innerQuery);
        query.include("hotel,user");
        query.setLimit(20);
        return query;
    }

    /**
     * 获取指定订单
     * @param hotel
     * @param account
     * @return
     */
    public static BmobQuery<Order> findOrder(Hotel hotel, String account) {

        BmobQuery<Order> query = new BmobQuery<Order>("Order");
        query.addWhereEqualTo("hotel", hotel);
        BmobQuery<User> innerQuery = new BmobQuery<User>("User");
        innerQuery.addWhereEqualTo("account", account);
        query.addWhereMatchesQuery("user", "User", innerQuery);
        query.include("hotel,user");
        query.setLimit(1);
        return query;
    }


    /**
     * 获取清洁订单
     * @param orderState
     * @param account
     * @return
     */
    public static BmobQuery<CleanOrder> getCleanOrders(Integer orderState, String account) {

        BmobQuery<CleanOrder> query = new BmobQuery<CleanOrder>("CleanOrder");
        query.addWhereEqualTo("state", orderState);
        BmobQuery<User> innerQuery = new BmobQuery<User>("User");
        innerQuery.addWhereEqualTo("account", account);
        query.addWhereMatchesQuery("user", "User", innerQuery);
        query.include("hotel,user");
        query.setLimit(20);
        return query;
    }


}
