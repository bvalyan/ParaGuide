package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by bvaly on 1/16/2017.
 */

public class HeroDisplayPrototype extends AppCompatActivity{
    public HeroData selectedHero = new HeroData();


    private TabsPagerAdapter mAdapter;

    String name;
    String scale;
    int difficulty;
    String picURL;
    String affinity1;
    String affinity2;
    String traits;
    String primaryPic;
    String secondary1Pic;
    String secondary2Pic;
    String secondary3Pic;
    String ultimatePic;
    String primary;
    String secondary1;
    String secondary2;
    String secondary3;
    String primaryDesc;
    String secondary1Desc;
    String secondary2Desc;
    String secondary3Desc;
    String ultDesc;
    String ultimate;
    int mobility;
    int durability;
    int abilityAttack;
    int basicAttack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_data_ptype);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle gifts = getIntent().getExtras();
        name = gifts.getString("name");
        scale = gifts.getString("attack");
        difficulty = gifts.getInt("difficulty");
        picURL = gifts.getString("picURL");
        affinity1 = gifts.getString("affinity1");
        affinity2 = gifts.getString("affinity2");
        traits = gifts.getString("traits");
        primaryPic = gifts.getString("primaryPic");
        secondary1Pic = gifts.getString("secondary1Pic");
        secondary2Pic = gifts.getString("secondary2Pic");
        secondary3Pic = gifts.getString("secondary3Pic");
        primaryDesc = gifts.getString("primaryDesc");
        secondary1Desc = gifts.getString("secondary1Desc");
        secondary2Desc = gifts.getString("secondary2Desc");
        secondary3Desc = gifts.getString("secondary3Desc");
        ultDesc = gifts.getString("ultDesc");
        primary = gifts.getString("primary");
        secondary1 = gifts.getString("secondary1");
        secondary2 = gifts.getString("secondary2");
        secondary3 = gifts.getString("secondary3");
        ultimate = gifts.getString("ultimate");
        ultimatePic = gifts.getString("ultimatePic");
        mobility = gifts.getInt("mobility");
        durability = gifts.getInt("durability");
        abilityAttack = gifts.getInt("abilityAttack");
        basicAttack = gifts.getInt("basicAttack");
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter adapterViewPager;
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        tabs.setViewPager(vpPager);
        vpPager.setCurrentItem(0);

        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

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
                    return HeroTipsFragment.newInstance(0, "Hero Tips", name, scale, difficulty, affinity1, affinity2, picURL, traits, primaryPic, secondary1Pic, secondary2Pic, secondary3Pic, ultimatePic, primary, secondary1, secondary2, secondary3, ultimate, primary, secondary1Desc, secondary2Desc, secondary3Desc, ultDesc);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return HeroGraphsFragment.newInstance(1, "Hero Stats", basicAttack, abilityAttack, durability, mobility);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return HeroLoreFragment.newInstance(2, "Hero Lore");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 : return "Hero Tips";
                case 1 : return "Hero Stats";
                case 2 : return "Hero Lore";
                default: return null;
            }
        }

    }
}

