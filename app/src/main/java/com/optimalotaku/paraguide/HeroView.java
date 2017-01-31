package com.optimalotaku.paraguide;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Jerek on 12/19/2016.
 */

public class HeroView extends ListActivity {

    ListView list;
    String [] text;
    String [] pics;
    FileManager fileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gather UI Objects
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.listtest);
        fileManager = new FileManager(this);
        final HashMap<String,HeroData> hData = (HashMap<String,HeroData>) getIntent().getSerializableExtra("HeroMap");


        //Get List of hero names from Map
        text = hData.keySet().toArray(new String[hData.size()]);
        pics = new String[hData.keySet().toArray().length];

        //Put the image URLs associated with each hero in a array
        for (int i=0; i< text.length;i++){
            HeroData hero = hData.get(text[i]);
            pics[i] = (hero.getImageIconURL());
        }


        CustomList adapter = new
                CustomList(this, text, pics);
        list=(ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), "You Clicked " +text[+ position], Toast.LENGTH_SHORT).show();
                //start new activity with method that takes in name and HeroData object and displays information
                Intent i = new Intent(HeroView.this,HeroDisplayPrototype.class);
                HeroData chosenHero = hData.get(text[position]);
                i.putExtra("HeroData",chosenHero);
                startActivity(i);

            }
        });

        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            //hide keyboard upon return
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }


        try {
            fileManager.saveHeroesToStorage(hData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
