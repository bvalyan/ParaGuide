package com.optimalotaku.paraguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bvaly on 1/8/2017.
 */

public class PlayerView extends AppCompatActivity implements PlayerInfoResponse {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.player_data_screen);

    }

    public void onSearch(View view) throws InterruptedException {

        //Gather UI Objects
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        EditText pEdit = (EditText) findViewById(R.id.playerText);

        //Create PlayerData Object
        PlayerData playerData = new PlayerData();

        ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(progressBar,pEdit.getText().toString(),playerData);
        playerInfo.delegate = this;
        playerInfo.execute();
    }
    @Override
    public void processPlayerInfoFinish(PlayerData pData) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        TextView responseView = (TextView) findViewById(R.id.playerresponseView);
        ImageView picDisplay = (ImageView) findViewById(R.id.playerHeroImages);

        progressBar.setVisibility(View.GONE);

        if (pData.getMatches() != null) {


            responseView.setText("Player Name: " + pData.getPlayerName());
            responseView.append("\n");
            responseView.append("Player Wins: " + pData.getWins());
            responseView.append("\n");
            responseView.append("Player Match Total: " + pData.getMatches());
            responseView.append("\n");
            responseView.append("Player Hero Kills: " + pData.getHeroKills());
            responseView.append("\n");
            responseView.append("Player Hero Deaths: " + pData.getDeaths());
            responseView.append("\n");
            responseView.append("Player Hero Assists: " + pData.getAssists());

            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                //hide keyboard upon return
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
        }
        else{
            Toast.makeText(PlayerView.this, "Player Not Found!!!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
