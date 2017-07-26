package com.optimalotaku.paraguide;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by bvaly on 1/29/2017.
 */

public class CardDisplay extends AppCompatActivity{
    CardData cotd;
    ParagonAPIAttrReplace attrTranslator;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.card_of_the_day_screen);


        cotd = (CardData) getIntent().getSerializableExtra("selectedCard");
        attrTranslator = new ParagonAPIAttrReplace();

        ImageView cotdImage = (ImageView) findViewById(R.id.cotdImage);
        cotdImage.setVisibility(View.VISIBLE);

        //Set Picture Image with Glide
        Glide.with(this).load(cotd.getImageUrl2()).into(cotdImage);

        //Resize image to be a percentage of width of device
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        TextView cotdTitle   = (TextView) findViewById(R.id.cotdTitle);
        TextView cotdEff = (TextView) findViewById(R.id.cotdeff);
        TextView cotdCD = (TextView) findViewById(R.id.cotd_cd);
        TextView cotdFullEff = (TextView) findViewById(R.id.cotd_full_eff);
        TextView effecttitle = (TextView) findViewById(R.id.efftitle);
        TextView cdTitle = (TextView) findViewById(R.id.cdtitle);
        String cotdTitle1;
        String cotdEff2 = new String();
        String cotdCooldown = new String();
        String cotdFullEffect = new String();


        cotdImage.getLayoutParams().width = (int) Math.round(width * 0.5);

        cotdImage.setVisibility(View.VISIBLE);

        cotdTitle1 = cotd.getName();


        List<CardEffect> effectList = cotd.getEffectList();
        for(CardEffect eff: effectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                Log.i("INFO","MainActivity - processCardInfoFinish - Stat: "+eff.getStat()+" Human Readable: "+ cotd.statToHumanReadable(eff.getStat()));
                cotdEff2 = cotdEff2 + "• " + cotd.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n";
            }
            if(eff.getDescription() != null){
                String apiText = attrTranslator.replaceStatWithText(eff.getDescription());
                cotdEff2 = cotdEff2 + "• "+apiText+"\n";
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cdTitle.setVisibility(View.VISIBLE);
                cotdCooldown =  eff.getCooldown();
            }
        }


        List<CardEffect> maxEffectList = cotd.getMaxEffectList();
        if(maxEffectList.size() > 0) {
            cotdFullEff.setVisibility(View.VISIBLE);
            effecttitle.setVisibility(View.VISIBLE);
            for (CardEffect eff : maxEffectList) {
                if (eff.getStat() != null && eff.getStatValue() != null) {
                    cotdFullEffect =   "• " + cotd.statToHumanReadable(eff.getStat()) + ": " + eff.getStatValue() + "\n";
                }
                if (eff.getDescription() != null) {
                    String apiText = attrTranslator.replaceStatWithText(eff.getDescription());
                    cotdFullEffect = cotdFullEffect + "• "+apiText+"\n";
                }
                if (eff.getCooldown() != null && Integer.parseInt(eff.getCooldown()) > 1) {
                    cotdFullEffect = cotdFullEffect + "• Cooldown: " + eff.getCooldown() + "s\n";
                }
            }
        }

        SpannableString ss = attrTranslator.replaceSymbolsWithImages(this,cotdFullEffect);
        SpannableString ss2 = attrTranslator.replaceSymbolsWithImages(this,cotdEff2);
        cotdTitle.setText(cotdTitle1);
        cotdEff.setText(ss2);
        cotdCD.setText(cotdCooldown);
        cotdFullEff.setText(ss);




    }

}
