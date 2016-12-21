package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Brandon on 12/15/16.
 */

public class DeckView extends AppCompatActivity implements DeckInfoResponse {

    private String authCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.login);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);


        if (authCode == null) {

            // need to get access token with OAuth2.0

            // set up webview for OAuth2 login
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.startsWith(Constants.REDIRECT_URI)) {

                        // extract OAuth2 access_code appended in url
                        if (url.indexOf("?code=") != -1) {

                            // store temporarily

                            authCode = mExtractToken(url);

                            //SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
                            //e.putString(SHPREF_KEY_ACCESS_TOKEN, accessToken);
                            //e.commit();

                            // spawn worker thread to do api calls t
                            ParagonAPIDeckInfo deckInfo = new ParagonAPIDeckInfo(authCode);
                            setDelegate(deckInfo);
                            deckInfo.execute();
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
            ParagonAPIDeckInfo deckInfo = new ParagonAPIDeckInfo(authCode);
            deckInfo.execute();
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
        sb.append(Constants.AUTHORIZE_PATH);
        sb.append(Constants.CLIENT_ID);
        return sb.toString();
    }

    public void endSession(View view){
        Intent intent = new Intent(DeckView.this, MainActivity.class);
        startActivity(intent);
    }

    public void setDelegate(ParagonAPIDeckInfo deckInfo){
        deckInfo.delegate = this;
    }

    @Override
    public void processDeckInfoFinish(List<DeckData> dDataList) {

        setContentView(R.layout.deckreadout);
        TextView responseView = (TextView) findViewById(R.id.textView);
        String deckListStr = "";

        for(DeckData deck : dDataList){

            deckListStr = deckListStr + deck.getDeckName() + "\n";
            deckListStr = deckListStr + deck.getHeroName() + "\n\n";


        }

        responseView.setText(deckListStr);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(responseView.getWindowToken(), 0);
        Button endButton = (Button) findViewById(R.id.button3);
        endButton.setVisibility(View.VISIBLE);


    }
}

