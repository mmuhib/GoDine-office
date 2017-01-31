package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.models.RestaurantType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sony on 09-12-2016.
 */

public class RestTypeAdapter extends BaseAdapter {

    ArrayList<RestaurantType> listRestaurant;

    LayoutInflater inflater;
    public RestTypeAdapter(Context context, ArrayList<RestaurantType> listRestaurant) {
        this.listRestaurant=listRestaurant;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listRestaurant.size();
    }

    @Override
    public Object getItem(int position) {
        return listRestaurant.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.simple_button_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        RestaurantType restaurantType = listRestaurant.get(position);
        String restType =  restaurantType.getRestType().trim();
        holder.tvTitle.setText(restType);

        return convertView;
    }
    private class  ViewHolder
    {
        TextView tvTitle;
        ViewHolder(View v)
        {
            tvTitle  = (TextView) v.findViewById(R.id.tvTitle) ;
        }
    }
}
