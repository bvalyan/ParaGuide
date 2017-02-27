package com.optimalotaku.paraguide;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jerek on 12/14/2016.
 */

public class HeroSkill implements Serializable {

    private String name;
    private String desc;
    private String type;
    private String imageURL;
    private Map<String,List<String>> modifiers;

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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Map<String, List<String>> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Map<String, List<String>> modifiers) {
        this.modifiers = modifiers;
    }
}
