package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;


public class CardFragment extends Fragment {

    ArrayList<AbilityObject> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Wonders[];
    String Images[];
    String SkillImages[];
    ArrayList<ChampionData> map;
    int mStackLevel = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = (ArrayList<ChampionData>) getArguments().getSerializable("heroes");
        //Wonders = getArguments().getStringArray("heronames");
        //Images = getArguments().getStringArray("heropics");

        initializeList();
    } 

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (map.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(map));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<AbilityObject> list;



        public MyAdapter(ArrayList<ChampionData> Data) {
            map = Data;
            //Collections.sort(list, new WonderComparator());
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listsingle, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.titleTextView.setText(map.get(position).getName());
            Picasso.with(getActivity()).load(map.get(position).getChampIconURL()).into(holder.coverImageView);
            if(map.get(position).getAbility1().imageURL!= null) {
                Picasso.with(getActivity()).load(map.get(position).getAbility1().getImageURL()).into(holder.skill1view);
            }
            else{
                Picasso.with(getActivity()).load(R.drawable.error_icons).into(holder.skill1view);
            }
            if(map.get(position).getAbility2().imageURL != null) {
                Picasso.with(getActivity()).load(map.get(position).getAbility2().getImageURL()).into(holder.skill2view);
            }
            else{
                Picasso.with(getActivity()).load(R.drawable.error_icons).into(holder.skill2view);
            }
            if(map.get(position).getAbility3().imageURL != null){
                Picasso.with(getActivity()).load(map.get(position).getAbility3().imageURL ).into(holder.skill3view);
            }
            else{
                Picasso.with(getActivity()).load(R.drawable.error_icons).into(holder.skill3view);
            }
            if(map.get(position).getAbility4().imageURL != null) {
                Picasso.with(getActivity()).load(map.get(position).getAbility4().imageURL).into(holder.skill4view);
            }
            else{
                Picasso.with(getActivity()).load(R.drawable.error_icons).into(holder.skill4view);
            }
            if(map.get(position).getAbility5().imageURL != null) {
                Picasso.with(getActivity()).load(map.get(position).getAbility5().imageURL).into(holder.skill5view);
            }
            else{
                Picasso.with(getActivity()).load(R.drawable.error_icons).into(holder.skill5view);
            }

            holder.coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newChampionFragment nextFrag= new newChampionFragment();
                    Bundle bundle = new Bundle();

                    /*bundle.putInt("difficulty", list.get(position).getDifficulty());
                    bundle.putInt("physPower", list.get(position).getPhysicalPower());
                    bundle.putInt("abPower", list.get(position).getAbilitypower());
                    bundle.putInt("mobility", list.get(position).getMobility());
                    bundle.putInt("durability", list.get(position).getDurability());
                    bundle.putString("imageurl", list.get(position).getImageURL());
                    bundle.putString("name", list.get(position).getCardName());
                    DialogFragment newFragment = newChampionFragment.newInstance(mStackLevel,list.get(position).getDifficulty(),list.get(position).getPhysicalPower(),list.get(position).getAbilitypower(),list.get(position).getMobility(),list.get(position).getDurability(),list.get(position).getImageURL(),list.get(position).getCardName());
                    newFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    // Create and show the dialog.
                    newFragment.show(ft, "dialog");*/

                }
            });

            holder.skill1view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 if(map.get(position).getAbility1().imageURL != null) {
                     Intent i = new Intent(getActivity(), SkillDisplay.class);
                     Bundle pck = new Bundle();
                     pck.putString("skillpic", map.get(position).getAbility1().getImageURL());
                     pck.putString("skillname", map.get(position).getAbility1().getName());
                     pck.putString("skillDesc", map.get(position).getAbility1().getDescription());
                     i.putExtras(pck);
                     startActivity(i);
                 }
                }
            });

            holder.skill2view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(map.get(position).getAbility2().getImageURL() != null){
                        Intent i = new Intent(getActivity(),SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", map.get(position).getAbility2().getImageURL());
                        pck.putString("skillname", map.get(position).getAbility2().getName());
                        pck.putString("skillDesc", map.get(position).getAbility2().getDescription());
                        i.putExtras(pck);
                        startActivity(i);
                    }

                }
            });

            holder.skill3view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(map.get(position).getAbility3().getImageURL() != null){
                        Intent i = new Intent(getActivity(),SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", map.get(position).getAbility3().getImageURL());
                        pck.putString("skillname", map.get(position).getAbility3().getName());
                        pck.putString("skillDesc", map.get(position).getAbility3().getDescription());
                        i.putExtras(pck);
                        startActivity(i);
                    }

                }
            });

            holder.skill4view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(map.get(position).getAbility4().getImageURL() != null) {
                        Intent i = new Intent(getActivity(), SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", map.get(position).getAbility4().getImageURL());
                        pck.putString("skillname", map.get(position).getAbility4().getName());
                        pck.putString("skillDesc", map.get(position).getAbility4().description);
                        i.putExtras(pck);
                        startActivity(i);
                    }
                }
            });
            holder.skill5view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(map.get(position).getAbility5().getImageURL() != null) {
                        Intent i = new Intent(getActivity(), SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", map.get(position).getAbility5().getImageURL());
                        pck.putString("skillname", map.get(position).getAbility5().getName());
                        pck.putString("skillDesc", map.get(position).getAbility5().description);
                        i.putExtras(pck);
                        startActivity(i);
                    }
                }
            });


        }

       @Override
        public int getItemCount() {
            return map.size();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public ImageView skill1view;
        public ImageView skill2view;
        public ImageView skill3view;
        public ImageView skill4view;
        public ImageView skill5view;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            coverImageView = v.findViewById(R.id.coverImageView);
            skill1view = v.findViewById(R.id.skill1View);
            skill2view = v.findViewById(R.id.skill2View);
            skill3view = v.findViewById(R.id.skill3View);
            skill4view = v.findViewById(R.id.skill4View);
            skill5view = v.findViewById(R.id.skill5View);
            }


    }

    class WonderComparator implements Comparator<WonderModel>{
            @Override
            public int compare(WonderModel a, WonderModel b) {
                return a.getCardName().compareToIgnoreCase(b.getCardName());
            }
    }


    public void initializeList() {
        listitems.clear();


        for(int i = 0; i < map.size(); i++) {
            WonderModel item = new WonderModel();
            item.setCardName(map.get(i).getName());
            ChampionData singledata = map.get(i);
            //item.setDifficulty(singledata.getDifficulty());
            //item.setMobility(singledata.getMobility());
            //item.setDurability(singledata.getDurability());
            //item.setPhysicalPower(singledata.getBasicAttack());
            //item.setAbilitypower(singledata.getAbilityAttack());
            item.setSkill1pic(singledata.getAbility1().getImageURL());
            item.setSkill1name(singledata.getAbility1().getName());
            String skillDesc = singledata.getAbility1().getDescription();
            item.setSkill1desc(skillDesc);
            item.setSkill2pic(singledata.getAbility2().getImageURL());
            item.setSkill2name(singledata.getAbility2().getName());
            skillDesc = singledata.getAbility2().getDescription();
            item.setSkill2desc(skillDesc);
            item.setSkill3pic(singledata.getAbility3().getImageURL());
            item.setSkill3name(singledata.getAbility3().getName());
            skillDesc = singledata.getAbility3().getDescription();
            item.setSkill3desc(skillDesc);
            item.setSkill4pic(singledata.getAbility4().getImageURL());
            item.setSkill4name(singledata.getAbility4().getName());
            skillDesc = singledata.getAbility4().getDescription();
            item.setSkill4desc(skillDesc);

            item.setImageURL(singledata.getChampIconURL());



        }
    }
}
