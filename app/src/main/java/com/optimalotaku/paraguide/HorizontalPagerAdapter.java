package com.optimalotaku.paraguide;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bvaly on 11/9/2017.
 */

public class HorizontalPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ChampionData> mData;
    private ArrayList<ChampionData> list;
    View mLayout;
    final ImageView background;

    public HorizontalPagerAdapter(final Context context, ArrayList<ChampionData> data, View mainLayout) {
        mContext = context;
        mLayout = mainLayout;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        list = mData;
        background = (ImageView) mLayout.findViewById(R.id.background);
        Picasso.with(mContext).load(list.get(0).getChampIconURL()).into(background);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        view = mLayoutInflater.inflate(R.layout.hero_page_layout, container, false);
        TextView heroNameView = (TextView) view.findViewById(R.id.hero_name);
        ImageView heroImageView = (ImageView) view.findViewById(R.id.hero_image);
        heroNameView.setText(list.get(position).getName());
        heroNameView.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(list.get(position).getChampIconURL()).into(heroImageView);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
    public void pageChanged(int position) {


        Picasso.with(mContext).load(list.get(position).getChampIconURL()).into(background);
    }



}
