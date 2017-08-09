package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvaly on 2/8/2017.
 */

public class playerGraphFragment extends Fragment {

    private RelativeLayout mainLayout;
    private PieChart mChart;
    private PieChart mChart2;
    private BarChart mChart3;
    private int[] yData = new int[2];// Keeps track of win/loss
    private String[] xData = {"Wins", "Losses"};
    private int[] yData2 = new int[3]; //Keeps track of K/D/A
    private String[] xData2 = {"Kills", "Deaths", "Assists"};
    private float[] yData3 = new float[4];
    private String[] labels = {"Avg. Tower Kills", "Avg. Hero Kills", " Avg. Deaths", "Avg. Assists"};
    PlayerData pData;
    private int page;
    private String title;
    int wins;
    int matches;
    int kills;
    int deaths;
    int assists;
    int towerKills;
    int coreKills;
    private String playerName;
    ProgressDialog dialog;


    public static playerGraphFragment newInstance(int page, String title, PlayerData pData, String playerName) {
        playerGraphFragment playerFrag = new playerGraphFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putSerializable("playerData", pData);
        args.putString("playerName", playerName);
        playerFrag.setArguments(args);
        return playerFrag;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        playerName = getArguments().getString("playerName");
        pData = new PlayerData();
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public class LabelFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public LabelFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            System.out.print(mValues);
            return mValues[(int) value];
        }
    }

    public class MyValueFormatter implements IValueFormatter
    {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0");
        }
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }



    private void addData(int wins, int matches) {

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        yData[0] = wins;
        int losses = matches-wins;
        yData[1] = losses;

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry(yData[i], i));

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Match Distribution");
        dataSet.setSliceSpace(1);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);


        //colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);

        mChart.setData(data);


        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
        mChart.animateXY(2000,2000);
    }

    private void addData2(int kills, int deaths, int assists) {//method for chart 2

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        yData2[0] = kills;
        yData2[1] = deaths;
        yData2[2] = assists;

        for (int i = 0; i < yData2.length; i++)
            yVals1.add(new PieEntry(yData2[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData2.length; i++)
            xVals.add(xData2[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "K/D/A Distribution");
        dataSet.setSliceSpace(1);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);

        mChart2.setData(data);


        // undo all highlights
        mChart2.highlightValues(null);

        // update pie chart
        mChart2.invalidate();
        mChart2.animateXY(2000,2000);
    }

    private void addData3(int towerKills, int coreKills, int kills, int deaths, int assists, int matches) {//method for chart 3


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yData3[0] = (float)towerKills/matches;
        yData3[1] = (float)kills/matches;
        yData3[2] = (float)deaths/matches;
        yData3[3] = (float)assists/matches;

        for (int i = 0; i < yData3.length; i++)
            yVals1.add(new BarEntry(i, yData3[i]));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < labels.length; i++)
            xVals.add(labels[i]);


        // create bar data set
        BarDataSet dataSet = new BarDataSet(yVals1, "Essential Stat Distribution");


        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate bar data object now

        BarData data = new BarData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart3.setData(data);


        // undo all highlights
        mChart3.highlightValues(null);

        // update pie chart
        XAxis barAxis = mChart3.getXAxis();

        //barAxis.setLabelCount(4);
        //barAxis.setAxisMaximum(3);
        barAxis.setDrawGridLines(false);
        mChart.getLegend().setTextColor(Color.WHITE);
        mChart2.getLegend().setTextColor(Color.WHITE);
        barAxis.setTextColor(Color.WHITE);
        mChart3.getLegend().setTextColor(Color.WHITE);
        mChart3.getAxisRight().setTextColor(Color.WHITE);
        mChart3.getAxisLeft().setTextColor(Color.WHITE);


        barAxis.setValueFormatter(new LabelFormatter(labels));
        mChart3.setAutoScaleMinMaxEnabled(true);
        mChart3.setVisibleXRangeMinimum(4);
        mChart3.invalidate();
        mChart3.animateXY(2000,2000);
    }

    public void DrawData(){
        addData(wins,matches);
        addData2(this.kills,this.deaths,this.assists);
        addData3(towerKills, coreKills, this.kills, this.deaths, this.assists, matches);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_playergraph, container, false);
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        mChart = (PieChart) view.findViewById(R.id.chart);
        mChart2 = (PieChart) view.findViewById(R.id.chart2);
        mChart3 = (BarChart) view.findViewById(R.id.chart3);
        // add pie chart to main layout
        //mainLayout.addView(mChart);
        //mainLayout.setBackgroundColor(Color.GRAY);

        // configure pie chart
        Description description1 = new Description();
        Description description2 = new Description();
        Description description3 = new Description();
        description1.setText("Win/Loss Analysis");
        description1.setTextColor(Color.WHITE);
        description2.setText("K/D/A Analysis");
        description2.setTextColor(Color.WHITE);
        description3.setText("");
        mChart.setUsePercentValues(true);
        mChart.setDescription(description1);
        mChart2.setDescription(description2);
        mChart3.setDescription(description3);
        final String[] playerJSONInfo = new String[1];

        try {
            //Gather UI Objects
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
            EditText pEdit = (EditText) view.findViewById(R.id.playerText);

            //Create PlayerData Object
            PlayerData playerData = new PlayerData();

            //make call to player info, get response to chart


            ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(this.getContext(), progressBar, playerName, playerData);

            playerJSONInfo[0] = playerInfo.execute().get();

            JSONObject playerStats;
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());


            try {

                playerStats = new JSONObject(playerJSONInfo[0]);

                pData.setMatches(playerStats.getJSONObject("pvp").getString("games_played"));
                pData.setWins(playerStats.getJSONObject("pvp").getString("games_won"));
                pData.setAssists(playerStats.getJSONObject("pvp").getString("assists_hero"));
                pData.setDeaths(playerStats.getJSONObject("pvp").getString("deaths_hero"));
                pData.setHeroKills(playerStats.getJSONObject("pvp").getString("kills_hero"));
                pData.setCoreKills(playerStats.getJSONObject("pvp").getString("kills_core"));
                pData.setTowerKills(playerStats.getJSONObject("pvp").getString("kills_towers"));
                pData.setGamesLeft(playerStats.getJSONObject("pvp").getString("games_left"));
                pData.setGamesReconnected(playerStats.getJSONObject("pvp").getString("games_reconnected"));
                //pData.setPlayerName(playerName);
            } catch (JSONException e) {

                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);

            if (pData.getMatches() != null) {

                // add data to local variables
                try{
                wins = Integer.parseInt(pData.getWins());}
                catch(NumberFormatException e){
                    wins = 0;
                }
                try{
                matches = Integer.parseInt(pData.getMatches());}
                catch(NumberFormatException e){
                    matches = 0;
                }
                try{
                kills = Integer.parseInt(pData.getHeroKills());}
                catch (NumberFormatException e){
                    kills = 0;
                }
                try{
                deaths = Integer.parseInt(pData.getDeaths());}
                catch (NumberFormatException e){
                    deaths = 0;
                }
                try{
                assists = Integer.parseInt(pData.getAssists());}
                catch (NumberFormatException e){
                    assists = 0;
                }
                try{
                towerKills = Integer.parseInt(pData.getTowerKills());}
                catch (NumberFormatException e){
                    towerKills = 0;
                }
                try{
                coreKills = Integer.parseInt(pData.getCoreKills());}
                catch (NumberFormatException e){
                    coreKills = 0;
                }

            } else {
                // Toast.makeText(playerGraphFragment.this, "Player Not Found!!!",
                //       Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // enable hole and configure

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setHoleRadius(17);
        mChart.setTransparentCircleRadius(20);
        mChart2.setDrawHoleEnabled(true);
        mChart2.setHoleColor(Color.TRANSPARENT);
        mChart2.setHoleRadius(17);
        mChart2.setTransparentCircleRadius(20);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart2.setRotationAngle(0);
        mChart2.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        LegendEntry win = new LegendEntry();
        LegendEntry losses = new LegendEntry();
        win.label = "Wins";
        win.formColor = ColorTemplate.JOYFUL_COLORS[0];
        losses.label = "Losses";
        losses.formColor = ColorTemplate.JOYFUL_COLORS[1];
        LegendEntry kills = new LegendEntry();
        LegendEntry deaths = new LegendEntry();
        LegendEntry assists = new LegendEntry();
        kills.label = "Kills";
        kills.formColor = ColorTemplate.VORDIPLOM_COLORS[0];
        deaths.label = "Deaths";
        deaths.formColor = ColorTemplate.VORDIPLOM_COLORS[1];
        assists.label = "Assists";
        assists.formColor = ColorTemplate.VORDIPLOM_COLORS[2];
        LegendEntry[] chart2 = {kills, deaths, assists};
        LegendEntry[] chart1 = {win, losses};


        Legend legend = mChart.getLegend();
        legend.setWordWrapEnabled(true);
        Legend legend2 = mChart2.getLegend();
        legend2.setWordWrapEnabled(true);
        Legend legend3 = mChart3.getLegend();
        legend3.setEnabled(false);


        legend.setCustom(chart1);
        legend2.setCustom(chart2);

        mChart3.setDrawBarShadow(false);
        mChart3.setDrawValueAboveBar(true);
        mChart3.setDrawGridBackground(false);
        XAxis xAxis = mChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(4);
        YAxis leftAxis = mChart3.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        DrawData();

        final TextView gradeView = (TextView) view.findViewById(R.id.grade);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        final TextView nameView = (TextView) view.findViewById(R.id.percent);
        final DecoView arcView = (DecoView)view.findViewById(R.id.dynamicArcView);

        try {
            ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(this.getContext(), progressBar, playerName, pData);
            //String[] playerJSONInfo = new String[1];
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
            // add listener for text inside of DecoView
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
        else if(finalScore > 84 && finalScore < 90){
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
    }

