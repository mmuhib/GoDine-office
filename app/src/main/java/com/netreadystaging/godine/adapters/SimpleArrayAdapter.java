package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.Staffmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class    SimpleArrayAdapter extends ArrayAdapter<Staffmodel> {
    Context context;
    ArrayList<Staffmodel> staffmodels;
    LayoutInflater inflater;

    public SimpleArrayAdapter(@NonNull Context context, ArrayList<Staffmodel> staffmodels) {
        super(context, R.layout.staffrow,staffmodels);
        this.context=context;
        this.staffmodels=staffmodels;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        VViewholder viewholder=null;
        if (row==null)
        {
            row=inflater.inflate(R.layout.staffrow,parent,false);
            viewholder=new VViewholder(row);
            row.setTag(viewholder);
        }
        else
        {
            viewholder= (VViewholder) row.getTag();
        }
        viewholder.staff.setText(staffmodels.get(position).getFirstname()+" "+staffmodels.get(position).getLastname());
        viewholder.id.setText(staffmodels.get(position).getUserId());
        return row;
    }
    class VViewholder
    {
        TextView staff,id;

        public VViewholder(View v) {
        staff= (TextView) v.findViewById(R.id.txt_staff);
            id= (TextView) v.findViewById(R.id.id);
        }
    }
}
