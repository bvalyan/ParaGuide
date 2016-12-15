package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) throws InterruptedException {

        //Gather UI Objects
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        EditText hEdit = (EditText) findViewById(R.id.heroText);
        TextView responseView = (TextView) findViewById(R.id.responseView);
        ImageView picDisplay = (ImageView) findViewById(R.id.HeroImages);

        //Create HeroData Object
        HeroData heroData = new HeroData();


        ParagonAPIHelper paraHelper = new ParagonAPIHelper(progressBar,hEdit.toString(),heroData);
        paraHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Picture
        picDisplay.setVisibility(View.VISIBLE);
        Glide.with(MainActivity.this).load(heroData.getImageIconURL()).into(picDisplay);

        //Set Summary Text
        responseView.setText(mobilityStatement);
        responseView.append("\n");
        responseView.append(basicAttackStatement);
        responseView.append("\n");
        responseView.append(durabilityStatement);
        responseView.append("\n");
        responseView.append(abilityAttackStatement);


        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            //hide keyboard upon return
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }


    }

}

