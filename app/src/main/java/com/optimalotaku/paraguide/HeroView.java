package com.optimalotaku.paraguide;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Jerek on 12/19/2016.
 */

public class HeroView extends android.support.v4.app.Fragment {

    ListView list;
    String[] text;
    String[] pics;
    FileManager fileManager;
    static HashMap<String,HeroData> map;


    public static HeroView newInstance(HashMap<String,HeroData> hMap) {

        map = hMap;
        return new HeroView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Gather UI Objects
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.listtest, container, false);
        fileManager = new FileManager(getContext());
        final HashMap<String, HeroData> hData = map;

        //Get List of hero names from Map
        text = hData.keySet().toArray(new String[hData.size()]);
        pics = new String[hData.keySet().toArray().length];

        Arrays.sort(text);

        //Put the image URLs associated with each hero in a array
        for (int i = 0; i < text.length; i++) {
            HeroData hero = hData.get(text[i]);
            pics[i] = (hero.getImageIconURL());
        }


        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new CardFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("heroes", hData);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        /*list=(ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), "You Clicked " +text[+ position], Toast.LENGTH_SHORT).show();
                //start new activity with method that takes in name and HeroData object and displays information
                Intent i = new Intent(HeroView.this,HeroDisplayPrototype.class);
                HeroData chosenHero = hData.get(text[position]);
                i.putExtra("HeroData",chosenHero);
                startActivity(i);

            }
        });*/

            View view2 = getActivity().getCurrentFocus();
            if (view2 != null) {
                //hide keyboard upon return
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }


            try {
                fileManager.saveHeroesToStorage(hData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    return view;
    }
}

