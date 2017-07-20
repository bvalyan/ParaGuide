package com.optimalotaku.paraguide;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 2/8/2017.
 */
public class PlayerTrendsFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    HashMap<String,HeroData> hDataMap;
    PlayerData pData;

    // newInstance constructor for creating fragment with arguments
    public static PlayerTrendsFragment newInstance(int page, String title, PlayerData pData, HashMap<String,HeroData> hDataMap) {
        PlayerTrendsFragment trendFrag = new PlayerTrendsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putSerializable("playerData", pData);
        args.putSerializable("heroDataMap",hDataMap);
        trendFrag.setArguments(args);
        return trendFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        pData = (PlayerData) getArguments().getSerializable("playerData");
        hDataMap = (HashMap<String,HeroData>) getArguments().getSerializable("heroDataMap");



    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO
        // pull text pics, and scores from API looping through to find top 3 heroes


        View view = inflater.inflate(R.layout.fragment_trend, container, false);

        final String []  text = new String[hDataMap.size()];
        String [] pics = new String[hDataMap.size()];
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String accountID = prefs.getString("ACCOUNT_ID", "NULL");
        int [] scores = new int[hDataMap.size()];
        Set<Map.Entry<String, HeroData>> entrySet = hDataMap.entrySet();
        int i = 0;
        HeroReview[] reviewPacket = new HeroReview[hDataMap.size()];
        for (int j = 0; j < hDataMap.size(); j += 1) {
            reviewPacket[j] = new HeroReview();
        }
        for(Map.Entry entry : entrySet){
            HeroData tempData = new HeroData();
            text[i] = entry.getKey().toString();
            tempData =  (HeroData) entry.getValue();
            pics[i] = tempData.getImageIconURL();
            try {
                scores[i] = CalculateScore(accountID, tempData.getId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                scores[i] = 0; //catch exception for empty stats
                e.printStackTrace();
            }
            reviewPacket[i].setPic(pics[i]);
            reviewPacket[i].setText(text[i]);
            reviewPacket[i].setScore(scores[i]);
            //create heroreview object here to save all aspects. change adapter to take in this object
            i++;
        }

        TrendList adapter = new TrendList(this.getActivity(), reviewPacket);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list3);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        return view;
    }

    private int CalculateScore(String accountID, String heroID) throws ExecutionException, InterruptedException, JSONException, IllegalAccessException {
        PlayerScoreFragment scoreOnly = new PlayerScoreFragment();
        HeroAPIStats stats = new HeroAPIStats(this.getContext());
        String heroRawData = stats.execute(accountID, heroID).get();
        JSONObject rawData = new JSONObject(heroRawData);

        PlayerData pHData = new PlayerData();

        pHData.setMatches(rawData.getJSONObject("pvp").optString("games_played"));
        pHData.setWins(rawData.getJSONObject("pvp").optString("games_won"));
        pHData.setAssists(rawData.getJSONObject("pvp").optString("assists_hero"));
        pHData.setDeaths(rawData.getJSONObject("pvp").optString("deaths_hero"));
        pHData.setHeroKills(rawData.getJSONObject("pvp").optString("kills_hero"));
        pHData.setInhibKills(rawData.getJSONObject("pvp").optString("kills_inhibitors"));
        pHData.setInhibAssists(rawData.getJSONObject("pvp").optString("assists_inhibitor"));
        pHData.setCoreKills(rawData.getJSONObject("pvp").optString("kills_core"));
        pHData.setTowerKills(rawData.getJSONObject("pvp").optString("kills_towers"));
        pHData.setTowerAssists(rawData.getJSONObject("pvp").optString("assists_towers"));
        //pData.setGamesLeft(rawData.getJSONObject("pvp").optString("games_left"));
        // pData.setGamesReconnected(rawData.getJSONObject("pvp").optString("games_reconnected"));
        pHData.setWardKills(rawData.getJSONObject("pvp").optString("kills_wards"));
        pHData.setStructureDamage(rawData.getJSONObject("pvp").optString("damage_done_structures"));
        pHData.setHeroDamage(rawData.getJSONObject("pvp").optString("damage_done_hero"));
        pHData.setMinionDamage(rawData.getJSONObject("pvp").optString("damage_done_minons"));
        pHData.setMinionKills(rawData.getJSONObject("pvp").optString("kills_minions"));
        pHData.setTimeePlayed(rawData.getJSONObject("pvp").optString("time_played"));
        pHData.setSurrenders(rawData.getJSONObject("pvp").optString("surrender_votes_started"));
        pHData.setXp(rawData.getJSONObject("total").optString("xp"));


        int score = scoreOnly.CalculateParaflow(pHData);
        return score;
    }
}
