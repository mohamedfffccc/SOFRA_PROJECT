package com.example.sofra.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.generalresponse.RegionsData;
import com.example.sofra.helper.ResturantDataSave;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;


public class RegisterResturantStep1Fragment extends Fragment {
    GeneralResponseAdapter adapter;
    ArrayList<RegionsData> city , region;
    UserApi userApi;
    String resturant_name;
    String email;
    String delivery_time;
    int cityid;
    int regionid;
    String password;
    String password_confirmation;
    String minimum_order;
    String deliver_cost;
    ResturantDataSave data;

    @BindView(R.id.registerfresturantragment1_edname)
    EditText registerfresturantragment1Edname;
    @BindView(R.id.registerfresturantragment1_edemail)
    EditText registerfresturantragment1Edemail;
    @BindView(R.id.registerfresturantragment1_edtime)
    EditText registerfresturantragment1Edtime;
    @BindView(R.id.registerfresturantragment1_spcity)
    Spinner registerfresturantragment1Spcity;
    @BindView(R.id.registerfresturantragment1_edvillage)
    Spinner registerfresturantragment1Edvillage;
    @BindView(R.id.registerfresturantragment1_edpassword)
    EditText registerfresturantragment1Edpassword;
    @BindView(R.id.registerfresturantragment1_passwordconfirmation)
    EditText registerfresturantragment1Passwordconfirmation;
    @BindView(R.id.registerfresturantragment1_minimumorder)
    EditText registerfresturantragment1Minimumorder;
    @BindView(R.id.registerfresturantragment1_delivercost)
    EditText registerfresturantragment1Delivercost;
    @BindView(R.id.registerresturantfragment_btnnext)
    Button registerresturantfragmentBtnnext;
    RegisterResturantStep2Fragment registerResturantStep2Fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_resturant_step1, container, false);
        registerResturantStep2Fragment=new RegisterResturantStep2Fragment();

        ButterKnife.bind(this,root);
        userApi=GetClient().create(UserApi.class);

        //TODO CITY
        addCity();

        adapter=new GeneralResponseAdapter(getActivity() , city, "select city");
        registerfresturantragment1Spcity.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerfresturantragment1Spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addRegion(position);
                adapter=new GeneralResponseAdapter(getActivity() , region, "select region");
                registerfresturantragment1Edvillage.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //TODO REGION

        // Inflate the layout for this fragment
        return root;
    }

    @OnClick(R.id.registerresturantfragment_btnnext)
    public void onViewClicked() {
        parceData();
        registerResturantStep2Fragment.resturantData=data;
        ReplaceFragment( getActivity().getSupportFragmentManager(), registerResturantStep2Fragment, R.id.usercycle_activity
                , null, "medo");

    }
    public void addCity()
    {
        city =new ArrayList<>();
        userApi.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus()==1) {
                    showPositiveToast(getActivity() , response.body().getMsg());

                    city.addAll(response.body().getData().getData());
                    Toast.makeText(getActivity() , city.toString() , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }
    public void addRegion(int city_id)
    {
        region =new ArrayList<>();
        userApi.getRegions(city_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus()==1) {
                    showPositiveToast(getActivity() , response.body().getMsg());

                    region.addAll(response.body().getData().getData());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }
    public void parceData()
    {
        resturant_name=registerfresturantragment1Edname.getText().toString();
        email = registerfresturantragment1Edemail.getText().toString();
        delivery_time=registerfresturantragment1Edtime.getText().toString();
        cityid=registerfresturantragment1Spcity.getSelectedItemPosition();
        regionid=registerfresturantragment1Edvillage.getSelectedItemPosition();
        password=registerfresturantragment1Edpassword.getText().toString();
        password_confirmation=registerfresturantragment1Passwordconfirmation.getText().toString();
        minimum_order=registerfresturantragment1Minimumorder.getText().toString();
        deliver_cost=registerfresturantragment1Delivercost.getText().toString();
        data = new ResturantDataSave(resturant_name,email,
                delivery_time,cityid,
                regionid,password,
                password_confirmation,
                minimum_order,deliver_cost);

    }

    // TODO: Rename method, update argument and hook method into UI event

}
