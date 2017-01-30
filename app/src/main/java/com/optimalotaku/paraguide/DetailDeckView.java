package com.optimalotaku.paraguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bvaly on 1/29/2017.
 */
public class DetailDeckView extends AppCompatActivity {
    private DeckData deckInfo;
    private ListView cardlistView;
    private GridView gridview;
    FileManager cardmanager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent decks = this.getIntent();
        cardmanager = new FileManager(this);
        deckInfo = (DeckData) decks.getSerializableExtra("deckObject");

        try {
            Map<String,List<CardData>> cDataMap = new HashMap<>();
            JSONArray cardCollection = new JSONArray(deckInfo.getDeckContents().toString());
            setContentView(R.layout.cardlist);
            gridview = (GridView) findViewById(R.id.gridview2);
            String [] pics = new String[cardCollection.length()];
            final String [] cardText = new String[cardCollection.length()];
            cDataMap = cardmanager.readCardsFromStorage();

            for (int i = 0; i < pics.length; i++){
                pics[i] = cardCollection.getJSONObject(i).getJSONObject("images").getString("medium_stats");
                cardText[i] = cardCollection.getJSONObject(i).getString("name");
            }

            MyDeckAdapter deckAdapter = new MyDeckAdapter(this, pics, cardText);
            gridview.setAdapter(deckAdapter);
            final Intent intent;
            intent = new Intent(this,CardDisplay.class);
            final Map<String, List<CardData>> finalCDataMap = cDataMap;

            final Button button = (Button) findViewById(R.id.deletebutton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deckDeletion(deckInfo.getDeckID());
                }
            });

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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected void deckDeletion(String deckID){
        confirmDialog();

    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder
                .setMessage("This will delete \"" +deckInfo.getDeckName() + "\"... Are you sure?")
                .setPositiveButton("Yeah!",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DeckDelete deleteInstance = new DeckDelete(getApplicationContext());
                        deleteInstance.execute(deckInfo.getDeckID());
                    }
                })
                .setNegativeButton("Not really.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
