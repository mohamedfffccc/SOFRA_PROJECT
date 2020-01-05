package com.example.sofra.view.activity.ui.myorders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerWithFragmentAdapter;
import com.example.sofra.view.fragment.BaseFragment;
import com.example.sofra.view.fragment.CurrentOrdersFragment;
import com.example.sofra.view.fragment.LastOrdersFragment;
import com.example.sofra.view.fragment.NewOrdersFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.R.string.*;

public class MyOrders extends Fragment {


    @BindView(R.id.orders_tabhome)
    TabLayout ordersTabhome;
    @BindView(R.id.orders_viewhome)
    ViewPager ordersViewhome;
    ViewPagerWithFragmentAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.categories, container, false);
        ButterKnife.bind(this, root);
        ordersViewhome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ordersTabhome));
        adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        adapter.addPager(new NewOrdersFragment(), getString(new_orders), 1);
        adapter.addPager(new CurrentOrdersFragment(), getString(current_orders), 2);
        adapter.addPager(new LastOrdersFragment(), getString(last_orders), 3);
        ordersViewhome.setAdapter(adapter);
        ordersTabhome.setupWithViewPager(ordersViewhome);

        return root;
    }
}