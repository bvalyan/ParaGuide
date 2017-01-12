package com.optimalotaku.paraguide;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jerek on 12/19/2016.
 */

public class CardData implements Serializable {

    private String name;
    private String id;
    private String imageUrl;


    public enum SlotType{
        UNKNOWN,PRIME,ACTIVE,PASSIVE,UPGRADE
    }
    SlotType slot;

    private List<CardEffect> effectList;
    private List<CardEffect> maxEffectList;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SlotType getSlot() {

        return slot;
    }

    public void setSlot(SlotType slot) {

        this.slot = slot;
    }

    public List<CardEffect> getEffectList() {

        return effectList;
    }

    public void setEffectList(List<CardEffect> effectList) {

        this.effectList = effectList;
    }

    public List<CardEffect> getMaxEffectList() {

        return maxEffectList;
    }

    public void setMaxEffectList(List<CardEffect> maxEffectList) {
        this.maxEffectList = maxEffectList;
    }

    public String statToHumanReadable(String stat){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(stat);
        String humanReadableStat = "";
        for(String w : words){
            humanReadableStat = humanReadableStat+" "+w;
        }

        return humanReadableStat;

    }
}
