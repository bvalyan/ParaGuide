package com.optimalotaku.paraguide;

/**
 * Created by Brandon on 1/16/17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

public class TrendList extends RecyclerView.Adapter<TrendList.ViewHolder> {

    private final Activity context;
    private final String[] web;
    private final String[] imageId;
    ProgressDialog progressDialog;
    private int [] heroScore;


    public TrendList(Activity context,
                     String[] web, String[] imageId, int [] scores) {
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.heroScore = scores;

    }

    @Override
    public TrendList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.listtrendsinglenew, parent, false);

        // Return a new holder instance
        TrendList.ViewHolder viewHolder = new TrendList.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrendList.ViewHolder holder, int position) {
        // Create background track
        holder.trendchart1.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
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

        final TextView scorePercent = (TextView) holder.itemView.findViewById(R.id.scorePercent);
        final TextView gradePercent = (TextView) holder.itemView.findViewById(R.id.gradePercent);
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

        final int series1Index = holder.trendchart1.addSeries(seriesItem1);


        //nameView.setTextSize(60);

        holder.heroText.setTextSize(25);
        holder.heroText.setTextColor(Color.WHITE);
        holder.heroText.setText(web[position]);

        Glide.with(holder.itemView.getContext()).load(imageId[position]).into(holder.heroImage);
        holder.trendchart1.addEvent(new DecoEvent.Builder(heroScore[position]).setIndex(series1Index).setDelay(1).build());


    }


    @Override
    public int getItemCount() {
        return web.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DecoView trendchart1;
        public ImageView heroImage;
        public TextView heroText;
        public TextView gradeNumber;
        public TextView gradeLetter;


        public ViewHolder(View itemView) {
            super(itemView);

            trendchart1 = (DecoView) itemView.findViewById(R.id.dynamicArcView2);
            heroImage = (ImageView) itemView.findViewById(R.id.img);
            heroText = (TextView) itemView.findViewById(R.id.txt);
            gradeNumber = (TextView) itemView.findViewById(R.id.scorePercent);
            gradeLetter = (TextView) itemView.findViewById(R.id.gradePercent);

        }
    }
}