package com.optimalotaku.paraguide;

/**
 * Created by Jerek on 12/19/2016.
 */

public class CardEffect {

    //Active Equipment Only
    private String description;
    private String cooldown;

    private String stat;
    private String statValue;
    private Boolean unique;

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

    public String getStat() {

        return stat;
    }

    public void setStat(String stat) {

        this.stat = stat;
    }

    public String getStatValue() {

        return statValue;
    }

    public void setStatValue(String statValue) {

        this.statValue = statValue;
    }

    public Boolean getUnique() {

        return unique;
    }

    public void setUnique(Boolean unique) {

        this.unique = unique;
    }
}
