package com.joker.module_home.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Hotel;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_home.R;
import com.joker.module_home.mvp.view.holder.FindRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class FindRecyclerViewAdapter extends DefaultAdapter<Hotel> {


    public FindRecyclerViewAdapter(){
        super(new ArrayList<>());
    }

    public FindRecyclerViewAdapter(List<Hotel> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Hotel> getHolder(View v, int viewType) {
        return new FindRecyclerViewHolder(v);
    }


    /**
     * item layout
     * @param viewType
     * @return
     */
    @Override
    public int getLayoutId(int viewType) {
        return R.layout.find_recyclerview_item;
    }

    public void setItems(List<Hotel> hotels){
        mInfos = hotels;
        notifyDataSetChanged();
    }

}
