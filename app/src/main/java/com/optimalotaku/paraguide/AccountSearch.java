package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 2/8/2017.
 */

public class AccountSearch extends Fragment {
    private EditText textSearch;
    ProgressDialog dialog;
    static HashMap<String,HeroData> heroDataMap;

    public static AccountSearch newInstance(HashMap<String,HeroData> heroMap) {

        Bundle args = new Bundle();
        heroDataMap = heroMap;
        AccountSearch fragment = new AccountSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.player_data_screen, container,false);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width  = WindowManager.LayoutParams.MATCH_PARENT;
        textSearch = (EditText) view.findViewById(R.id.playerText);
        final String[] newString = new String[1];
        Button search = (Button) view.findViewById(R.id.playerqueryButton);
        final HashMap<String,HeroData> hData = heroDataMap;
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
                    Toast.makeText(getContext(), "Account not found!!!!",
                            Toast.LENGTH_LONG).show();
                }else{
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, NewPlayerDisplay.newInstance(textSearch.getText().toString(),heroDataMap))
                            .commit();
                }

            }
        });

    return view;
    }
}
