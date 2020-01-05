package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.ReviewsAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.dialoge.RateDialoge;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


public class CommentsAndRate extends Fragment {
    public Restaurant restaurantdata;


    @BindView(R.id.commentsfragment_tvrate)
    TextView commentsfragmentTvrate;
    @BindView(R.id.commentsfragment_btnaddcomment)
    Button commentsfragmentBtnaddcomment;
    @BindView(R.id.commentsfragment_commentslist)
    RecyclerView commentsfragmentCommentslist;
    LinearLayoutManager linearLayoutManager;
    private UserApi userApi;
  public   ArrayList<OffersData> reviewsDatalist;
    private ReviewsAdapter reviewsadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments_and_rate, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        reviewsDatalist = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        commentsfragmentCommentslist.setLayoutManager(linearLayoutManager);
        setSharedPreferences(getActivity());
//        getReviews(restaurantdata.getId());

        getReviews(restaurantdata.getId());
        // Inflate the layout for this fragment
        return root;
    }

    public void getReviews(int resturantid) {

        userApi.getReviews(LoadData(getActivity(),API_TOKEN)
                , resturantid).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {

                try {
                    if (response.body().getStatus() == 1) {
                        reviewsDatalist.clear();
                        reviewsDatalist.addAll(response.body().getData().getData());
                        reviewsadapter = new ReviewsAdapter(getActivity(), (BaseActivity) getActivity(), reviewsDatalist);
                        commentsfragmentCommentslist.setAdapter(reviewsadapter);
                        reviewsadapter.notifyDataSetChanged();


                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.commentsfragment_btnaddcomment)
    public void onViewClicked() {
        FragmentManager manager = getFragmentManager();
        RateDialoge rateDialoge = new RateDialoge(CommentsAndRate.this);
        rateDialoge.show(manager,"rate dialoge");
        rateDialoge.Restdata=restaurantdata;
    }
}
