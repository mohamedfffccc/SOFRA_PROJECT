package com.example.sofra.view.fragment.orders.restaurantorders;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.orders.OrdersData;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

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
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {
    public OrdersData data;
    ArrayAdapter<String> adapter;
    @BindView(R.id.orderdetails_tvdetails)
    TextView orderdetailsTvdetails;
    @BindView(R.id.orderdetails_ivorderimage)
    CircularImageView orderdetailsIvorderimage;
    @BindView(R.id.orderdetails_tvname)
    TextView orderdetailsTvname;
    @BindView(R.id.orderdetails_tvdate)
    TextView orderdetailsTvdate;
    @BindView(R.id.orderdetails_relim)
    RelativeLayout orderdetailsRelim;
    @BindView(R.id.orderdetails_tvaddress)
    TextView orderdetailsTvaddress;
    @BindView(R.id.orderdetails_lindate)
    LinearLayout orderdetailsLindate;
    @BindView(R.id.orderdetails_lvitems)
    ListView orderdetailsLvitems;
    @BindView(R.id.orderdetails_tvordercost)
    TextView orderdetailsTvordercost;
    @BindView(R.id.orderdetails_tvdeliverycost)
    TextView orderdetailsTvdeliverycost;
    @BindView(R.id.orderdetails_tvtotal)
    TextView orderdetailsTvtotal;
    @BindView(R.id.orderdetails_tvpayment)
    TextView orderdetailsTvpayment;
    @BindView(R.id.orderdetails_lintexts)
    LinearLayout orderdetailsLintexts;
    @BindView(R.id.orderdetails_ivdelete)
    ImageView orderdetailsIvdelete;
    @BindView(R.id.orderdetails_btncancel)
    Button orderdetailsBtncancel;
    @BindView(R.id.orderdetails_lin1)
    LinearLayout orderdetailsLin1;
    @BindView(R.id.orderdetails_ivdagree)
    ImageView orderdetailsIvdagree;
    @BindView(R.id.orderdetails_btn_agree)
    Button orderdetailsBtnAgree;
    @BindView(R.id.orderdetails_lin2)
    LinearLayout orderdetailsLin2;
    @BindView(R.id.orderdetails_ivdcall)
    ImageView orderdetailsIvdcall;
    @BindView(R.id.orderdetails_btn_call)
    Button orderdetailsBtnCall;
    @BindView(R.id.orderdetails_lin3)
    LinearLayout orderdetailsLin3;

    private List<String> data2 = new ArrayList<>();
    double ordercost = 0;
    private UserApi userapi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, root);
        setData();
        userapi=GetClient().create(UserApi.class);
        // Inflate the layout for this fragment
        return root;
    }

    public void setData() {
        Glide.with(getActivity()).load(data.getRestaurant().getPhotoUrl()).into(orderdetailsIvorderimage);
        orderdetailsTvname.setText(data.getClient().getName());
        orderdetailsTvdate.setText(data.getCreatedAt());
        orderdetailsTvaddress.setText(getActivity().getString(R.string.address) + " " + data.getAddress());
        orderdetailsTvdeliverycost.setText(getActivity().getString(R.string.delivery_cost) + data.getRestaurant().getDeliveryCost());
        data2.add(getString(R.string.order_details));
        for (int i = 0; i < data.getItems().size(); i++) {
            data2.add(data.getItems().get(i).getName() + "                                                  " + data.getItems().get(i).getPrice() + "$");
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textView, data2);
            orderdetailsLvitems.setAdapter(adapter);
            ordercost = ordercost + Double.parseDouble(data.getItems().get(i).getPrice());
            orderdetailsTvordercost.setText(getString(R.string.order_cost) + String.valueOf(ordercost));


        }
        double total = ordercost + Double.parseDouble(data.getRestaurant().getDeliveryCost());
        orderdetailsTvtotal.setText(getString(R.string.total) + "" + total);
        if (data.getPaymentMethodId().equals("1")) {
            orderdetailsTvpayment.setText(getString(R.string.payment) + " cash");

        } else {
            orderdetailsTvpayment.setText(getString(R.string.payment) + " online");


        }
        if (data.getState().equals("pending")) {

        } else if (data.getState().equals("accepted")) {

            orderdetailsLin1.setVisibility(View.GONE);
            orderdetailsBtnAgree.setText(R.string.confirm_delivery);
            orderdetailsIvdagree.setImageResource(R.drawable.ic_like);
            orderdetailsBtnCall.setText(data.getClient().getPhone());
        } else if (data.getState().equals("delivered")) {
            orderdetailsBtnAgree.setText(R.string.completed_order);
            orderdetailsBtnAgree.setGravity(Gravity.CENTER);
            orderdetailsLin1.setVisibility(View.GONE);
            orderdetailsLin3.setVisibility(View.GONE);
            orderdetailsBtncancel.setBackgroundResource(R.drawable.accepteditembackground);
            orderdetailsLin2.setBackgroundResource(R.drawable.accepteditembackground);
            orderdetailsIvdagree.setVisibility(View.GONE);
        } else if (data.getState().equals("declined") || data.getState().equals("rejected")) {
            orderdetailsBtnCall.setText(R.string.declined_order);
            orderdetailsBtnCall.setGravity(Gravity.CENTER);
            orderdetailsBtnCall.setBackgroundResource(R.drawable.callbuttonbackground);
            orderdetailsLin3.setBackgroundResource(R.drawable.callbuttonbackground);
            orderdetailsLin2.setVisibility(View.GONE);
            orderdetailsLin1.setVisibility(View.GONE);
            orderdetailsIvdcall.setVisibility(View.GONE);
        }


    }


    @OnClick({R.id.orderdetails_btncancel, R.id.orderdetails_btn_agree, R.id.orderdetails_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.orderdetails_btncancel:
                if (data.getState().equals("pending")) {
rejectOrder(data.getId());
                }

                break;
            case R.id.orderdetails_btn_agree:
                if (data.getState().equals("pending")) {
                    acceptOrder(data.getId());

                } else if (data.getState().equals("accepted")) {
                    confirmOrder(data.getId());
                }


                    break;
            case R.id.orderdetails_btn_call:
                if (!data.getState().equals("delivered")) {
                    Intent intent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:"+data.getClient().getPhone()));
                    getActivity().startActivity(intent);

                }
                break;
        }
    }
    private void acceptOrder(int id) {
        showProgressDialog(getActivity(),getActivity().getString(R.string.please_wait));
        userapi.acceptOrder(LoadData(getActivity() , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        showPositiveToast(getActivity() , response.body().getMsg());

                    }
                    else {
                        showNegativeToast(getActivity() , response.body().getMsg());

                    }

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
    private void confirmOrder(int id) {
        showProgressDialog(getActivity(),getActivity().getString(R.string.please_wait));
        userapi.confirmOrder(LoadData(getActivity() , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        showPositiveToast(getActivity() , response.body().getMsg());

                    }
                    else {
                        showNegativeToast(getActivity() , response.body().getMsg());

                    }

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
    private void rejectOrder(int id) {
        showProgressDialog(getActivity(),getActivity().getString(R.string.please_wait));
        userapi.rejectOrder(LoadData(getActivity() , REST_API_TOKEN) ,id ).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        showPositiveToast(getActivity() , response.body().getMsg());

                    }
                    else {
                        showNegativeToast(getActivity() , response.body().getMsg());

                    }

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




}
