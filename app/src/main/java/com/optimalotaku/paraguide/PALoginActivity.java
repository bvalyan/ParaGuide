package com.optimalotaku.paraguide;

/**
 * Created by bvaly on 4/18/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class PALoginActivity extends AppCompatActivity {
    private EditText mUsername;
    private  EditText mPass;

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.usernameEdit);
        mPass = (EditText) findViewById(R.id.passEdit);

    }

    public void joinChat(View view) {
        String username = mUsername.getText().toString();
        String password = mPass.getText().toString();

        if (!isValid(username)) {
            return;
        }
        if(!password.equals(Constants.CHAT_PASS)){
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Password!")
                    .setMessage("You have entered an incorrect password for this room.")
                    .setPositiveButton("OK! Sorry...", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        SharedPreferences sp = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.DATASTREAM_UUID, username);
        edit.apply();

        Intent intent = new Intent(this, PAChatActivity.class);
        startActivity(intent);
    }

    private static boolean isValid(String username) {
        return username.matches("^[a-zA-Z0-9_]+$");
    }
}
