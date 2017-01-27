package com.optimalotaku.paraguide;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Jerek on 1/8/2017.
 */

public class CardOfTheDayView extends AppCompatActivity{

    CardData cotd;
    ParagonAPIAttrReplace attrTranslator;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.card_of_the_day_screen);


        cotd = (CardData) getIntent().getSerializableExtra("CardOfTheDay");
        attrTranslator = new ParagonAPIAttrReplace();
        
        ImageView cotdImage = (ImageView) findViewById(R.id.cotdImage);
        cotdImage.setVisibility(View.VISIBLE);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        cotdImage.startAnimation(myFadeInAnimation);
        TextView cotdText   = (TextView) findViewById(R.id.cotdText);
        String cotdStr;

        //Set Picture Image with Glide
        Glide.with(this).load(cotd.getImageUrl2()).into(cotdImage);

        //Resize image to be a percentage of width of device
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        cotdImage.getLayoutParams().width = (int) Math.round(width * 0.5);

        cotdImage.setVisibility(View.VISIBLE);

        cotdStr = "Name: "+cotd.getName()+"\n\n";

        cotdStr = cotdStr + "Card Effects:\n\n";
        List<CardEffect> effectList = cotd.getEffectList();
        for(CardEffect eff: effectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                Log.i("INFO","MainActivity - processCardInfoFinish - Stat: "+eff.getStat()+" Human Readable: "+ cotd.statToHumanReadable(eff.getStat()));
                cotdStr = cotdStr + "• " + cotd.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n";
            }
            if(eff.getDescription() != null){
                String apiText = attrTranslator.replaceStatWithText(eff.getDescription());
                cotdStr = cotdStr + "• "+apiText+"\n";
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cotdStr = cotdStr + "• Cooldown: " + eff.getCooldown() + "s\n";
            }
        }
        cotdStr = cotdStr + "\n";


        List<CardEffect> maxEffectList = cotd.getMaxEffectList();
        if(maxEffectList.size() > 0) {
            cotdStr = cotdStr + "Fully Upgraded Card Effects:\n";
            for (CardEffect eff : maxEffectList) {
                if (eff.getStat() != null && eff.getStatValue() != null) {
                    cotdStr = cotdStr + "• " + cotd.statToHumanReadable(eff.getStat()) + ": " + eff.getStatValue() + "\n";
                }
                if (eff.getDescription() != null) {
                    String apiText = attrTranslator.replaceStatWithText(eff.getDescription());
                    cotdStr = cotdStr + "• "+apiText+"\n";
                }
                if (eff.getCooldown() != null && Integer.parseInt(eff.getCooldown()) > 1) {
                    cotdStr = cotdStr + "• Cooldown: " + eff.getCooldown() + "s\n";
                }
            }
        }

        SpannableString ss = attrTranslator.replaceSymbolsWithImages(this,cotdStr);
        cotdText.setText(ss);

        



    }


}
