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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;


/**
 * Created by bvaly on 1/8/2017.
 */

public class PlayerView extends AppCompatActivity implements PlayerInfoResponse {

    private RelativeLayout mainLayout;
    private PieChart mChart;
    private int[] yData = new int[2];
    private String[] xData = {"Wins", "Losses"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.player_data_screen);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mChart = (PieChart) findViewById(R.id.chart);
        // add pie chart to main layout
        //mainLayout.addView(mChart);
        mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        // configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDescription("ParaFlow Player Analysis");


        // enable hole and configure

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(PlayerView.this,
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });



        // customize legends

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);



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


            responseView.setText("Player Name: " + pData.getPlayerName());
            responseView.append("\n");
            responseView.append("Player Wins: " + pData.getWins());
            responseView.append("\n");
            responseView.append("Player Match Total: " + pData.getMatches());
            responseView.append("\n");
            responseView.append("Player Hero Kills: " + pData.getHeroKills());
            responseView.append("\n");
            responseView.append("Player Hero Deaths: " + pData.getDeaths());
            responseView.append("\n");
            responseView.append("Player Hero Assists: " + pData.getAssists());
            // add data
            int wins = Integer.parseInt(pData.getWins());
            int matches = Integer.parseInt(pData.getMatches());

            addData(wins,matches);

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

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yData[0] = wins;
        int losses = matches-wins;
        yData[1] = losses;

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

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

        PieData data = new PieData(xVals, dataSet);
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
}
