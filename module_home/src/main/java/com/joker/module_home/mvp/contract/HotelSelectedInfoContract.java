package com.joker.module_home.mvp.contract;

import com.example.commonres.beans.Order;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;


public interface HotelSelectedInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void isCollectResult(Boolean result);
        void setCollectResult(Boolean result ,Boolean isCollect, String tips);
        void orderResult(Boolean result, String tips, Order order);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
