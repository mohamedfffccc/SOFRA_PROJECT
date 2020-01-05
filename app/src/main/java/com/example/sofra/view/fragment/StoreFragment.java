package com.example.sofra.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.UserStoreAdapter;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.view.activity.AuthActivity;
import com.example.sofra.view.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends BaseFragment {


    @BindView(R.id.userstore_storelist)
    RecyclerView userstoreStorelist;
    LinearLayoutManager linearLayoutManager;
    UserStoreAdapter adapter;
    @BindView(R.id.storefragment_tvtotalprice)
    TextView storefragmentTvtotalprice;
    @BindView(R.id.storefragment_btnconfirm)
    Button storefragmentBtnconfirm;
    @BindView(R.id.storefragment_btnaddmore)
    Button storefragmentBtnaddmore;
    @BindView(R.id.storefragment_tvtotal)
    TextView storefragmentTvtotal;
    private RoomDao roomDao;

    public List<OrderItem> data2;
    double total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_store_fragmenr, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        setUpActivity();
        userstoreStorelist.setLayoutManager(linearLayoutManager);


        adapter = new UserStoreAdapter(getActivity(), (BaseActivity) getActivity(), data2);
        userstoreStorelist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < data2.size(); i++) {
            total = total + (data2.get(i).getQuantity() * data2.get(i).getPrice());


        }
        storefragmentTvtotalprice.setText("$ " + String.valueOf(total));


        // Inflate the layout for this fragment
        return root;
    }


    @OnClick({R.id.storefragment_btnconfirm, R.id.storefragment_btnaddmore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.storefragment_btnconfirm:
                try {
                    if (LoadData(getActivity(), API_TOKEN) == (null)) {
                        getActivity().startActivity(new Intent(getActivity(), AuthActivity.class));


                    } else {
                        NewOrderFragment newOrderFragment = new NewOrderFragment();
                        newOrderFragment.data = data2;
                        ReplaceFragment(getActivity().getSupportFragmentManager(), newOrderFragment, R.id.homecycle
                                , null, "medo");


                    }

                } catch (Exception e) {

                }


                break;
            case R.id.storefragment_btnaddmore:
                super.onback();
                super.onback();
                break;
        }
    }

    @Override
    public void onback() {
        super.onback();
    }


}