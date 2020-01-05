package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantOrdersAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.orders.Orders;
import com.example.sofra.data.model.orders.OrdersData;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.view.activity.BaseActivity;

import java.util.ArrayList;

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
public class RestaurantNewOrders extends Fragment {
    UserApi userApi;
    ArrayList<OrdersData> orderslist;
    RestaurantOrdersAdapter adapter;
    @BindView(R.id.fragmenrestauranttneworders_orderslist)
    RecyclerView fragmenrestauranttnewordersOrderslist;


    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private int Maxpage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_new_orders, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());

        userApi = GetClient().create(UserApi.class);
        orderslist = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmenrestauranttnewordersOrderslist.setLayoutManager(linearLayoutManager);
        adapter = new RestaurantOrdersAdapter(getActivity(), (BaseActivity) getActivity(), orderslist);
        fragmenrestauranttnewordersOrderslist.setAdapter(adapter);

        getNewOrders(LoadData(getActivity() , REST_API_TOKEN), 1);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getNewOrders(LoadData(getActivity() , REST_API_TOKEN), current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        fragmenrestauranttnewordersOrderslist.addOnScrollListener(onEndLess);


        // Inflate the layout for this fragment
        return root;
    }

    private void getNewOrders(String api_token, int page) {
        userApi.getMyRestaurantOrders(api_token, "pending", page).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        orderslist.addAll(response.body().getData().getData());
                        Maxpage = response.body().getData().getLastPage();
                        adapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {

            }
        });
    }

}
