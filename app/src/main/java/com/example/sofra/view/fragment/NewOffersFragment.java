package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantOffersAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.activity.ui.profile.Profile;

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
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


public class NewOffersFragment extends Fragment {
    LinearLayoutManager linearLayoutManager;
    ArrayList<OffersData> offersDatalist;
    RestaurantOffersAdapter adapter;
    UserApi userApi;
    @BindView(R.id.newuseroffers_rvoffers)
    RecyclerView newuseroffersRvoffers;
    @BindView(R.id.newoffersfrag_btnadd)
    Button newoffersfragBtnadd;
    @BindView(R.id.newoffersfrag_ivoffers)
    ImageView newoffersfragIvoffers;
    private int Maxpage;
    private OnEndLess onEndLess;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_user_offers, container, false);
        ButterKnife.bind(this, root);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        newuseroffersRvoffers.setLayoutManager(linearLayoutManager);
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());
        offersDatalist = new ArrayList<>();

        if (LoadData(getActivity(), USER_TYPE).equals("client")) {
            getMyOffers(1);
            newoffersfragBtnadd.setVisibility(View.GONE);
        } else if (LoadData(getActivity(), USER_TYPE).equals("resturant")) {
            getMyRestOffers(1);
        }


        // Inflate the layout for this fragment
        return root;
    }

    public void getMyOffers(int page) {
       // showProgressDialog(getActivity(), getString(R.string.please_wait));
//        userApi.getMyOffers(LoadData(getActivity() , API_TOKEN) , page).enqueue(new Callback<Offers>() {
//            @Override
//            public void onResponse(Call<Offers> call, Response<Offers> response) {
//                try {
//                    dismissProgressDialog();
//                    if (response.body().getStatus()==1) {
//                        offersDatalist.addAll(response.body().getData().getData());
//                        Maxpage=response.body().getData().getLastPage();
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//                catch (Exception e)
//                {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Offers> call, Throwable t) {
//
//            }
//        });
//        onEndLess = new OnEndLess(linearLayoutManager, 1) {
//            @Override
//            public void onLoadMore(int current_page) {
//
//                if (current_page <= Maxpage) {
//                    if (Maxpage != 0 && current_page != 1) {
//                        onEndLess.previous_page = current_page;
//                        getMyOffers(current_page);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        onEndLess.current_page = onEndLess.previous_page;
//                    }
//                } else {
//                    onEndLess.current_page = onEndLess.previous_page;
//
//                }
//
//
//            }
//        };
//        newuseroffersRvoffers.addOnScrollListener(onEndLess);

    }

    public void getMyRestOffers(int page) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.getMyRestaurantOffers(LoadData(getActivity(), REST_API_TOKEN), page).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                try {
                    dismissProgressDialog();
                    if (response.body().getStatus() == 1) {
                        offersDatalist.addAll(response.body().getData().getData());
                        Maxpage = response.body().getData().getLastPage();
//                        adapter.notifyDataSetChanged();
                        adapter = new RestaurantOffersAdapter(getActivity(), (BaseActivity) getActivity(), offersDatalist);
                        newuseroffersRvoffers.setAdapter(adapter);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
//        onEndLess = new OnEndLess(linearLayoutManager, 1) {
//            @Override
//            public void onLoadMore(int current_page) {
//
//                if (current_page <= Maxpage) {
//                    if (Maxpage != 0 && current_page != 1) {
//                        onEndLess.previous_page = current_page;
//                        getMyOffers(current_page);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        onEndLess.current_page = onEndLess.previous_page;
//                    }
//                } else {
//                    onEndLess.current_page = onEndLess.previous_page;
//
//                }
//
//
//            }
//        };
//        newuseroffersRvoffers.addOnScrollListener(onEndLess);
//
//
    }

    @OnClick(R.id.newoffersfrag_btnadd)
    public void onViewClicked() {
        ReplaceFragment(getActivity().getSupportFragmentManager(), new AddOfferFragment(), R.id.homecycle
                , null, "medo");
        SaveData(getActivity(),"updateoffer" , "addoffer");

    }
}
