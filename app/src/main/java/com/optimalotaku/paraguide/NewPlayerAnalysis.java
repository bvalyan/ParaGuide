package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by bvaly on 12/6/2017.
 */

public class NewPlayerAnalysis extends Fragment {
    private EditText textSearch;
    private PlayerData pData;
    ProgressDialog dialog;
    static HashMap<String,HeroData> heroDataMap;
    static String name;
    private String jsonResponse;
    private android.os.Handler handler = new android.os.Handler();

    public static NewPlayerAnalysis newInstance(HashMap<String,HeroData> heroMap) {

        Bundle args = new Bundle();
        heroDataMap = heroMap;
        NewPlayerAnalysis fragment = new NewPlayerAnalysis();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.player_data_screen, container, false);


        return view;
    }

    @Override
    public void onViewCreated (final View topView, Bundle savedInstanceState) {

        final RequestQueue queue = Volley.newRequestQueue(getContext());

        textSearch = (EditText) topView.findViewById(R.id.playerText);
        Button search = (Button) topView.findViewById(R.id.playerqueryButton);
        final ProgressBar determinateBar = (ProgressBar) topView.findViewById(R.id.determinateBar);
        final PercentRelativeLayout barLayout = (PercentRelativeLayout) topView.findViewById(R.id.bar_layout);
        final TextView percentNumberView = (TextView) topView.findViewById(R.id.bar_percent_number);
        determinateBar.setMax(100);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                barLayout.setVisibility(View.VISIBLE);
                name = textSearch.getText().toString();
                String url = Constants.PARAGON_PLAYER_URL + name;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            determinateBar.setProgress(25);
                                            percentNumberView.setText("25%");
                                        }
                                    });
                                    parseResponse(response, topView, determinateBar, barLayout, percentNumberView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY: PLAYERFINDING", "That didn't work!");
                        Toast.makeText(getActivity(), "Connection Error!",
                                Toast.LENGTH_LONG).show();
                        topView.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(Constants.API_KEY, Constants.API_VALUE);

                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });


    }


    private void parseResponse(String response, View view, final ProgressBar determinateBar, PercentRelativeLayout barLayout, final TextView percentNumber) throws JSONException {
        if(!response.equals("{}")){
            JSONObject obj2 = null;
            obj2 = new JSONObject(response);
            response = obj2.getString("accountId");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    determinateBar.setProgress(50);
                    percentNumber.setText("50%");
                }
            });
            seekData(response, view, determinateBar, barLayout, percentNumber);
        }
        else{
            Toast.makeText(getActivity(), "Account not found!",
                    Toast.LENGTH_LONG).show();
            barLayout.setVisibility(View.GONE);
        }
    }

    private void seekData(String response, final View view, final ProgressBar determinateBar, final PercentRelativeLayout barLayout, final TextView percentNumber) {
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        // Request a string response from the provided URL.
        String requestURL = "https://developer-paragon.epicgames.com/v1/account/" + response + "/stats";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //pData = (PlayerData) getIntent().getSerializableExtra("PlayerData");
                        jsonResponse = response;
                        //TODO: test implemented response
                        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs2);
                        ViewPager vpPager = (ViewPager) view.findViewById(R.id.viewpager2);
                        FragmentStatePagerAdapter adapterViewPager;
                        adapterViewPager = new MyPagerAdapter(getActivity().getSupportFragmentManager());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                determinateBar.setProgress(75);
                                percentNumber.setText("75%");
                            }
                        });
                        vpPager.setAdapter(adapterViewPager);
                        tabs.setViewPager(vpPager);
                        vpPager.setCurrentItem(0);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                percentNumber.setText("100%");
                                determinateBar.setProgress(100);
                                barLayout.setVisibility(View.GONE);
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY: PLAYERSTATS", "That didn't work!");
                Toast.makeText(getActivity(), "Connection Error!",
                        Toast.LENGTH_LONG).show();
                determinateBar.setVisibility(View.GONE);
                barLayout.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constants.API_KEY, Constants.API_VALUE);

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue2.add(stringRequest);
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
                    return playerGraphFragment.newInstance(0, "Player Data", pData, name, jsonResponse);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PlayerTrendsFragment.newInstance(1, "Player Trends", pData, heroDataMap);
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
