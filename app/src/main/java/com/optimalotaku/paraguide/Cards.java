package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvalyan on 8/2/17.
 */

public class Cards extends Fragment {

    GridView gridview;
    FileManager cardmanager;
    static CardData incCard;

    public static Cards newInstance(CardData cotd) {

        Bundle args = new Bundle();
        incCard = cotd;
        Cards fragment = new Cards();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final Intent intent;
        View view = inflater.inflate(R.layout.cardlist2, container, false);
        cardmanager = new FileManager(getContext());
        Map<String,List<CardData>> cDataMap = new HashMap<>();
        try {
            cDataMap = cardmanager.readCardsFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Map<String, List<CardData>> finalCDataMap = cDataMap;
        gridview = (GridView) view.findViewById(R.id.gridview2);
        final SharedPreferences prefs = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        String authCode = prefs.getString("signedIn", "null");
        APICardList allCards = new APICardList(authCode, getActivity());
        JSONArray cardArray = null;
        try {
            String response = allCards.execute().get();
            cardArray = new JSONArray(response);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        final CardData[] cardList = new CardData[cardArray.length()];
        for(int j = 0; j<cardArray.length();j++){
            cardList[j] = new CardData();
        }

        try {
            for (int i = 0; i < cardArray.length(); i++) {
                cardList[i].setId(cardArray.getJSONObject(i).getString("id"));
                cardList[i].setName(cardArray.getJSONObject(i).getString("name"));
                cardList[i].setImageUrl(cardArray.getJSONObject(i).getJSONArray("levels").getJSONObject(0).getJSONObject("images").getString("large"));
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        CustomComparator cardCompare = new CustomComparator();
        Arrays.sort(cardList, cardCompare);
        MyDeckAdapter CardAdapter = new MyDeckAdapter(getActivity(), cardList);
        gridview.setAdapter(CardAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main drawer
                gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


                //Toast.makeText(getApplicationContext(), "You Clicked " +cardList[+ position].getName(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < finalCDataMap.get("All").size(); i++){
                    if(finalCDataMap.get("All").get(i).getName().equals(cardList[position].getName())){
                        CardData finalChoice = finalCDataMap.get("All").get(i);
                        finalChoice.setBareImageUrl(cardList[position].getImageUrl());
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, CardDisplay.newInstance(finalChoice))
                                .addToBackStack("NEW")
                                .commit();
                    }
                }
            }
        });

        return view;

    }
    public class CustomComparator implements Comparator<CardData> {
        @Override
        public int compare(CardData o1, CardData o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}
