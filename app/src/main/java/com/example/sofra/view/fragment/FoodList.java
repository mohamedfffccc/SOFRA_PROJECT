package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.UserFoodAdapter;
import com.example.sofra.adapter.UserCategoriesAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Categories;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.view.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodList extends BaseFragment {
    public int category_id = 0;


    @BindView(R.id.listofoffers)
    RecyclerView listofoffers;
    ArrayList<Category> data;
    UserCategoriesAdapter adapter;
    public Restaurant resturantData2;
    LinearLayoutManager linearLayoutManager;
    UserFoodAdapter foodOffersAdapter;
    @BindView(R.id.foodlist_rvlistoffers)
    RecyclerView foodlistRvlistoffers;
    UserApi userApi;
    ArrayList<OffersData> foodlist;
    GridLayoutManager gridLayoutManager;
    private UserFoodAdapter userfoodadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_list, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        addDataToGrid();
        foodlist = new ArrayList<>();
        getItems(category_id, resturantData2.getId());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        foodlistRvlistoffers.setLayoutManager(linearLayoutManager);

        return root;
    }


    public void addDataToGrid() {
        data = new ArrayList<>();
        data.add(new Category("All"
                , "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcThgwBsaHfG9JsLTxTWUboS18RYAa8W4xSBHwUx6QED-ytXC85e"
                , 0));
//        data.add(new Category("new offers" , "http://cloudlab-tech.com/home/wp-content/uploads/2019/04/New-Offers.jpg"
//        ,1));
        userApi.getCategories(resturantData2.getId()).enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                try {
                    if (response.body().getStatus() == 1) {


                        data.addAll(response.body().getData());

                        gridLayoutManager = new GridLayoutManager(getActivity(), data.size());
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
                        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
                        listofoffers.setLayoutManager(linearLayoutManager2);
                        adapter = new UserCategoriesAdapter(getActivity(), (BaseActivity) getActivity(), data, resturantData2.getId(), FoodList.this);
                        listofoffers.setAdapter(adapter);

                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });

    }

    public void getItems(int cat_id, int resturantId) {
        showProgressDialog(getActivity() , getActivity().getString(R.string.please_wait));
        userApi.getItems(resturantId, cat_id).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        foodlist.clear();
                        foodlist.addAll(response.body().getData().getData());
                        userfoodadapter = new UserFoodAdapter(getActivity(), (BaseActivity) getActivity(), foodlist);
                        foodlistRvlistoffers.setAdapter(userfoodadapter);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }
    public  void  getNewOffers(int restaurant_id)
    {
        showProgressDialog(getActivity() , getActivity().getString(R.string.please_wait));
        userApi.getNewOffers(restaurant_id).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        foodlist.clear();
                        foodlist.addAll(response.body().getData().getData());
                        userfoodadapter = new UserFoodAdapter(getActivity(), (BaseActivity) getActivity(), foodlist);
                        foodlistRvlistoffers.setAdapter(userfoodadapter);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });

    }


}
