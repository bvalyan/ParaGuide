package com.optimalotaku.paraguide;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jerek on 12/19/2016.
 */

public class ChampionView extends android.support.v4.app.Fragment {

    ListView list;
    ArrayList<String> text = new ArrayList<>();
    ArrayList<String> pics = new ArrayList<>();
    FileManager fileManager;
    static ArrayList<ChampionData> map;


    public static ChampionView newInstance(ArrayList<ChampionData> hMap) {

        map = hMap;
        return new ChampionView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Gather UI Objects
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.listtest, container, false);
        fileManager = new FileManager(getContext());
        final ArrayList<ChampionData> hData = map;

        //Get List of hero names from Map
        for (int i = 0; i < map.size(); i++){
            String name = map.get(i).getName();
            String pic = map.get(i).getChampIconURL();
            text.add(name);
            pics.add(pic);
        }



        Collections.sort(text);

        //Put the image URLs associated with each hero in a array
        for (int i = 0; i < text.size(); i++) {
            ChampionData hero = hData.get(i);
            pics.set(i, hero.getChampIconURL());
        }


        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

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
                Intent i = new Intent(ChampionView.this,HeroDisplayPrototype.class);
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


    return view;
    }
}

