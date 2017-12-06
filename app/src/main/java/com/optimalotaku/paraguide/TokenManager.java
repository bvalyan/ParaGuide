package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 12/15/16.
 */

public class TokenManager extends Fragment{

    private String authCode;
    FileManager deckManager;
    ListView list;
    private GridView gridview;
    ProgressDialog progressDialog;
    String [] pics2;
    String [] cardText;
    static boolean logouts = false;
    static Menu menus;
    static HashMap<String,HeroData> heroDataMap;

    public static TokenManager newInstance(boolean logout, Menu menu,HashMap<String,HeroData> heroMap) {
        logouts = logout;
        menus = menu;
        heroDataMap = heroMap;
        return new TokenManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle extras = getActivity().getIntent().getExtras();

        View view = inflater.inflate(R.layout.login, container, false);
        deckManager = new FileManager(getContext());
        final WebView myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);


        if (logouts) {

            myWebView.setWebViewClient(new WebViewClient() {
                SharedPreferences.Editor e = getActivity().getPreferences(Context.MODE_PRIVATE).edit();

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    return true;
                }
            });
            String deauthorizationUri = mReturnDeAuthorizationRequestUri();
            myWebView.loadUrl(deauthorizationUri);
            myWebView.clearCache(true);
            CookieSyncManager.createInstance(getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            Toast.makeText(getActivity().getApplicationContext(), "Signed out!",
                    Toast.LENGTH_LONG).show();
            for (int menuItemIndex = 0; menuItemIndex < menus.size(); menuItemIndex++) {
                MenuItem menuItem = menus.getItem(menuItemIndex);
                if (menuItem.getItemId() == R.id.signinbutton) {
                    menuItem.setVisible(true);
                }
                if (menuItem.getItemId() == R.id.signoutbutton) {
                    menuItem.setVisible(false);
                }
            }
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, NewHomeFragment.newInstance(heroDataMap))
                    .commit();
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        authCode = prefs.getString("signedIn", null);

        if (authCode == null && logouts == false) {

            // need to get access token with OAuth2.0

            // set up webview for OAuth2 login

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

                            SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo",Context.MODE_PRIVATE).edit();
                            e.putString("signedIn", authCode);
                            e.apply();

                            // spawn worker thread to do api calls
                            //ParagonAPIDeckInfo deckInfo = new ParagonAPIDeckInfo(authCode, TokenManager.this);
                            //setDelegate(deckInfo);
                            //deckInfo.execute();

                          /*  AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(TokenManager.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(TokenManager.this);
                            }
                            builder.setTitle("Signed In! Welcome to Paraguide!")
                                    .setMessage("Are you sure you want to delete this entry?")
                                    .setPositiveButton("Thanks!", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();*/

                            try {
                                getToken(getActivity().getApplicationContext(), authCode);
                            } catch (ExecutionException e1) {
                                e1.printStackTrace();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            Toast.makeText(getActivity().getApplicationContext(), "Signed in!",
                                    Toast.LENGTH_LONG).show();
                            for (int menuItemIndex = 0; menuItemIndex < menus.size(); menuItemIndex++) {
                                MenuItem menuItem = menus.getItem(menuItemIndex);
                                if (menuItem.getItemId() == R.id.signinbutton) {
                                    menuItem.setVisible(false);
                                }
                                if (menuItem.getItemId() == R.id.signoutbutton) {
                                    menuItem.setVisible(true);
                                }
                            }
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, NewHomeFragment.newInstance(heroDataMap))
                                    .commit();
                            //

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

        } else {
            // have access token, so spawn worker thread to do api calls

            //ParagonAPIDeckInfo deckInfo = new ParagonAPIDeckInfo(authCode, TokenManager.this);
            //deckInfo.execute();
        }


        //myWebView.loadUrl("http://www.example.com");
        return view;
    }



    public String checkToken(Context context, String authCode) throws ExecutionException, InterruptedException, ParseException {
        SharedPreferences prefs = context.getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        String expireDate = prefs.getString("EXPIRE_TIME", "null");
        String userID = prefs.getString("ACCOUNT_ID", "null");


        if(expireDate.equals("null")){// if we don't have a token, go get one!
            getToken(context, authCode);
            expireDate = prefs.getString("EXPIRE_TIME", "null");
        }
        else{ //check to make sure token hasnt expired. If it has, go get a new one!
            GregorianCalendar calendar = new GregorianCalendar();
            DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
            Date date = format.parse(expireDate);
            calendar.setTime(date);
            Calendar now = new GregorianCalendar();
            int compare = calendar.compareTo(now);
            if(compare < 0){
                Log.e("TOKEN","Gotta get tokens!");
                userID = getToken(context, authCode);
            }
        }
        return userID;
    }

    public String getToken(Context context, String authCode) throws ExecutionException, InterruptedException {
        ParagonAPITokenJob obtainToken = new ParagonAPITokenJob(authCode, context);
        String userID = obtainToken.execute().get();
        return userID;
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
    private String mReturnDeAuthorizationRequestUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.LOGOUT_PATH);
        sb.append(Constants.CLIENT_ID);
        return sb.toString();
    }


}

