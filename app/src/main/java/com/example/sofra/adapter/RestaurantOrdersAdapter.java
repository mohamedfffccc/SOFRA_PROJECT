package com.example.sofra.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.orders.OrdersData;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.OrderDetailsFragment;

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
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


/**
 * Created by medo on 13/11/2016.
 */

public class RestaurantOrdersAdapter extends RecyclerView.Adapter<RestaurantOrdersAdapter.OrdersViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<OrdersData> orderslist = new ArrayList<>();
    String usertype;
    private Typeface type;
    private UserApi userapi;
    String state;
    int i;
    private int position;


    public RestaurantOrdersAdapter(Context context, BaseActivity activity, List<OrdersData> orderslist) {
        this.context = context;
        this.activity = activity;
        this.orderslist = orderslist;
        userapi=GetClient().create(UserApi.class);
        setSharedPreferences(activity);


    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurantorder_item, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont4.ttf");
        holder.resturantorderitemBtnCall.setTypeface(type);
        holder.resturantorderitemBtncancel.setTypeface(type);
        holder.resturantorderitemBtnAgree.setTypeface(type);
        holder.resturantorderitemIvusername.setTypeface(type);
        holder.resturantorderitemTvorderid.setTypeface(type);
        holder.resturantorderitemTvtotalcost.setTypeface(type);
        holder.resturantorderitemTvaddress.setTypeface(type);


        OrdersData ordersData = orderslist.get(position);

        holder.resturantorderitemIvusername.setText("client name : " + ordersData.getClient().getName());
        holder.resturantorderitemTvtotalcost.setText("total :" + ordersData.getTotal()+ " $");
        for (int i = 0; i < ordersData.getItems().size(); i++) {
            holder.resturantorderitemTvorderid.setText("orderid :" + ordersData.getItems().get(i).getPivot().getOrderId());
        }
        holder.resturantorderitemTvaddress.setText("address :" + ordersData.getAddress());
        if (ordersData.getState().equals("pending")) {
        } else if (ordersData.getState().equals("accepted")) {

            holder.resturantorderitemLin1.setVisibility(View.GONE);
            holder.resturantorderitemBtnAgree.setText(R.string.confirm_delivery);
            holder.resturantorderitemIvdagree.setImageResource(R.drawable.ic_like);
            holder.resturantorderitemBtnCall.setText(ordersData.getClient().getPhone());
        } else if (ordersData.getState().equals("delivered")) {
            holder.resturantorderitemBtnAgree.setText(R.string.completed_order);
            holder.resturantorderitemBtnAgree.setGravity(Gravity.CENTER);
            holder.resturantorderitemLin1.setVisibility(View.GONE);
            holder.resturantorderitemLin3.setVisibility(View.GONE);
            holder.resturantorderitemBtncancel.setBackgroundResource(R.drawable.accepteditembackground);
            holder.resturantorderitemLin2.setBackgroundResource(R.drawable.accepteditembackground);
            holder.resturantorderitemIvdagree.setVisibility(View.GONE);
        } else if (ordersData.getState().equals("declined") || ordersData.getState().equals("rejected")) {
            holder.resturantorderitemBtnCall.setText(R.string.declined_order);
            holder.resturantorderitemBtnCall.setGravity(Gravity.CENTER);
            holder.resturantorderitemBtnCall.setBackgroundResource(R.drawable.callbuttonbackground);
            holder.resturantorderitemLin3.setBackgroundResource(R.drawable.callbuttonbackground);
            holder.resturantorderitemLin2.setVisibility(View.GONE);
            holder.resturantorderitemLin1.setVisibility(View.GONE);
            holder.resturantorderitemIvdcall.setVisibility(View.GONE);
        }
        holder.resturantorderitemBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ordersData.getState().equals("declined")&&!ordersData.equals("rejected")) {
                    Intent intent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:"+ordersData.getClient().getPhone()));
                   context.startActivity(intent);

                }
            }
        });
        holder.resturantorderitemBtnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=position;
                if (orderslist.get(position).getState().equals("pending")) {
                    acceptOrder(orderslist.get(position).getId());
                }
                else if (orderslist.get(position).getState().equals("accepted"))
                {
                    confirmOrder(orderslist.get(position).getId());
                }



            }
        });
        holder.resturantorderitemBtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderslist.get(position).getState().equals("pending")) {
                    rejectOrder(orderslist.get(position).getId());
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsFragment orderDetailsFragment=new OrderDetailsFragment();
                orderDetailsFragment.data=orderslist.get(position);
                ReplaceFragment(activity.getSupportFragmentManager(), orderDetailsFragment, R.id.homecycle
                        , null, "medo");
            }
        });
//
    }
    private void acceptOrder(int id) {
        showProgressDialog(activity,context.getString(R.string.please_wait));
        userapi.acceptOrder(LoadData(activity , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
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
    private void confirmOrder(int id) {
        showProgressDialog(activity,context.getString(R.string.please_wait));
        userapi.confirmOrder(LoadData(activity , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
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
    private void rejectOrder(int id) {
        showProgressDialog(activity,context.getString(R.string.please_wait));
        userapi.rejectOrder(LoadData(activity , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
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

        @BindView(R.id.resturantorderitem_ivuserimage)
        ImageView resturantorderitemIvuserimage;
        @BindView(R.id.resturantorderitem_ivusername)
        TextView resturantorderitemIvusername;
        @BindView(R.id.resturantorderitem_tvorderid)
        TextView resturantorderitemTvorderid;
        @BindView(R.id.resturantorderitem_tvtotalcost)
        TextView resturantorderitemTvtotalcost;
        @BindView(R.id.resturantorderitem_tvaddress)
        TextView resturantorderitemTvaddress;
        @BindView(R.id.resturantorderitem_ivdelete)
        ImageView resturantorderitemIvdelete;
        @BindView(R.id.resturantorderitem_lin1)
        LinearLayout resturantorderitemLin1;
        @BindView(R.id.resturantorderitem_ivdagree)
        ImageView resturantorderitemIvdagree;
        @BindView(R.id.resturantorderitem_lin2)
        LinearLayout resturantorderitemLin2;
        @BindView(R.id.resturantorderitem_ivdcall)
        ImageView resturantorderitemIvdcall;
        @BindView(R.id.resturantorderitem_lin3)
        LinearLayout resturantorderitemLin3;
        @BindView(R.id.resturantorderitem_btncancel)
        Button resturantorderitemBtncancel;
        @BindView(R.id.resturantorderitem_btn_agree)
        Button resturantorderitemBtnAgree;
        @BindView(R.id.resturantorderitem_btn_call)
        Button resturantorderitemBtnCall;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
