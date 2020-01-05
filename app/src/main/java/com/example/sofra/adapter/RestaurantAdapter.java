package com.example.sofra.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.ResturantDataFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.RESTURANT_DELIVERY_COST;
import static com.example.sofra.data.local.SofraConstants.RESTURANT_ID;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;


/**
 * Created by medo on 13/11/2016.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ResturantViewHolder> {
    private Context context;
    private BaseActivity activity;
    private List<Restaurant> resturantlist = new ArrayList<>();


    public RestaurantAdapter(Context context, BaseActivity activity, List<Restaurant> resturantlist) {
        this.context = context;
        this.activity = activity;
        this.resturantlist = resturantlist;
        setSharedPreferences(activity);
    }

    @NonNull
    @Override
    public ResturantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false);
        return new ResturantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantViewHolder holder, int position) {
        Restaurant data = resturantlist.get(position);
        Glide.with(context).load(data.getPhotoUrl()).into(holder.resturantimage);
        holder.resturantname.setText(data.getName());
        holder.resturantrate.setRating(data.getRate());
        holder.minimumorder.setText(activity.getString(R.string.minimum_order) + data.getMinimumCharger());
        holder.deliverycost.setText(activity.getString(R.string.delivery_cost) + data.getDeliveryCost());
        if (data.getAvailability().equals("open")) {
            holder.resturantstatus.setText(R.string.open);
        } else if(data.getAvailability().equals("closed")) {
            holder.reststateimg.setBackgroundResource(R.drawable.restclosed);
            holder.resturantstatus.setText(R.string.closed);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant resturantData = resturantlist.get(position);
                ResturantDataFragment resturantDataFragment = new ResturantDataFragment();

                resturantDataFragment.data = resturantData;
                ReplaceFragment(activity.getSupportFragmentManager(), resturantDataFragment, R.id.homecycle
                        , null, "medo");
                SaveData(activity,RESTURANT_DELIVERY_COST , resturantlist.get(position).getDeliveryCost());
                SaveData(activity,RESTURANT_ID , String.valueOf(resturantlist.get(position).getId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return resturantlist.size();
    }

    public class ResturantViewHolder extends RecyclerView.ViewHolder {
        CircularImageView resturantimage;
        RatingBar resturantrate;
        TextView resturantname, minimumorder, deliverycost, resturantstatus, reststateimg;


        public ResturantViewHolder(@NonNull View itemView) {
            super(itemView);
            resturantimage = (CircularImageView) itemView.findViewById(R.id.resturant_image);
            resturantrate = (RatingBar) itemView.findViewById(R.id.resturantrate);
            resturantname = (TextView) itemView.findViewById(R.id.resturantname);
            minimumorder = (TextView) itemView.findViewById(R.id.minimumorder);
            deliverycost = (TextView) itemView.findViewById(R.id.deliverycost);
            resturantstatus = (TextView) itemView.findViewById(R.id.resturantstatus);
            reststateimg = (TextView) itemView.findViewById(R.id.resturantstatusimage);


        }
    }
}
