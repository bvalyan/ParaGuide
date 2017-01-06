package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CardInfoResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.homescreen);
        getCardData();

    }

    public void heroClick(View view){
        View view2 = this.getCurrentFocus();
        if(view2 != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        Intent intent = new Intent(MainActivity.this, HeroView.class);
        startActivity(intent);
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

    public void getCardData(){
        ParagonAPICardInfo cardInfo = new ParagonAPICardInfo();
        cardInfo.delegate = this;
        cardInfo.execute();
    }

    @Override
    public void processCardInfoFinish(List<CardData> cDataList) {

        ParagonAPIAttrReplace attrTranslator = new ParagonAPIAttrReplace();

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
        CardData cardOfTheDay = cDataList.get(chosenCard);

        ImageView cotdImage = (ImageView) findViewById(R.id.cardOfTheDay);
        TextView cotdText   = (TextView) findViewById(R.id.cardOfTheDayText);

        //Set Picture Image with Glide
        Glide.with(this).load("https:" + cardOfTheDay.getImageUrl()).into(cotdImage);
        cotdImage.setVisibility(View.VISIBLE);

        cotdText.setText("Name: "+cardOfTheDay.getName()+"\n\n");

        cotdText.append("Card Effects:\n");
        List<CardEffect> effectList = cardOfTheDay.getEffectList();
        for(CardEffect eff: effectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                Log.i("INFO","MainActivity - processCardInfoFinish - Stat: "+eff.getStat()+" Human Readable: "+ cardOfTheDay.statToHumanReadable(eff.getStat()));
                cotdText.append("\t• " + cardOfTheDay.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n");
            }
            if(eff.getDescription() != null){
               cotdText.append("\t• " + attrTranslator.replaceSymbolsWithText(eff.getDescription()) + "\n");
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cotdText.append("\t• Cooldown: " + eff.getCooldown() + "s\n" );
            }
        }
        cotdText.append("\n");

        cotdText.append("Max Card Effects:\n");
        List<CardEffect> maxEffectList = cardOfTheDay.getMaxEffectList();
        for(CardEffect eff: maxEffectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                cotdText.append("\t• " + cardOfTheDay.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n");
            }
            if(eff.getDescription() != null){
                cotdText.append("\t• " + attrTranslator.replaceSymbolsWithText(eff.getDescription()) + "\n");
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cotdText.append("\t• Cooldown: " + eff.getCooldown() + "s\n" );
            }
        }

    }
}

