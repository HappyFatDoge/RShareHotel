package com.example.guet.sharehotel.mvp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/27.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public HomeViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
