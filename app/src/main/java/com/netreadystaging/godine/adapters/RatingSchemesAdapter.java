package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itsolutionts.filterhelper.adapter.SingleSelectListAdapter;
import com.netreadystaging.godine.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 1/17/2017.
 */

public class RatingSchemesAdapter extends SingleSelectListAdapter {

    LayoutInflater inflater = null ;
    private int resource ;
    public RatingSchemesAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
        this.resource = resource ;
        inflater = LayoutInflater.from(context) ;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView==null)
        {
            convertView = inflater.inflate(this.resource,parent,false);
            holder = new ViewHolder();
            holder.checkbox = (ImageView) convertView.findViewById(R.id.checkBox) ;
            holder.ratingBar = (AppCompatRatingBar)  convertView.findViewById(R.id.ratingBar) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }
        switch (position)
        {
            case 0 :
                holder.ratingBar.setRating(1.0f);
                break ;

            case 1 :
                holder.ratingBar.setRating(2.0f);
                break ;

            case 2 :
                holder.ratingBar.setRating(3.0f);
                break ;

            case 3 :
                holder.ratingBar.setRating(4.0f);
                break ;

            case 4 :
                holder.ratingBar.setRating(5.0f);
                break ;
        }
        if(selectedIndex== position) holder.checkbox.setSelected(true);
        else holder.checkbox.setSelected(false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView checkbox  = (ImageView) v.findViewById(R.id.checkBox);
                if (!checkbox.isSelected()) {
                    checkbox.setSelected(true);
                    RelativeLayout lastRL = (RelativeLayout) parent.getChildAt(selectedIndex) ;
                    ImageView lastSelectCTV =  (ImageView) lastRL.findViewById(R.id.checkBox);
                    lastSelectCTV.setSelected(false);
                    selectedIndex = position;
                }
            }
        });

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView checkbox  = (ImageView) v ;
                if (!checkbox.isSelected()) {
                    checkbox.setSelected(true);
                    RelativeLayout lastRL = (RelativeLayout) parent.getChildAt(selectedIndex) ;
                    ImageView lastSelectCTV =  (ImageView) lastRL.findViewById(R.id.checkBox);
                    lastSelectCTV.setSelected(false);
                    selectedIndex = position;
                }
            }
        });
        return convertView;
    }

//    @Override
//    public String getSelectedItem() {
//        String rating =  "1 Rating" ;
//        switch(selectedIndex)
//        {
//            case 0 :
//                rating = "1 Star" ;
//                break ;
//            case 1 :
//                rating = "2 Star" ;
//                break ;
//            case 2 :
//                rating = "3 Star" ;
//                break ;
//            case 3 :
//                rating = "4 Star" ;
//                break ;
//            case 4 :
//                rating = "5 Star" ;
//                break ;
//
//        }
//        return rating;
//    }
    private static class ViewHolder {
        AppCompatRatingBar ratingBar ;
        ImageView checkbox ;
    }
}
