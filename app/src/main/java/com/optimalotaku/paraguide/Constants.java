package com.optimalotaku.paraguide;

/**
 * Created by Jerek on 12/14/2016.
 */

public class Constants {
    //Access Constants
    public static final String API_KEY          = "X-Epic-ApiKey";
    public static final String API_VALUE        = "b6d974bd42ec4a2f9b2322034bd0d0e0";
    public static final String CLIENT_ID        = "5cbc82af86414e03a549dbb811dfbbc5";
    public static final String CLIENT_SECRET    = "ea931c9fb41249e5a67b4038e121a2a8";
    public static final String AUTH_VAR         = "Authorization";
    public static final String REDIRECT_URI     = "https://optimalotaku.com/paraguide/";
    public static final String AUTHORIZE_PATH   = "https://developer-paragon.epicgames.com/v1/auth/login/";

    //Chat Constants

    public static final String PUBNUB_PUBLISH_KEY = "pub-c-0aa1963e-343a-4835-bbef-9ae72a9bbcb2";     // Replace with your publish key
    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-0c599104-1e2b-11e7-bd07-02ee2ddab7fe";   // Replace with your subscribe key

    public static final String CHANNEL_NAME = "Paragon Academy";     // replace with more meaningful channel name
    public static final String MULTI_CHANNEL_NAMES = "";    // ditto
    public static final String CHAT_PASS = "dekkerisbae";

    public static final String DATASTREAM_PREFS = "com.pubnub.example.android.datastream.pubnubdatastreams.DATASTREAM_PREFS";
    public static final String DATASTREAM_UUID = "com.pubnub.example.android.datastream.pubnubdatastreams.DATASTREAM_UUID";

    //Paragon Version
    public static final Double PARAGON_VERSION  = 40.31; // Actual version 40.3, updated for Wukong patch



    public static final double currentKillMax       = 8.3;
    public static final float currentMin            =  0;
    public static final double currentAssistMax     = 9.0;
    public static final double currentTowerKillMax  = 1.5;
    public static final double currentDeathMax      = 3.75;
    public static final double crrentinhibAssistMax =  .35;
    public static final double currentInhibKillsMax = .45;
    public static final double currentheroDamageMax = 12500;
    public static final double currentMinionDamageMax = 50000;
    public static final double currentMinionKillMax = 135;
    public static final double currentTowerAssistMax = .90;
    public static final double currentMaxWardKills = .90;
    public static final double currentMaxStructDamage = 3400;
    public static final double currentMaxTimePlayed = 5000000;
    public static final double currentMaxGamesPlayed = 2800;




}
