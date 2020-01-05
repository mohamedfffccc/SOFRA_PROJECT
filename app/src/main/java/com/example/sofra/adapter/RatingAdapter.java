package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofra.R;
import com.example.sofra.helper.ResturantOffers;

import java.util.ArrayList;

public class RatingAdapter extends BaseAdapter {
    ArrayList<ResturantOffers> data;
    Context context;

    public RatingAdapter(Context context , ArrayList<ResturantOffers> data) {
        this.context = context;
        this.data=data;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.offersitem,null);
        ResturantOffers offers = data.get(position);
        ImageView iv = (ImageView) view.findViewById(R.id.offersimage);
        iv.setImageResource(offers.image);
        TextView tv = (TextView) view.findViewById(R.id.offersname);
        tv.setText(offers.offername);
        return view;
    }
}
