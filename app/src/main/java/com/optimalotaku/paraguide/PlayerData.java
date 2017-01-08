package com.optimalotaku.paraguide;

/**
 * Created by bvaly on 1/8/2017.
 */

public class PlayerData {

    private String playerName;
    private String wins;
    private String matches;
    private String heroKills;
    private String deaths;
    private String assists;

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
}
