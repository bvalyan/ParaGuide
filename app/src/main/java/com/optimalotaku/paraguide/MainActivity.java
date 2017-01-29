package com.optimalotaku.paraguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements CardInfoResponse, ImageLoaderResponse, HeroInfoResponse {

    CardData cotd;
    GridView gridview;
    FileManager fileManager;
    String [] text;
    String [] pics;
    ListView list;
    Map<String,HeroData> heroMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.grid_home);
        gridview = (GridView) findViewById(R.id.gridview);
        fileManager = new FileManager(this);

        getCardData();
        try {
            getHeroData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main menu
                gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                switch(position){
                    case 0 :
                        intent = new Intent(MainActivity.this, PlayerView.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(MainActivity.this, DeckView.class);
                        startActivity(intent);
                        break;
                    case 2 :
                        Toast.makeText(MainActivity.this, "Coming Soon!",
                        Toast.LENGTH_SHORT).show();
                        break;
                    case 3 :
                        intent = new Intent(MainActivity.this, HeroView.class);
                        intent.putExtra("heroMap", heroMap);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(MainActivity.this, CardOfTheDayView.class);
                        intent.putExtra("CardOfTheDay",cotd);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(MainActivity.this, newsView.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    public void getCardData(){
        Map<String,List<CardData>> cDataMap = new HashMap<>();
        try {
            cDataMap = fileManager.readCardsFromStorage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileManager.isLatestCardData(cDataMap)){

            Log.i("INFO", "MainActivity - getCardData(): Card data does not exist or is outdated. Grabbing current data from API ");

            ParagonAPICardInfo cardInfo = new ParagonAPICardInfo();
            cardInfo.delegate = this;
            cardInfo.execute();
        }
        else{
            Log.i("INFO", "MainActivity - getCardData(): Card data does exist and is current. Grabbing current data from file - cards.data ");
            processCardInfoFromFile(cDataMap);
        }
    }

    public void getHeroData() throws IOException {
        Map<String,HeroData> hDataMap = new HashMap<>();
        try{
            hDataMap = fileManager.readHeroFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(fileManager.isLatestHeroData(hDataMap)){
            Log.i("INFO", "FileManager - readHeroesToStorage: Hero data obtained from device successfully");
            ParagonAPIHeroInfo heroInfo = new ParagonAPIHeroInfo();
            heroInfo.delegate = this;
            heroInfo.execute();
            try {
                fileManager.saveHeroesToStorage(hDataMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i("INFO", "MainActivity - getCardData(): Hero data does exist and is current. Grabbing current data from file - hero.data ");
            processHeroFromFile(hDataMap);
        }
    }



    public void processCardInfoFromFile(Map<String,List<CardData>> cDataMap) {

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */

        List<CardData> cDataList = cDataMap.get("Equip");

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar today = Calendar.getInstance();
        String todayStr = formatter.format(today.getTime());
        Log.i("INFO", "Today's Date: " + todayStr);
        String[] todayParts = todayStr.split("-");
        Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
        Log.i("INFO", "Today's Date Sum: " + dateSum.toString());
        Integer chosenCard = dateSum % cDataList.size();
        Log.i("INFO", "Today's Chosen Card Index: " + chosenCard.toString());

        //Grab the chosen card
        this.cotd = cDataList.get(chosenCard);

        ImageLoader imgLoader = new ImageLoader(this.cotd);
        imgLoader.delegate = this;
        imgLoader.execute();
    }

    public void processHeroFromFile(final Map<String,HeroData> hData) throws IOException {
        //Get List of hero names from Map
        text = hData.keySet().toArray(new String[hData.size()]);
        pics = new String[hData.keySet().toArray().length];

        //Put the image URLs associated with each hero in a array
        for (int i=0; i< text.length;i++){
            HeroData hero = hData.get(text[i]);
            pics[i] = (hero.getImageIconURL());
        }

        try {
            fileManager.saveHeroesToStorage(hData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    //@Override
    public void processCardInfoFinish(Map<String,List<CardData>> cDataMap) {

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */

        List<CardData> cDataList = cDataMap.get("Equip");

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar today = Calendar.getInstance();
        String todayStr = formatter.format(today.getTime());
        Log.i("INFO","Today's Date: "+ todayStr);
        String[] todayParts = todayStr.split("-");
        Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
        Log.i("INFO","Today's Date Sum: "+dateSum.toString());
        Integer chosenCard = dateSum % cDataList.size();
        Log.i("INFO","Today's Chosen Card Index: "+chosenCard.toString());

        //Grab the chosen card
        this.cotd = cDataList.get(chosenCard);

        try {
            fileManager.saveCardsToStorage(cDataMap.get("All"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageLoader imgLoader = new ImageLoader(this.cotd);
        imgLoader.delegate = this;
        imgLoader.execute();



    }

    @Override
    public void processImageLoaderFinish(Bitmap imgBitmap) {
        //Set the grid view Adapter
        gridview.setAdapter(new MyAdapter(this,imgBitmap));
    }

    @Override
    public void processHeroInfoFinish(Map<String, HeroData> hdata) {
        //Get List of hero names from Map
        text = hdata.keySet().toArray(new String[hdata.size()]);
        pics = new String[hdata.keySet().toArray().length];

        //Put the image URLs associated with each hero in a array
        for (int i=0; i< text.length;i++){
            HeroData hero = hdata.get(text[i]);
            pics[i] = (hero.getImageIconURL());
        }
    }
}

