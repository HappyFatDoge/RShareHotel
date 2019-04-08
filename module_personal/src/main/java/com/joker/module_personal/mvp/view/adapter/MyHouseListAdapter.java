package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Hotel;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.holder.MyHouseListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/8.
 */
public class MyHouseListAdapter extends DefaultAdapter<Hotel> {

    private EditHotelListener mEditHotelListener;
    private OperationListener mOperationListener;

    public MyHouseListAdapter(){
        this(new ArrayList<>());
    }

    public MyHouseListAdapter(List<Hotel> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Hotel> getHolder(View v, int viewType) {
        return new MyHouseListHolder(v, mEditHotelListener, mOperationListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.house_list_item;
    }


    public interface EditHotelListener{
        void edit(View view, int position);
    }

    public interface OperationListener{
        void operate(View view, int position);
    }

    public void setEditHotelListener(EditHotelListener editHotelListener) {
        mEditHotelListener = editHotelListener;
    }

    public void setOperationListener(OperationListener operationListener) {
        mOperationListener = operationListener;
    }

    /**
     * 添加item list
     * @param list
     */
    public void setItems(List<Hotel> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }


    /**
     * 溢出item
     * @param hotel
     */
    public void removeItem(Hotel hotel){
        this.mInfos.remove(hotel);
        notifyDataSetChanged();
    }
}
