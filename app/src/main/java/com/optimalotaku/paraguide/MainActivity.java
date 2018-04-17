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
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CardData cotd;
    FileManager fileManager;
    HashMap<String,HeroData> heroDataMap;
    ArrayList<ChampionData> championDataList;
    HashMap<String,List<CardData>> cDataMap;
    ProgressDialog progress;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    String authCode;
    Menu menu;
    SharedPreferences prefs;
    SharedPreferences.Editor e;
    DrawerLayout drawer;
    RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("authInfo", MODE_PRIVATE);
        e = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        AppRater.app_launched(this);
        createAPISession();

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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(3);
        findViewById(R.id.menu_action_1).startAnimation(rotation);
        switch (item.getItemId()) {
            case R.id.menu_action_1:
                versionUpdate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void createAPISession() {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormatUTC.format(Calendar.getInstance().getTime());
        String signature = GetMD5Hash(Constants.PALADINS_DEV_ID + "createsession" + Constants.PALADINS_AUTH_KEY + date);

        String url = Constants.PALADINS_API_URI + "createsessionjson/" + Constants.PALADINS_DEV_ID + "/" + signature + "/" + date;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText("Response: " + response.toString());
                        Log.i("SUCCESS", "CREATE SESSION SUCCESS");
                        try {
                            e.putString("session_id", response.getString("session_id"));
                            e.putLong("session_time", System.currentTimeMillis());
                            e.apply();
                            versionUpdate();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FAILURE", "CREATE SESSION FAILURE");


                    }
                });
        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);

    }

   /* private void databaseUpdater() {
        if(!prefs.getString("session_id","").equals("") && System.currentTimeMillis() < prefs.getLong("session_time",0) +  900000){
            championUpdate();
            versionUpdate();
        }
        else {
            createAPISession();
        }
    }*/

    private void versionUpdate() {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormatUTC.format(Calendar.getInstance().getTime());
        String session = prefs.getString("session_id","");
        int langCode = prefs.getInt("lang_code",1);
        String signature = GetMD5Hash(Constants.PALADINS_DEV_ID + "getpatchinfo" + Constants.PALADINS_AUTH_KEY + date);
        String url = Constants.PALADINS_API_URI + "getpatchinfojson/" + Constants.PALADINS_DEV_ID + "/" + signature + "/" + session + "/" + date;
        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTextView.setText("Response: " + response.toString());
                        Log.i("SUCCESS", "VERSION ACQUISITION SUCCESS");
                        try {
                            JSONObject versionObject = new JSONObject(response);
                            String versionString = versionObject.getString("version_string");
                            String currentVersion = prefs.getString("version","");
                            if(versionString.equals(currentVersion)){
                                Toast.makeText(MainActivity.this, "Database up to date!",
                                        Toast.LENGTH_LONG).show();
                                Log.i("INFO", "HeroView - onCreate(): Champion data does exist and is current. Grabbing current data from file ");
                                championDataList =  FileManager.readChampsFromStorage(MainActivity.this);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(R.id.fragment_container, NewHomeFragment.newInstance(championDataList))
                                        .commit();
                            }
                            else {
                                championUpdate(versionString);
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FAILURE", "VERSION ACQUISITION FAILURE");
                    }
                });
        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);
    }

    private void championUpdate(final String versionString)  {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormatUTC.format(Calendar.getInstance().getTime());
        String session = prefs.getString("session_id","");
        int langCode = prefs.getInt("lang_code",1);
        String signature = GetMD5Hash(Constants.PALADINS_DEV_ID + "getchampions" + Constants.PALADINS_AUTH_KEY + date);
        String url = Constants.PALADINS_API_URI + "getchampionsjson/" + Constants.PALADINS_DEV_ID + "/" + signature + "/" + session + "/" + date + "/" + langCode;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse (JSONArray response) {
                        //mTextView.setText("Response: " + response.toString());
                        Log.i("SUCCESS", "CHAMPION ACQUISITION SUCCESS");
                        ArrayList champList = new ArrayList();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject currentObject = response.getJSONObject(i);
                                ChampionData champion = new ChampionData();
                                AbilityObject ability1 = new AbilityObject(response.getJSONObject(i).getJSONObject("Ability_1").getInt("Id"), response.getJSONObject(i).getJSONObject("Ability_1").getString("Summary"), response.getJSONObject(i).getJSONObject("Ability_1").getString("Description"), response.getJSONObject(i).getJSONObject("Ability_1").getString("URL"));
                                AbilityObject ability2 = new AbilityObject(response.getJSONObject(i).getJSONObject("Ability_2").getInt("Id"), response.getJSONObject(i).getJSONObject("Ability_2").getString("Summary"), response.getJSONObject(i).getJSONObject("Ability_2").getString("Description"), response.getJSONObject(i).getJSONObject("Ability_2").getString("URL"));
                                AbilityObject ability3 = new AbilityObject(response.getJSONObject(i).getJSONObject("Ability_3").getInt("Id"), response.getJSONObject(i).getJSONObject("Ability_3").getString("Summary"), response.getJSONObject(i).getJSONObject("Ability_3").getString("Description"), response.getJSONObject(i).getJSONObject("Ability_3").getString("URL"));
                                AbilityObject ability4 = new AbilityObject(response.getJSONObject(i).getJSONObject("Ability_4").getInt("Id"), response.getJSONObject(i).getJSONObject("Ability_4").getString("Summary"), response.getJSONObject(i).getJSONObject("Ability_4").getString("Description"), response.getJSONObject(i).getJSONObject("Ability_4").getString("URL"));
                                AbilityObject ability5 = new AbilityObject(response.getJSONObject(i).getJSONObject("Ability_5").getInt("Id"), response.getJSONObject(i).getJSONObject("Ability_5").getString("Summary"), response.getJSONObject(i).getJSONObject("Ability_5").getString("Description"), response.getJSONObject(i).getJSONObject("Ability_5").getString("URL"));
                                champion.setAbility1(ability1);
                                champion.setAbility2(ability2);
                                champion.setAbility3(ability3);
                                champion.setAbility4(ability4);
                                champion.setAbility5(ability5);
                                champion.setChampIconURL(currentObject.getString("ChampionIcon_URL"));
                                champion.setHealth(currentObject.getInt("Health"));
                                champion.setLore(currentObject.getString("Lore"));
                                champion.setName(currentObject.getString("Name"));
                                champion.setOnFreeRotation(currentObject.getString("OnFreeRotation"));
                                champion.setRoles(currentObject.getString("Roles"));
                                champion.setSpeed(currentObject.getString("Speed"));
                                champion.setTitle(currentObject.getString("Title"));
                                champion.setId(currentObject.getInt("id"));
                                champion.setLatestChamp(currentObject.getString("latestChampion"));

                                champList.add(champion);
                            }

                            String fileName = "Champions";
                            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(champList);
                            oos.close();

                            String currentVersion = prefs.getString("version","");
                            Toast.makeText(MainActivity.this, "Database updated! Welcome to patch " + currentVersion + "!",
                                    Toast.LENGTH_LONG).show();

                            championDataList =  FileManager.readChampsFromStorage(MainActivity.this);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.fragment_container, NewHomeFragment.newInstance(championDataList))
                                    .commit();


                        }
                        catch (JSONException a){
                            a.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        e.putString("version", versionString);
                        e.apply();
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FAILURE", "CHAMPION ACQUISITION FAILURE");
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonArrayRequest);
    }

    public static String GetMD5Hash(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        Intent intent;

        switch (item.getItemId()) {
            case R.id.navigation_item_0:
                if(championDataList.size() < 1){
                    versionUpdate();
                }
                else{
                    Log.i("HERODATA", "Hero data present! Good to go!");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, NewHomeFragment.newInstance(championDataList))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
                // mCurrentSelectedPosition = 1;
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
                if(championDataList.size() < 1){
                    versionUpdate();
                }
                else{
                    Log.i("Champ Data", "Champion data present! Good to go!");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, HeroView.newInstance(championDataList))
                        .addToBackStack("NEW")
                        .commit();
                drawer.closeDrawer(Gravity.LEFT);
                return true;
            case R.id.navigation_item_5:
               /* getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, AffinitySelectionFragment.newInstance(cotd))
                        .addToBackStack("NEW")
                        .commit();*/
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

