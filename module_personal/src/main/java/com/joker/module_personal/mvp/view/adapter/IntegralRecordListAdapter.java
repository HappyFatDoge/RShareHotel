package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.IntegralRecord;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;

import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class IntegralRecordListAdapter extends DefaultAdapter<IntegralRecord> {


    public IntegralRecordListAdapter(List<IntegralRecord> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<IntegralRecord> getHolder(View v, int viewType) {
        return null;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.integral_record_list_item;
    }
}
