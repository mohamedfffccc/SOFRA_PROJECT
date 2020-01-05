package com.example.sofra.adapter;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;



public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private Context context;
    private BaseActivity activity;
    private List<OffersData> reviewslist = new ArrayList<>();

    public ReviewsAdapter(Context context, BaseActivity activity, List<OffersData> reviewslist) {
        this.context = context;
        this.activity = activity;
        this.reviewslist = reviewslist;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        OffersData reviewsData = reviewslist.get(position);
        holder.clientname.setText(reviewsData.getClient().getName());
        holder.comment.setText(reviewsData.getComment());
        switch (reviewsData.getRate())
        {
            case "1" :
                holder.rateimage.setImageResource(R.drawable.ic_rate1);
                break;
            case "2" :
                holder.rateimage.setImageResource(R.drawable.ic_rate222222222222222);
                break;
            case "3" :
                holder.rateimage.setImageResource(R.drawable.ic_rate222222222222222);
                break;
            case "4" :
                holder.rateimage.setImageResource(R.drawable.ic_rate4);
                break;
            case "5" :
                holder.rateimage.setImageResource(R.drawable.ic_ratea5);
                break;
        }


    }



    @Override
    public int getItemCount() {
        return reviewslist.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView rateimage;
        TextView clientname, comment;


        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            rateimage = (ImageView) itemView.findViewById(R.id.comment_item_rateimage);
            comment = (TextView) itemView.findViewById(R.id.comment_item_comment);
            clientname = (TextView) itemView.findViewById(R.id.comment_item_clientname);


        }
    }
}
