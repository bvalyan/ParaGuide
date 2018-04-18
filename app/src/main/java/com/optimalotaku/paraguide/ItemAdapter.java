package com.optimalotaku.paraguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bvalyan on 4/18/18.
 */

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    ArrayList<ItemObject> items;

    public ItemAdapter(ArrayList<ItemObject> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.listtrendsinglenew, parent, false);

        // Return a new holder instance
        ItemAdapter.ViewHolder viewHolder = new ItemAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemDescView.setText(items.get(position).getDescription());
        holder.itemName.setText(items.get(position).getName());
        holder.price.setText("PRICE: " + String.valueOf(items.get(position).getPrice()));
        Picasso.with(holder.itemView.getContext()).load(items.get(position).getImageURL()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemDescView;
        public ImageView itemImage;
        public TextView itemName;
        public TextView price;



        public ViewHolder(View itemView) {
            super(itemView);

            setIsRecyclable(false);
            price = itemView.findViewById(R.id.item_price);
            itemDescView = itemView.findViewById(R.id.itemDescView);
            itemImage = itemView.findViewById(R.id.img);
            itemName = itemView.findViewById(R.id.txt);


        }
    }

}
