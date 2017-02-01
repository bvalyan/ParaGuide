package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 2/1/17.
 */

public class MyCardView extends AppCompatActivity{
    private String authCode;
    FileManager deckManager;
    ListView list;
    private GridView gridview;
    ProgressDialog progressDialog;
    String [] pics2;
    String [] cardText;
    FileManager cardmanager;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String myCardString = null;
        final Intent intent;
        intent = new Intent(this,CardDisplay.class);
        cardmanager = new FileManager(this);
        try {
            Map<String,List<CardData>> cDataMap = new HashMap<>();
            setContentView(R.layout.cardlist2);
            gridview = (GridView) findViewById(R.id.gridview2);
            MyCardAPI myCards = new MyCardAPI(MyCardView.this);
            myCards.execute();
            myCardString = myCards.get();
            JSONArray myCardList = new JSONArray(myCardString);
            cardText = new String[myCardList.length()];
            pics2 = new String[myCardList.length()];
            cDataMap = cardmanager.readCardsFromStorage();
            final Map<String, List<CardData>> finalCDataMap = cDataMap;

            for (int i = 0; i < pics2.length; i++){
                pics2[i] = myCardList.getJSONObject(i).getJSONObject("images").getString("medium_stats");
                cardText[i] = myCardList.getJSONObject(i).getString("name");
            }

            MyDeckAdapter deckAdapter = new MyDeckAdapter(MyCardView.this, pics2, cardText);
            gridview.setAdapter(deckAdapter);


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main menu
                    gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Toast.makeText(getApplicationContext(), "You Clicked " +cardText[+ position], Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < finalCDataMap.get("All").size(); i++){
                        if(finalCDataMap.get("All").get(i).getName().equals(cardText[position])){
                            intent.putExtra("selectedCard", finalCDataMap.get("All").get(i));
                        }
                    }
                    startActivity(intent);
                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
