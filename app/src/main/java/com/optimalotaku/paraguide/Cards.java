package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvalyan on 8/2/17.
 */

public class Cards extends AppCompatActivity {

    GridView gridview;
    FileManager cardmanager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final Intent intent;
        intent = new Intent(this,CardDisplay.class);
        setContentView(R.layout.cardlist2);
        cardmanager = new FileManager(this);
        Map<String,List<CardData>> cDataMap = new HashMap<>();
        try {
            cDataMap = cardmanager.readCardsFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Map<String, List<CardData>> finalCDataMap = cDataMap;
        gridview = (GridView) findViewById(R.id.gridview2);
        final SharedPreferences prefs = getSharedPreferences("authInfo", MODE_PRIVATE);
        final SharedPreferences.Editor e = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        String authCode = prefs.getString("signedIn", "null");
        APICardList allCards = new APICardList(authCode, this);
        JSONArray cardArray = null;
        try {
            String response = allCards.execute().get();
            cardArray = new JSONArray(response);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        final CardData[] cardList = new CardData[cardArray.length()];
        for(int j = 0; j<cardArray.length();j++){
            cardList[j] = new CardData();
        }

        try {
            for (int i = 0; i < cardArray.length(); i++) {
                cardList[i].setId(cardArray.getJSONObject(i).getString("id"));
                cardList[i].setName(cardArray.getJSONObject(i).getString("name"));
                cardList[i].setImageUrl(cardArray.getJSONObject(i).getJSONArray("levels").getJSONObject(0).getJSONObject("images").getString("large"));
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        CustomComparator cardCompare = new CustomComparator();
        Arrays.sort(cardList, cardCompare);
        MyDeckAdapter CardAdapter = new MyDeckAdapter(this, cardList);
        gridview.setAdapter(CardAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main drawer
                gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


                //Toast.makeText(getApplicationContext(), "You Clicked " +cardList[+ position].getName(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < finalCDataMap.get("All").size(); i++){
                    if(finalCDataMap.get("All").get(i).getName().equals(cardList[position].getName())){
                        CardData finalChoice = finalCDataMap.get("All").get(i);
                        finalChoice.setBareImageUrl(cardList[position].getImageUrl());
                        intent.putExtra("selectedCard", finalChoice);
                    }
                }
                startActivity(intent);
            }
        });

    }
    public class CustomComparator implements Comparator<CardData> {
        @Override
        public int compare(CardData o1, CardData o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}
