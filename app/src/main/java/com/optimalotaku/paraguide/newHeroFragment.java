package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bvalyan on 7/18/17.
 */

public class newHeroFragment extends DialogFragment{

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
    FABRevealLayout layout;
    String name;
    String imageURL;

    static newHeroFragment newInstance(int num, int difficulty, int physPower, int abPower, int mobility, int durability, String imageURL, String name) {
        newHeroFragment f = new newHeroFragment();


        Bundle bundle = new Bundle();

        bundle.putInt("difficulty", difficulty);
        bundle.putInt("physPower", physPower);
        bundle.putInt("abPower", abPower);
        bundle.putInt("mobility", mobility);
        bundle.putInt("durability", durability);
        bundle.putString("imageurl", imageURL);
        bundle.putString("name", name);
        // Supply num input as an argument.
        Bundle args = new Bundle();
        bundle.putInt("num", num);
        f.setArguments(bundle);

        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        difficulty = getArguments().getInt("difficulty");
        abilityPower = getArguments().getInt("abPower");
        physicalPower = getArguments().getInt("physPower");
        durability = getArguments().getInt("durability");
        mobility = getArguments().getInt("mobility");
        imageURL = getArguments().getString("imageurl");
        name = getArguments().getString("name");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_hero_screen, container, false);
        IconRoundCornerProgressBar difficultyBar = (IconRoundCornerProgressBar) view.findViewById(R.id.difficulty_bar);
        IconRoundCornerProgressBar mobilityBar = (IconRoundCornerProgressBar) view.findViewById(R.id.mobility_bar);
        IconRoundCornerProgressBar durabilityBar = (IconRoundCornerProgressBar) view.findViewById(R.id.durability_bar);
        IconRoundCornerProgressBar physicalBar = (IconRoundCornerProgressBar) view.findViewById(R.id.physical_power_bar);
        IconRoundCornerProgressBar abilityBar = (IconRoundCornerProgressBar) view.findViewById(R.id.ability_power_bar);
        TextView name = (TextView) view.findViewById(R.id.hero_name);
        ImageView coverView = (ImageView) view.findViewById(R.id.hero_portrait);
        layout = (FABRevealLayout) view.findViewById(R.id.fab_reveal_layout);

        name.setText(this.name);
        difficultyBar.setProgress(difficulty*50);
        float  prog = difficultyBar.getProgress();
        float max = difficultyBar.getMax();
        mobilityBar.setProgress(mobility*50);
        durabilityBar.setProgress(durability*50);
        physicalBar.setProgress(physicalPower*50);
        abilityBar.setProgress(abilityPower*50);
        Glide.with(getContext()).load(imageURL).into(coverView);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((19 * width)/20, (19 * height)/20);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        layout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                     return false;
                    }
                });
            }

            @Override
            public void onSecondaryViewAppeared(FABRevealLayout fabRevealLayout, View secondaryView) {
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK ){

                            layout.revealMainView();

                            return true;

                        }

                        return false;
                    }
                });
            }
        });


        super.onResume();
    }

}