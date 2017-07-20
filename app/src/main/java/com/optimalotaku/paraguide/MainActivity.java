package com.optimalotaku.paraguide;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


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

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.drawer_layout);
        VideoView videoview = (VideoView) findViewById(R.id.paragon_vid);
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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Snackbar.make(mContentFrame, "Item One",
                                Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_item_2:
                        Snackbar.make(mContentFrame, "Item Two",
                                Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 1;
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

      /*  gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main drawer
                gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                switch(position){
                    case 0 :
                        intent = new Intent(MainActivity.this, AccountSearch.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(MainActivity.this, DeckView.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        break;
                    case 2 :
                        //intent = new Intent(MainActivity.this, PAChatActivity.class);
                        //startActivity(intent);
                        Toast.makeText(MainActivity.this, "Coming Soon!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3 :
                        intent = new Intent(MainActivity.this, HeroView.class);
                        intent.putExtra("HeroMap",heroDataMap);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(MainActivity.this, CardOfTheDayView.class);
                        intent.putExtra("CardOfTheDay",cotd);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(MainActivity.this, newsView.class);
                        startActivity(intent);
                        break;
                }
            }
        });*/
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

        List<CardData> cDataList = cDataMap.get("Equip");

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar today = Calendar.getInstance();
        String todayStr = formatter.format(today.getTime());
        Log.i("INFO","Today's Date: "+ todayStr);
        String[] todayParts = todayStr.split("-");
        Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
        Log.i("INFO","Today's Date Sum: "+dateSum.toString());
        Integer chosenCard = dateSum % cDataList.size();
        Log.i("INFO","Today's Chosen Card Index: "+chosenCard.toString());

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

