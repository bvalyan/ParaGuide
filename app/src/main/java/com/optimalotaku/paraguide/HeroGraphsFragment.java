package com.optimalotaku.paraguide;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Brandon on 1/17/17.
 */
public class HeroGraphsFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private int basicAttack;
    private int abilityAttack;
    private int durability;
    private int mobility;
    private int[] yData = new int[4];// Keeps track of basic, ability, mobility, durability
    private String[] labels = {"Basic Attack", "Ability Attack", "Durability", "Mobility"};

    private int [] PARAGON_TEMPLATE = {Color.rgb(121, 123, 127), Color.rgb(137, 134, 103), Color.rgb(0, 0, 0), Color.rgb(64, 76, 32)};



    // newInstance constructor for creating fragment with arguments
    public static HeroGraphsFragment newInstance(int page, String title, int basicAttack, int abilityAttack, int durability, int mobility) {
        HeroGraphsFragment graphFrag = new HeroGraphsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putInt("basicAttack",basicAttack);
        args.putInt("abilityAttack", abilityAttack);
        args.putInt("durability", durability);
        args.putInt("mobility", mobility);
        graphFrag.setArguments(args);
        return graphFrag;
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

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        basicAttack = getArguments().getInt("basicAttack");
        abilityAttack = getArguments().getInt("abilityAttack");
        durability = getArguments().getInt("durability");
        mobility = getArguments().getInt("mobility");
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

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        //view.setBackgroundColor(Color.parseColor("#ffffff"));
        PieChart mChart = (PieChart) view.findViewById(R.id.heroChart1);
//        PieChart mChart2 = (PieChart) view.findViewById(R.id.heroChart2);
        Description description1 = new Description();
        description1.setText("Hero Stats");
        description1.setTextColor(Color.WHITE);
        mChart.setDescription(description1);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setHoleRadius(17);
        mChart.setTransparentCircleRadius(20);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

   /*     mChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });*/



        yData[0] = basicAttack;
        yData[1] = abilityAttack;
        yData[2] = mobility;
        yData[3] = durability;

        LegendEntry mobility = new LegendEntry();
        LegendEntry durability = new LegendEntry();
        LegendEntry basicAttack = new LegendEntry();
        LegendEntry abilityAttack = new LegendEntry();

        basicAttack.label = "Basic Attack";
        abilityAttack.label = "Ability Attack";
        durability.label = "Durability";
        mobility.label = "Mobility";



        basicAttack.formColor = PARAGON_TEMPLATE[0];
        abilityAttack.formColor = PARAGON_TEMPLATE[1];
        mobility.formColor = PARAGON_TEMPLATE[2];
        durability.formColor = PARAGON_TEMPLATE[3];
        LegendEntry[] chart1 = {mobility, durability, basicAttack, abilityAttack};
        Legend legend = mChart.getLegend();
        legend.setCustom(chart1);
        legend.setTextColor(Color.WHITE);

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry(yData[i], i));

        PieDataSet dataSet = new PieDataSet(yVals1, "Statistics");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : PARAGON_TEMPLATE)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setSliceSpace(1);
        dataSet.setSelectionShift(5);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.highlightValues(null);
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateXY(2000,2000);



        return view;
    }
}
