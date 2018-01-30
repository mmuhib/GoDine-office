package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;


import com.netreadystaging.godine.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.netreadystaging.godine.models.Restaurant;

/**
 * Created by sony on 09-12-2016.
 */

public class SearchRestaurantAdapter extends ArrayAdapter<Restaurant> {
    Context context;
    ArrayList<Restaurant> listRestaurant;
    LayoutInflater inflater;
    int Resource;
    public SearchRestaurantAdapter(Context context, int resource, ArrayList<Restaurant> listRestaurant) {
        super(context, resource, listRestaurant);
        this.context=context;
        this.listRestaurant=listRestaurant;
        this.Resource=resource;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        ViewHolder holder=null;
        if(row==null)
        {
            row=inflater.inflate(R.layout.restaurant_search_list,parent,false);
            holder=new ViewHolder(row); row.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) row.getTag();
        }
        Restaurant restaurantModel = listRestaurant.get(position);
        String restaurantImageURL =  restaurantModel.getImage().trim();
        if(!restaurantImageURL.isEmpty()) {
            if(!restaurantImageURL.contains("http://") && !restaurantImageURL.contains("https://"))
            {
                restaurantImageURL = "https://"+restaurantImageURL;
            }
            Picasso.with(getContext()).load(restaurantImageURL).fit().into(holder.imageresataurant);
        }
        else
        {
            Picasso.with(getContext()).load(R.mipmap.ic_launcher).fit().into(holder.imageresataurant);

        }

        holder.idd.setText(restaurantModel.getId());
        holder.restautname.setText(restaurantModel.getName());
        holder.restaurantaddress.setText(restaurantModel.getAddress());
        holder.region.setText(restaurantModel.getArea());
        holder.review.setText(restaurantModel.getReview()+" Review");
        holder.cuisine.setText(restaurantModel.getRestaurantCusine());
        holder.lunch.setText("Lunch:$"+restaurantModel.getLunch());
        holder.dinner.setText("Dinner:$"+restaurantModel.getDinner());
        holder.distance.setText(restaurantModel.getMiles()+" miles");
        holder.ratingBar.setRating(restaurantModel.getRating());
        String type=restaurantModel.getResttype().toString();
        if(type.equalsIgnoreCase("Affiliate Restaurant"))
        {
            holder.txt_rstype.setText("Partner");
            holder.txt_restoffer.setText("2 for 1");

        }
        else if(type.equalsIgnoreCase("Restaurant"))
        {
            holder.txt_rstype.setText("Premium Partner");
            holder.txt_restoffer.setText("50% OFF");
        }

        if(restaurantModel.getOffers()>0)
            holder.restOfferImage.setVisibility(View.VISIBLE);
        else
            holder.restOfferImage.setVisibility(View.GONE);
        String name=context.getClass().getName().toString();

        Log.d("Name",name);
        if(name.equalsIgnoreCase("com.netreadystaging.godine.activities.main.NewSignUp"))
        {
            holder.lloffer.setVisibility(View.GONE);
            holder.lunch.setVisibility(View.GONE);
            holder.dinner.setVisibility(View.GONE);
            holder.cuisine.setVisibility(View.GONE);
            holder.txt_avgprice.setVisibility(View.GONE);
        }
        return row;
    }
    class  ViewHolder
    {
        ImageView imageresataurant,restOfferImage;
        TextView restautname,restaurantaddress,region,review,cuisine,lunch,dinner,distance,idd,txt_avgprice,txt_rstype,txt_restoffer;
        AppCompatRatingBar ratingBar ;
        LinearLayout lloffer;
        ViewHolder(View v)
        {
            imageresataurant= (ImageView) v.findViewById(R.id.restaurantImage);
            idd= (TextView) v.findViewById(R.id.idd);
            restautname= (TextView) v.findViewById(R.id.restautname);
            restaurantaddress= (TextView) v.findViewById(R.id.restaurantaddress);
            region= (TextView) v.findViewById(R.id.region);
            txt_avgprice= (TextView) v.findViewById(R.id.txt_avgprice);
            review= (TextView) v.findViewById(R.id.review);
            cuisine= (TextView) v.findViewById(R.id.cuisine);
            lunch= (TextView) v.findViewById(R.id.lunch);
            dinner= (TextView) v.findViewById(R.id.dinner);
            distance= (TextView) v.findViewById(R.id.distance);
            ratingBar= (AppCompatRatingBar) v.findViewById(R.id.ratingBar);
            restOfferImage= (ImageView) v.findViewById(R.id.restOfferImage);
           lloffer= (LinearLayout) v.findViewById(R.id.ll_offer);
            txt_rstype=(TextView) v.findViewById(R.id.txt_rs);
            txt_restoffer=(TextView) v.findViewById(R.id.rest_offer);

            Drawable drawable = ratingBar.getProgressDrawable();
            //drawable.setColorFilter(Color.parseColor("#FF3399FF"), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
