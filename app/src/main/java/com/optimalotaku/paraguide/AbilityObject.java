package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by bvaly on 4/16/2018.
 */

class AbilityObject implements Serializable {
    int id;
    String name;
    String description;
    String imageURL;

    public AbilityObject( int id, String name, String description, String imageURL){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
