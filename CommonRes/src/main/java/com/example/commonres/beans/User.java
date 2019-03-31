package com.example.commonres.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/27.
 */
public class User extends BmobObject implements Serializable {

    private String account;
    private String password;
    private String name;
    private String sexy;
    private Boolean faceRegister;
    private String icon;

    public User() {

    }

    public String getAccount() {
        return account;
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexy() {
        return sexy;
    }

    public void setSexy(String sexy) {
        this.sexy = sexy;
    }

    public Boolean getFaceRegister() {
        return faceRegister;
    }

    public void setFaceRegister(Boolean faceRegister) {
        this.faceRegister = faceRegister;
    }
}