package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CardFragment extends Fragment {

    ArrayList<WonderModel> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Wonders[];
    String Images[];
    String SkillImages[];
    HashMap map;
    int mStackLevel = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = (HashMap) getArguments().getSerializable("heroes");
        //Wonders = getArguments().getStringArray("heronames");
        //Images = getArguments().getStringArray("heropics");

        initializeList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<WonderModel> list;



        public MyAdapter(ArrayList<WonderModel> Data) {
            list = Data;
            Collections.sort(list, new WonderComparator());
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

            holder.titleTextView.setText(list.get(position).getCardName());
            Glide.with(getActivity()).load(list.get(position).getImageURL()).into(holder.coverImageView);
            holder.shareImageView.setImageResource(list.get(position).getAffinity1());
            holder.likeImageView.setImageResource(list.get(position).getAffinity2());
            if(list.get(position).getSkill1pic() != null) {
                Glide.with(getActivity()).load(list.get(position).getSkill1pic()).into(holder.skill1view);
            }
            else{
                Glide.with(getActivity()).load(R.drawable.error_icons).into(holder.skill1view);
            }
            if(list.get(position).getSkill2pic() != null) {
                Glide.with(getActivity()).load(list.get(position).getSkill2pic()).into(holder.skill2view);
            }
            else{
                Glide.with(getActivity()).load(R.drawable.error_icons).into(holder.skill2view);
            }
            if(list.get(position).getSkill3pic() != null){
                Glide.with(getActivity()).load(list.get(position).getSkill3pic()).into(holder.skill3view);
            }
            else{
                Glide.with(getActivity()).load(R.drawable.error_icons).into(holder.skill3view);
            }
            if(list.get(position).getSkill4pic() != null) {
                Glide.with(getActivity()).load(list.get(position).getSkill4pic()).into(holder.skill4view);
            }
            else{
                Glide.with(getActivity()).load(R.drawable.paragon_white).into(holder.skill4view);
            }

            holder.coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newHeroFragment nextFrag= new newHeroFragment();
                    Bundle bundle = new Bundle();

                    bundle.putInt("difficulty", list.get(position).getDifficulty());
                    bundle.putInt("physPower", list.get(position).getPhysicalPower());
                    bundle.putInt("abPower", list.get(position).getAbilitypower());
                    bundle.putInt("mobility", list.get(position).getMobility());
                    bundle.putInt("durability", list.get(position).getDurability());
                    bundle.putString("imageurl", list.get(position).getImageURL());
                    bundle.putString("name", list.get(position).getCardName());
                    DialogFragment newFragment = newHeroFragment.newInstance(mStackLevel,list.get(position).getDifficulty(),list.get(position).getPhysicalPower(),list.get(position).getAbilitypower(),list.get(position).getMobility(),list.get(position).getDurability(),list.get(position).getImageURL(),list.get(position).getCardName());
                    newFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    // Create and show the dialog.
                    newFragment.show(ft, "dialog");

                }
            });

            holder.skill1view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 if(list.get(position).getSkill1name() != null) {
                     Intent i = new Intent(getActivity(), SkillDisplay.class);
                     Bundle pck = new Bundle();
                     pck.putString("skillpic", list.get(position).getSkill1pic());
                     pck.putString("skillname", list.get(position).getSkill1name());
                     pck.putString("skillDesc", list.get(position).getSkill1desc());
                     i.putExtras(pck);
                     startActivity(i);
                 }
                }
            });

            holder.skill2view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(position).getSkill2name() != null){
                        Intent i = new Intent(getActivity(),SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", list.get(position).getSkill2pic());
                        pck.putString("skillname", list.get(position).getSkill2name());
                        pck.putString("skillDesc", list.get(position).getSkill2desc());
                        i.putExtras(pck);
                        startActivity(i);
                    }

                }
            });

            holder.skill3view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(position).getSkill3name() != null){
                        Intent i = new Intent(getActivity(),SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", list.get(position).getSkill3pic());
                        pck.putString("skillname", list.get(position).getSkill3name());
                        pck.putString("skillDesc", list.get(position).getSkill3desc());
                        i.putExtras(pck);
                        startActivity(i);
                    }

                }
            });

            holder.skill4view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(position).getSkill4name() != null) {
                        Intent i = new Intent(getActivity(), SkillDisplay.class);
                        Bundle pck = new Bundle();
                        pck.putString("skillpic", list.get(position).getSkill4pic());
                        pck.putString("skillname", list.get(position).getSkill4name());
                        pck.putString("skillDesc", list.get(position).getSkill4desc());
                        i.putExtras(pck);
                        startActivity(i);
                    }
                }
            });


        }

       @Override
        public int getItemCount() {
            return list.size();
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

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.affinity1ImageView);
            shareImageView = (ImageView) v.findViewById(R.id.affinity2ImageView);
            skill1view = (ImageView) v.findViewById(R.id.skill1View);
            skill2view = (ImageView) v.findViewById(R.id.skill2View);
            skill3view = (ImageView) v.findViewById(R.id.skill3View);
            skill4view = (ImageView) v.findViewById(R.id.skill4View);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  /*  int id = (int)likeImageView.getTag();
                        if( id == R.drawable.ic_like){

                            likeImageView.setTag(R.drawable.ic_liked);
                            likeImageView.setImageResource(R.drawable.ic_liked);

                            Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();

                        }else{

                            likeImageView.setTag(R.drawable.ic_like);
                            likeImageView.setImageResource(R.drawable.ic_like);
                            Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();


                        }*/

                }
            });



            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                    /*Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
*/


                }
            });


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

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            WonderModel item = new WonderModel();
            ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
            Map.Entry pair = (Map.Entry)it.next();
            item.setCardName(pair.getKey().toString());
            HeroData singledata = (HeroData) pair.getValue();
            item.setDifficulty(singledata.getDifficulty());
            item.setMobility(singledata.getMobility());
            item.setDurability(singledata.getDurability());
            item.setPhysicalPower(singledata.getBasicAttack());
            item.setAbilitypower(singledata.getAbilityAttack());
            item.setSkill1pic(singledata.getPrimarySkill().getImageURL());
            item.setSkill1name(singledata.getPrimarySkill().getName());
            String skillDesc = replacer.replaceStatWithText(singledata.getPrimarySkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(singledata.getPrimarySkill().getModifiers(),skillDesc);
            item.setSkill1desc(skillDesc);
            item.setSkill2pic(singledata.getSecondarySkill().getImageURL());
            item.setSkill2name(singledata.getSecondarySkill().getName());
            skillDesc = replacer.replaceStatWithText(singledata.getSecondarySkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(singledata.getSecondarySkill().getModifiers(),skillDesc);
            item.setSkill2desc(skillDesc);
            item.setSkill3pic(singledata.getAlternateSkill().getImageURL());
            item.setSkill3name(singledata.getAlternateSkill().getName());
            skillDesc = replacer.replaceStatWithText(singledata.getAlternateSkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(singledata.getAlternateSkill().getModifiers(),skillDesc);
            item.setSkill3desc(skillDesc);
            item.setSkill4pic(singledata.getUltimateSkill().getImageURL());
            item.setSkill4name(singledata.getUltimateSkill().getName());
            skillDesc = replacer.replaceStatWithText(singledata.getUltimateSkill().getDesc());
            skillDesc = replacer.replaceModifiersWithText(singledata.getUltimateSkill().getModifiers(),skillDesc);
            item.setSkill4desc(skillDesc);

            item.setImageURL(singledata.getImageIconURL());

           /* String affinity1 = singledata.getAffinity1();
            String affinity2 = singledata.getAffinity2();
            switch (affinity1.toLowerCase()){
                case "order" : item.setAffinity1(R.drawable.order_affinity_icon);
                    break;
                case "fury"  : item.setAffinity1(R.drawable.fury_affinity_icon);
                    break;
                case "corruption" : item.setAffinity1(R.drawable.corruption_affinity_icon);
                    break;
                case "intellect"  : item.setAffinity1(R.drawable.intellect_affinity_icon);
                    break;
                case "growth" : item.setAffinity1(R.drawable.growth_affinity_icon);
                    break;
            }

            switch (affinity2.toLowerCase()){
                case "order" : item.setAffinity2(R.drawable.order_affinity_icon);
                    break;
                case "fury"  : item.setAffinity2(R.drawable.fury_affinity_icon);
                    break;
                case "corruption" : item.setAffinity2(R.drawable.corruption_affinity_icon);
                    break;
                case "intellect"  : item.setAffinity2(R.drawable.intellect_affinity_icon);
                    break;
                case "growth" : item.setAffinity2(R.drawable.growth_affinity_icon);
                    break;
            }
            //it.remove(); // avoids a ConcurrentModificationException*/
            listitems.add(item);
        }
    }
}
