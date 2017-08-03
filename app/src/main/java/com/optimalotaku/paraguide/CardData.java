package com.optimalotaku.paraguide;

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
    private String rarity;
    private String affinity;
    private String trait;
    private Integer intellectGemCost;
    private Integer vitalityGemCost;
    private Integer dexterityGemCost;
    private Integer goldCost;
    private List<CardLevel> cardLevels;



    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    private String imageUrl2;

    private Double version;

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

        this.imageUrl = "http://developer-paragon-cdn.epicgames.com/Images/"+imageUrl;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getAffinity() {
        return affinity;
    }

    public void setAffinity(String affinity) {
        this.affinity = affinity;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public Integer getIntellectGemCost() {
        return intellectGemCost;
    }

    public void setIntellectGemCost(Integer intellectGemCost) {
        this.intellectGemCost = intellectGemCost;
    }

    public Integer getVitalityGemCost() {
        return vitalityGemCost;
    }

    public void setVitalityGemCost(Integer vitalityGemCost) {
        this.vitalityGemCost = vitalityGemCost;
    }

    public Integer getDexterityGemCost() {
        return dexterityGemCost;
    }

    public void setDexterityGemCost(Integer dexterityGemCost) {
        this.dexterityGemCost = dexterityGemCost;
    }

    public Integer getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(Integer goldCost) {
        this.goldCost = goldCost;
    }

    public List<CardLevel> getCardLevels() {
        return cardLevels;
    }

    public void setCardLevels(List<CardLevel> cardLevels) {
        this.cardLevels = cardLevels;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String statToHumanReadable(String stat){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(stat);
        String humanReadableStat = "";
        for(String w : words){
            humanReadableStat = humanReadableStat+" "+w;
        }

        return humanReadableStat;

    }

    public String getFileName(){
        String fileName = name.replace(" ","_");

        return fileName;
    }
}
