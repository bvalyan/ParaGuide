package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

/**
 * Created by bvaly on 2/8/2017.
 */

public class NewPlayerDisplay extends Fragment {

    private PlayerData pData;
    private HashMap<String,HeroData> hDataMap;
    private static String playerName;
    private static HashMap<String,HeroData> map;

    public static NewPlayerDisplay newInstance(String name, HashMap<String,HeroData> heroMap) {

        Bundle args = new Bundle();
        playerName = name;
        map = heroMap;

        NewPlayerDisplay fragment = new NewPlayerDisplay();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.new_player_data, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //pData = (PlayerData) getIntent().getSerializableExtra("PlayerData");
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs2);
        ViewPager vpPager = (ViewPager) view.findViewById(R.id.viewpager2);
        FragmentStatePagerAdapter adapterViewPager;
        adapterViewPager = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        tabs.setViewPager(vpPager);
        vpPager.setCurrentItem(0);

        return view;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
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
                    return PlayerTrendsFragment.newInstance(1, "Player Trends", pData, map);
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
