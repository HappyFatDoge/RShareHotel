package com.joker.module_order.mvp.contract;

import com.example.commonres.beans.Order;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;


public interface UnConfirmContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void getUnConfirmOrdersResult(Boolean result, String tips, List<Order> orderList);
        void cancelOrderResult(Boolean result,String tips,Order order);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
