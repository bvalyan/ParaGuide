package com.optimalotaku.paraguide;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CardInfoResponse, ImageLoaderResponse, HeroInfoResponse, NavigationView.OnNavigationItemSelectedListener {

    CardData cotd;
    FileManager fileManager;
    HashMap<String,HeroData> heroDataMap;
    HashMap<String,List<CardData>> cDataMap;
    ProgressDialog progress;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    String authCode;
    Menu menu;
    SharedPreferences prefs;
    SharedPreferences.Editor e;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("authInfo", MODE_PRIVATE);
        e = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fileManager = new FileManager(this);
        heroDataMap = new HashMap<>();
        cDataMap = new HashMap<>();
        authCode = prefs.getString("signedIn", "null");

        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        drawer = (DrawerLayout) findViewById(R.id.nav_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();

        if(!authCode.equals("null")) {
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



        if (savedInstanceState != null) {
            return;
        }
        else{


            authCode = prefs.getString("signedIn", "null");

            progress = ProgressDialog.show(this, "Loading",
                    "Checking for new Card/Hero data...", true);

            getHeroData();
            getCardData();

            progress.dismiss();
            AppRater.app_launched(this);

        }
    }

    private String mReturnAuthorizationRequestUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.AUTHORIZE_PATH);
        sb.append(Constants.CLIENT_ID);
        sb.append(Constants.RESPONSE_TYPE);
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, NewHomeFragment.newInstance(heroDataMap))
                    .commit();
        }
    }

    @Override
    public void processHeroInfoFinish(final HashMap<String,HeroData> hData){
        heroDataMap = hData;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, NewHomeFragment.newInstance(heroDataMap))
                .commit();
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
        List<CardData> cDataList = cDataMap.get("All");

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        Intent intent;

        switch (item.getItemId()) {
            case R.id.signoutbutton:
                e.remove("signedIn");
                e.apply();
                authCode = prefs.getString("signedIn", "null");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, TokenManager.newInstance(true, menu, heroDataMap))
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
                //mCurrentSelectedPosition = 0;
                return true;
            case R.id.signinbutton:
                authCode = prefs.getString("signedIn", "null");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, TokenManager.newInstance(false, menu, heroDataMap))
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
                //mCurrentSelectedPosition = 0;
                return true;
            case R.id.navigation_item_0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, NewHomeFragment.newInstance(heroDataMap))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
                // mCurrentSelectedPosition = 1;
                return true;
            case R.id.navigation_item_1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, AccountSearch.newInstance(heroDataMap))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
               // mCurrentSelectedPosition = 1;
                return true;
            case R.id.navigation_item_2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, CardDisplay.newInstance(cotd))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
               // mCurrentSelectedPosition = 2;
                return true;
            case R.id.navigation_item_3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, newsView.newInstance())
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
              //  mCurrentSelectedPosition = 3;
                return true;
            case R.id.navigation_item_4:
                intent = new Intent(MainActivity.this, HeroView.class);
                intent.putExtra("HeroMap", heroDataMap);
                startActivity(intent);
              //  mCurrentSelectedPosition = 4;
                drawer.closeDrawer(Gravity.LEFT);
                return true;
            case R.id.navigation_item_5:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, Cards.newInstance(cotd))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
               // mCurrentSelectedPosition = 5;
                return true;
                    /*case R.id.navigation_item_6:
                        intent = new Intent(MainActivity.this, MyDecks.class);
                        startActivity(intent);
                        mCurrentSelectedPosition = 6;
                        return true;*/
            default:
                return true;
        }
    }
}

class AppRater {
    private final static String APP_TITLE = "Paraguide";// App Name
    private final static String APP_PNAME = "com.optimalotaku.paraguide";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

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

