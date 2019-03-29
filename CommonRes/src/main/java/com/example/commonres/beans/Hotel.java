package com.example.commonres.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class Hotel extends BmobObject implements Serializable {

    private String name;
    private String address;
    private BmobDate startDate;
    private BmobDate endDate;
    private Double grade;
    private Integer price;
    private Integer comment;
    private Integer type;
    private Integer rooms;
    private String lockAddress;

    private Integer area;
    private String description;
    private String mode;
    private String houseType;

    private String url;
    private String district;
    private String city;
    private Integer available;
    private User host;

    public Hotel() {
        //setTableName("Hotel");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public String getLockAddress() {
        return lockAddress;
    }

    public void setLockAddress(String lockAddress) {
        this.lockAddress = lockAddress;
    }

    public BmobDate getStartDate() {
        return startDate;
    }

    public void setStartDate(BmobDate startDate) {
        this.startDate = startDate;
    }

    public BmobDate getEndDate() {
        return endDate;
    }

    public void setEndDate(BmobDate endDate) {
        this.endDate = endDate;
    }
}
