package com.example.sofra.view.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerWithFragmentAdapter;
import com.example.sofra.view.fragment.CurrentOrdersFragment;
import com.example.sofra.view.fragment.LastOrdersFragment;
import com.example.sofra.view.fragment.NewOrdersFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.R.string.current_orders;
import static com.example.sofra.R.string.last_orders;
import static com.example.sofra.R.string.new_orders;

public class OrdersFragment extends Fragment {


    @BindView(R.id.orders_tabhome2)
    TabLayout ordersTabhome;
    @BindView(R.id.orders_viewhome2)
    ViewPager ordersViewhome;
    ViewPagerWithFragmentAdapter2 adapter;
    OrdersLoaderFragment ordersLoaderFragment1 = new OrdersLoaderFragment();
    OrdersLoaderFragment ordersLoaderFragment2 = new OrdersLoaderFragment();
    OrdersLoaderFragment ordersLoaderFragment3 = new OrdersLoaderFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.categories2, container, false);
        ButterKnife.bind(this, root);
        ordersViewhome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ordersTabhome));
        adapter = new ViewPagerWithFragmentAdapter2(getChildFragmentManager());
        adapter.addPager(ordersLoaderFragment1, getString(new_orders), 1);
        ordersLoaderFragment1.key="pending";
        adapter.addPager(ordersLoaderFragment2, getString(current_orders), 2);
        ordersLoaderFragment2.key="current";
        adapter.addPager(ordersLoaderFragment3, getString(last_orders), 3);
        ordersLoaderFragment3.key="completed";
        ordersViewhome.setAdapter(adapter);
        ordersTabhome.setupWithViewPager(ordersViewhome);

        return root;
    }
}