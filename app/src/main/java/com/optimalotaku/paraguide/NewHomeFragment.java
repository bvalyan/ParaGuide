package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 11/9/2017.
 */

public class NewHomeFragment extends Fragment {
    Animation in;
    Animation buttonIN;
    Animation greetingIN;
    int stopPosition;
    TextView greeting = null;
    TextView pHeroKills = null;
    TextView pCoreKills = null;
    TextView pGamesWon = null;
    HorizontalPagerAdapter adapter;
    String userID = "";
    String userName = "";
    static ArrayList<ChampionData> championList;
    Menu menu;
    String authCode;

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



        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.vicvp);
        final HorizontalPagerAdapter adapter = new HorizontalPagerAdapter(getContext(),championList,view);
        horizontalInfiniteCycleViewPager.setAdapter(adapter);
        horizontalInfiniteCycleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                adapter.pageChanged(horizontalInfiniteCycleViewPager.getRealItem());
            }
        });
        //championList = null;  //test
        return view;
    }

    }

