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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CardInfoResponse, ImageLoaderResponse {

    CardData cotd;
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.grid_home);
        gridview = (GridView) findViewById(R.id.gridview);
        getCardData();

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
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(MainActivity.this, CardOfTheDayView.class);
                        intent.putExtra("CardOfTheDay",cotd);
                        startActivity(intent);
                        break;
                    case 5 :
                        Toast.makeText(MainActivity.this, "Coming Soon!",
                        Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    public void getCardData(){
        ParagonAPICardInfo cardInfo = new ParagonAPICardInfo();
        cardInfo.delegate = this;
        cardInfo.execute();
    }

    //@Override
    public void processCardInfoFinish(List<CardData> cDataList) {

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */
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

        ImageLoader imgLoader = new ImageLoader(this.cotd);
        imgLoader.delegate = this;
        imgLoader.execute();



    }

    @Override
    public void processImageLoaderFinish(Bitmap imgBitmap) {
        //Set the grid view Adapter
        gridview.setAdapter(new MyAdapter(this,imgBitmap));
    }
}

