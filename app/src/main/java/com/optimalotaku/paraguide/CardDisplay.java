package com.optimalotaku.paraguide;

import android.app.DialogFragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.DisplayMetrics;
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

public class CardDisplay extends DialogFragment{
    static CardData cotd;
    ParagonAPIAttrReplace attrTranslator;

    public static CardDisplay newInstance(CardData data) {

        Bundle args = new Bundle();
        cotd = data;

        CardDisplay fragment = new CardDisplay();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.card_of_the_day_screen, container, false);
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
        String description = cotd.getCardLevels().get(level).getAbilites().get(0).getDescription();
        int vitality = cotd.getVitalityGemCost();
        int dexterity = cotd.getDexterityGemCost();
        try {
            cotdCooldown = cotd.getCardLevels().get(0).getAbilites().get(0).getCooldown();
            cotdCooldown = android.text.Html.fromHtml(cotdCooldown).toString();
        }
        catch (Exception e){

        }
        int intellect = cotd.getIntellectGemCost();
        int gold = cotd.getGoldCost();
        String rarityText = cotd.getRarity();

        if (cotdCooldown.equals("")){
            cotdCooldown = "N/A";
        }

        SpannableString ss = attrTranslator.replaceSymbolsWithImages((AppCompatActivity) getActivity(),cotdFullEffect);
        SpannableString ss2 = attrTranslator.replaceSymbolsWithImages((AppCompatActivity) getActivity(),description);

        cotdTitle.setText(cotdTitle1);
        cotdEff.setText(ss2);
        cotdCD.setText(cotdCooldown);
        vitCost.setText(String.valueOf(vitality));
        dexCost.setText(String.valueOf(dexterity));
        intCost.setText(String.valueOf(intellect));
        rarity.setText(rarityText);
        goldCost.setText(String.valueOf(gold));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((19 * width)/20, params.height);
    }

}
