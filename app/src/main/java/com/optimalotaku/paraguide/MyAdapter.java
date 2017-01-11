package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 1/7/17.
 */

final class MyAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private Bitmap cotdBitMapImg;

    public MyAdapter(Context context, Bitmap cotdBitMapImg) {
        mInflater = LayoutInflater.from(context);

        mItems.add(new Item("Player Lookup",       R.drawable.playerlookup));
        mItems.add(new Item("Deck Lookup",   R.drawable.decklookup));
        mItems.add(new Item("Competitive Sphere", R.drawable.competitivepic));
        mItems.add(new Item("Hero Data",      R.drawable.heropic));
        mItems.add(new Item("Card of the Day", R.drawable.cotdpic));
        mItems.add(new Item("News & Information", R.drawable.newspic));
        this.cotdBitMapImg = cotdBitMapImg;
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
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        if(item.drawableId == R.drawable.cotdpic){
            picture.setImageBitmap(this.cotdBitMapImg);
        }
        else {
            picture.setImageResource(item.drawableId);
            name.setText(item.name);
        }

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
}