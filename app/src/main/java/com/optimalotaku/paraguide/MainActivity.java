package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.homescreen);

    }

    public void heroClick(View view){
        View view2 = this.getCurrentFocus();
        if(view2 != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        Intent intent = new Intent(MainActivity.this, HeroView.class);
        startActivity(intent);
    }


    public void loginSend(View view){
        View view2 = this.getCurrentFocus();
        if(view2 != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
        Intent intent = new Intent(MainActivity.this, DeckView.class);
        startActivity(intent);
    }

}

