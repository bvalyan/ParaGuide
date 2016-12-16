package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Brandon on 12/15/16.
 */

public class webLogin extends AppCompatActivity {

    private static final String SHPREF_KEY_ACCESS_TOKEN = "Access_Token";
    private String accessToken;
    private static final String REDIRECT_URI	= "https://optimalotaku.com/paraguide/";
    private static final String AUTHORIZE_PATH	= "https://developer-paragon.epicgames.com/v1/auth/login/";
    private static final String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
    private static final String clientID = "5cbc82af86414e03a549dbb811dfbbc5";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
        String clientID = "5cbc82af86414e03a549dbb811dfbbc5";
        setContentView(R.layout.login);
        WebView myWebView = (WebView) findViewById(R.id.webview);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();

        //AccountManagerFuture<Bundle> authToken = am.getAuthToken();

        if (accessToken == null) {

            // need to get access token with OAuth2.0

            // set up webview for OAuth2 login
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if ( url.startsWith(REDIRECT_URI) ) {

                        // extract OAuth2 access_token appended in url
                        if ( url.indexOf("?code=") != -1 ) {
                            accessToken = mExtractToken(url);

                            // store in default SharedPreferences
                            SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
                            e.putString(SHPREF_KEY_ACCESS_TOKEN, accessToken);
                            e.commit();

                            // spawn worker thread to do api calls to get list of contacts to display
                            new MyWebservicesAsyncTask().execute(accessToken);
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

            // have access token, so spawn worker thread to do api calls to get list of contacts to display
            new MyWebservicesAsyncTask().execute(accessToken);
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

    private class MyWebservicesAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {


            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // called in UI thread
            if (result) {

            }
            super.onPostExecute(result);
        }
    }

}
