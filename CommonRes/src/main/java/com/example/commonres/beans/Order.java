package com.example.commonres.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class Order extends BmobObject implements Serializable {

    private User user;
    private Hotel hotel;
    private Integer state;
    private Double price;
    private BmobDate checkInTime;
    private BmobDate checkOutTime;
    private Integer days;
    private Integer currentOrder;

    private User host;

    public Order() {

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BmobDate getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(BmobDate checkInTime) {
        this.checkInTime = checkInTime;
    }

    public BmobDate getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(BmobDate checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Integer currentOrder) {
        this.currentOrder = currentOrder;
    }
}

