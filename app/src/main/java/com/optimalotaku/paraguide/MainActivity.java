package com.optimalotaku.paraguide;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements CardInfoResponse, ImageLoaderResponse, HeroInfoResponse {

    CardData cotd;
    GridView gridview;
    FileManager fileManager;
    HashMap<String,HeroData> heroDataMap;
    HashMap<String,List<CardData>> cDataMap;
    ProgressDialog progress;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    ScalableVideoView videoview;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    int stopPosition;
    Animation in;
    Animation buttonIN;
    String authCode;
    Animation greetingIN;
    TokenManager check = new TokenManager();
    TextView greeting = null;
    TextView pHeroKills = null;
    TextView pCoreKills = null;
    TextView pGamesWon  = null;
    String userID = "";
    String userName = "";
    Menu menu = null;

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



    private void setupDrawerLayout() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

    }

    @Override
    public void onPause() {
        Log.d("pause", "onPause called");
        super.onPause();
        stopPosition = videoview.getCurrentPosition(); //stopPosition is an int
        videoview.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "onResume called");

        if(!authCode.equals("null")) {
            final WebView myWebView = (WebView) findViewById(R.id.login_page);
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.e("URL", url);
                    if (url.equals("https://accounts.epicgames.com/authorize/index?client_id=5cbc82af86414e03a549dbb811dfbbc5")) {
                        url = "https://accounts.epicgames.com/authorize/index?response_type=code&client_id=5cbc82af86414e03a549dbb811dfbbc5";
                        Log.e("URLSWITCH TO ", url);
                        myWebView.loadUrl(url);
                    }
                    if (url.startsWith(Constants.REDIRECT_URI)) {

                        // extract OAuth2 access_code appended in url
                        if (url.indexOf("?code=") != -1) {

                            // store temporarily
                            authCode = mExtractToken(url);
                            SharedPreferences.Editor e = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
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
                check.checkToken(this, authCode); //check token expire time on resume
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            APIHomeScreenInfo info = new APIHomeScreenInfo();
            info.execute(userID);
            String userNameJSON;
            try {
                userNameJSON = info.get();
                JSONObject displayName = new JSONObject(userNameJSON);
                userName = displayName.getString("displayName");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (NullPointerException el) {
                el.printStackTrace();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
            ProgressBar pBar = new ProgressBar(this);
            PlayerData pData = new PlayerData();

            ParagonAPIPlayerInfo homeInfo = new ParagonAPIPlayerInfo(this, pBar, userName, pData);
            homeInfo.execute();
            String response = null;
            try {
                response = homeInfo.get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
            Log.i("INFO", response);
            JSONObject playerStats = null;

            try {
                playerStats = new JSONObject(response);
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
            pHeroKills.startAnimation(greetingIN);
            pCoreKills.startAnimation(greetingIN);
            pGamesWon.startAnimation(greetingIN);
            pHeroKills.setText("Lifetime Hero Kills: " + pData.getHeroKills());
            pCoreKills.setText("Lifetime Core Takedowns " + pData.getCoreKills());
            pGamesWon.setText("Lifetime Wins " + pData.getWins());
            greeting.setVisibility(View.VISIBLE);
            greeting.startAnimation(greetingIN);
            greeting.setText("Welcome back, " + userName + ". Who's the competition today?");
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem = menu.getItem(menuItemIndex);
                if (menuItem.getItemId() == R.id.signinbutton) {
                    menuItem.setVisible(false);
                }
                if (menuItem.getItemId() == R.id.signoutbutton) {
                    menuItem.setVisible(true);
                }
            }
        }
        videoview.seekTo(stopPosition);
        videoview.start(); //Or use resume() if it doesn't work. I'm not sure
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.drawer_layout);
        final SharedPreferences prefs = getSharedPreferences("authInfo", MODE_PRIVATE);
        final SharedPreferences.Editor e = getSharedPreferences("authInfo",Context.MODE_PRIVATE).edit();
        //e.remove("signedIn");
        //e.apply();
        videoview = (ScalableVideoView) findViewById(R.id.paragon_vid);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0,0);
            }
        });
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(7000);
        buttonIN = new AlphaAnimation(0.0f, 1.0f);
        buttonIN.setDuration(8000);
        greetingIN = new AlphaAnimation(0.0f, 1.0f);

        greetingIN.setDuration(9000);
        TextView title = (TextView) findViewById(R.id.title_5);
        title.startAnimation(in);

        Button analyze = (Button) findViewById(R.id.analyze_button);
        analyze.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AccountSearch.class);
                intent.putExtra("HeroMap",heroDataMap);
                startActivity(intent);
            }
        });
        analyze.startAnimation(buttonIN);
        //videoview.setDisplayMode(ScalableVideoView.DisplayMode.FULL_SCREEN);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.trailer);
        videoview.setVideoURI(uri);
        videoview.start();
        //gridview = (GridView) findViewById(R.id.gridview);
        fileManager = new FileManager(this);
        heroDataMap = new HashMap<>();
        cDataMap = new HashMap<>();

        setUpToolbar();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        mDrawerLayout.post(new Runnable() {

            @Override
            public void run() {

                mDrawerToggle.syncState();

            }

        });

        setupDrawerLayout();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        authCode = prefs.getString("signedIn", "null");


        menu = mNavigationView.getMenu();
        greeting = (TextView) findViewById(R.id.personalized_greeting);
        pHeroKills = (TextView) findViewById(R.id.personalized_hero_kill_stat);
        pCoreKills = (TextView) findViewById(R.id.personalized_core_kill_stat);
        pGamesWon  = (TextView) findViewById(R.id.personalized_games_won_stat);

        if(!authCode.equals("null")){
            Log.e("Auth" ,"Auth code is: " + authCode);
            final WebView myWebView = (WebView) findViewById(R.id.login_page);
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
                            SharedPreferences.Editor e = getSharedPreferences("authInfo",Context.MODE_PRIVATE).edit();
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
                check.checkToken(this, authCode);
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
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
            ProgressBar pBar = new ProgressBar(this);
            PlayerData pData = new PlayerData();

            ParagonAPIPlayerInfo homeInfo = new ParagonAPIPlayerInfo(this,pBar,userName,pData);
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
            pHeroKills.startAnimation(greetingIN);
            pCoreKills.startAnimation(greetingIN);
            pGamesWon.startAnimation(greetingIN);
            pHeroKills.setText("Lifetime Hero Kills: " + pData.getHeroKills());
            pCoreKills.setText("Lifetime Core Takedowns " + pData.getCoreKills());
            pGamesWon.setText("Lifetime Wins " + pData.getWins());
            greeting.setVisibility(View.VISIBLE);
            greeting.startAnimation(greetingIN);
            greeting.setText("Welcome back, " +userName+ ". Who's the competition today?");
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.signinbutton){
                    menuItem.setVisible(false);
                }
                if (menuItem.getItemId() == R.id.signoutbutton){
                    menuItem.setVisible(true);
                }
            }
        }
        else {

        }


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);

                Intent intent;

                switch (menuItem.getItemId()) {
                    case R.id.signoutbutton:
                        intent = new Intent(MainActivity.this,TokenManager.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("HeroMap",heroDataMap);
                        bundle.putBoolean("logout", true);
                        e.remove("signedIn");
                        e.apply();
                        intent.putExtras(bundle);
                        startActivity(intent);
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.signinbutton:
                        intent = new Intent(MainActivity.this,TokenManager.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_item_1:
                        intent = new Intent(MainActivity.this, AccountSearch.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.navigation_item_2:
                        intent = new Intent(MainActivity.this, CardOfTheDayView.class);
                        intent.putExtra("CardOfTheDay",cotd);
                        startActivity(intent);
                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.navigation_item_3:
                        intent = new Intent(MainActivity.this, newsView.class);
                        startActivity(intent);
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.navigation_item_4:
                        intent = new Intent(MainActivity.this, HeroView.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        mCurrentSelectedPosition = 4;
                        return true;
                    case R.id.navigation_item_5:
                        intent = new Intent(MainActivity.this, Cards.class);
                        startActivity(intent);
                        mCurrentSelectedPosition = 5;
                        return true;
                    case R.id.navigation_item_6:
                        intent = new Intent(MainActivity.this, MyDecks.class);
                        startActivity(intent);
                        mCurrentSelectedPosition = 6;
                        return true;
                    default:
                        return true;
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        progress = ProgressDialog.show(this, "Loading",
                "Checking for new Card/Hero data...", true);

        getHeroData();
        getCardData();

        progress.dismiss();
        AppRater.app_launched(this);
    }

    private String mReturnAuthorizationRequestUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.AUTHORIZE_PATH);
        sb.append(Constants.CLIENT_ID);
        sb.append(Constants.RESPONSE_TYPE);
        return sb.toString();
    }

    public void getHeroData(){
        fileManager = new FileManager(this);

        try {
            heroDataMap = fileManager.readHeroFromStorage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileManager.isLatestHeroData(heroDataMap)) {

            Log.i("INFO", "HeroView - onCreate(): Hero data does not exist or is outdated. Grabbing current data from API ");

            ParagonAPIHeroInfo heroInfo = new ParagonAPIHeroInfo();
            heroInfo.delegate = this;
            heroInfo.execute();
        }
        else{
            Log.i("INFO", "HeroView - onCreate(): Hero data does exist and is current. Grabbing current data from file - hero.data ");
        }
    }

    @Override
    public void processHeroInfoFinish(final HashMap<String,HeroData> hData){
        heroDataMap = hData;
    }


    public void getCardData(){
        try {
            cDataMap = fileManager.readCardsFromStorage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileManager.isLatestCardData(cDataMap)){

            Log.i("INFO", "MainActivity - getCardData(): Card data does not exist or is outdated. Grabbing current data from API ");

            ParagonAPICardInfo cardInfo = new ParagonAPICardInfo();
            cardInfo.delegate = this;
            cardInfo.execute();
        }
        else{
            Log.i("INFO", "MainActivity - getCardData(): Card data does exist and is current. Grabbing current data from file - cards.data ");
            processCardInfoFromFile(cDataMap);
        }
    }



    public void processCardInfoFromFile(HashMap<String,List<CardData>> cDataMap) {

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */

        List<CardData> cDataList = cDataMap.get("Equip");

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar today = Calendar.getInstance();
        String todayStr = formatter.format(today.getTime());
        Log.i("INFO", "Today's Date: " + todayStr);
        String[] todayParts = todayStr.split("-");
        Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
        Log.i("INFO", "Today's Date Sum: " + dateSum.toString());
        Integer chosenCard = dateSum % cDataList.size();
        Log.i("INFO", "Today's Chosen Card Index: " + chosenCard.toString());

        //Grab the chosen card
        this.cotd = cDataList.get(chosenCard);

        ImageLoader imgLoader = new ImageLoader(this.cotd);
        imgLoader.delegate = this;
        imgLoader.execute();
    }






    //@Override
    public void processCardInfoFinish(HashMap<String,List<CardData>> cDataMap) {

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */

        Integer chosenCard = 0;
        List<CardData> cDataList = cDataMap.get("Equip");

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Calendar today = Calendar.getInstance();
            String todayStr = formatter.format(today.getTime());
            Log.i("INFO", "Today's Date: " + todayStr);
            String[] todayParts = todayStr.split("-");
            Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
            Log.i("INFO", "Today's Date Sum: " + dateSum.toString());
            chosenCard = dateSum % cDataList.size();
            Log.i("INFO", "Today's Chosen Card Index: " + chosenCard.toString());
        }

        catch (ArithmeticException a){
            chosenCard = 1;
            Log.e("CARDERROR", a.getMessage());
        }

            //Grab the chosen card
            this.cotd = cDataList.get(chosenCard);

            try {
                fileManager.saveCardsToStorage(cDataMap.get("All"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            ImageLoader imgLoader = new ImageLoader(this.cotd);
            imgLoader.delegate = this;
            imgLoader.execute();






    }

    @Override
    public void processImageLoaderFinish(Bitmap imgBitmap) {
        //Set the grid view Adapter
       // gridview.setAdapter(new MyAdapter(this,imgBitmap));
    }

    private String mExtractToken(String url) {
        // url has format https://localhost/#access_token=<tokenstring>&token_type=Bearer&expires_in=315359999
        String[] sArray = url.split("code=");
        System.out.println(sArray[1]);
        return sArray[1];
    }

}

class AppRater {
    private final static String APP_TITLE = "Paraguide";// App Name
    private final static String APP_PNAME = "com.optimalotaku.paraguide";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }



    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        tv.setWidth(width);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }

}

