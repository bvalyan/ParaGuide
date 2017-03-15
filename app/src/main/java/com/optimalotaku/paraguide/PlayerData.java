package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by bvaly on 1/8/2017.
 */

public class PlayerData implements Serializable {

    private String playerName;
    private String wins;
    private String matches;
    private String heroKills;
    private String deaths;
    private String assists;
    private String coreKills;
    private String coreAssists;
    private String towerKills;
    private String towerAssists;
    private String minionDamage;
    private String minionKills;
    private String gamesLeft;
    private String gamesReconnected;
    private String inhibKills;
    private String inhibAssists;
    private String wardKills;
    private String structureDamage;
    private String heroDamage;
    private String timePlayed;
    private String xp;
    private String surrenders;



    public String getHeroDamage() {
        return heroDamage;
    }

    public void setHeroDamage(String heroDamage) {
        this.heroDamage = heroDamage;
    }

    public String getCoreAssists() {
        return coreAssists;
    }

    public String getMinionDamage() {
        return minionDamage;
    }

    public void setMinionDamage(String minionDamage) {
        this.minionDamage = minionDamage;
    }

    public String getMinionKills() {
        return minionKills;
    }

    public void setMinionKills(String minionKills) {
        this.minionKills = minionKills;
    }

    public void setCoreAssists(String coreAssists) {
        this.coreAssists = coreAssists;
    }

    public String getTowerAssists() {
        return towerAssists;
    }

    public void setTowerAssists(String towerAssists) {
        this.towerAssists = towerAssists;
    }

    public String getInhibKills() {
        return inhibKills;
    }

    public void setInhibKills(String inhibKills) {
        this.inhibKills = inhibKills;
    }

    public String getInhibAssists() {
        return inhibAssists;
    }

    public void setInhibAssists(String inhibAssists) {
        this.inhibAssists = inhibAssists;
    }

    public String getWardKills() {
        return wardKills;
    }

    public void setWardKills(String wardKills) {
        this.wardKills = wardKills;
    }

    public String getStructureDamage() {
        return structureDamage;
    }

    public void setStructureDamage(String structureDamage) {
        this.structureDamage = structureDamage;
    }

    public String getTimeePlayed() {
        return timePlayed;
    }

    public void setTimeePlayed(String timeePlayed) {
        this.timePlayed = timeePlayed;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public String getGamesLeft() {
        return gamesLeft;
    }

    public void setGamesLeft(String gamesLeft) {
        this.gamesLeft = gamesLeft;
    }

    public String getGamesReconnected() {
        return gamesReconnected;
    }

    public void setGamesReconnected(String gamesReconnected) {
        this.gamesReconnected = gamesReconnected;
    }

    public String getCoreKills() {
        return coreKills;
    }

    public void setCoreKills(String coreKills) {
        this.coreKills = coreKills;
    }

    public String getTowerKills() {
        return towerKills;
    }

    public void setTowerKills(String towerKills) {
        this.towerKills = towerKills;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getHeroKills() {
        return heroKills;
    }

    public void setHeroKills(String heroKills) {
        this.heroKills = heroKills;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getSurrenders() {
        return surrenders;
    }

    public void setSurrenders(String surrenders) {
        this.surrenders = surrenders;
    }
}
