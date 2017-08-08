package com.optimalotaku.paraguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Brandon on 1/13/17.
 */

public class newsView extends Activity{
    private WebView newsView;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.news);

        this.newsView = (WebView)findViewById(R.id.newsview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            this.newsView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            this.newsView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        WebSettings settings = newsView.getSettings();
        settings.setJavaScriptEnabled(true);
        newsView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        //progressBar = ProgressDialog.show(newsView.this, "", "Retrieving Paragon News page...");

        newsView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                /*if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }*/
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                //Toast.makeText(, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        newsView.loadUrl("https://www.epicgames.com/paragon/en-US/news");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Prevents view from closing when back is pressed
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (newsView.canGoBack()) {
                        newsView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
