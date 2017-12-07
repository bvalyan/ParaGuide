package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Brandon on 1/7/17.
 */

final class MyDeckAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private String affinity;
    private Bitmap cotdBitMapImg;
    // Size in bytes (10 MB)
    private static final long PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 10;

    public MyDeckAdapter(Context context, CardData[] cardList, String affinity, String oldName) {

        this.affinity = affinity;
        mInflater = LayoutInflater.from(context);

        for(int i = 0; i < cardList.length; i++){
            Item card = new Item(cardList[i].getName(), cardList[i].getImageUrl(), cardList[i].getId());
            if(cardList[i].getAffinity().toLowerCase().equals(affinity.toLowerCase()) || cardList[i].getAffinity().toLowerCase().equals(oldName.toLowerCase())){
                mItems.add(card);
            }
        }

        Collections.sort(mItems, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int result;
                result = o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
                if ( result < 0){
                    return -1;
                }
                else if(result == 0){
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });
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
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;



        int maxSize = 150;


        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.findViewById(R.id.picture);
        name = (TextView) v.findViewById(R.id.text);

        Item item = getItem(i);



        // Use OkHttp as downloader
        Downloader downloader = new OkHttpDownloader(v.getContext(),
                Integer.MAX_VALUE);



        Cache memoryCache = new LruCache(maxSize);

        Picasso picasso = new Picasso.Builder(v.getContext())
                .downloader(downloader).memoryCache(memoryCache).build();

        //picasso.with(v.getContext()).setIndicatorsEnabled(true);

        Glide.with(v.getContext()).load(item.drawable1)
                .apply(RequestOptions
                        .fitCenterTransform()
                        .placeholderOf(R.drawable.icon))
                .into(picture);
        name.setText(item.name);

        return v;
    }

    private static class Item {
        public final String name;
        public final String drawable1;
        public final String id;

        Item(String name, String drawable, String id) {
            this.name = name;
            this.drawable1 = drawable;
            this.id = id;
        }


    }
}