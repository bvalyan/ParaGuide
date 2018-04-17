package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by bvaly on 4/16/2018.
 */

class ChampionData implements Serializable{
    private AbilityObject ability1, ability2, ability3, ability4, ability5;
    private int ability1ID, ability2ID, ability3ID, ability4ID, ability5ID;
    private String ability1Desc, ability2Desc, ability3Desc, ability4Desc, ability5Desc;
    private String ability1ImageURL, ability2ImageURL, ability3ImageURL, ability4ImageURL, ability5ImageURL;
    private String ability1Name, ability2Name, ability3Name, ability4Name, ability5Name;
    private String champCardURL;
    private String champIconURL;
    private int health;
    private String lore;
    private String name;
    private String onFreeRotation;
    private String roles;
    private String speed;
    private String title;
    private int id;
    private String latestChamp;

    public AbilityObject getAbility1() {
        return ability1;
    }

    public void setAbility1(AbilityObject ability1) {
        this.ability1 = ability1;
    }

    public AbilityObject getAbility2() {
        return ability2;
    }

    public void setAbility2(AbilityObject ability2) {
        this.ability2 = ability2;
    }

    public AbilityObject getAbility3() {
        return ability3;
    }

    public void setAbility3(AbilityObject ability3) {
        this.ability3 = ability3;
    }

    public AbilityObject getAbility4() {
        return ability4;
    }

    public void setAbility4(AbilityObject ability4) {
        this.ability4 = ability4;
    }

    public AbilityObject getAbility5() {
        return ability5;
    }

    public void setAbility5(AbilityObject ability5) {
        this.ability5 = ability5;
    }

    public int getAbility1ID() {
        return ability1ID;
    }

    public void setAbility1ID(int ability1ID) {
        this.ability1ID = ability1ID;
    }

    public int getAbility2ID() {
        return ability2ID;
    }

    public void setAbility2ID(int ability2ID) {
        this.ability2ID = ability2ID;
    }

    public int getAbility3ID() {
        return ability3ID;
    }

    public void setAbility3ID(int ability3ID) {
        this.ability3ID = ability3ID;
    }

    public int getAbility4ID() {
        return ability4ID;
    }

    public void setAbility4ID(int ability4ID) {
        this.ability4ID = ability4ID;
    }

    public int getAbility5ID() {
        return ability5ID;
    }

    public void setAbility5ID(int ability5ID) {
        this.ability5ID = ability5ID;
    }

    public String getAbility1Desc() {
        return ability1Desc;
    }

    public void setAbility1Desc(String ability1Desc) {
        this.ability1Desc = ability1Desc;
    }

    public String getAbility2Desc() {
        return ability2Desc;
    }

    public void setAbility2Desc(String ability2Desc) {
        this.ability2Desc = ability2Desc;
    }

    public String getAbility3Desc() {
        return ability3Desc;
    }

    public void setAbility3Desc(String ability3Desc) {
        this.ability3Desc = ability3Desc;
    }

    public String getAbility4Desc() {
        return ability4Desc;
    }

    public void setAbility4Desc(String ability4Desc) {
        this.ability4Desc = ability4Desc;
    }

    public String getAbility5Desc() {
        return ability5Desc;
    }

    public void setAbility5Desc(String ability5Desc) {
        this.ability5Desc = ability5Desc;
    }

    public String getAbility1ImageURL() {
        return ability1ImageURL;
    }

    public void setAbility1ImageURL(String ability1ImageURL) {
        this.ability1ImageURL = ability1ImageURL;
    }

    public String getAbility2ImageURL() {
        return ability2ImageURL;
    }

    public void setAbility2ImageURL(String ability2ImageURL) {
        this.ability2ImageURL = ability2ImageURL;
    }

    public String getAbility3ImageURL() {
        return ability3ImageURL;
    }

    public void setAbility3ImageURL(String ability3ImageURL) {
        this.ability3ImageURL = ability3ImageURL;
    }

    public String getAbility4ImageURL() {
        return ability4ImageURL;
    }

    public void setAbility4ImageURL(String ability4ImageURL) {
        this.ability4ImageURL = ability4ImageURL;
    }

    public String getAbility5ImageURL() {
        return ability5ImageURL;
    }

    public void setAbility5ImageURL(String ability5ImageURL) {
        this.ability5ImageURL = ability5ImageURL;
    }

    public String getAbility1Name() {
        return ability1Name;
    }

    public void setAbility1Name(String ability1Name) {
        this.ability1Name = ability1Name;
    }

    public String getAbility2Name() {
        return ability2Name;
    }

    public void setAbility2Name(String ability2Name) {
        this.ability2Name = ability2Name;
    }

    public String getAbility3Name() {
        return ability3Name;
    }

    public void setAbility3Name(String ability3Name) {
        this.ability3Name = ability3Name;
    }

    public String getAbility4Name() {
        return ability4Name;
    }

    public void setAbility4Name(String ability4Name) {
        this.ability4Name = ability4Name;
    }

    public String getAbility5Name() {
        return ability5Name;
    }

    public void setAbility5Name(String ability5Name) {
        this.ability5Name = ability5Name;
    }

    public String getChampCardURL() {
        return champCardURL;
    }

    public void setChampCardURL(String champCardURL) {
        this.champCardURL = champCardURL;
    }

    public String getChampIconURL() {
        return champIconURL;
    }

    public void setChampIconURL(String champIconURL) {
        this.champIconURL = champIconURL;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnFreeRotation() {
        return onFreeRotation;
    }

    public void setOnFreeRotation(String onFreeRotation) {
        this.onFreeRotation = onFreeRotation;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatestChamp() {
        return latestChamp;
    }

    public void setLatestChamp(String latestChamp) {
        this.latestChamp = latestChamp;
    }
}
