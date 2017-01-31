package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.MemberReview;
import com.netreadystaging.godine.views.RatingCardView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by lenovo on 1/23/2017.
 */

public class MemberReviewAdapter extends BaseAdapter {

    private final ArrayList<MemberReview> listMemberReview;
    private final LayoutInflater inflater;
    private final Context context;
    private int selectedIndex = 0 ;

    public MemberReviewAdapter(Context context , ArrayList<MemberReview> listMemberReview) {
        this.listMemberReview =  listMemberReview ;
        inflater  = LayoutInflater.from(context);
        this.context  = context ;
    }

    @Override
    public int getCount() {
        return listMemberReview.size();
    }

    @Override
    public MemberReview getItem(int position) {
        return listMemberReview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MemberReviewAdapter.ViewHolder holder = null ;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.member_review_list_item,parent,false);
            holder = new MemberReviewAdapter.ViewHolder();
            holder.tvMemberName = (TextView) convertView.findViewById(R.id.tvMemberName) ;
            holder.memberRatingCard = (RatingCardView)  convertView.findViewById(R.id.memberRatingCard) ;
            holder.tvMemberReviewDate = (TextView) convertView.findViewById(R.id.tvMemberReviewDate)  ;
            holder.ivMemberPicture = (ImageView) convertView.findViewById(R.id.ivMemberPicture) ;
            convertView.setTag(holder);
        }
        else{
            holder = (MemberReviewAdapter.ViewHolder) convertView.getTag() ;
        }

        MemberReview memberReview = listMemberReview.get(position);
        String memberName  = memberReview.getFirstName()+" "+memberReview.getLastName();
        holder.tvMemberName.setText(memberName);
        holder.tvMemberReviewDate.setText(memberReview.getReviewDate());
        String profileImageUrl  = "" ;
        if(!profileImageUrl.isEmpty()) Picasso.with(context).load("").into(holder.ivMemberPicture);
        holder.memberRatingCard.setView(memberReview.getRatingCard());

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
                        listItem.findViewById(R.id.memberDetailsContainer).setSelected(false);
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
        ImageView ivMemberPicture ;
        TextView tvMemberName  ;
        TextView tvMemberReviewDate;
        RatingCardView memberRatingCard ;
    }
}
