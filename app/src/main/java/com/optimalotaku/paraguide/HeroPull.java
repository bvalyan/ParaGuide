package com.optimalotaku.paraguide;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Brandon on 12/19/16.
 */

public class HeroPull extends AppCompatActivity {
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
        if(MainActivity.isBetween(hData.getMobility(),1,3)){
            responseView.setText(Constants.LOW_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getMobility(),4,7)){
            responseView.setText(Constants.MED_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getMobility(),8,10)){
            responseView.setText(Constants.HI_MOBILITY_STATEMENT);
            responseView.append("\n");
        }
        else{
            Log.i("INFO","No valid Mobility given: "+hData.getMobility().toString());
        }

        //Basic Attack
        if(MainActivity.isBetween(hData.getBasicAttack(),1,3)){
            responseView.append(Constants.LOW_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getBasicAttack(),4,7)){
            responseView.append(Constants.MED_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getBasicAttack(),8,10)){
            responseView.append(Constants.HI_BASIC_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else{
            Log.i("INFO","No valid Basic Attack given: "+hData.getBasicAttack().toString());
        }

        //Durability
        if(MainActivity.isBetween(hData.getDurability(),1,3)){
            responseView.append(Constants.LOW_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getDurability(),4,7)){
            responseView.append(Constants.MED_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getDurability(),8,10)){
            responseView.append(Constants.HI_DURABILITY_STATEMENT);
            responseView.append("\n");
        }
        else{
            Log.i("INFO","No valid Durability given: "+hData.getDurability().toString());
        }


        //Ability Attack
        if(MainActivity.isBetween(hData.getAbilityAttack(),1,3)){
            responseView.append(Constants.LOW_ABILITY_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getAbilityAttack(),4,7)){
            responseView.append(Constants.MED_ABILITY_ATTACK_STATEMENT);
            responseView.append("\n");
        }
        else if(MainActivity.isBetween(hData.getAbilityAttack(),8,10)){
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
}
