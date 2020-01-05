package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.room.RoomManger.getInistance;


/**
 * Created by medo on 13/11/2016.
 */

public class UserStoreAdapter extends RecyclerView.Adapter<UserStoreAdapter.CategoriesViewHolder> {



    private Context context;
    private BaseActivity activity;
    private List<OrderItem> orderlist = new ArrayList<>();
    private Typeface type;
    private UserApi userapi;
    RoomDao roomDao;

    public UserStoreAdapter(Context context, BaseActivity activity, List<OrderItem> orderlist) {
        this.context = context;
        this.activity = activity;
        this.orderlist = orderlist;
        userapi = GetClient().create(UserApi.class);
        setSharedPreferences(activity);
        roomDao = getInistance(context).roomDao();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userstore_orderitem, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        OrderItem orderitem = orderlist.get(position);
        Glide.with(context).load(orderitem.getPhoto()).into(holder.storeitemIvimage);
        holder.storeitemTvname.setText(orderitem.getItem_name());
        holder.storeitemTvprice.setText("$ " + String.valueOf(orderitem.getPrice()));
        holder.storeitemTvcount.setText(String.valueOf(orderitem.getQuantity()));
        type = Typeface.createFromAsset(activity.getAssets(), "fonts/bigfont5.otf");
        holder.storeitemTvname.setTypeface(type);
        holder.storeitemTvcount.setTypeface(type);
        holder.storeitemTvprice.setTypeface(type);
        holder.orderitemTvquantity.setTypeface(type);
        holder.storeitemBtndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        roomDao.removeItem(orderlist.get(position));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                orderlist.remove(position);
                                notifyDataSetChanged();
                            }
                        });


                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderitem_tvquantity)
        TextView orderitemTvquantity;
        @BindView(R.id.storeitem_ivimage)
        ImageView storeitemIvimage;
        @BindView(R.id.storeitem_tvname)
        TextView storeitemTvname;
        @BindView(R.id.storeitem_tvprice)
        TextView storeitemTvprice;
        @BindView(R.id.storeitem_tvcount)
        TextView storeitemTvcount;
        @BindView(R.id.storeitem_ivadd)
        ImageView storeitemIvadd;
        @BindView(R.id.storeitem_ivremove)
        ImageView storeitemIvremove;
        @BindView(R.id.storeitem_rel1)
        RelativeLayout storeitemRel1;
        @BindView(R.id.storeitem_btndelete)
        ImageView storeitemBtndelete;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
