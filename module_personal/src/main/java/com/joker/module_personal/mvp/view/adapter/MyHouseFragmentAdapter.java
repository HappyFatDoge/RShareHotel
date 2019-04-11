package com.joker.module_personal.mvp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/11.
 */
public class MyHouseFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public MyHouseFragmentAdapter(FragmentManager fm, List<Fragment> list){
        this(fm);
        this.list = list;
    }

    public MyHouseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
