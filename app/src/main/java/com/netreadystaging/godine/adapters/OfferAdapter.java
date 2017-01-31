package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.OfferList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sony on 19-01-2017.
 */

public class OfferAdapter extends ArrayAdapter<OfferList> {
    Context context;
    ArrayList<OfferList> offerLists;
    LayoutInflater inflater;
    public OfferAdapter(Context context, int resource, ArrayList<OfferList> offerLists) {
        super(context, R.layout.offer_row, offerLists);
        this.context=context;
        this.offerLists=offerLists;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
      Viewhold holder=null;
        if (row == null) {
            row = inflater.inflate(R.layout.offer_row, parent, false);
            holder=new Viewhold(row);
            row.setTag(holder);
        } else {
            holder= (Viewhold) row.getTag();
        }
        OfferList offerLt=offerLists.get(position);
        String offerimage=offerLt.getImage().trim().replaceAll("\\s+","%20");
        if(!offerimage.isEmpty()) {
            if(!offerimage.contains("http://") && !offerimage.contains("https://"))
            {
                offerimage = "https://"+offerimage;
            }
            Picasso.with(getContext()).load(offerimage).fit().into(holder.Imageoffer);
            Log.d("Muhib",offerimage);
        }

        Log.d("Muhib",offerimage);
        holder.Displayname.setText(offerLt.getRestaurantName());
        holder.offername.setText(offerLt.getOfferName());
       holder.description.setText(offerLt.getDescription());
        holder.Offerenddate.setText(offerLt.getOfferEndDate());
        holder.Offer.setText(offerLt.getOffer());
        holder.ids.setText(offerLt.getRestaurantId());
        return row;
    }
    class Viewhold {
        TextView Displayname,offername,ids;
        TextView description,Offerenddate;
        ImageView Imageoffer;
        TextView Offer;
        Viewhold(View view)
        {
            Displayname = (TextView) view.findViewById(R.id.txt_RestaurantNam);
            Imageoffer= (ImageView) view.findViewById(R.id.imgoffer);
           offername= (TextView) view.findViewById(R.id.offername);
            description= (TextView) view.findViewById(R.id.description);
            Offer= (TextView) view.findViewById(R.id.offer);
            Offerenddate= (TextView) view.findViewById(R.id.offerenddate);
            ids= (TextView) view.findViewById(R.id.listids);
        }

    }
}
