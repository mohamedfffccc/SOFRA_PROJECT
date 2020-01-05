package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerWithFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.R.string.current_orders;
import static com.example.sofra.R.string.last_orders;
import static com.example.sofra.R.string.new_orders;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantOrdersFragment extends Fragment {


    @BindView(R.id.restaurantorders_tabhome)
    TabLayout restaurantordersTabhome;
    @BindView(R.id.restaurantorders_viewhome)
    ViewPager restaurantordersViewhome;
    private ViewPagerWithFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_orders, container, false);
        ButterKnife.bind(this, root);
        restaurantordersViewhome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(restaurantordersTabhome));
        adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        adapter.addPager(new RestaurantNewOrders(), getString(new_orders) , 1);
        adapter.addPager(new RestaurantCurrentOrders(), getString(current_orders) , 2) ;
        adapter.addPager(new RestaurantLastOrders(), getString(last_orders) , 3);
        restaurantordersViewhome.setAdapter(adapter);
        restaurantordersTabhome.setupWithViewPager(restaurantordersViewhome);

        return root;
        // Inflate the layout for this fragment
    }

}
