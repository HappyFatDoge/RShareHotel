package com.joker.module_personal.mvp.contract;

import android.content.Context;

import com.example.commonres.beans.Hotel;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;


public interface MyHouseContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Context getViewContext();
        void findMyHouseResult(Boolean result, String tips, List<Hotel> list);
        void removeHouseResult(Boolean result, String tips, Hotel hotel);
        void receiveOrderResult(Boolean result, String tips, int position);
        void rejectOrderResult(Boolean result, String tips, int position);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
