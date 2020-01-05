package com.example.sofra.view.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.data.model.restaurants.RestaurantDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResturantInfo extends BaseFragment {
    public Restaurant resturantData;
    @BindView(R.id.resturantinfo_tvstatus)
    TextView resturantinfoTvstatus;
    @BindView(R.id.resturantinfo_tvcity)
    TextView resturantinfoTvcity;
    @BindView(R.id.resturantinfo_tvregion)
    TextView resturantinfoTvregion;
    @BindView(R.id.resturantinfo_tvminimumorder)
    TextView resturantinfoTvminimumorder;
    @BindView(R.id.resturantinfo_tvdeliverycosttv)
    TextView resturantinfoTvdeliverycosttv;
    UserApi userApi;
    private Typeface type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_resturant_info, container, false);
        ButterKnife.bind(this, root);
        userApi=GetClient().create(UserApi.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bigfont5.otf");
        setInfo();
        return root;
    }

    public void setInfo() {

userApi.getDetails(resturantData.getId()).enqueue(new Callback<RestaurantDetails>() {
    @Override
    public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
        try {
            if (response.body().getStatus()==1) {
                resturantinfoTvcity.setText(getString(R.string.city) + response.body().getData().getRegion().getCity().getName());
                resturantinfoTvcity.setTypeface(type);
              resturantinfoTvregion.setText(getString(R.string.region) + response.body().getData().getRegion().getName());
              resturantinfoTvregion.setTypeface(type);
                resturantinfoTvminimumorder.setText(getString(R.string.minimum_order) + resturantData.getMinimumCharger() + " $");
                resturantinfoTvminimumorder.setTypeface(type);
                resturantinfoTvdeliverycosttv.setText(getString(R.string.delivery_cost) + resturantData.getDeliveryCost() + " $");
                resturantinfoTvdeliverycosttv.setTypeface(type);
                    resturantinfoTvstatus.setText(getString(R.string.state) + resturantData.getAvailability());
                    resturantinfoTvstatus.setTypeface(type);

            }
            else
            {
                Toast.makeText(getContext() , response.body().getMsg() , Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onFailure(Call<RestaurantDetails> call, Throwable t) {

    }
});

    }
}
