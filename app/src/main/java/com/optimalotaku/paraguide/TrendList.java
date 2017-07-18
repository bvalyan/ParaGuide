package com.optimalotaku.paraguide;

/**
 * Created by Brandon on 1/16/17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class TrendList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final String[] imageId;
    ProgressDialog progressDialog;
    private int [] heroScore;
    public TrendList(Activity context,
                     String[] web, String[] imageId, int [] scores) {
        super(context, R.layout.listtrendsingle, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.heroScore = scores;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //final TextView nameView = (TextView) view.findViewById(R.id.percent);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listtrendsingle, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        DecoView score = (DecoView) rowView.findViewById(R.id.dynamicArcView2);



// Create background track
        score.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(70f)
                .build());

//Create data series track
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 255, 158, 158))
                //.setRange(0, seriesMax, 0)
                .setInitialVisibility(true)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#f44242"), 0.7f))
                //.setSeriesLabel(new SeriesLabel.Builder("PARAFLOW Score: %.0f%%").build())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                //.setInset(new PointF(32f, 32f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(1000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        final TextView scorePercent = (TextView) rowView.findViewById(R.id.scorePercent);
        final TextView gradePercent = (TextView) rowView.findViewById(R.id.gradePercent);
        scorePercent.setTextColor(Color.WHITE);

        String grade;
        if( heroScore[position] > 95){
            grade = "A+";
        }
        else if(heroScore[position] <= 95 && heroScore[position] > 89){
            grade = "A";
        }
        else if(heroScore[position] > 85 && heroScore[position] < 90){
            grade = "B+";
        }
        else if(heroScore[position] > 79 && heroScore[position] < 86){
            grade = "B";
        }
        else if(heroScore[position] > 75 && heroScore[position] < 80){
            grade = "C+";
        }
        else if(heroScore[position] > 69 && heroScore[position] < 76){
            grade = "C";
        }
        else if(heroScore[position] > 65 && heroScore[position] < 70){
            grade = "D+";
        }
        else if(heroScore[position] > 59 && heroScore[position] < 66){
            grade = "D";
        }
        else if(heroScore[position] > 0 && heroScore[position] < 60){
            grade = "F";
        }
        else{
            grade = "N/A";
        }

        gradePercent.setTextColor(Color.WHITE);
        gradePercent.setText(grade);
        final String format = "%.0f%%";
        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (format.contains("%%")) {
                    float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                    scorePercent.setText(String.format(format, percentFilled * 100f));
                } else {
                    scorePercent.setText(String.format(format, currentPosition));
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        final int series1Index = score.addSeries(seriesItem1);


        //nameView.setTextSize(60);

        txtTitle.setTextSize(25);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setText("  "+web[position]);

        Glide.with(getContext()).load(imageId[position]).into(imageView);
        score.addEvent(new DecoEvent.Builder(heroScore[position]).setIndex(series1Index).setDelay(1).build());

        return rowView;
    }
}