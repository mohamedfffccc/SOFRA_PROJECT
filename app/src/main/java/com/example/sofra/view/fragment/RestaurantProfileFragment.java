package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.generalresponse.RegionsData;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadList;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.AVAILABILITY;
import static com.example.sofra.data.local.SofraConstants.CITY_ID;
import static com.example.sofra.data.local.SofraConstants.DELIVERY_COST;
import static com.example.sofra.data.local.SofraConstants.MINIMUM_ORDER;
import static com.example.sofra.data.local.SofraConstants.REGION_ID;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstants.REST_IMAGE;
import static com.example.sofra.data.local.SofraConstants.REST_NAME;
import static com.example.sofra.data.local.SofraConstants.REST_PASSWORD;
import static com.example.sofra.data.local.SofraConstants.REST_PHONE;
import static com.example.sofra.data.local.SofraConstants.WHATS_UP;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantProfileFragment extends Fragment {


    @BindView(R.id.restaurantprofile_edname)
    EditText restaurantprofileEdname;
    @BindView(R.id.restaurantprofile_edemail)
    EditText restaurantprofileEdemail;
    @BindView(R.id.restaurantprofile_edphone)
    EditText restaurantprofileEdphone;
    @BindView(R.id.restaurantprofile_spcity)
    Spinner restaurantprofileSpcity;
    @BindView(R.id.restaurantprofile_spregion)
    Spinner restaurantprofileSpregion;
    @BindView(R.id.restaurantprofile_edpassword)
    EditText restaurantprofileEdpassword;
    @BindView(R.id.restaurantprofile_spcategoryname)
    EditText restaurantprofileSpcategoryname;
    @BindView(R.id.restaurantprofile_eddelivercost)
    EditText restaurantprofileEddelivercost;
    @BindView(R.id.restaurantprofile_edminimumorder)
    EditText restaurantprofileEdminimumorder;
    @BindView(R.id.restaurantprofile_swstate)
    Switch restaurantprofileSwstate;
    @BindView(R.id.restaurantprofile_edphonecommunications)
    EditText restaurantprofileEdphonecommunications;
    @BindView(R.id.restaurantprofile_edwhatsup)
    EditText restaurantprofileEdwhatsup;
    @BindView(R.id.restaurantprofile_ivrestaurantimage)
    CircularImageView restaurantprofileIvrestaurantimage;
    GeneralResponseAdapter adapter;
    ArrayList<RegionsData> city, region;
    @BindView(R.id.restaurantprofile_btnedit)
    Button restaurantprofileBtnedit;
    private UserApi userApi;
    StringBuilder sb = new StringBuilder();
    List<String> data = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_profile, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());
        addCity();
        adapter = new GeneralResponseAdapter(getActivity(), city, getActivity().getString(R.string.select_city));
        restaurantprofileSpcity.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addData();
        restaurantprofileSwstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (LoadData(getActivity(),AVAILABILITY).equals("open")) {
                    changeState("closed");
                }
                else if (LoadData(getActivity(),AVAILABILITY).equals("closed"))
                {
                    changeState("open");
                }
            }
        });
        return root;
    }

    private void addData() {
        Glide.with(getActivity()).load(LoadData(getActivity(), REST_IMAGE)).into(restaurantprofileIvrestaurantimage);
        restaurantprofileEddelivercost.setText(LoadData(getActivity(), DELIVERY_COST));
        restaurantprofileEdminimumorder.setText(LoadData(getActivity(), MINIMUM_ORDER));
        restaurantprofileEdname.setText(LoadData(getActivity(), REST_NAME));
        restaurantprofileEdphone.setText(LoadData(getActivity(), REST_PHONE));
        restaurantprofileEdphonecommunications.setText(LoadData(getActivity(), REST_PHONE));
        restaurantprofileEdwhatsup.setText(LoadData(getActivity(), WHATS_UP));
        restaurantprofileEdpassword.setText(LoadData(getActivity(), REST_PASSWORD));
        restaurantprofileEdemail.setText(LoadData(getActivity(), REST_EMAIL));
        if (LoadData(getActivity(), AVAILABILITY).equals("open")) {
            restaurantprofileSwstate.setChecked(true);
        } else    if (LoadData(getActivity(), AVAILABILITY).equals("closed")) {
            restaurantprofileSwstate.setChecked(false);
        }
        data.addAll(LoadList(getActivity(), "categories"));
        for (int i = 0; i < data.size(); i++) {
            sb.append(data.get(i) + " - ");

        }
        restaurantprofileSpcategoryname.setText(sb.toString());
    }

    public void addCity() {
        city = new ArrayList<>();
        userApi.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus() == 1) {
                    showPositiveToast(getActivity(), response.body().getMsg());

                    city.addAll(response.body().getData().getData());
                    restaurantprofileSpcity.setSelection(Integer.parseInt(LoadData(getActivity(), CITY_ID)));
                    addRegion(restaurantprofileSpcity.getSelectedItemPosition());

                    adapter = new GeneralResponseAdapter(getActivity(), region, "select region");
                    restaurantprofileSpregion.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void addRegion(int city_id) {
        region = new ArrayList<>();
        userApi.getRegions(city_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    showPositiveToast(getActivity(), response.body().getMsg());

                    region.addAll(response.body().getData().getData());
                    restaurantprofileSpregion.setSelection(Integer.parseInt(LoadData(getActivity(), REGION_ID)));

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }
    public void changeState(String state)
    {
        showProgressDialog(getActivity() , getString(R.string.please_wait));
        userApi.changeState(state , LoadData(getActivity(),REST_API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus()==1) {
                    showPositiveToast(getActivity() , "state changed");
                }


            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }





}
