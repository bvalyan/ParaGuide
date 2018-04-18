package com.optimalotaku.paraguide;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bvalyan on 12/7/17.
 */

public class AffinitySelectorAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    View mLayout;
    final ImageView background;
    AffinityObject[] affinityObjects = new AffinityObject[5];
    ArrayList<ItemObject> items = new ArrayList<>();
    Fragment fragment;

    public AffinitySelectorAdapter(final Context context, View mainLayout, Fragment fragment, ArrayList<ItemObject> items) {
        mContext = context;
        mLayout = mainLayout;
        mLayoutInflater = LayoutInflater.from(context);
        this.fragment = fragment;
        background = mLayout.findViewById(R.id.background);
        this.items = items;
        background.setBackgroundColor(affinityObjects[0].getColor());


    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        view = mLayoutInflater.inflate(R.layout.affinity_card, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CARD TOUCH", "You've selected " + affinityObjects[position].getTitle());
                fragment.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, Cards.newInstance(items, affinityObjects[position].getTitle(), affinityObjects[position].getOldName()))
                        .addToBackStack("NEW")
                        .commit();

            }
        });
        ImageView backgroundLayout = view.findViewById(R.id.background_color_layout);
        backgroundLayout.setBackgroundColor(affinityObjects[position].getColor());
        TextView affinityTitle = view.findViewById(R.id.affinity_title);
        ImageView affinityIcon = view.findViewById(R.id.affinity_icon);
        affinityTitle.setText(affinityObjects[position].getTitle());
        affinityTitle.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(affinityObjects[position].getImage()).into(affinityIcon);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    public void pageChanged(int position) {
        try {
            background.setBackgroundColor(affinityObjects[position].getColor());
        }
        catch (Exception e){

        }
    }
}
