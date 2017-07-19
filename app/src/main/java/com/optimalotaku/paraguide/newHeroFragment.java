package com.optimalotaku.paraguide;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bvalyan on 7/18/17.
 */

public class newHeroFragment extends android.support.v4.app.Fragment{

    ArrayList<WonderModel> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Wonders[];
    String Images[];
    String SkillImages[];
    HashMap map;
    int difficulty;
    int abilityPower;
    int physicalPower;
    int durability;
    int mobility;
    String name;
    String imageURL;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        difficulty = bundle.getInt("difficulty");
        abilityPower = bundle.getInt("abPower");
        physicalPower = bundle.getInt("physPower");
        durability = bundle.getInt("durability");
        mobility = bundle.getInt("mobility");
        imageURL = bundle.getString("imageurl");
        name = bundle.getString("name");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_hero_screen, container, false);
        ProgressBar difficultyBar = (ProgressBar) view.findViewById(R.id.difficulty_bar);
        ProgressBar mobilityBar = (ProgressBar) view.findViewById(R.id.mobility_bar);
        ProgressBar durabilityBar = (ProgressBar) view.findViewById(R.id.durability_bar);
        ProgressBar physicalBar = (ProgressBar) view.findViewById(R.id.physical_power_bar);
        ProgressBar abilityBar = (ProgressBar) view.findViewById(R.id.ability_power_bar);
        TextView name = (TextView) view.findViewById(R.id.hero_name);
        ImageView coverView = (ImageView) view.findViewById(R.id.hero_portrait);

        name.setText(this.name);
        difficultyBar.setProgress(difficulty*25, true);
        mobilityBar.setProgress(mobility*10, true);
        durabilityBar.setProgress(durability*10, true);
        physicalBar.setProgress(physicalPower*10, true);
        abilityBar.setProgress(abilityPower*10, true);
        Glide.with(getContext()).load(imageURL).into(coverView);

        return view;
    }
}