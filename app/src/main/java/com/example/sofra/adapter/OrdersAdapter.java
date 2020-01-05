package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.orders.OrdersData;
import com.example.sofra.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


/**
 * Created by medo on 13/11/2016.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<OrdersData> orderslist = new ArrayList<>();
    private Typeface type;
    private int position;
    UserApi userapi;

    public OrdersAdapter(Context context, BaseActivity activity, List<OrdersData> orderslist) {
        this.context = context;
        this.activity = activity;
        this.orderslist = orderslist;
        userapi=GetClient().create(UserApi.class);
        setSharedPreferences(activity);


    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders_item, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont4.ttf");
        holder.orderitemBtncancel.setTypeface(type);
        holder.orderitemResturantname.setTypeface(type);
        holder.orderitemOrderid.setTypeface(type);
        holder.orderitemOrdecost.setTypeface(type);
        holder.orderitemClientaddress.setTypeface(type);


        OrdersData ordersData = orderslist.get(position);
        holder.orderitemOrdecost.setText("total :" + ordersData.getTotal()+ " $");


        for (int i = 0; i < ordersData.getItems().size(); i++) {
            holder.orderitemResturantname.setText(ordersData.getItems().get(i).getName());
            holder.orderitemOrderid.setText("orderid :" + ordersData.getItems().get(i).getPivot().getOrderId());
            Glide.with(context).load(ordersData.getItems().get(i).getPhotoUrl()).into(holder.orderitemResturantimage);}

        holder.orderitemClientaddress.setText("address :" + ordersData.getAddress());
        if (ordersData.getState().equals("pending")) {

        }
        else if (ordersData.getState().equals("accepted")) {
            holder.orderitemBtncancel.setText(R.string.confirm_delivery);
            holder.orderitemBtncancel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.ordersitemRel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.ordersitemRel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.orderitemImage.setImageResource(R.drawable.ic_done_all_black_24dp);
        }
        else if(ordersData.getState().equals("delivered"))
        {
            holder.orderitemBtncancel.setText(R.string.completed_order);
            holder.orderitemBtncancel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.ordersitemRel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.orderitemImage.setVisibility(View.GONE);


        }
        else if (ordersData.getState().equals("declined") || ordersData.getState().equals("rejected"))
        {
            holder.orderitemBtncancel.setText(R.string.declined_order);
            holder.orderitemBtncancel.setBackgroundResource(R.drawable.callbuttonbackground);
            holder.ordersitemRel.setBackgroundResource(R.drawable.callbuttonbackground);
            holder.orderitemImage.setVisibility(View.GONE);
        }
        holder.orderitemBtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderslist.get(position).getState().equals("pending")) {
                    rejecUsertOrder(orderslist.get(position).getId());
                }
                else if (ordersData.getState().equals("accepted")) {
                  confirmUsertOrder(orderslist.get(position).getId());
                }

            }
        });
    }
    private void rejecUsertOrder(int id) {
        showProgressDialog(activity,context.getString(R.string.please_wait));
        userapi.declineClientOrder(LoadData(activity , API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(activity, response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        showPositiveToast(activity , response.body().getMsg());
                        orderslist.remove(position);
                        notifyDataSetChanged();
                    }
                    else {
                        showNegativeToast(activity , response.body().getMsg());

                    }

                }
                catch (Exception e)
                {


                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }
    private void confirmUsertOrder(int id) {
        showProgressDialog(activity,context.getString(R.string.please_wait));
        userapi.confimUserOrder(LoadData(activity , API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(activity, response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        showPositiveToast(activity , response.body().getMsg());
                        orderslist.remove(position);
                        notifyDataSetChanged();
                    }
                    else {
                        showNegativeToast(activity , response.body().getMsg());

                    }

                }
                catch (Exception e)
                {


                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }





    @Override
    public int getItemCount() {
        return orderslist.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderitem_resturantimage)
        ImageView orderitemResturantimage;
        @BindView(R.id.orderitem_resturantname)
        TextView orderitemResturantname;
        @BindView(R.id.orderitem_orderid)
        TextView orderitemOrderid;
        @BindView(R.id.orderitem_ordecost)
        TextView orderitemOrdecost;
        @BindView(R.id.orderitem_clientaddress)
        TextView orderitemClientaddress;
        @BindView(R.id.orderitem_btncancel)
        Button orderitemBtncancel;
        @BindView(R.id.orderitem_image)
        ImageView orderitemImage;
        @BindView(R.id.ordersitem_lin)
        LinearLayout ordersitemLin;
        @BindView(R.id.ordersitem_rel)
        RelativeLayout ordersitemRel;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
