package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.AddOfferFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

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
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


/**
 * Created by medo on 13/11/2016.
 */

public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ResturantViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<OffersData> offersDataList = new ArrayList<>();
    private Typeface type;
    private UserApi userapi;
    private int position;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public RestaurantOffersAdapter(Context context, BaseActivity activity, List<OffersData> offersDataList) {
        this.context = context;
        this.activity = activity;
        this.offersDataList = offersDataList;
        userapi = GetClient().create(UserApi.class);
        setSharedPreferences(activity);
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @NonNull
    @Override
    public ResturantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.useroffer_item, parent, false);
        return new ResturantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantViewHolder holder, int position) {
        OffersData data = offersDataList.get(position);
        viewBinderHelper.bind(holder.offeritemSwipe1, String.valueOf(data.getId()));

        holder.offeritemTvrestaurantname.setText(data.getName());
        Glide.with(context).load(data.getPhotoUrl()).into(holder.offeritemIvrestaurantimage);
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont3.otf");
        holder.offeritemTvrestaurantname.setTypeface(type);
        holder.offeritemBtndetails.setTypeface(type);
        if (LoadData(activity, USER_TYPE).equals("client")) {
            holder.userofferitemLinswipe.setVisibility(View.GONE);

        } else if (LoadData(activity, USER_TYPE).equals("resturant")) {
            holder.offeritemBtndetails.setVisibility(View.GONE);
        }
        holder.userofferitemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOffer(offersDataList.get(position).getId());

            }
        });
        holder.userofferitemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OffersData Editoffer = offersDataList.get(position);
                AddOfferFragment newoffer = new AddOfferFragment();
                newoffer.offersData = Editoffer;
                ReplaceFragment(activity.getSupportFragmentManager(), newoffer, R.id.homecycle
                        , null, "medo");

                SaveData(activity, "updateoffer", "updateoffer");
            }
        });


    }

    private void deleteOffer(int id) {
        showProgressDialog(activity, context.getString(R.string.please_wait));
        userapi.deleteOffer(id, LoadData(activity, REST_API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(activity, response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(activity, response.body().getMsg());
                        offersDataList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        showNegativeToast(activity, response.body().getMsg());

                    }

                } catch (Exception e) {


                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return offersDataList.size();
    }

    public class ResturantViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userofferitem_delete)
        ImageView userofferitemDelete;
        @BindView(R.id.userofferitem_edit)
        ImageView userofferitemEdit;
        @BindView(R.id.userofferitem_linswipe)
        RelativeLayout userofferitemLinswipe;
        @BindView(R.id.offeritem_ivrestaurantimage)
        CircularImageView offeritemIvrestaurantimage;
        @BindView(R.id.offeritem_tvrestaurantname)
        TextView offeritemTvrestaurantname;
        @BindView(R.id.offeritem_btndetails)
        Button offeritemBtndetails;

        @BindView(R.id.offeritem_swipe1)
        SwipeRevealLayout offeritemSwipe1;

        public ResturantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
