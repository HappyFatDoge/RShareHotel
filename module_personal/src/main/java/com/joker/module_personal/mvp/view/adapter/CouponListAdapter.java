package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Coupon;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.holder.CouponListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class CouponListAdapter extends DefaultAdapter<Coupon> {

    public CouponListAdapter(){
        this(new ArrayList<>());
    }

    public CouponListAdapter(List<Coupon> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Coupon> getHolder(View v, int viewType) {
        return new CouponListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.coupon_list_item;
    }
}
