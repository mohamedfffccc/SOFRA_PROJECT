package com.example.sofra.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.view.activity.HomeCycle;

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
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.saveList;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.AVAILABILITY;
import static com.example.sofra.data.local.SofraConstants.CATEGORIES_NAMES;
import static com.example.sofra.data.local.SofraConstants.CITY_ID;
import static com.example.sofra.data.local.SofraConstants.CITY_NAME;
import static com.example.sofra.data.local.SofraConstants.DELIVERY_COST;
import static com.example.sofra.data.local.SofraConstants.EMAIL;
import static com.example.sofra.data.local.SofraConstants.MINIMUM_ORDER;
import static com.example.sofra.data.local.SofraConstants.NAME;
import static com.example.sofra.data.local.SofraConstants.PASSWORD;
import static com.example.sofra.data.local.SofraConstants.PHONE;
import static com.example.sofra.data.local.SofraConstants.PROFILE_IMAGE;
import static com.example.sofra.data.local.SofraConstants.REGION_ID;
import static com.example.sofra.data.local.SofraConstants.REGION_NAME;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.REST_CITY_ID;
import static com.example.sofra.data.local.SofraConstants.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstants.REST_IMAGE;
import static com.example.sofra.data.local.SofraConstants.REST_NAME;
import static com.example.sofra.data.local.SofraConstants.REST_PASSWORD;
import static com.example.sofra.data.local.SofraConstants.REST_PHONE;
import static com.example.sofra.data.local.SofraConstants.REST_REGION_ID;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;
import static com.example.sofra.data.local.SofraConstants.WHATS_UP;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    UserApi userApi;
    String user_type;


    @BindView(R.id.loginfragment_edemail)
    EditText loginfragmentEdemail;
    @BindView(R.id.loginfragment_edpassword)
    EditText loginfragmentEdpassword;
    @BindView(R.id.loginfragment_btnlogin)
    Button loginfragmentBtnlogin;
    @BindView(R.id.loginfragment_ivregister)
    ImageView loginfragmentIvregister;
    RegisterResturantStep1Fragment registerResturantStep1Fragment;
    ClientRegisterFragment clientRegisterFragment;
    @BindView(R.id.loginfragment_tv_forget)
    TextView loginfragmentTvForget;

    private ArrayList<String> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        user_type = LoadData(getActivity(), USER_TYPE);
        registerResturantStep1Fragment = new RegisterResturantStep1Fragment();
        clientRegisterFragment = new ClientRegisterFragment();
        setSharedPreferences(getActivity());
        return root;
    }

    @OnClick({R.id.loginfragment_btnlogin, R.id.loginfragment_ivregister, R.id.loginfragment_tv_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginfragment_btnlogin:
                if (user_type.equals("resturant")) {
                    loginRestaurant(loginfragmentEdemail.getText().toString(),
                            loginfragmentEdpassword.getText().toString());
                } else if (user_type.equals("client")) {
                    loginUser(loginfragmentEdemail.getText().toString(),
                            loginfragmentEdpassword.getText().toString());
                }
                break;
            case R.id.loginfragment_ivregister:
                if (user_type.equals("resturant")) {
                    ReplaceFragment(getActivity().getSupportFragmentManager(), registerResturantStep1Fragment, R.id.usercycle_activity
                            , null, "medo");
                } else if (user_type.equals("client")) {
                    ReplaceFragment(getActivity().getSupportFragmentManager(), clientRegisterFragment, R.id.usercycle_activity
                            , null, "medo");
                }
                break;
            case R.id.loginfragment_tv_forget:

                ForgetPasswordFragment forgetPasswordFragment = new ForgetPasswordFragment();
                ReplaceFragment(getActivity().getSupportFragmentManager(), forgetPasswordFragment, R.id.usercycle_activity
                        , null, "medo");

                break;
        }
    }

    public void loginUser(String email, String password) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.loginUser(email, password).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        SaveData(getActivity(), API_TOKEN, response.body().getData().getApiToken());
                        SaveData(getActivity(), PROFILE_IMAGE, response.body().getData().getUser().getPhotoUrl());
                        SaveData(getActivity(), PASSWORD, loginfragmentEdpassword.getText().toString());

                        SaveData(getActivity(), NAME, response.body().getData().getUser().getName());
                        SaveData(getActivity(), EMAIL, response.body().getData().getUser().getEmail());
                        SaveData(getActivity(), PHONE, response.body().getData().getUser().getPhone());
                        SaveData(getActivity(), CITY_ID, response.body().getData().getUser().getRegion().getCityId());
                        SaveData(getActivity() ,CITY_NAME , response.body().getData().getUser().getRegion().getCity().getName());
                        SaveData(getActivity(), REGION_ID, String.valueOf(response.body().getData().getUser().getRegion().getId()));
                        SaveData(getActivity() , REGION_NAME , response.body().getData().getUser().getRegion().getName());
getActivity().finish();

                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });
    }

    public void loginRestaurant(String email, String password) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.loginRestaurant(email, password).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        SaveData(getActivity(), REST_API_TOKEN, response.body().getData().getApiToken());
                        SaveData(getActivity(), REST_IMAGE, response.body().getData().getUser().getPhotoUrl());
                        SaveData(getActivity(), REST_PASSWORD, loginfragmentEdpassword.getText().toString());

                        SaveData(getActivity(), REST_NAME, response.body().getData().getUser().getName());
                        SaveData(getActivity(), REST_EMAIL, response.body().getData().getUser().getEmail());
                        SaveData(getActivity(), REST_PHONE, response.body().getData().getUser().getPhone());
                        SaveData(getActivity(), REST_CITY_ID, response.body().getData().getUser().getRegion().getCityId());
                        SaveData(getActivity(), REST_REGION_ID, String.valueOf(response.body().getData().getUser().getRegion().getId()));
                        SaveData(getActivity(), DELIVERY_COST, response.body().getData().getUser().getDeliveryCost());
                        SaveData(getActivity(), MINIMUM_ORDER, response.body().getData().getUser().getMinimumCharger());
                        SaveData(getActivity(), DELIVERY_COST, response.body().getData().getUser().getDeliveryCost());
                        SaveData(getActivity(), AVAILABILITY, response.body().getData().getUser().getAvailability());
                        SaveData(getActivity(), WHATS_UP, response.body().getData().getUser().getWhatsapp());
                        for (int i = 0; i < response.body().getData().getUser().getCategories().size(); i++) {
                            categories.add(response.body().getData().getUser().getCategories().get(i).getName());
                            saveList(getActivity(), CATEGORIES_NAMES, categories);

                        }

                        Toast.makeText(getActivity(), response.body().getData().getUser().getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), HomeCycle.class);
                        startActivity(intent);

                    } else {

                        showNegativeToast(getActivity(), response.body().getMsg());
                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                dismissProgressDialog();
            }
        });

    }


}
