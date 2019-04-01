package com.example.commonres.beans;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class LockBean {

    //门锁名称
    private String lockName;

    //门锁物理地址
    private String address;

    public LockBean(String lockName , String address){
        this.lockName = lockName;
        this.address = address;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
