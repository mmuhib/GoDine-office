package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.RestaurantMyReview;
import com.netreadystaging.godine.views.RatingCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lenovo on 1/23/2017.
 */

public class RestaurantMyReviewAdapter extends BaseAdapter {

    private final ArrayList<RestaurantMyReview> listRestaurantMyReview;
    private final LayoutInflater inflater;
    private final Context context;
    private int selectedIndex = 0 ;

    public RestaurantMyReviewAdapter(Context context , ArrayList<RestaurantMyReview> listRestaurantMyReview) {
        this.listRestaurantMyReview =  listRestaurantMyReview ;
        inflater  = LayoutInflater.from(context);
        this.context  = context ;
    }

    @Override
    public int getCount() {
        return listRestaurantMyReview.size();
    }

    @Override
    public RestaurantMyReview getItem(int position) {
        return listRestaurantMyReview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        RestaurantMyReviewAdapter.ViewHolder holder = null ;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.rest_my_review_list_item,parent,false);
            holder = new RestaurantMyReviewAdapter.ViewHolder();
            holder.tvMemberName = (TextView) convertView.findViewById(R.id.tvMemberName) ;
            holder.memberRatingCard = (RatingCardView)  convertView.findViewById(R.id.memberRatingCard) ;
            holder.ivRestaurantPicture = (ImageView) convertView.findViewById(R.id.ivRestaurantPicture) ;
            holder.tvMyRestReviewDate = (TextView) convertView.findViewById(R.id.tvMyRestReviewDate);
            holder.tvRestPlaceAddress = (TextView) convertView.findViewById(R.id.tvRestPlaceAddress);
            convertView.setTag(holder);
        }
        else{
            holder = (RestaurantMyReviewAdapter.ViewHolder) convertView.getTag() ;
        }

        RestaurantMyReview restaurantMyReview = listRestaurantMyReview.get(position);
        String restName  = restaurantMyReview.getRestName();
        holder.tvMemberName.setText(restName);
        holder.memberRatingCard.setView(restaurantMyReview.getRatingCard());
        StringBuilder sb = new StringBuilder();
        sb.append(restaurantMyReview.getAddress()).append("\n").append(restaurantMyReview.getCity())
                .append(", ").append(restaurantMyReview.getState()).append(", ").append(restaurantMyReview.getPostalCode());

        holder.tvRestPlaceAddress.setText(sb.toString());
        holder.tvMyRestReviewDate.setText(restaurantMyReview.getReviewDate());
        String restImageUrl  =  restaurantMyReview.getRestImageUrl().trim() ;
        if( !restImageUrl.isEmpty()) {
            if(!restImageUrl.startsWith("http://") && !restImageUrl.startsWith("https://")){
                restImageUrl = "https://"+restImageUrl ;
            }
            Picasso.with(context).load(restImageUrl).into(holder.ivRestaurantPicture);
        }

        final RelativeLayout  restaurantDetailsContainer = (RelativeLayout) convertView.findViewById(R.id.restaurantDetailsContainer);

        if(selectedIndex==position) {
            holder.memberRatingCard.setVisibility(View.VISIBLE);
            convertView.setSelected(true);
        }
        else {
            holder.memberRatingCard.setVisibility(View.GONE);
            convertView.setSelected(false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    v.setSelected(true);
                    v.findViewById(R.id.memberRatingCard).setVisibility(View.VISIBLE);
                    LinearLayout listItem  = (LinearLayout) parent.getChildAt(selectedIndex);
                    if(listItem!=null) {
                        listItem.findViewById(R.id.restaurantDetailsContainer).setSelected(false);
                        listItem.findViewById(R.id.memberRatingCard).setVisibility(View.GONE);
                    }
                    selectedIndex =  position ;
                    notifyDataSetChanged();
                }
            }
        });
                return convertView;
    }

    private static class ViewHolder {
        ImageView ivRestaurantPicture ;
        TextView tvMyRestReviewDate ;
        TextView tvRestPlaceAddress ;
        TextView tvMemberName  ;
        RatingCardView memberRatingCard ;
    }
}
