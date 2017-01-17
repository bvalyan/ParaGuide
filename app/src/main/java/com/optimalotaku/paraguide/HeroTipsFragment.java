package com.optimalotaku.paraguide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Brandon on 1/17/17.
 */
public class HeroTipsFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private String name;
    private String picURL;
    private int difficulty;
    private String scale;
    private String affinity1;
    private String affinity2;

    // newInstance constructor for creating fragment with arguments
    public static HeroTipsFragment newInstance(int difficulty,String title, String name, String scale, int page, String affinity1, String affinity2, String picURL) {
        HeroTipsFragment tipsFrag = new HeroTipsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putString("name", name);
        args.putString("attack", scale);
        args.putInt("difficulty", difficulty);
        args.putString("picURL", picURL);
        args.putString("affinity1", affinity1);
        args.putString("affinity2", affinity2);
        tipsFrag.setArguments(args);
        return tipsFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        name = getArguments().getString("name");
        scale = getArguments().getString("attack");
        picURL = getArguments().getString("picURL");
        difficulty = getArguments().getInt("difficulty");
        affinity1 = getArguments().getString("affinity1");
        affinity2 = getArguments().getString("affinity2");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        TextView nameView = (TextView) view.findViewById(R.id.titleText);
        ImageView heroImage = (ImageView) view.findViewById(R.id.titlePic);
        TextView description = (TextView) view.findViewById(R.id.descText);
        ImageView aff1 =  (ImageView) view.findViewById(R.id.aff1Pic);
        ImageView aff2 =  (ImageView) view.findViewById(R.id.aff2Pic);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvTitle);
        nameView.setTextSize(30);
        nameView.setText(name);
        Glide.with(this).load(picURL).into(heroImage);
        HeroData heroData = new HeroData();
        description.setTextSize(18);
        String scaling = "This hero's primary attack type is " + scale.toLowerCase() + ".\n";
        String difficultyRating = "This hero's difficulty is rated at a " + difficulty + ".\n";
        description.append(scaling);
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        description.append(difficultyRating);
        tvLabel.setText(page + " -- " + title);

        switch (affinity1.toLowerCase()){
            case "growth":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.growth_affinity_icon);
                aff1.setImageBitmap(bitmap);
                break;
            case "order":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.order_affinity_icon);
                aff1.setImageBitmap(bitmap);
                break;
            case "corruption":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.corruption_affinity_icon);
                aff1.setImageBitmap(Bitmap.createScaledBitmap(bitmap,70,70,false));
                break;
            case "fury" :
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fury_affinity_icon);
                aff1.setImageBitmap(bitmap);
                break;
            case "intellect":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.intellect_affinity_icon);
                aff1.setImageBitmap(bitmap);
                break;
        }

        aff1.setImageBitmap(bitmap);

        switch (affinity2.toLowerCase()){
            case "growth":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.growth_affinity_icon);
                aff2.setImageBitmap(bitmap2);
                break;
            case "order":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.order_affinity_icon);
                aff2.setImageBitmap(bitmap2);
                break;
            case "corruption":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.corruption_affinity_icon);
                aff2.setImageBitmap(Bitmap.createScaledBitmap(bitmap2,70,70,false));
                break;
            case "fury" :
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.fury_affinity_icon);
                aff2.setImageBitmap(bitmap2);
                break;
            case "intellect":
                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.intellect_affinity_icon);
                aff2.setImageBitmap(bitmap2);
                break;
        }

    return view;
    }
}
