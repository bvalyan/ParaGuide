package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 2/8/2017.
 */

public class AccountSearch extends AppCompatActivity {
    private EditText textSearch;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_data_screen);
        textSearch = (EditText) findViewById(R.id.playerText);
        final String[] newString = new String[1];
        Button search = (Button) findViewById(R.id.playerqueryButton);
        final HashMap<String,HeroData> hData = (HashMap<String,HeroData>) getIntent().getSerializableExtra("HeroMap");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIPlayerCheck check = new APIPlayerCheck();
                try {
                     newString[0] = check.execute(textSearch.getText().toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(newString[0] == null || !newString[0].contains("accountId")){
                    Toast.makeText(AccountSearch.this, "Account not found!!!!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Intent i = new Intent(AccountSearch.this, NewPlayerDisplay.class);
                    i.putExtra("HeroMap",hData);
                    i.putExtra("name", textSearch.getText().toString());
                    startActivity(i);
                }

            }
        });


    }
}
