package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
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


public class MainActivity  extends AppCompatActivity implements AsyncResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) throws InterruptedException {

        //Gather UI Objects
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        EditText hEdit = (EditText) findViewById(R.id.heroText);

        //Create HeroData Object
        HeroData heroData = new HeroData();

        ParagonAPIHelper paraHelper = new ParagonAPIHelper(progressBar,hEdit.getText().toString(),heroData);
        paraHelper.delegate = this;
        paraHelper.execute();
    }

    public void loginSend(View view){
        View view2 = this.getCurrentFocus();
        if(view2 != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
        Intent intent = new Intent(MainActivity.this, DeckView.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(HeroData hData){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView responseView = (TextView) findViewById(R.id.responseView);
        ImageView picDisplay = (ImageView) findViewById(R.id.HeroImages);

        progressBar.setVisibility(View.GONE);

        //Picture
        picDisplay.setVisibility(View.VISIBLE);
        Log.i("INFO","MainActivity - processFinish() - ImageURL: "+ hData.getImageIconURL());
        Glide.with(this).load("https:"+hData.getImageIconURL()).into(picDisplay);

        //Set Summary Text
        //Mobility
        if(isBetween(hData.getMobility(),1,3)){
            responseView.setText(Constants.LOW_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getMobility(),4,7)){
            responseView.setText(Constants.MED_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getMobility(),8,10)){
            responseView.setText(Constants.HI_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else{
           Log.i("INFO","No valid Mobility given: "+hData.getMobility().toString());
        }

        //Basic Attack
        if(isBetween(hData.getBasicAttack(),1,3)){
            responseView.append(Constants.LOW_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getBasicAttack(),4,7)){
            responseView.append(Constants.MED_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getBasicAttack(),8,10)){
            responseView.append(Constants.HI_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else{
            Log.i("INFO","No valid Basic Attack given: "+hData.getBasicAttack().toString());
        }

        //Durability
        if(isBetween(hData.getDurability(),1,3)){
            responseView.append(Constants.LOW_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getDurability(),4,7)){
            responseView.append(Constants.MED_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getDurability(),8,10)){
            responseView.append(Constants.HI_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else{
            Log.i("INFO","No valid Durability given: "+hData.getDurability().toString());
        }


        //Ability Attack
        if(isBetween(hData.getAbilityAttack(),1,3)){
            responseView.append(Constants.LOW_ABILITY_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getAbilityAttack(),4,7)){
            responseView.append(Constants.MED_ABILITY_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(isBetween(hData.getAbilityAttack(),8,10)){
            responseView.append(Constants.HI_ABILITY_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else {
            Log.i("INFO", "No valid Durability given: " + hData.getAbilityAttack().toString());
        }


        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            //hide keyboard upon return
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

    }

    //Helper Functions
    public static boolean isBetween( Integer x, Integer lower, Integer upper) {
        return lower <= x && x <= upper;
    }

}

