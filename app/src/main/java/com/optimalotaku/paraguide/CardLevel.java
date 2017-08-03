package com.optimalotaku.paraguide;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jerek on 8/3/2017.
 */

public class CardLevel implements Serializable {

    private Integer levelNum;
    private List<CardAbility> abilites;
    private String imageURL;

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public List<CardAbility> getAbilites() {
        return abilites;
    }

    public void setAbilites(List<CardAbility> abilites) {
        this.abilites = abilites;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = "http://developer-paragon-cdn.epicgames.com/Images/"+imageURL;
    }
}
