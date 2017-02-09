package com.optimalotaku.paraguide;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

import org.json.JSONException;
import org.json.JSONObject;

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

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Match Distribution");
        dataSet.setSliceSpace(1);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());
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
        data.setValueTextColor(Color.BLACK);

        mChart3.setData(data);


        // undo all highlights
        mChart3.highlightValues(null);

        // update pie chart
        XAxis barAxis = mChart3.getXAxis();
        //barAxis.setLabelCount(4);
        //barAxis.setAxisMaximum(3);
        barAxis.setDrawGridLines(false);


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
        mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        // configure pie chart
        Description description1 = new Description();
        Description description2 = new Description();
        Description description3 = new Description();
        description1.setText("Win/Loss Analysis");
        description2.setText("Kill/Death/Assist Analysis");
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

            ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(progressBar, playerName, playerData);
            playerJSONInfo[0] = playerInfo.execute().get();
            JSONObject playerStats;

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

                // add data
                wins = Integer.parseInt(pData.getWins());
                matches = Integer.parseInt(pData.getMatches());
                kills = Integer.parseInt(pData.getHeroKills());
                deaths = Integer.parseInt(pData.getDeaths());
                assists = Integer.parseInt(pData.getAssists());
                towerKills = Integer.parseInt(pData.getTowerKills());
                coreKills = Integer.parseInt(pData.getCoreKills());

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
        win.formColor = ColorTemplate.LIBERTY_COLORS[0];
        losses.label = "losses";
        losses.formColor = ColorTemplate.LIBERTY_COLORS[1];
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

        return view;
        }
    }

