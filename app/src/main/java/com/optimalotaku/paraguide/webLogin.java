package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brandon on 12/15/16.
 */

public class webLogin extends AppCompatActivity {

    private static final String SHPREF_KEY_ACCESS_TOKEN = "Access_Token";
    private String accessToken;
    private static final String REDIRECT_URI = "https://optimalotaku.com/paraguide/";
    private static final String AUTHORIZE_PATH = "https://developer-paragon.epicgames.com/v1/auth/login/";
    private static final String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
    private static final String clientID = "5cbc82af86414e03a549dbb811dfbbc5";
    private String authCode;
    private String token;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
        String clientID = "5cbc82af86414e03a549dbb811dfbbc5";
        setContentView(R.layout.login);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        //SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();

        //AccountManagerFuture<Bundle> authToken = am.getAuthToken();

        if (authCode == null) {

            // need to get access token with OAuth2.0

            // set up webview for OAuth2 login
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.startsWith(REDIRECT_URI)) {

                        // extract OAuth2 access_code appended in url
                        if (url.indexOf("?code=") != -1) {

                            // store temporarily

                            authCode = mExtractToken(url);

                            //SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
                            //e.putString(SHPREF_KEY_ACCESS_TOKEN, accessToken);
                            //e.commit();

                            // spawn worker thread to do api calls t
                            new MyWebservicesAsyncTask().execute();
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
            new MyWebservicesAsyncTask().execute();
        }


        //myWebView.loadUrl("http://www.example.com");
    }

    private String mExtractToken(String url) {
        // url has format https://localhost/#access_token=<tokenstring>&token_type=Bearer&expires_in=315359999
        String[] sArray = url.split("code=");
        System.out.println(sArray[1]);
        return sArray[1];
    }

    private String mReturnAuthorizationRequestUri() {
        StringBuilder sb = new StringBuilder();
        sb.append(AUTHORIZE_PATH);
        sb.append(clientID);
        return sb.toString();
    }

    private String encode64() {
        String combineForBytes = Constants.CLIENT_ID + ':' + Constants.CLIENT_SECRET;
        String encodedBytes = Base64.encodeToString(combineForBytes.getBytes(), Base64.NO_WRAP);
        return encodedBytes.toString().trim();
    }

    private class MyWebservicesAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... urls) {

            String token;
            String accountID;
            String expireTime;
            URL url = null;
            URL url2 = null;
            HttpURLConnection urlConnection = null;
            HttpURLConnection urlConnection2 = null;
            try {
                url = new URL("https://developer-paragon.epicgames.com/v1/auth/token/" + authCode);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                urlConnection.addRequestProperty(Constants.AUTH_VAR, "Basic " + encode64());
                String testEncode = encode64();
                System.out.println(urlConnection.toString());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder stringBuilder2 = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                try {
                    JSONObject obj = null;

                    obj = new JSONObject(stringBuilder.toString());
                    token = obj.getString("token");
                    accountID = obj.getString("accountId");
                    expireTime = obj.getString("expireTime");
                    url2 = new URL("https://developer-paragon.epicgames.com/v1/account/"+accountID+"/decks");
                    urlConnection2 = (HttpURLConnection) url2.openConnection();
                    urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                    urlConnection2.addRequestProperty(Constants.AUTH_VAR, "Bearer " + token);

                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

                    String line2;
                    while ((line2 = bufferedReader2.readLine()) != null) {
                        stringBuilder2.append(line2).append("\n");
                    }
                    bufferedReader2.close();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return stringBuilder2.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }  finally {
                urlConnection.disconnect();
            }
        }


        protected void onPostExecute(String response){

            String token;
            String accountID;
            String expireTime;
            String deckID = null;
            String deckName = null;
            String heroName = null;



            TextView responseView = (TextView) findViewById(R.id.textView);

            if (response == null) {
                Log.i("INFO", "ACCOUNT ERROR");
            } else {

                Log.i("INFO", response);
                JSONArray deckArray = null;

                try {
                    //setContentView(R.layout.activity_main);

                    setContentView(R.layout.deckreadout);
                    responseView = (TextView) findViewById(R.id.textView);
                    deckArray = new JSONArray(response);
                    for(int i = 0; i < deckArray.length(); i++){
                        JSONObject deck = deckArray.getJSONObject(i);
                        deckName = "Deck Name: " + deck.getString("name");
                        deckID = "Deck ID: " + deck.getString("id");
                        heroName = "Hero Name: " + deck.getJSONObject("hero").getString("name");
                        if(i < 1){
                            responseView.setText(deckID);}
                        else
                            responseView.append(deckID);
                        responseView.append("\n");
                        responseView.append(deckName);
                        responseView.append("\n");
                        responseView.append(heroName);
                        responseView.append("\n");
                        responseView.append("\n");
                    }

                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(responseView.getWindowToken(), 0);
                    Button endButton = (Button) findViewById(R.id.button3);
                    endButton.setVisibility(View.VISIBLE);




                } catch (JSONException e) {
                    e.printStackTrace();
                    }
                }
            }
        }

        public void endSession(View view){
            Intent intent = new Intent(webLogin.this, MainActivity.class);
            startActivity(intent);
        }
    }

