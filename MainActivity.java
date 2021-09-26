package com.example.madp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

import Fragments.cardpaymenttabFragment;

import Fragments.cashondeliveryFragment;
import Fragments.phonepayFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private PagerAdapter pagerAdapter;
    private TabLayout tablayout;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        tablayout = findViewById(R.id.tablayout);

        List<Fragment> list = new ArrayList<>();
        list.add(new cardpaymenttabFragment());
        list.add(new phonepayFragment());
        list.add(new cashondeliveryFragment());

        viewpager = findViewById(R.id.viewpager);
        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager(),list);
        viewpager.setAdapter(pagerAdapter);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tablayout.selectTab(tablayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}