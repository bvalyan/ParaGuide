package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by Brandon on 4/17/18.
 */

class ItemObject implements Serializable{
    private String description;
    private String name;
    private int iconID;
    private int itemID;
    private int price;
    private String shortDescription;
    private int championID;
    private String imageURL;
    private String itemType;
    private int talentRewardLevel;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getChampionID() {
        return championID;
    }

    public void setChampionID(int championID) {
        this.championID = championID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getTalentRewardLevel() {
        return talentRewardLevel;
    }

    public void setTalentRewardLevel(int talentRewardLevel) {
        this.talentRewardLevel = talentRewardLevel;
    }
}
