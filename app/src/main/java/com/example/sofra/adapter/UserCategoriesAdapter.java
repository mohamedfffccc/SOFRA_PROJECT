package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.FoodList;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;


/**
 * Created by medo on 13/11/2016.
 */

public class UserCategoriesAdapter extends RecyclerView.Adapter<UserCategoriesAdapter.CategoriesViewHolder> {


    private Integer resturantId;
    private Context context;
    private BaseActivity activity;
    private List<Category> categoryList = new ArrayList<>();
    private Typeface type;
    private UserApi userapi;
    public FoodList foodList;


    public UserCategoriesAdapter(Context context, BaseActivity activity, List<Category> categoryList, Integer id, FoodList foodList) {
        this.context = context;
        this.activity = activity;
        this.categoryList = categoryList;
        userapi = GetClient().create(UserApi.class);
        setSharedPreferences(activity);
        this.foodList=foodList;
        resturantId = id;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offersitem, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category categorydata = categoryList.get(position);
        Glide.with(context).load(categorydata.getPhotoUrl()).into(holder.offersimage);
        holder.offersname.setText(categorydata.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,categoryList.get(position).getName() , Toast.LENGTH_SHORT).show();

                foodList.getItems(categoryList.get(position).getId(), resturantId);
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offersimage)
        CircularImageView offersimage;
        @BindView(R.id.offersname)
        TextView offersname;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
