package com.example.madp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class SlidePageAdapter extends FragmentPagerAdapter {

   private List<Fragment> fragments;

   private  String [] tabTitle = new String[]{"Card Payment", "Google Pay", "Cash on Delivery"};


    public SlidePageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments  = fragments;

    }

    public Fragment getItem(int position){
       return fragments.get(position);
    }


    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public int getCount() {

        return fragments.size();
    }
}
