package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

/**
 * Created by bvaly on 2/8/2017.
 */

public class NewPlayerDisplay extends AppCompatActivity {

    private PlayerData pData;
    private HashMap<String,HeroData> hDataMap;
    private String playerName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_player_data);
        Intent i = this.getIntent();
        playerName = i.getStringExtra("name");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //pData = (PlayerData) getIntent().getSerializableExtra("PlayerData");
        hDataMap = (HashMap<String,HeroData>) getIntent().getSerializableExtra("HeroMap");
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs2);
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager2);
        FragmentPagerAdapter adapterViewPager;
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        tabs.setViewPager(vpPager);
        vpPager.setCurrentItem(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return playerGraphFragment.newInstance(0, "Player Data", pData, playerName);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PlayerTrendsFragment.newInstance(1, "Player Trends", pData, hDataMap);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Data";
                case 1:
                    return "Score";
                default:
                    return null;
            }
        }

    }
}
