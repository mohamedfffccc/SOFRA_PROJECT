package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.RestaurantDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommissionFragment extends Fragment {
    UserApi userApi;
    @BindView(R.id.commission_tvcommissionquantity)
    TextView commissionTvcommissionquantity;
    @BindView(R.id.commission_tvresturantpayments)
    TextView commissionTvresturantpayments;
    @BindView(R.id.commission_tvappcommissions)
    TextView commissionTvappcommissions;
    @BindView(R.id.commission_tvcommissionpays)
    TextView commissionTvcommissionpays;
    @BindView(R.id.commission_tvcommissionrest)
    TextView commissionTvcommissionrest;
    @BindView(R.id.commission_tvahly)
    TextView commissionTvahly;
    @BindView(R.id.commission_tvraghy)
    TextView commissionTvraghy;
    @BindView(R.id.commission_tvpersonname)
    TextView commissionTvpersonname;
    @BindView(R.id.commission_linbankaccount)
    LinearLayout commissionLinbankaccount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_commission, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        getData();
        // Inflate the layout for this fragment
        return root;
    }

    private void getData() {
        userApi.getCommission(LoadData(getActivity(), REST_API_TOKEN)).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
try {
    if (response.body().getStatus()==1) {
        commissionTvcommissionquantity.setText("يرجي تحويل نسبة وقدرها" + response.body().getData().getCommission()
        + "في مدة اقصاها شهر علما بان جميع الطلبات مسجلة علي حسابك الشخصي");
        commissionTvresturantpayments.setText(getActivity().getString(R.string.resturant_payments) + response.body().getData().getTotal() + "$");
        commissionTvappcommissions.setText(getActivity().getString(R.string.app_commission) + response.body().getData().getCommissions() + "$");
        commissionTvcommissionpays.setText(getActivity().getString(R.string.paid) + response.body().getData().getPayments() + "$");
        commissionTvcommissionrest.setText(getActivity().getString(R.string.rest) + response.body().getData().getNetCommissions() + "$");


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
