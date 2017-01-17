package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

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

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter adapterViewPager;
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(0);

        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(HeroDisplayPrototype.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();

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
                    return HeroTipsFragment.newInstance(0, "Hero Tips", name, scale, difficulty, affinity1, affinity2, picURL);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return HeroGraphsFragment.newInstance(1, "Hero Stats");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return HeroLoreFragment.newInstance(2, "Hero Lore");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}

