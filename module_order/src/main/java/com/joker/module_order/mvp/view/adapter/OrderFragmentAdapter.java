package com.joker.module_order.mvp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class OrderFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> orderFragmentList;

    public OrderFragmentAdapter(FragmentManager fm, List<Fragment> list){
        this(fm);
        this.orderFragmentList = list;
    }

    public OrderFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return orderFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return orderFragmentList.size();
    }
}
