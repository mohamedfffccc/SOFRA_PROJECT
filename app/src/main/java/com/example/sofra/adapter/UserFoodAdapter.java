package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.AddOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.helper.HelperMethod.ReplaceFragment;


/**
 * Created by medo on 13/11/2016.
 */

public class UserFoodAdapter extends RecyclerView.Adapter<UserFoodAdapter.FoodViewHolder> {
    @BindView(R.id.foodlist_offerrealprice)
    TextView foodlistOfferrealprice;
    private Context context;
    private BaseActivity activity;
    private List<OffersData> offersDataList = new ArrayList<>();
    private Typeface type;

    public UserFoodAdapter(Context context, BaseActivity activity, List<OffersData> offersDataList) {
        this.context = context;
        this.activity = activity;
        this.offersDataList = offersDataList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodlist_offeritem, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        OffersData data = offersDataList.get(position);
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont3.otf");

        Glide.with(context).load(data.getPhotoUrl()).into(holder.offerimage);
        holder.offername.setText(data.getName());
        holder.offerdescription.setText(data.getDescription());
        holder.offerprice.setText(data.getPrice() + "$");

        holder.offerdescription.setTypeface(type);
        holder.offername.setTypeface(type);
        holder.offerprice.setTypeface(type);
        if (data.getHasOffer()==true) {
            holder.offerprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.foodlistOfferrealprice.setText(data.getOfferPrice() + " $");
            holder.foodlistOfferrealprice.setTypeface(type);
        }
        else
        {
            holder.foodlistOfferrealprice.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrderFragment addOrderFragment = new AddOrderFragment();
                addOrderFragment.offerdata = offersDataList.get(position);
                ReplaceFragment(activity.getSupportFragmentManager(), addOrderFragment, R.id.homecycle
                        , null, "medo");

            }
        });

    }

    @Override
    public int getItemCount() {
        return offersDataList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView offerimage;
        TextView offername, offerdescription, offerprice;
        @BindView(R.id.foodlist_offerrealprice)
        TextView foodlistOfferrealprice;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            offerimage = (ImageView) itemView.findViewById(R.id.foodlist_offerimage);
            offername = (TextView) itemView.findViewById(R.id.foodlist_offername);
            offerdescription = (TextView) itemView.findViewById(R.id.foodlist_offerdescription);
            offerprice = (TextView) itemView.findViewById(R.id.foodlist_offerprice);


        }
    }
}
