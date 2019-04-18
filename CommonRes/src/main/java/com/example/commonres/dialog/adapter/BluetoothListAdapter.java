package com.example.commonres.dialog.adapter;

import android.view.View;

import com.example.commonres.R;
import com.example.commonres.beans.LockBean;
import com.example.commonres.dialog.hodler.BluetoothListHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class BluetoothListAdapter extends DefaultAdapter<LockBean> {

    public BluetoothListAdapter(){
        this(new ArrayList<LockBean>());
    }

    public BluetoothListAdapter(List<LockBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<LockBean> getHolder(View v, int viewType) {
        return new BluetoothListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.public_bluetooth_list_item;
    }


    public void setItems(List<LockBean> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 添加设备
     * @param device
     */
    public void addDevice(LockBean device){
        mInfos.add(device);
        notifyDataSetChanged();
    }


    /**
     * 清空设备
     */
    public void clearDevice(){
        mInfos.clear();
        notifyDataSetChanged();
    }
}
