package com.example.sofra.view.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.CategoriesAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.dialoge.NewCategoryDialoge;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstants.REST_PASSWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantHomeFragment extends Fragment {
    ArrayList<Category> categorieslist;
    CategoriesAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    @BindView(R.id.mycategoriesfragment_rvcategories)
    RecyclerView mycategoriesfragmentRvcategories;
    UserApi userApi;
    @BindView(R.id.mycategoriesfragment_tvcat)
    TextView mycategoriesfragmentTvcat;
    @BindView(R.id.mycategoriesfragment_btnaddnew)
    ImageView mycategoriesfragmentBtnaddnew;
    private Typeface type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_categories, container, false);
        ButterKnife.bind(this, root);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/font2.TTF");
        mycategoriesfragmentTvcat.setTypeface(type);

        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        categorieslist = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mycategoriesfragmentRvcategories.setLayoutManager(linearLayoutManager);
        getMyCategories();
        //
        // ;

        // Inflate the layout for this fragment
        return root;
    }

    public void getMyCategories() {
        userApi.loginRestaurant(LoadData(getActivity(), REST_EMAIL), LoadData(getActivity(), REST_PASSWORD)).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        categorieslist.clear();
                        categorieslist.addAll(response.body().getData().getUser().getCategories());
                        adapter = new CategoriesAdapter(getActivity(), (BaseActivity) getActivity(), categorieslist);
                        mycategoriesfragmentRvcategories.setAdapter(adapter);
                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.mycategoriesfragment_btnaddnew)
    public void onViewClicked() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        NewCategoryDialoge dialoge = new NewCategoryDialoge(RestaurantHomeFragment.this);
        dialoge.show(manager,getString(R.string.new_category));
        SaveData(getActivity(),"edit" , "add");
    }
}
