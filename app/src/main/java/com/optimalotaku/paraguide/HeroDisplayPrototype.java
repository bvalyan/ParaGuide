package com.optimalotaku.paraguide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by bvaly on 1/16/2017.
 */

public class HeroDisplayPrototype extends AppCompatActivity{
    public HeroData selectedHero = new HeroData();



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_data_ptype);
        TextView nameView = (TextView) findViewById(R.id.titleText);
        ImageView heroImage = (ImageView) findViewById(R.id.titlePic);
        TextView description = (TextView) findViewById(R.id.descText);
        ImageView aff1 =  (ImageView) findViewById(R.id.aff1Pic);
        ImageView aff2 =  (ImageView) findViewById(R.id.aff2Pic);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle gifts = getIntent().getExtras();
        String name = gifts.getString("name");
        String scale = gifts.getString("attack");
        int difficulty =  gifts.getInt("difficulty");
        String picURL = gifts.getString("picURL");
        String affinity1 = gifts.getString("affinity1");
        String affinity2 = gifts.getString("affinity2");

        nameView.setTextSize(50);
        nameView.setText(name);
        Glide.with(this).load(picURL).into(heroImage);
        HeroData heroData = new HeroData();
        description.setTextSize(15);
        String scaling = "This hero's primary attack type is " + scale.toLowerCase() + ".\n";
        String difficultyRating = "This hero's difficulty is rated at a " + difficulty + ".\n";
        description.append(scaling);
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        description.append(difficultyRating);
        switch (affinity1.toLowerCase()){
            case "growth":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.growth_affinity_icon);
                break;
            case "order":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.order_affinity_icon);
                break;
            case "corruption":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.corruption_affinity_icon);
                break;
            case "fury" :
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fury_affinity_icon);
                break;
            case "intellect":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.intellect_affinity_icon);
                break;
        }

        aff1.setImageBitmap(bitmap);

        switch (affinity2.toLowerCase()){
            case "growth":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.growth_affinity_icon);
                break;
            case "order":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.order_affinity_icon);
                break;
            case "corruption":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.corruption_affinity_icon);
                break;
            case "fury" :
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.fury_affinity_icon);
                break;
            case "intellect":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.intellect_affinity_icon);
                break;
        }

        aff2.setImageBitmap(bitmap2);


    }



}
