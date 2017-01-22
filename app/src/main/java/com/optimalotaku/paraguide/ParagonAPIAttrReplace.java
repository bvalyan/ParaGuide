package com.optimalotaku.paraguide;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerek on 1/4/2017.
 */

public class ParagonAPIAttrReplace {

    private String[] attrSymbList = {"{attr:mp}","{attr:hp}","{attr:lfstl}","{attr:mpreg}","{attr:hpreg}","{attr:physdmg}","{attr:dmgbns}","{attr:enar}"
                                   ,"{attr:spd}","{attr:endmg}","{attr:shld}","{attr:physar}","{attr:atkspd}","{attr:cdr}","{attr:dmg}","{attr:crtch}"};

    private String[] attrTextList = {"MP","HP","LifeSteal","MP Regen","HP Regen","Power","Damage Bonus","Ability Armor"
                                   ,"Max Movement Speed","Power","Shield","Basic Armor","Attack Speed","Cooldown Reduction","Power","Critical Chance"};

    private String[] statSymbList = {"{status:burn}","{status:slow}","{status:psn}","{status:bleed}"};
    private String[] statTextList = {"Burn","Slow","Poison","Bleed"};

    private List<String> replaceList = Arrays.asList("{attr:mp}","{attr:hp}","{attr:lfstl}","{attr:mpreg}","{attr:hpreg}","{attr:physdmg}"
                                                    ,"{attr:dmgbns}","{attr:enar}","{attr:spd}","{attr:endmg}","{attr:shld}","{attr:physar}"
                                                    ,"{attr:atkspd}","{attr:cdr}","{attr:dmg}","{attr:crtch}");


    public ParagonAPIAttrReplace(){

    };


    public String replaceSymbolsWithText(String APIText){

        /* Replace Status Symbols*/
        String statReplacementTxt = StringUtils.replaceEach(APIText, statSymbList, statTextList);

        return statReplacementTxt; //Always return the last string in the series
    }

    public SpannableString replaceSymbolsWithImages(AppCompatActivity activity, String APIText){
        SpannableString ss = new SpannableString(APIText);
        List<Integer>strLocs = new ArrayList<>();
        Log.i("INFO","replaceSymbolWithImages() - Input Text:\n"+APIText);


        for(String replaceStr: replaceList){


            int index = APIText.indexOf(replaceStr);
            while(index >= 0) {
                Log.i("INFO","replaceSymbolWithImages() - found "+replaceStr+" here: "+Integer.toString(index));
                strLocs.add(index);
                index = APIText.indexOf(replaceStr, index+1);
            }

            for(Integer strLoc: strLocs) {
                //Get icon from resources
                Drawable icon = chooseIcon(activity,replaceStr);
                //Set Icon bounds and span
                Log.i("INFO","replaceSymbolWithImages() - intrinsic width-height: "+Integer.toString(icon.getIntrinsicWidth())+" - "+Integer.toString(icon.getIntrinsicHeight()));
                icon.setBounds(0, 0, icon.getIntrinsicWidth(),icon.getIntrinsicHeight() );
                ImageSpan span = new ImageSpan(icon);
                Log.i("INFO","replaceSymbolWithImages() - span range: "+Integer.toString(strLoc)+" - "+Integer.toString(strLoc + replaceStr.length()));
                ss.setSpan(span,strLoc,strLoc + replaceStr.length(), 0);
            }

            strLocs.clear();

        }

        return ss;


    }

    public Drawable chooseIcon(AppCompatActivity activity, String iconString){

        Drawable icon;

        Log.i("INFO","chooseIcon() - string to replace with image: "+iconString);

        switch (iconString){
            case "{attr:mp}":
                icon = activity.getResources().getDrawable(R.drawable.icon_max_mana_128x_to_16x16,null);
                return icon;
            case "{attr:hp}":
                icon = activity.getResources().getDrawable(R.drawable.icon_max_health_128x_to_16x16,null);
                return icon;
            case "{attr:lfstl}":
                icon = activity.getResources().getDrawable(R.drawable.icon_lifesteal_128x_to_16x16,null);
                return icon;
            case "{attr:mpreg}":
                icon = activity.getResources().getDrawable(R.drawable.icon_mana_regen_128x_to_16x16,null);
                return icon;
            case "{attr:hpreg}":
                icon = activity.getResources().getDrawable(R.drawable.icon_health_regen_128x_to_16x16,null);
                return icon;
            case "{attr:physdmg}":
                icon = activity.getResources().getDrawable(R.drawable.icon_power,null);
                return icon;
            case "{attr:dmgbns}":
                icon = activity.getResources().getDrawable(R.drawable.icon_cleave_128x_to_16x16,null);
                return icon;
            case "{attr:enar}":
                icon = activity.getResources().getDrawable(R.drawable.icon_energy_armour_128x_to_16x16,null);
                return icon;
            case "{attr:spd}":
                icon = activity.getResources().getDrawable(R.drawable.icon_movement_speed_128x_to_16x16,null);
                return icon;
            case "{attr:endmg}":
                icon = activity.getResources().getDrawable(R.drawable.icon_energy_rating_128x_to_16x16,null);
                return icon;
            case "{attr:shld}":
                icon = activity.getResources().getDrawable(R.drawable.icon_shield,null);
                return icon;
            case "{attr:physar}":
                icon = activity.getResources().getDrawable(R.drawable.icon_physical_armour_128x_to_16x16,null);
                return icon;
            case "{attr:atkspd}":
                icon = activity.getResources().getDrawable(R.drawable.icon_attack_speed_128x_to_16x16,null);
                return icon;
            case "{attr:cdr}":
                icon = activity.getResources().getDrawable(R.drawable.icon_cooldown_reduction_128x_to_16x16,null);
                return icon;
            case "{attr:dmg}":
                icon = activity.getResources().getDrawable(R.drawable.icon_power,null);
                return icon;
            case "{attr:crtch}":
                icon = activity.getResources().getDrawable(R.drawable.icon_critical_strike_chance_128x_to_16x16,null);
                return icon;
            default:
                icon = null;
                return icon;

        }


    }
}
