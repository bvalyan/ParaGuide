package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brandon on 1/30/17.
 */
public class DeckDelete extends AsyncTask<String,Void,Void>{
    Context mcontext;


    public DeckDelete(Context context) {
        this.mcontext = context;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String deckID = strings[0];
        String authToken;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        authToken = prefs.getString("TOKEN","NULL");
        String accountID = prefs.getString("ACCOUNT_ID", "NULL");
        try {
            URL url = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/deck/" + deckID);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection.addRequestProperty(Constants.AUTH_VAR, "Bearer " + authToken);
            urlConnection.setRequestMethod("DELETE");
            System.out.println(urlConnection.getResponseCode());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(){
        new AlertDialog.Builder(this.mcontext)
                .setTitle("Deck Deleted!")
                .setMessage("Deck has been cleared!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
