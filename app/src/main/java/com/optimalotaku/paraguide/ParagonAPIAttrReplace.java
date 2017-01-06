package com.optimalotaku.paraguide;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Jerek on 1/4/2017.
 */

public class ParagonAPIAttrReplace {

    public String[] attrSymbList = {"{attr:mp}","{attr:hp}","{attr:lfstl}","{attr:mpreg}","{attr:hpreg}","{attr:physdmg}","{attr:dmgbns}","{attr:enar}"
                                   ,"{attr:spd}","{attr:endmg}","{attr:shld}","{attr:physar}","{attr:atkspd}","{attr:cdr}","{attr:dmg}","{attr:crtch}"};

    public String[] attrTextList = {"MP","HP","LifeSteal","MP Regen","HP Regen","Power","Damage Bonus","Ability Armor"
                                   ,"Max Movement Speed","Power","Shields","Basic Armor","Attack Speed","Cooldown Reduction","Power","Critical Chance"};


    public ParagonAPIAttrReplace(){

    };


    public String replaceSymbolsWithText(String APIText){

        String replacementTxt = StringUtils.replaceEach(APIText, attrSymbList, attrTextList);

        return replacementTxt;
    }
}
