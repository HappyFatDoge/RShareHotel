package com.example.commonres.beans;

import cn.bmob.v3.BmobObject;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class Collection extends BmobObject {
    private User user;
    private Hotel hotel;

    public Collection() {
    }

    public Collection(User user, Hotel hotel) {
        this.user = user;
        this.hotel = hotel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}

