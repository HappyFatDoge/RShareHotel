package com.joker.module_personal.mvp.contract;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface PostCleanOrderListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void orderResult(Boolean result, String tips, Hotel hotel);
        void findRentHouseResult(Boolean result, String tips, List<Hotel> hotel);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
