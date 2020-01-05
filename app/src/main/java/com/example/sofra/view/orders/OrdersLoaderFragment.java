package com.example.sofra.view.orders;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.OrdersAdapter;
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
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersLoaderFragment extends Fragment {
    UserApi userApi;
    ArrayList<OrdersData> orderslist;
    OrdersAdapter2 adapter;
    @BindView(R.id.fragmentcurrentorders_orderslist2)
    RecyclerView fragmentcurrentordersOrderslist;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private int Maxpage;
    public String key;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());

        orderslist = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentcurrentordersOrderslist.setLayoutManager(linearLayoutManager);
        adapter = new OrdersAdapter2(getActivity(), (BaseActivity) getActivity(), orderslist);
        fragmentcurrentordersOrderslist.setAdapter(adapter);

        getNewOrders(LoadData(getActivity(),API_TOKEN), 1);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getNewOrders(LoadData(getActivity(),API_TOKEN), 1);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        fragmentcurrentordersOrderslist.addOnScrollListener(onEndLess);


        // Inflate the layout for this fragment
        return root;
    }

    private void getNewOrders(String api_token, int page) {
        userApi.getMyOrders(api_token, key, page).enqueue(new Callback<Orders>() {
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
