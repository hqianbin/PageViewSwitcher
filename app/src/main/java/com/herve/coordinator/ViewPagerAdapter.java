package com.herve.coordinator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 5;
    private String tabTitles[] = new String[]{"", "分享", "收藏", "关注", "关注者"};
    List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragments = new ArrayList<>();
        for(int i = 0; i < PAGE_COUNT; i++) {
            fragments.add(PageFragment.newInstance(i + 1));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}