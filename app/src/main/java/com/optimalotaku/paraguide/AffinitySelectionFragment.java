package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;

/**
 * Created by bvalyan on 12/7/17.
 */

public class AffinitySelectionFragment extends Fragment{

    static ArrayList<ItemObject> itemObjects;

    public static AffinitySelectionFragment newInstance(ArrayList<ItemObject> cotd) {

        Bundle args = new Bundle();
        itemObjects = cotd;
        AffinitySelectionFragment fragment = new AffinitySelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.affinity_selection_screen, container, false);

        return view;
    }

    @Override
    public void onViewCreated (final View topView, Bundle savedInstanceState) {
        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                topView.findViewById(R.id.affinity_wheel);


        final AffinitySelectorAdapter adapter = new AffinitySelectorAdapter(getContext(), topView, AffinitySelectionFragment.this, itemObjects);
        horizontalInfiniteCycleViewPager.setAdapter(adapter);
        horizontalInfiniteCycleViewPager.notifyDataSetChanged();
        horizontalInfiniteCycleViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                adapter.pageChanged(horizontalInfiniteCycleViewPager.getRealItem());
            }
        });
    }
}
