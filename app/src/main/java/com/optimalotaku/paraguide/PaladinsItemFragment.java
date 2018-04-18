package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by bvalyan on 12/7/17.
 */

public class PaladinsItemFragment extends Fragment{

    static ArrayList<ItemObject> itemObjects;
    RecyclerView itemRecyclerView;

    public static PaladinsItemFragment newInstance(ArrayList<ItemObject> cotd) {

        Bundle args = new Bundle();
        itemObjects = cotd;
        PaladinsItemFragment fragment = new PaladinsItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.paladins_item_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated (final View topView, Bundle savedInstanceState) {
        itemRecyclerView = topView.findViewById(R.id.item_recycler);
        ItemAdapter adapter = new ItemAdapter(itemObjects);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(topView.getContext()));
        itemRecyclerView.setAdapter(adapter);
    }
}
