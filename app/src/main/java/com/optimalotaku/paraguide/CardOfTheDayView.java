package com.optimalotaku.paraguide;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by bvaly on 1/29/2017.
 */

public class CardOfTheDayView extends Fragment{
    static CardData cotd;
    ParagonAPIAttrReplace attrTranslator;

    public static CardOfTheDayView newInstance(CardData card) {

        Bundle args = new Bundle();
        cotd = card;
        CardOfTheDayView fragment = new CardOfTheDayView();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.card_of_the_day_screen, container,false);


        attrTranslator = new ParagonAPIAttrReplace();

        ImageView cotdImage = (ImageView) view.findViewById(R.id.cotdImage);
        cotdImage.setVisibility(View.VISIBLE);

        //Set Picture Image with Glide
        Glide.with(this).load(cotd.getCardLevels().get(0).getImageURL()).into(cotdImage);

        //Resize image to be a percentage of width of device
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        TextView cotdTitle   = (TextView) view.findViewById(R.id.cotdTitle);
        TextView cotdEff = (TextView) view.findViewById(R.id.cotdeff);
        TextView cotdCD = (TextView) view.findViewById(R.id.cotd_cd);
        TextView cdTitle = (TextView) view.findViewById(R.id.cdtitle);
        TextView vitCost = (TextView) view.findViewById(R.id.vitality_gem_cost);
        TextView dexCost = (TextView) view.findViewById(R.id.dexterity_gem_cost);
        TextView intCost = (TextView) view.findViewById(R.id.intellect_gem_cost);
        TextView goldCost = (TextView) view.findViewById(R.id.gold_cost);
        TextView rarity = (TextView) view.findViewById(R.id.rarity);
        String cotdTitle1;
        String cotdEff2 = new String();
        String cotdCooldown = new String();
        String cotdFullEffect = new String();


        cotdImage.getLayoutParams().width = (int) Math.round(width * 0.5);

        cotdImage.setVisibility(View.VISIBLE);

        cotdTitle1 = cotd.getName();


        int level= 0;
        String effectName = cotd.getCardLevels().get(level).getAbilites().get(0).getName();
        String description = cotd.getCardLevels().get(level).getAbilites().get(0).getDescription();
        try {
            cotdCooldown = cotd.getCardLevels().get(0).getAbilites().get(0).getCooldown();
            cotdCooldown = android.text.Html.fromHtml(cotdCooldown).toString();
        }
        catch (Exception e){

        }
        int vitality = cotd.getVitalityGemCost();
        int dexterity = cotd.getDexterityGemCost();
        int intellect = cotd.getIntellectGemCost();
        int gold = cotd.getGoldCost();
        String rarityText = cotd.getRarity();



        SpannableString ss = attrTranslator.replaceSymbolsWithImages((AppCompatActivity) getActivity(),cotdFullEffect);
        SpannableString ss2 = attrTranslator.replaceSymbolsWithImages((AppCompatActivity) getActivity(),description);

        cotdTitle.setText(cotdTitle1);
        cotdEff.setText(ss2);
        cotdCD.setText(cotdCooldown);
        vitCost.setText(Integer.toString(vitality));
        dexCost.setText(Integer.toString(dexterity));
        intCost.setText(Integer.toString(intellect));
        rarity.setText(rarityText);
        goldCost.setText(Integer.toString(gold));

        return view;
    }

}
