package com.optimalotaku.paraguide;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 2/8/2017.
 */
public class PlayerScoreFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private String playerName;
    private PlayerData pData;


    // newInstance constructor for creating fragment with arguments
    public static PlayerScoreFragment newInstance(int page, String title, PlayerData pData, String playerName) {
        PlayerScoreFragment scoreFrag = new PlayerScoreFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putSerializable("playerData", pData);
        args.putString("playerName", playerName);
        scoreFrag.setArguments(args);
        return scoreFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        playerName = getArguments().getString("playerName");


    }

    public int CalculateParaflow(PlayerData pData) throws IllegalAccessException {
        double accurateScore = 0;
        int score = 0;

        Field[] declaredFields = pData.getClass().getDeclaredFields();

        for(Field f : declaredFields){
            f.setAccessible(true); //Additional line
            Object v =  f.get(pData);
            if ((v == null  && f.isSynthetic() == false) || v == ""){
                f.set(pData, "0");
            }
        }

        double avgKills = Double.parseDouble(pData.getHeroKills())/ Double.parseDouble(pData.getMatches());
        double avgAssists = Double.parseDouble(pData.getAssists())/Double.parseDouble(pData.getMatches());
        double avgHeroDmg = Double.parseDouble(pData.getHeroDamage())/Double.parseDouble(pData.getMatches());
        double avgMinionDmg = Double.parseDouble(pData.getMinionDamage())/Double.parseDouble(pData.getMatches());
        double avgMinionKills = Double.parseDouble(pData.getMinionKills())/Double.parseDouble(pData.getMatches());
        double avgDeaths = Double.parseDouble(pData.getDeaths())/Double.parseDouble(pData.getMatches());
        double avgTowerKills = Double.parseDouble(pData.getTowerKills())/Double.parseDouble(pData.getMatches());
        double avgTowerAssists = Double.parseDouble(pData.getTowerAssists())/Double.parseDouble(pData.getMatches());
        double avgInhibKills = Double.parseDouble(pData.getInhibKills())/Double.parseDouble(pData.getMatches());
        double avgInhibAssists = Double.parseDouble(pData.getInhibAssists())/Double.parseDouble(pData.getMatches());
        double avgWardKills = Double.parseDouble(pData.getWardKills())/Double.parseDouble(pData.getMatches());
        double avgStuctDamage = Double.parseDouble(pData.getStructureDamage())/Double.parseDouble(pData.getMatches());
        //int gamesLeft = Integer.parseInt(pData.getGamesLeft())-Integer.parseInt(pData.getGamesReconnected());
       // double surrenderProb = Double.parseDouble(pData.getSurrenders())/Double.parseDouble(pData.getMatches());
        double timePlayed = Double.parseDouble(pData.getTimeePlayed());
        double gamesPlayed = Double.parseDouble(pData.getMatches());

        double killScore = (avgKills - Constants.currentMin) / (Constants.currentKillMax - Constants.currentMin);
        double assistScore = (avgAssists - Constants.currentMin) / (Constants.currentAssistMax - Constants.currentMin);
        double heroDMGScore = (avgHeroDmg - Constants.currentMin) / (Constants.currentheroDamageMax - Constants.currentMin);
        double minionDMGScore = (avgMinionDmg - Constants.currentMin) / (Constants.currentMinionDamageMax - Constants.currentMin);
        double minionKillScore = (avgMinionKills - Constants.currentMin) / (Constants.currentMinionKillMax - Constants.currentMin);
        double deathScore = (avgDeaths - Constants.currentMin) / (Constants.currentDeathMax - Constants.currentMin);
        double towerKillSCore = (avgTowerKills - Constants.currentMin) / (Constants.currentTowerKillMax - Constants.currentMin);
        double towerAssistScore = (avgTowerAssists - Constants.currentMin) / (Constants.currentTowerAssistMax - Constants.currentMin);
        double inhibkillScore = (avgInhibKills - Constants.currentMin) / (Constants.currentInhibKillsMax - Constants.currentMin);
        double inhibAssistScore = (avgInhibAssists - Constants.currentMin) / (Constants.crrentinhibAssistMax - Constants.currentMin);
        double wardScore = (avgWardKills - Constants.currentMin) / (Constants.currentMaxWardKills - Constants.currentMin);
        double structScore = (avgStuctDamage - Constants.currentMin) / (Constants.currentMaxStructDamage - Constants.currentMin);
        double timeScore = (timePlayed - Constants.currentMin) / (Constants.currentMaxTimePlayed - Constants.currentMin);
        double gameScore = (gamesPlayed - Constants.currentMin) / (Constants.currentMaxGamesPlayed - Constants.currentMin);

        if(avgAssists - avgKills > 2){
            assistScore = assistScore*2.0;
            killScore = killScore*.25;
        }

        double mechanicScore = ((killScore*10) + ((assistScore)*10) - ((deathScore*.25)*10) + (heroDMGScore*10) + (minionDMGScore*10) + (minionKillScore*10))/60;
        mechanicScore = (mechanicScore * 100) * .40;

        double effeciencyScore = ((towerKillSCore*10) + (towerAssistScore*10) + (inhibkillScore*10) + (inhibAssistScore*10) + (structScore*10) + (wardScore*10))/60;
        effeciencyScore = (effeciencyScore * 100) * .50;
        //double behaviorScore = ((gamesLeft) * (surrenderProb*10))/20;
        //behaviorScore = behaviorScore * 100 * .05;
        double experienceScore = ((timeScore * 10) + (gameScore*10))/20;
        experienceScore = (experienceScore* 100) * .10;


        accurateScore = (mechanicScore +  effeciencyScore  + experienceScore);
        accurateScore = (accurateScore/75)*100;
        score = (int) accurateScore;
        if(score > 100)
            score = 100;

        return score;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_score, container, false);
        final TextView gradeView = (TextView) view.findViewById(R.id.grade);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        final TextView nameView = (TextView) view.findViewById(R.id.percent);
        final DecoView arcView = (DecoView)view.findViewById(R.id.dynamicArcView);

        try {
            ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(this.getContext(), progressBar, playerName, pData);
            String[] playerJSONInfo = new String[1];
            playerJSONInfo[0] = playerInfo.execute().get();
            JSONObject rawData = new JSONObject(playerJSONInfo[0]);
            pData = new PlayerData();

            pData.setMatches(rawData.getJSONObject("pvp").getString("games_played"));
            pData.setWins(rawData.getJSONObject("pvp").getString("games_won"));
            pData.setAssists(rawData.getJSONObject("pvp").getString("assists_hero"));
            pData.setDeaths(rawData.getJSONObject("pvp").getString("deaths_hero"));
            pData.setHeroKills(rawData.getJSONObject("pvp").getString("kills_hero"));
            pData.setInhibKills(rawData.getJSONObject("pvp").getString("kills_inhibitors"));
            pData.setInhibAssists(rawData.getJSONObject("pvp").getString("assists_inhibitor"));
            pData.setCoreKills(rawData.getJSONObject("pvp").getString("kills_core"));
            pData.setTowerKills(rawData.getJSONObject("pvp").getString("kills_towers"));
            pData.setTowerAssists(rawData.getJSONObject("pvp").getString("assists_towers"));
            //pData.setGamesLeft(rawData.getJSONObject("pvp").getString("games_left"));
           // pData.setGamesReconnected(rawData.getJSONObject("pvp").getString("games_reconnected"));
            pData.setWardKills(rawData.getJSONObject("pvp").getString("kills_wards"));
            pData.setStructureDamage(rawData.getJSONObject("pvp").getString("damage_done_structures"));
            pData.setHeroDamage(rawData.getJSONObject("pvp").getString("damage_done_hero"));
            pData.setMinionDamage(rawData.getJSONObject("pvp").getString("damage_done_minons"));
            pData.setMinionKills(rawData.getJSONObject("pvp").getString("kills_minions"));
            pData.setTimeePlayed(rawData.getJSONObject("pvp").getString("time_played"));
            pData.setSurrenders(rawData.getJSONObject("pvp").getString("surrender_votes_started"));
            pData.setXp(rawData.getJSONObject("total").getString("xp"));




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


// Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(70f)
                .build());

//Create data series track
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 161, 186, 226))
                //.setRange(0, seriesMax, 0)
                .setInitialVisibility(true)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.7f))
                //.setSeriesLabel(new SeriesLabel.Builder("PARAFLOW Score: %.0f%%").build())
                .setInterpolator(new OvershootInterpolator())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                //.setInset(new PointF(32f, 32f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(8000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        final int series1Index = arcView.addSeries(seriesItem1);

        final String format = "%.0f%%";
        nameView.setTextSize(60);

        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (format.contains("%%")) {
                    float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                    nameView.setText(String.format(format, percentFilled * 100f));
                } else {
                    nameView.setText(String.format(format, currentPosition));
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());

        int finalScore = 0;
        try {
            finalScore = CalculateParaflow(pData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String grade;
        if( finalScore > 95){
            grade = "A+";
        }
        else if(finalScore < 95 && finalScore > 89){
            grade = "A";
        }
        else if(finalScore > 85 && finalScore < 90){
            grade = "B+";
        }
        else if(finalScore > 79 && finalScore < 85){
            grade = "B";
        }
        else if(finalScore > 75 && finalScore < 80){
            grade = "C+";
        }
        else if(finalScore > 69 && finalScore < 76){
            grade = "C";
        }
        else if(finalScore > 65 && finalScore < 70){
            grade = "D+";
        }
        else if(finalScore > 59 && finalScore < 66){
            grade = "D";
        }
        else if(finalScore < 60 && finalScore >= 1){
            grade = "F";
            Log.i("INFO", "" + finalScore);
        }
        else{
            grade = "N/A";
        }
        arcView.addEvent(new DecoEvent.Builder(finalScore).setIndex(series1Index).setDelay(4000).build());
        gradeView.setTextSize(40);
        gradeView.setText(grade);

        return view;
    }
}
