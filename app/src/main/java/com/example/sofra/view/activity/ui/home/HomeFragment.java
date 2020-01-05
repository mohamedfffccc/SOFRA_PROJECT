package com.example.sofra.view.activity.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.adapter.RestaurantAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.generalresponse.RegionsData;
import com.example.sofra.data.model.restaurants.Restaurants;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class HomeFragment extends BaseFragment {
    UserApi userApi;
    ArrayList<Restaurant> resturantsData;
    RestaurantAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.resturantlist)
    RecyclerView resturantlist;
    @BindView(R.id.resturantslist_btnsearch)
    ImageView resturantslistBtnsearch;
    @BindView(R.id.resturantslist_edkeyword)
    EditText resturantslistEdkeyword;
    @BindView(R.id.resturantslist_spcity)
    Spinner resturantslistSpcity;
    private OnEndLess onEndLess;
    private int Maxpage;
    private ArrayList<RegionsData> city;
    private GeneralResponseAdapter generaladapter;
    private Typeface type;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bigfont5.otf");
        resturantslistEdkeyword.setTypeface(type);



        addCity();

        generaladapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.select_city));
        resturantslistSpcity.setAdapter(generaladapter);
        generaladapter.notifyDataSetChanged();
        resturantsData = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        resturantlist.setLayoutManager(linearLayoutManager);
        adapter = new RestaurantAdapter(getActivity(), (BaseActivity) getActivity(), resturantsData);
        resturantlist.setAdapter(adapter);
        getResturantList(1);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getResturantList(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        resturantlist.addOnScrollListener(onEndLess);

        return root;
    }

    public void getResturantList(int page) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.getAllResturants(page).enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        resturantsData.addAll(response.body().getData().getData());
                        Maxpage = response.body().getData().getLastPage();
                        adapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {

            }
        });

    }

    public void addCity() {
        city = new ArrayList<>();
        userApi.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus() == 1) {
                    showPositiveToast(getActivity(), response.body().getMsg());

                    city.addAll(response.body().getData().getData());
                    Toast.makeText(getActivity(), city.toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void filterData(String keyword, int region_id) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.filterRestaurants(keyword, region_id).enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        resturantsData.clear();
                        resturantsData.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.resturantslist_btnsearch)
    public void onViewClicked() {
        filterData(resturantslistEdkeyword.getText().toString(),resturantslistSpcity.getSelectedItemPosition());
    }
}