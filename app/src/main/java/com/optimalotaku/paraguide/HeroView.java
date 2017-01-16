package com.optimalotaku.paraguide;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jerek on 12/19/2016.
 */

public class HeroView extends ListActivity implements HeroInfoResponse {

    ListView list;
    String [] text;
    String [] pics;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gather UI Objects
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        EditText hEdit = (EditText) findViewById(R.id.heroText);

        //Create HeroData Object
        HeroData heroData = new HeroData();
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.listtest);
        newHeroPrototype heroInfo = new newHeroPrototype();
        heroInfo.delegate = this;
        heroInfo.execute();
    }

    public void onSearch(View view) throws InterruptedException {




    }


    @Override
    public void processHeroInfoFinish(final HeroData[] hData){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView responseView = (TextView) findViewById(R.id.responseView);
        ImageView picDisplay = (ImageView) findViewById(R.id.HeroImages);


//        progressBar.setVisibility(View.GONE);

         text = new String[hData.length];
         pics = new String[hData.length];

        for (int i=0; i< hData.length;i++){
            text[i]= (hData[i].getName());
            pics[i] = (hData[i].getImageIconURL());
        }


        CustomList adapter = new
                CustomList(this, text, pics);
        list=(ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "You Clicked " +text[+ position], Toast.LENGTH_SHORT).show();
                //start new activity with method that takes in name and HeroData object and displays information
            }
        });

        //Picture
 /*       if(hData.getImageIconURL() != null && !hData.getImageIconURL().equals("")) {
            picDisplay.setVisibility(View.VISIBLE);
            Log.i("INFO", "MainActivity - processHeroInfoFinish() - ImageURL: " + hData.getImageIconURL());
            Glide.with(this).load("https:" + hData.getImageIconURL()).into(picDisplay);
        }



        if(!hData.getEmpty()) {
            //Set Summary Text
            //Mobility
            if (isBetween(hData.getMobility(), 1, 3)) {
                responseView.setText(Constants.LOW_MOBILITY_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getMobility(), 4, 7)) {
                responseView.setText(Constants.MED_MOBILITY_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getMobility(), 8, 10)) {
                responseView.setText(Constants.HI_MOBILITY_STATEMENT);
                responseView.append("\n");
            } else {
                Log.i("INFO", "No valid Mobility given: " + hData.getMobility().toString());
            }

            //Basic Attack
            if (isBetween(hData.getBasicAttack(), 1, 3)) {
                responseView.append(Constants.LOW_BASIC_ATTACK_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getBasicAttack(), 4, 7)) {
                responseView.append(Constants.MED_BASIC_ATTACK_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getBasicAttack(), 8, 10)) {
                responseView.append(Constants.HI_BASIC_ATTACK_STATEMENT);
                responseView.append("\n");
            } else {
                Log.i("INFO", "No valid Basic Attack given: " + hData.getBasicAttack().toString());
            }

            //Durability
            if (isBetween(hData.getDurability(), 1, 3)) {
                responseView.append(Constants.LOW_DURABILITY_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getDurability(), 4, 7)) {
                responseView.append(Constants.MED_DURABILITY_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getDurability(), 8, 10)) {
                responseView.append(Constants.HI_DURABILITY_STATEMENT);
                responseView.append("\n");
            } else {
                Log.i("INFO", "No valid Durability given: " + hData.getDurability().toString());
            }


            //Ability Attack
            if (isBetween(hData.getAbilityAttack(), 1, 3)) {
                responseView.append(Constants.LOW_ABILITY_ATTACK_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getAbilityAttack(), 4, 7)) {
                responseView.append(Constants.MED_ABILITY_ATTACK_STATEMENT);
                responseView.append("\n");
            } else if (isBetween(hData.getAbilityAttack(), 8, 10)) {
                responseView.append(Constants.HI_ABILITY_ATTACK_STATEMENT);
                responseView.append("\n");
            } else {
                Log.i("INFO", "No valid Durability given: " + hData.getAbilityAttack().toString());
            }
        }
        else{
            EditText hEdit = (EditText) findViewById(R.id.heroText);
            responseView.setText("No hero data available for: "+hEdit.getText().toString()+"\nPlease try again.");
        }*/


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
