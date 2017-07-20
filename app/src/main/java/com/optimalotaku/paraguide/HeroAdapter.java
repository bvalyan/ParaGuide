package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bvalyan on 7/19/17.
 */

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ImageViewHolder> {

    private final AnimalItemClickListener animalItemClickListener;
    private ArrayList<WonderModel> list;


    public HeroAdapter(ArrayList<WonderModel> Data, AnimalItemClickListener animalItemClickListener) {
        this.animalItemClickListener = animalItemClickListener;
        list = Data;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listsingle, parent, false));
    }


    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final HeroData herodata = new HeroData();


        herodata.setAbilityAttack(list.get(position).getAbilitypower());
        herodata.setBasicAttack(list.get(position).getPhysicalPower());
        herodata.setDurability(list.get(position).getDurability());
        herodata.setMobility(list.get(position).getMobility());
        herodata.setDifficulty(list.get(position).getDifficulty());


        holder.titleTextView.setText(list.get(position).getCardName());
        //picURL = list.get(position).getImageURL();
        Picasso.with(holder.itemView.getContext()).load(list.get(position).getImageURL()).into(holder.coverImageView);
        //holder.shareImageView.setImageResource(list.get(position).getAffinity1());
        //holder.likeImageView.setImageResource(list.get(position).getAffinity2());
        Glide.with(holder.itemView.getContext()).load(list.get(position).getSkill1pic()).into(holder.skill1view);
        Glide.with(holder.itemView.getContext()).load(list.get(position).getSkill2pic()).into(holder.skill2view);
        Glide.with(holder.itemView.getContext()).load(list.get(position).getSkill3pic()).into(holder.skill3view);
        Glide.with(holder.itemView.getContext()).load(list.get(position).getSkill4pic()).into(holder.skill4view);
        ViewCompat.setTransitionName(holder.coverImageView, list.get(position).getCardName());

        holder.coverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalItemClickListener.onAnimalItemClick(holder.getAdapterPosition(), herodata, holder.coverImageView);
            }
        });

        holder.skill1view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SkillDisplay.class);
                Bundle pck = new Bundle();
                pck.putString("skillpic", list.get(position).getSkill1pic());
                pck.putString("skillname", list.get(position).getSkill1name());
                pck.putString("skillDesc", list.get(position).getSkill1desc());
                i.putExtras(pck);
                v.getContext().startActivity(i);
            }
        });

        holder.skill2view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SkillDisplay.class);
                Bundle pck = new Bundle();
                pck.putString("skillpic", list.get(position).getSkill2pic());
                pck.putString("skillname", list.get(position).getSkill2name());
                pck.putString("skillDesc", list.get(position).getSkill2desc());
                i.putExtras(pck);
                v.getContext().startActivity(i);
            }
        });

        holder.skill3view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SkillDisplay.class);
                Bundle pck = new Bundle();
                pck.putString("skillpic", list.get(position).getSkill3pic());
                pck.putString("skillname", list.get(position).getSkill3name());
                pck.putString("skillDesc", list.get(position).getSkill3desc());
                i.putExtras(pck);
                v.getContext().startActivity(i);
            }
        });

        holder.skill4view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SkillDisplay.class);
                Bundle pck = new Bundle();
                pck.putString("skillpic", list.get(position).getSkill4pic());
                pck.putString("skillname", list.get(position).getSkill4name());
                pck.putString("skillDesc", list.get(position).getSkill4desc());
                i.putExtras(pck);
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public ImageView skill1view;
        public ImageView skill2view;
        public ImageView skill3view;
        public ImageView skill4view;

        public ImageViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) itemView.findViewById(R.id.affinity1ImageView);
            shareImageView = (ImageView) itemView.findViewById(R.id.affinity2ImageView);
            skill1view = (ImageView) itemView.findViewById(R.id.skill1View);
            skill2view = (ImageView) itemView.findViewById(R.id.skill2View);
            skill3view = (ImageView) itemView.findViewById(R.id.skill3View);
            skill4view = (ImageView) itemView.findViewById(R.id.skill4View);
        }
    }
}

