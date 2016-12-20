package com.optimalotaku.paraguide;

import java.util.List;

/**
 * Created by Jerek on 12/19/2016.
 */

public class CardData {

    private String name;
    private String id;
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
}
