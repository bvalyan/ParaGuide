package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bvaly on 11/9/2017.
 */

public class NewHomeFragment extends Fragment {

    static ArrayList<ChampionData> championList;
    TextView titleView;
    TextView loreBit;
    String TAG = NewHomeFragment.class.getSimpleName();

    public static NewHomeFragment newInstance(ArrayList<ChampionData> heroDataMap) {
        championList = heroDataMap;
        return new NewHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_main_fragment, container, false);
        final SharedPreferences prefs = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        titleView = view.findViewById(R.id.title_5);
        loreBit = view.findViewById(R.id.lore_bit);
        AssetManager am = view.getContext().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "bulletproof.ttf"));

        titleView.setTypeface(typeface);
        titleView.setTextColor(Color.parseColor("#33ffc3"));


        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                view.findViewById(R.id.vicvp);
        final HorizontalPagerAdapter adapter = new HorizontalPagerAdapter(getContext(),championList,view);
        horizontalInfiniteCycleViewPager.setAdapter(adapter);
        loreBit.setText(championList.get(0).getLore()); //initial lore set
        horizontalInfiniteCycleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                int realItem = horizontalInfiniteCycleViewPager.getRealItem();
                adapter.pageChanged(realItem);
                loreBit.setText(championList.get(realItem).getLore());
            }
        });
        //championList = null;  //test
        return view;
    }

    }

