package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 11/9/2017.
 */

public class NewHomeFragment extends Fragment {
    Animation in;
    Animation buttonIN;
    Animation greetingIN;
    TokenManager check = new TokenManager();
    int stopPosition;
    TextView greeting = null;
    TextView pHeroKills = null;
    TextView pCoreKills = null;
    TextView pGamesWon  = null;
    String userID = "";
    String userName = "";
    static HashMap<String, HeroData> map;
    Menu menu;
    String authCode;

    String TAG = MainFragment.class.getSimpleName();

    public static NewHomeFragment newInstance(HashMap<String, HeroData> heroDataMap) {
        map = heroDataMap;
        return new NewHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_main_fragment, container, false);
        final SharedPreferences prefs = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo",Context.MODE_PRIVATE).edit();
        authCode = prefs.getString("signedIn", "null");
        greeting = (TextView) view.findViewById(R.id.personalized_greeting);
        pHeroKills = (TextView) view.findViewById(R.id.personalized_hero_kill_stat);
        pCoreKills = (TextView) view.findViewById(R.id.personalized_core_kill_stat);
        pGamesWon  = (TextView) view.findViewById(R.id.personalized_games_won_stat);

        if(!authCode.equals("null")){
            Log.e("Auth" ,"Auth code is: " + authCode);
            final WebView myWebView = (WebView) view.findViewById(R.id.login_page);
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.e("URL", url);
                    if(url.equals("https://accounts.epicgames.com/authorize/index?client_id=5cbc82af86414e03a549dbb811dfbbc5") ){
                        url= "https://accounts.epicgames.com/authorize/index?response_type=code&client_id=5cbc82af86414e03a549dbb811dfbbc5";
                        Log.e("URLSWITCH TO ", url);
                        myWebView.loadUrl(url);
                    }
                    if (url.startsWith(Constants.REDIRECT_URI)) {

                        // extract OAuth2 access_code appended in url
                        if (url.indexOf("?code=") != -1) {

                            // store temporarily
                            authCode = mExtractToken(url);
                            Log.e("Auth", "Succesfully obtained a new auth code " + authCode);
                            SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
                            e.putString("signedIn", authCode);
                            e.apply();
                        }
                        // don't go to redirectUri
                        return true;
                    }
                    // load the webpage from url: login and grant access
                    return super.shouldOverrideUrlLoading(view, url); // return false;
                }
            });

            // do OAuth2 login
            String authorizationUri = mReturnAuthorizationRequestUri();
            myWebView.loadUrl(authorizationUri);

            try {
                Log.e("Auth", "Attempting login with new auth code " + authCode);
                check.checkToken(getContext(), authCode);
            } catch (ExecutionException f) {
                f.printStackTrace();
            } catch (InterruptedException f) {
                f.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            userID = prefs.getString("ACCOUNT_ID", "null");
            Log.e("Auth", "User ID obtained is " + userID);
            APIHomeScreenInfo info = new APIHomeScreenInfo();
            info.execute(userID);
            String userNameJSON;
            try {
                userNameJSON = info.get();
                JSONObject displayName = new JSONObject(userNameJSON);
                userName = displayName.getString("displayName");
                Log.e("Auth", "User name obtained is " + userName);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (NullPointerException el){
                el.printStackTrace();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
            ProgressBar pBar = new ProgressBar(getContext());
            PlayerData pData = new PlayerData();

            ParagonAPIPlayerInfo homeInfo = new ParagonAPIPlayerInfo(getContext(),pBar,userName,pData);
            homeInfo.execute();
            String response = null;
            try {
                response = homeInfo.get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
            JSONObject playerStats = null;

            try {
                playerStats = new JSONObject(response);
                Log.i("INFO", response);
            } catch (JSONException a) {
                a.printStackTrace();
            }

            try {
                pData.setMatches(playerStats.getJSONObject("pvp").getString("games_played"));
            } catch (JSONException a) {
                pData.setMatches("0");
            }
            try {
                pData.setWins(playerStats.getJSONObject("pvp").getString("games_won"));
            } catch (JSONException a) {
                pData.setWins("0");
            }
            try {
                pData.setAssists(playerStats.getJSONObject("pvp").getString("assists_hero"));
            } catch (JSONException a) {
                pData.setAssists("0");
            }
            try {
                pData.setDeaths(playerStats.getJSONObject("pvp").getString("deaths_hero"));
            } catch (JSONException a) {
                pData.setDeaths("0");
            }
            try {
                pData.setHeroKills(playerStats.getJSONObject("pvp").getString("kills_hero"));
            } catch (JSONException a) {
                pData.setHeroKills("0");
            }
            try {
                pData.setCoreKills(playerStats.getJSONObject("pvp").getString("kills_core"));
            } catch (JSONException a) {
                pData.setCoreKills("0");
            }
            try {
                pData.setTowerKills(playerStats.getJSONObject("pvp").getString("kills_towers"));
            } catch (JSONException a) {
                pData.setTowerKills("0");
            }
            try {
                pData.setGamesLeft(playerStats.getJSONObject("pvp").getString("games_left"));
            } catch (JSONException a) {
                pData.setGamesLeft("0");
            }
            try {
                pData.setGamesReconnected(playerStats.getJSONObject("pvp").getString("games_reconnected"));
            } catch (JSONException a) {
                pData.setGamesReconnected("0");
            }

            pHeroKills.setTextColor(Color.parseColor("#cec18e"));
            pCoreKills.setTextColor(Color.parseColor("#cec18e"));
            pGamesWon.setTextColor(Color.parseColor("#cec18e"));
            pHeroKills.setVisibility(View.VISIBLE);
            pCoreKills.setVisibility(View.VISIBLE);
            pGamesWon.setVisibility(View.VISIBLE);
            pHeroKills.setText("Lifetime Hero Kills: " + pData.getHeroKills());
            pCoreKills.setText("Lifetime Core Takedowns: " + pData.getCoreKills());
            pGamesWon.setText("Lifetime Wins: " + pData.getWins());
            greeting.setVisibility(View.VISIBLE);
            greeting.setText("Welcome back, " +userName+ ". Who's the competition today?");
        }
        else {

        }
        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.vicvp);
        final HorizontalPagerAdapter adapter = new HorizontalPagerAdapter(getContext(), map, view);
        horizontalInfiniteCycleViewPager.setAdapter(adapter);
        horizontalInfiniteCycleViewPager.startAutoScroll(true);
        horizontalInfiniteCycleViewPager.setScrollDuration(2500);
        horizontalInfiniteCycleViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                adapter.pageChanged(horizontalInfiniteCycleViewPager.getRealItem());
            }
        });
        return view;

    }



    private String mExtractToken(String url) {
        // url has format https://localhost/#access_token=<tokenstring>&token_type=Bearer&expires_in=315359999
        String[] sArray = url.split("code=");
        System.out.println(sArray[1]);
        return sArray[1];
    }

    private String mReturnAuthorizationRequestUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.AUTHORIZE_PATH);
        sb.append(Constants.CLIENT_ID);
        sb.append(Constants.RESPONSE_TYPE);
        return sb.toString();
    }

    @Override
    public void onPause() {
        Log.d("pause", "onPause called");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "onResume called");
    }


}