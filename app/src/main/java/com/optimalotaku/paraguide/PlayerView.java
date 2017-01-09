package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by bvaly on 1/8/2017.
 */

public class PlayerView extends AppCompatActivity implements PlayerInfoResponse{

    private RelativeLayout mainLayout;
    private PieChart mChart;
    private PieChart mChart2;
    private BarChart mChart3;
    private int[] yData = new int[2];// Keeps track of win/loss
    private String[] xData = {"Wins", "Losses"};
    private int[] yData2 = new int[3]; //Keeps track of K/D/A
    private String[] xData2 = {"Kills", "Deaths", "Assists"};
    private int[] yData3 = new int[5];
    private String[] labels = {"Core Kills", "Tower Kills", "Hero Kills", "Deaths", "Assists"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.player_data_screen);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mChart = (PieChart) findViewById(R.id.chart);
        mChart2 = (PieChart) findViewById(R.id.chart2);
        mChart3 = (BarChart) findViewById(R.id.chart3);
        // add pie chart to main layout
        //mainLayout.addView(mChart);
        mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        // configure pie chart
        Description description1 = new Description();
        Description description2 = new Description();
        Description description3 = new Description();
        description1.setText("Win/Loss Analysis");
        description2.setText("Kill/Death/Assist Analysis");
        description3.setText("ParaFlow Player Analysis");
        mChart.setUsePercentValues(true);
        mChart.setDescription(description1);
        mChart2.setDescription(description2);
        mChart3.setDescription(description3);



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

        LegendEntry wins = new LegendEntry();
        LegendEntry losses = new LegendEntry();
        wins.label = "Wins";
        wins.formColor = ColorTemplate.LIBERTY_COLORS[0];
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
        LegendEntry[] chart2 = {kills,deaths,assists};
        LegendEntry[] chart1 = {wins, losses};


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


    public void onSearch(View view) throws InterruptedException {

        //Gather UI Objects
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        EditText pEdit = (EditText) findViewById(R.id.playerText);

        //Create PlayerData Object
        PlayerData playerData = new PlayerData();

        ParagonAPIPlayerInfo playerInfo = new ParagonAPIPlayerInfo(progressBar,pEdit.getText().toString(),playerData);
        playerInfo.delegate = this;
        playerInfo.execute();
    }



    @Override
    public void processPlayerInfoFinish(PlayerData pData) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        TextView responseView = (TextView) findViewById(R.id.playerresponseView);


        progressBar.setVisibility(View.GONE);

        if (pData.getMatches() != null) {


            // add data
            int wins = Integer.parseInt(pData.getWins());
            int matches = Integer.parseInt(pData.getMatches());
            int kills = Integer.parseInt(pData.getHeroKills());
            int deaths = Integer.parseInt(pData.getDeaths());
            int assists = Integer.parseInt(pData.getAssists());
            int towerKills = Integer.parseInt(pData.getTowerKills());
            int coreKills = Integer.parseInt(pData.getCoreKills());
            int healingDone = Integer.parseInt(pData.getHeroHealing());
            int selfHealing = Integer.parseInt(pData.getSelfHealing());

            addData(wins,matches);
            addData2(kills,deaths,assists);
            addData3(towerKills, coreKills, kills, deaths, assists);

            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                //hide keyboard upon return
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
        }
        else{
            Toast.makeText(PlayerView.this, "Player Not Found!!!",
                    Toast.LENGTH_SHORT).show();
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

    private void addData3(int towerKills, int coreKills, int kills, int deaths, int assists) {//method for chart 3

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yData3[0] = towerKills;
        yData3[1] = coreKills;
        yData3[2] = kills;
        yData3[3] = deaths;
        yData3[4] = assists;

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
}
