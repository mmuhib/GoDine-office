package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.MemberList;
import com.netreadystaging.godine.utils.DownloadImageTask;
import com.netreadystaging.godine.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        holder.name.setText(listofmember.get(position).getFirstName()+" "+ listofmember.get(position).getLastName());
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
    //    new DownloadImageTask((ImageView) view.findViewById(R.id.memberimg),progressBar).execute("https://godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        String mail=listofmember.get(position).getEmail();

        holder.bar.setVisibility(View.VISIBLE);
        final Viewhold finalHolder = holder;
        Picasso.with(getContext()).load("https://godineclub.com/Portals/0/Images/Verification%20images/"+mail+".jpg").into(holder.profileImage, new Callback() {
            @Override
            public void onSuccess() {
                finalHolder.bar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                Utility.message(getContext(),"There is some problem in loading image");
            }
        });
      //  holder.profileImage.setImageResource(R.drawable.img);
        holder.Userid.setText(listofmember.get(position).getUserId());
        return row;
    }

    class Viewhold {
        TextView name,Userid;
        TextView Cellno;
        ImageView profileImage;
        ProgressBar bar;
        Viewhold(View view)
        {
            name = (TextView) view.findViewById(R.id.memberlistname);
            Cellno = (TextView) view.findViewById(R.id.memberlistcall);
           profileImage= (ImageView) view.findViewById(R.id.member_image);
            Userid= (TextView) view.findViewById(R.id.memberid);
            bar= (ProgressBar) view.findViewById(R.id.progressBa);
        }

    }
}
