package com.example.commonres.utils;

import com.example.commonres.beans.User;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/27.
 */
public class LoginUtil {

    private User mUser;
    private boolean isLogin;
    private String city;

    private LoginUtil(){
        mUser = new User();
        isLogin = false;
    }

    /**
     * 线程安全的单例模式
     * @return
     */
    public static LoginUtil getInstance(){
        return LoginUtilHolder.loginUtil;
    }


    private static class LoginUtilHolder{
        private static final LoginUtil loginUtil = new LoginUtil();
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
