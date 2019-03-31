package com.example.commonres.utils;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class OrderStateUtil {

    public static String getState(Integer state){
        switch (state) {
            case 1:
                return "待付款";
            case 2:
                return "未入住";
            case 3:
                return "正在入住";
            case 4:
                return "待评价";
            case 5:
                return "交易成功";
            case 6:
                return "交易取消";
        }
        return null;
    }
}
