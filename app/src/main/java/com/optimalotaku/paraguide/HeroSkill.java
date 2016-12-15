package com.optimalotaku.paraguide;

import java.util.HashMap;

/**
 * Created by Jerek on 12/14/2016.
 */

public class HeroSkill {

    private String name;
    private String desc;
    private String type;
    private HashMap<Integer,Double[]> modifiers;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<Integer, Double[]> getModifiers() {
        return modifiers;
    }

    public void setModifiers(HashMap<Integer, Double[]> modifiers) {
        this.modifiers = modifiers;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
