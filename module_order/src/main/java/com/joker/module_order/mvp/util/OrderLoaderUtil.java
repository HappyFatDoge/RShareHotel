package com.joker.module_order.mvp.util;


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

}
