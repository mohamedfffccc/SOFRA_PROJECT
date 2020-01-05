package com.example.sofra.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerWithFragmentAdapter;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.adapter.ViewPagerWithFragmentAdapter.vis;

public class ResturantDataFragment extends Fragment {
    @BindView(R.id.tab_home)
    TabLayout tabHome;
    @BindView(R.id.viewpagerhome)
    ViewPager viewpagerhome;
    FoodList foodList = new FoodList();
    @BindView(R.id.reldata)
    RelativeLayout reldata;
    @BindView(R.id.resturantdata_resturantimage)
    ImageView resturantdataResturantimage;
    @BindView(R.id.resturantdata_resturantname)
    TextView resturantdataResturantname;

    public Restaurant data;
    ResturantInfo resturantInfo = new ResturantInfo();
    private ViewPagerWithFragmentAdapter adapter;
    public boolean state;
    CommentsAndRate commentsAndRate = new CommentsAndRate();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_resturant_data, container, false);
        ButterKnife.bind(this, root);
//        rel.setVisibility(View.GONE);
        viewpagerhome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHome));
        adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());

        adapter.addPager(foodList, " food list", 1);
        adapter.addPager(commentsAndRate, "   comments and rates", 2);
        adapter.addPager(resturantInfo, " restaurant info", 3);
        viewpagerhome.setAdapter(adapter);
//        Glide.with(getActivity()).load(data.getPhotoUrl()).into(resturantdataResturantimage);
        resturantdataResturantname.setText(data.getName());
        tabHome.setupWithViewPager(viewpagerhome);
        foodList.resturantData2 = data;
        resturantInfo.resturantData = data;
        commentsAndRate.restaurantdata = data;

        if (vis == 1) {
            reldata.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return root;
    }

    public void setvisibility(ImageView iv) {


    }


}
