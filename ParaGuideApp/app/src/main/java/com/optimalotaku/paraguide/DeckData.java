package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by Jerek on 12/19/2016.
 */

public class DeckData implements Serializable {

    private String deckName;
    private String deckID;
    private String heroName;
    private String accountID;
    private String deckContents;

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getDeckID() {
        return deckID;
    }

    public void setDeckID(String deckID) {
        this.deckID = deckID;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getAccountID() {
        return accountID;
    }

    public String getDeckContents() {
        return deckContents;
    }

    public void setDeckContents(String deckContents) {
        this.deckContents = deckContents;
    }
}
