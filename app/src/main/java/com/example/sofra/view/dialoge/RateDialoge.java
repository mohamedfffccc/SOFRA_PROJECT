package com.example.sofra.view.dialoge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.sofra.R;
import com.example.sofra.adapter.RatingAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.helper.ResturantOffers;
import com.example.sofra.view.fragment.CommentsAndRate;
import com.example.sofra.view.fragment.ResturantDataFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RateDialoge extends DialogFragment {
    @BindView(R.id.ratedialoge_tvrate)
    TextView ratedialogeTvrate;
    @BindView(R.id.ratedialoge_gvrate)
    GridView ratedialogeGvrate;
    @BindView(R.id.ratedialoge_edcomment)
    EditText ratedialogeEdcomment;
    @BindView(R.id.ratedialoge_btncomment)
    Button ratedialogeBtncomment;
    ArrayList<ResturantOffers> data;
    RatingAdapter adapter;
    UserApi userApi;
    public CommentsAndRate commentsAndRate;


    Context context;
    public Restaurant Restdata;
    public int value = 0;

    public RateDialoge(CommentsAndRate commentsAndRate) {
        this.commentsAndRate = commentsAndRate;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialoge_rate, container, false);
        setSharedPreferences(getActivity());
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        addDataToGrid();
        adapter = new RatingAdapter(getActivity(), data);
        ratedialogeGvrate.setAdapter(adapter);
        ratedialogeGvrate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                value = position + 1;
            }
        });

        return root;
    }

    public void addDataToGrid() {
        data = new ArrayList<>();
        data.add(new ResturantOffers(R.drawable.ic_rate1, null));
        data.add(new ResturantOffers(R.drawable.ic_rate222222222222222, null));
        data.add(new ResturantOffers(R.drawable.ic_rate3, null));
        data.add(new ResturantOffers(R.drawable.ic_rate4, null));
        data.add(new ResturantOffers(R.drawable.ic_ratea5, null));
    }

    @OnClick(R.id.ratedialoge_btncomment)
    public void onViewClicked() {


        rateResturant(value, Restdata.getId(), ratedialogeEdcomment.getText().toString(), LoadData(getActivity(), API_TOKEN)
        );
        commentsAndRate.getReviews(Restdata.getId());
        getDialog().dismiss();


    }

    public void rateResturant(int rate, int resturant_id, String comment, String api_token) {
        showProgressDialog(getActivity(), "please wait");
        userApi.rateResturant(rate, comment, resturant_id, api_token).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();


                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                    } else if (response.body().getStatus() == 0) {
                        showNegativeToast(getActivity(), response.body().getMsg());

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });


    }
}
