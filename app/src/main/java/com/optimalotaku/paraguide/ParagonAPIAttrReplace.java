package com.optimalotaku.paraguide;

import android.text.SpannableString;

import org.apache.commons.lang3.StringUtils;

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


    public ParagonAPIAttrReplace(){

    };


    public String replaceSymbolsWithText(String APIText){

        /* Replace Attribute Symbols*/
        String attrReplacementTxt = StringUtils.replaceEach(APIText, attrSymbList, attrTextList);
        /* Replace Status Symbols*/
        String statReplacementTxt = StringUtils.replaceEach(attrReplacementTxt, statSymbList, statTextList);

        return statReplacementTxt; //Always return the last string in the series
    }

    public SpannableString replaceSymbolsWithImages(String APIText){
        
    }
}
