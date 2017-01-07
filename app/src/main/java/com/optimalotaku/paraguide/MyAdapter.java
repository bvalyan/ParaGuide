package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bvaly on 1/6/2017.
 */

    final class MyAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private Context mContext;


    public MyAdapter(Context context) {

        mInflater = LayoutInflater.from(context);

        mItems.add(new Item("Hero Data", R.drawable.heropic));
        mItems.add(new Item("Deck List", R.drawable.deckpic));
        mItems.add(new Item("Player Lookup", R.drawable.lookuppic));
        mItems.add(new Item("Card of the Day", R.drawable.cotdpic));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;
        ImageView picture;
        TextView name;


        if (v == null) {
            v = mInflater.inflate(R.layout.griditem, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));

        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }

    class MyOnClickListener extends AppCompatActivity implements View.OnClickListener {
        private final int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            // Preform a function based on the position
            Intent intent;
            switch (position){
                case 0: intent = new Intent(this, HeroView.class);
                    startActivity(intent);
                    break;
                case 1: intent = new Intent(this, DeckView.class);
                    startActivity(intent);
                    break;
                case 2: ;
                    break;
                case 3: ;
                    break;
            }

        }
    }
}