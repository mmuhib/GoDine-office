package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.MemberList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhib on 15-12-2016.
 */

public class MemberAdapter extends ArrayAdapter<MemberList> {
    ArrayList<MemberList> listofmember;
    LayoutInflater inflater;

    public MemberAdapter(Context context, int resource, ArrayList<MemberList> listofmember) {
        super(context, R.layout.member_custom_row, listofmember);
        this.listofmember = listofmember;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
          Viewhold holder=null;
        if (row == null) {
            row = inflater.inflate(R.layout.member_custom_row, parent, false);
              holder=new Viewhold(row);
             row.setTag(holder);
        } else {
             holder= (Viewhold) row.getTag();
        }
        holder.Displayname.setText(listofmember.get(position).getDisplayName());
        String Cell=listofmember.get(position).getCell();
        if(Cell.isEmpty())
        {
            holder.Cellno.setText(listofmember.get(position).getTelephone());
            Log.d("Cell",Cell);
        }
        else
        {
            holder.Cellno.setText(Cell);
            Log.d("Cell",listofmember.get(position).getCell());
        }
        holder.profileImage.setImageResource(R.drawable.img);
        holder.Userid.setText(listofmember.get(position).getUserId());
        return row;
    }

    class Viewhold {
        TextView Displayname,Userid;
        TextView Cellno;
        ImageView profileImage;
        Viewhold(View view)
        {
            Displayname = (TextView) view.findViewById(R.id.memberlistname);
            Cellno = (TextView) view.findViewById(R.id.memberlistcall);
           profileImage= (ImageView) view.findViewById(R.id.member_image);
            Userid= (TextView) view.findViewById(R.id.memberid);
        }

    }
}
