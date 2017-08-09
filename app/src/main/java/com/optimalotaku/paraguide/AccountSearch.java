package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.player_data_screen);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width  = WindowManager.LayoutParams.MATCH_PARENT;
        textSearch = (EditText) findViewById(R.id.playerText);
        final String[] newString = new String[1];
        Button search = (Button) findViewById(R.id.playerqueryButton);
        final HashMap<String,HeroData> hData = (HashMap<String,HeroData>) getIntent().getSerializableExtra("HeroMap");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                APIPlayerCheck check = new APIPlayerCheck(); // call APIPlayerCheck to verify account exists
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
                    Intent i = new Intent(AccountSearch.this, NewPlayerDisplay.class); //Account checks out. Load saved hero data and store name to intent for stat processing
                    i.putExtra("HeroMap",hData);
                    i.putExtra("name", textSearch.getText().toString());
                    startActivity(i);
                }

            }
        });


    }
}
