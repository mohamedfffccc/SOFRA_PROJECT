package com.example.sofra.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.CategoriesFoodItemsAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.view.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class CategoryFoodItems extends Fragment {

    @BindView(R.id.fooditemsfragment_rvfoodlist)
    RecyclerView fooditemsfragmentRvfoodlist;
    UserApi userApi;
    CategoriesFoodItemsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public Category category;
    ArrayList<OffersData> foodItemsDatalist;
    @BindView(R.id.fooditemsfragment_tvcatname)
    TextView fooditemsfragmentTvcatname;
    @BindView(R.id.myitemsfragment_btnaddnew)
    ImageView myitemsfragmentBtnaddnew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_food_items, container, false);
        ButterKnife.bind(this, root);
        fooditemsfragmentTvcatname.setText(category.getName());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        userApi = GetClient().create(UserApi.class);
        foodItemsDatalist = new ArrayList<>();
        fooditemsfragmentRvfoodlist.setLayoutManager(linearLayoutManager);
        setSharedPreferences(getActivity());
        getMyItems(LoadData(getActivity(), REST_API_TOKEN), category.getId());
        // Inflate the layout for this fragment
        return root;
    }

    public void getMyItems(String api_token, int category_id) {
        showProgressDialog(getActivity(), "please wait");
        userApi.getMyItems(api_token, category_id).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        foodItemsDatalist.addAll(response.body().getData().getData());
                        adapter = new CategoriesFoodItemsAdapter(getActivity(), (BaseActivity) getActivity(), foodItemsDatalist);
                        fooditemsfragmentRvfoodlist.setAdapter(adapter);
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.myitemsfragment_btnaddnew)
    public void onViewClicked() {
        SaveData(getActivity() , "additem" , "additem");
        NewItemFragment newItemFragment = new NewItemFragment();
        newItemFragment.categorydata=category;
        ReplaceFragment(getActivity().getSupportFragmentManager(), newItemFragment, R.id.homecycle
                , null, "medo");
    }
}
