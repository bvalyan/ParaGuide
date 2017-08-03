package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by Jerek on 8/3/2017.
 */

public class CardAbility implements Serializable {

    private String name;
    private String description;
    private String cooldown;
    private String manacost;

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

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getManacost() {
        return manacost;
    }

    public void setManacost(String manacost) {
        this.manacost = manacost;
    }
}
