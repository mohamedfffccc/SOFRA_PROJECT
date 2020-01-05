package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.data.model.generalresponse.GeneralResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.CITY_NAME;
import static com.example.sofra.data.local.SofraConstants.NAME;
import static com.example.sofra.data.local.SofraConstants.PHONE;
import static com.example.sofra.data.local.SofraConstants.REGION_NAME;
import static com.example.sofra.data.local.SofraConstants.RESTURANT_DELIVERY_COST;
import static com.example.sofra.data.local.room.RoomManger.getInistance;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends Fragment {
    public List<OrderItem> data;
    public Restaurant restauran;
    List<String> notes = new ArrayList<>();
    List<Integer> quantities = new ArrayList<>();
    List<Integer> items = new ArrayList<>();
    int paymentmethod_id;
    @BindView(R.id.neworder_etdetails)
    EditText neworderEtdetails;
    @BindView(R.id.neworder_tvaddress)
    TextView neworderTvaddress;
    @BindView(R.id.neworder_etaddress)
    EditText neworderEtaddress;
    @BindView(R.id.neworder_btncash)
    RadioButton neworderBtncash;
    @BindView(R.id.neworder_btnonline)
    RadioButton neworderBtnonline;
    @BindView(R.id.neworder_rgpayment)
    RadioGroup neworderRgpayment;
    @BindView(R.id.neworder_tvtotal)
    TextView neworderTvtotal;
    @BindView(R.id.neworder_tvdeliverycost)
    TextView neworderTvdeliverycost;
    @BindView(R.id.neworder_tvtotalcost)
    TextView neworderTvtotalcost;
    @BindView(R.id.neworder_btnconfirm)
    Button neworderBtnconfirm;
    double total,totalcost;
    UserApi userApi;
    RoomDao roomDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_order, container, false);
        ButterKnife.bind(this, root);
        userApi=GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());
        roomDao=getInistance(getActivity()).roomDao();
        setData();
        neworderRgpayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.neworder_btncash :
                        paymentmethod_id=1;
                        break;
                    case R.id.neworder_btnonline :
                        paymentmethod_id=2;
                        break;
                }
            }
        });
        return root;
    }

    @OnClick(R.id.neworder_btnconfirm)
    public void onViewClicked() {
        requestNewOrder();
        deleteAll();
    }
    public void  setData()
    {
        neworderEtaddress.setText(LoadData(getActivity() , CITY_NAME) + "-" + LoadData(getActivity() , REGION_NAME));
        for (int i = 0; i < data.size(); i++) {
            total=total+(data.get(i).getQuantity() * data.get(i).getPrice());
            notes.add(data.get(i).getNote());
            quantities.add(data.get(i).getQuantity());
            items.add(data.get(i).getItem_id());


        }
        neworderTvtotal.setText(getActivity().getString(R.string.total) + String.valueOf(total) + " $");


        neworderTvdeliverycost.setText(getActivity().getString(R.string.delivery_cost) +  LoadData(getActivity(),RESTURANT_DELIVERY_COST) + "$");
        totalcost=total+Double.parseDouble(LoadData(getActivity(),RESTURANT_DELIVERY_COST));
        neworderTvtotalcost.setText(getActivity().getString(R.string.total_cost) +  String.valueOf(totalcost) + "$");


    }
    public void requestNewOrder()
    {
        showProgressDialog(getActivity() , getActivity().getString(R.string.please_wait));
        userApi.addNewOrder(data.get(0).restaurant_id , neworderEtdetails.getText().toString() ,
                neworderEtaddress.getText().toString() , paymentmethod_id ,
                LoadData(getActivity(),PHONE) , LoadData(getActivity(),NAME) ,
                LoadData(getActivity(),API_TOKEN) , items , quantities , notes)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus()==1) {
showPositiveToast(getActivity() , response.body().getMsg());                            }

                        }
                        catch (Exception e)
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
    }
    public void deleteAll()
    {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                roomDao.delAll();
            }
        });
    }
}
