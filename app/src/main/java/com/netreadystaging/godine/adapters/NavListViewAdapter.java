package com.netreadystaging.godine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.netreadystaging.godine.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sony on 21-07-2016.
 */
public class NavListViewAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> list;
    private final int[] drawables;
    LayoutInflater inflater ;
    public NavListViewAdapter(Context context , ArrayList<String> list , int[] drawables) {
        this.context =context ;
        this.list =  list  ;
        this.drawables = drawables ;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.nav_list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.tvNavLabel); // title
        ImageView ivNav = (ImageView) vi.findViewById(R.id.ivNavLabel) ;

        title.setText(list.get(position));
        ivNav.setImageResource(drawables[position]);
        return vi;
    }
}
