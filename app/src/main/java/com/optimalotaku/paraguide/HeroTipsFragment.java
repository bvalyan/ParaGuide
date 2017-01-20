package com.optimalotaku.paraguide;

import android.content.Intent;
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
    private String traits;
    private String primary;
    private String secondary1;
    private String secondary2;
    private String secondary3;
    private String ultimate;
    private String primaryDesc;
    private String secondary1Desc;
    private String secondary2Desc;
    private String secondary3Desc;
    private String ultDesc;
    private String primaryPic;
    private String secondary1Pic;
    private String secondary2Pic;
    private String secondary3Pic;
    private String ultimatePic;


    // newInstance constructor for creating fragment with arguments
    public static HeroTipsFragment newInstance(int page, String title, String name, String scale, int difficulty, String affinity1, String affinity2, String picURL, String traits, String primaryPic, String secondary1Pic, String secondary2Pic, String secondary3Pic, String ultimatePic, String primary, String secondary1, String secondary2, String secondary3, String ultimate, String primaryDesc, String secondary1Desc, String secondary2Desc, String secondary3Desc, String ultDesc) {
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
        args.putString("traits", traits);
        args.putString("primary", primary);
        args.putString("secondary1", secondary1);
        args.putString("secondary2", secondary2);
        args.putString("secondary3", secondary3);
        args.putString("ultimate", ultimate);
        args.putString("primaryDesc", primaryDesc);
        args.putString("secondary1Desc", secondary1Desc);
        args.putString("secondary2Desc", secondary2Desc);
        args.putString("secondary3Desc", secondary3Desc);
        args.putString("ultDesc", ultDesc);
        args.putString("primaryPic", primaryPic);
        args.putString("secondary1Pic", secondary1Pic);
        args.putString("secondary2Pic", secondary2Pic);
        args.putString("secondary3Pic", secondary3Pic);
        args.putString("ultimatePic", ultimatePic);
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
        traits = getArguments().getString("traits");
        primary = getArguments().getString("primary");
        secondary1 = getArguments().getString("secondary1");
        secondary2 = getArguments().getString("secondary2");
        secondary3 = getArguments().getString("secondary3");
        ultimate = getArguments().getString("ultimate");
        primaryDesc = getArguments().getString("primaryDesc");
        secondary1Desc = getArguments().getString("secondary1Desc");
        secondary2Desc = getArguments().getString("secondary2Desc");
        secondary3Desc = getArguments().getString("secondary3Desc");
        ultDesc = getArguments().getString("ultDesc");
        primaryPic = getArguments().getString("primaryPic");
        secondary1Pic = getArguments().getString("secondary1Pic");
        secondary2Pic = getArguments().getString("secondary2Pic");
        secondary3Pic = getArguments().getString("secondary3Pic");
        ultimatePic = getArguments().getString("ultimatePic");
    }

    private View.OnClickListener mButtonClickListener1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle package2 = new Bundle();
            package2.putString("skillpic", secondary1Pic);
            package2.putString("skillname", secondary1);
            package2.putString("skillDesc", secondary1Desc);
            i.putExtras(package2);
            startActivity(i);
        }
    };

    private View.OnClickListener mButtonClickListener2 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle package2 = new Bundle();
            package2.putString("skillpic", secondary2Pic);
            package2.putString("skillname", secondary2);
            package2.putString("skillDesc", secondary2Desc);
            i.putExtras(package2);
            startActivity(i);
        }
    };

    private View.OnClickListener mButtonClickListener3 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle package2 = new Bundle();
            package2.putString("skillpic", secondary3Pic);
            package2.putString("skillname", secondary3);
            package2.putString("skillDesc", secondary3Desc);
            i.putExtras(package2);
            startActivity(i);
        }
    };


    private View.OnClickListener mButtonClickListener4 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle package2 = new Bundle();
            package2.putString("skillpic", ultimatePic);
            package2.putString("skillname", ultimate);
            package2.putString("skillDesc", ultDesc);
            i.putExtras(package2);
            startActivity(i);
        }
    };

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
        ImageView secSkill1 = (ImageView) view.findViewById(R.id.skillOne);
        ImageView secSkill2 = (ImageView) view.findViewById(R.id.skillTwo);
        ImageView secSkill3 = (ImageView) view.findViewById(R.id.skillThree);
        ImageView ultimate = (ImageView) view.findViewById(R.id.ultimate);
        secSkill1.setOnClickListener(mButtonClickListener1);
        secSkill2.setOnClickListener(mButtonClickListener2);
        secSkill3.setOnClickListener(mButtonClickListener3);
        ultimate.setOnClickListener(mButtonClickListener4);
        nameView.setTextSize(30);
        nameView.setText(name);
        Glide.with(this).load(picURL).into(heroImage);
        Glide.with(this).load(secondary1Pic).into(secSkill1);
        Glide.with(this).load(secondary2Pic).into(secSkill2);
        Glide.with(this).load(secondary3Pic).into(secSkill3);
        Glide.with(this).load(ultimatePic).into(ultimate);
        HeroData heroData = new HeroData();
        description.setTextSize(18);
        String scaling = "\u2022" + "This hero's primary attack type is " + scale.toLowerCase() + ".\n";
        String difficultyRating = "\u2022" + "This hero's difficulty is rated at a " + difficulty + " out of 3"+".\n";
        String traitDesc = "\u2022" + "This hero is best described as: " + traits + "\n";
        description.append(scaling);
        description.append("\n");
        description.append(difficultyRating);
        description.append("\n");
        description.append(traitDesc);
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;



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
                Glide.with(this).load(R.drawable.corruption_affinity_icon).into(aff1);
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
                Glide.with(this).load(R.drawable.corruption_affinity_icon).into(aff2);
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
