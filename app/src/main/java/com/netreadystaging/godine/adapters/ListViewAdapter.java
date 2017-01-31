package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.netreadystaging.godine.R;

import com.netreadystaging.godine.models.BillingHistory;

import java.util.ArrayList;

/**
 * Created by Muhib on 03-12-2016.
 */

public class ListViewAdapter extends ArrayAdapter<BillingHistory> {
    Context context;
    ArrayList<BillingHistory> billing;
    LayoutInflater inflater;

    public ListViewAdapter(Context context, int billing_custom_row, ArrayList<BillingHistory> billing) {
        super(context, R.layout.billing_custom_row,billing);
        this.context=context;
        this.billing=billing;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        Viewhold holder=null;
        if(row==null)
        {
            row= inflater.inflate(R.layout.billing_custom_row,parent,false);
            holder=new Viewhold(row);
            row.setTag(holder);
        }
        else
        {
            holder= (Viewhold) row.getTag();
        }

         holder.Date.setText(billing.get(position).getOrderDate());
         holder.Rs.setText("$"+billing.get(position).getTotalAmount());
         holder.Status.setText(billing.get(position).getSalesPaymentStatus());
         holder.Status.setTextColor(Color.BLUE);
        return row;
    }
    class Viewhold
    {
        TextView Date;
        TextView Rs;
        TextView Status;
        Viewhold(View view)
        {
            Date= (TextView) view.findViewById(R.id.date);
             Rs= (TextView) view.findViewById(R.id.paid);
            Status= (TextView) view.findViewById(R.id.status);
        }

    }
}
