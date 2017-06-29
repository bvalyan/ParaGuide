package com.optimalotaku.paraguide;

/**
 * Created by Brandon on 1/16/17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final String[] imageId;
    public CustomList(Activity context,
                      String[] web, String[] imageId) {
        super(context, R.layout.listsingle, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listsingle, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setTextSize(20);
        txtTitle.setText("  "+web[position]);
        Glide.with(getContext()).load(imageId[position]).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        return rowView;
    }
}