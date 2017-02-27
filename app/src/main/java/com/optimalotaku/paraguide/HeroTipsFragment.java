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
    private HeroData heroData; 


    // newInstance constructor for creating fragment with arguments
    public static HeroTipsFragment newInstance(int page, String title, HeroData hdata) {
        HeroTipsFragment tipsFrag = new HeroTipsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putSerializable("heroData", hdata);
        tipsFrag.setArguments(args);
        return tipsFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        heroData = (HeroData) getArguments().getSerializable("heroData");
    }

    private View.OnClickListener mButtonClickListener1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle pck = new Bundle();
            ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
            pck.putString("skillpic", heroData.getAlternateSkill().getImageURL());
            pck.putString("skillname", heroData.getAlternateSkill().getName());
            String skillDesc = replacer.replaceStatWithText(heroData.getAlternateSkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(heroData.getAlternateSkill().getModifiers(),skillDesc);
            pck.putString("skillDesc", skillDesc);
            i.putExtras(pck);
            startActivity(i);
        }
    };

    private View.OnClickListener mButtonClickListener2 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle pck = new Bundle();
            ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
            pck.putString("skillpic", heroData.getPrimarySkill().getImageURL());
            pck.putString("skillname", heroData.getPrimarySkill().getName());
            String skillDesc = replacer.replaceStatWithText(heroData.getPrimarySkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(heroData.getPrimarySkill().getModifiers(),skillDesc);
            pck.putString("skillDesc", skillDesc);
            i.putExtras(pck);
            startActivity(i);
        }
    };

    private View.OnClickListener mButtonClickListener3 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle pck = new Bundle();
            ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
            pck.putString("skillpic", heroData.getSecondarySkill().getImageURL());
            pck.putString("skillname", heroData.getSecondarySkill().getName());
            String skillDesc = replacer.replaceStatWithText(heroData.getSecondarySkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(heroData.getSecondarySkill().getModifiers(),skillDesc);
            pck.putString("skillDesc", skillDesc);
            i.putExtras(pck);
            startActivity(i);
        }
    };


    private View.OnClickListener mButtonClickListener4 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),SkillDisplay.class);
            Bundle pck = new Bundle();
            ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
            pck.putString("skillpic", heroData.getUltimateSkill().getImageURL());
            pck.putString("skillname", heroData.getUltimateSkill().getName());
            String skillDesc = replacer.replaceStatWithText(heroData.getUltimateSkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(heroData.getUltimateSkill().getModifiers(),skillDesc);
            pck.putString("skillDesc", skillDesc);
            i.putExtras(pck);
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
        nameView.setText(heroData.getName());
        Glide.with(this).load(heroData.getImageIconURL()).into(heroImage);
        Glide.with(this).load(heroData.getAlternateSkill().getImageURL()).into(secSkill1);
        Glide.with(this).load(heroData.getPrimarySkill().getImageURL()).into(secSkill2);
        Glide.with(this).load(heroData.getSecondarySkill().getImageURL()).into(secSkill3);
        Glide.with(this).load(heroData.getUltimateSkill().getImageURL()).into(ultimate);

        description.setTextSize(18);
        String scaling = "\u2022" + "This hero's primary attack type is " + heroData.getAttackType().toLowerCase() + ".\n";
        String difficultyRating = "\u2022" + "This hero's difficulty is rated at a " + heroData.getDifficulty().toString() + " out of 3"+".\n";
        String traitDesc = "\u2022" + "This hero is best described as: " + heroData.getTraits() + "\n";
        description.append(scaling);
        description.append("\n");
        description.append(difficultyRating);
        description.append("\n");
        description.append(traitDesc);
        Bitmap bitmap;
        Bitmap bitmap2;



        switch (heroData.getAffinity1().toLowerCase()){
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


        switch (heroData.getAffinity2().toLowerCase()){
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
