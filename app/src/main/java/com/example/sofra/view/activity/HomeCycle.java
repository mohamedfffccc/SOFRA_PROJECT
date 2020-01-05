package com.example.sofra.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sofra.R;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.view.activity.ui.More;
import com.example.sofra.view.activity.ui.home.HomeFragment;
import com.example.sofra.view.activity.ui.myorders.MyOrders;
import com.example.sofra.view.activity.ui.profile.Profile;
import com.example.sofra.view.fragment.CommissionFragment;
import com.example.sofra.view.fragment.RestaurantHomeFragment;
import com.example.sofra.view.fragment.RestaurantOrdersFragment;
import com.example.sofra.view.fragment.RestaurantProfileFragment;
import com.example.sofra.view.fragment.StoreFragment;
import com.example.sofra.view.orders.OrdersFragment;
import com.example.sofra.view.orders.OrdersLoaderFragment;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;
import static com.example.sofra.data.local.room.RoomManger.getInistance;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;

public class HomeCycle extends BaseActivity {
    private RoomDao roomDao;


    @BindView(R.id.homecycle_homeitem)
    ImageView homecycleHomeitem;
    @BindView(R.id.homecycle_profileitem)
    ImageView homecycleProfileitem;
    @BindView(R.id.homecycle_categoriesitem)
    ImageView homecycleCategoriesitem;
    @BindView(R.id.homecycle_moreitem)
    ImageView homecycleMoreitem;
    @BindView(R.id.homecycle)
    RelativeLayout homecycle;
    @BindView(R.id.addtostore)
    ImageView addtostore;
    @BindView(R.id.notifications)
    ImageView notifications;
    @BindView(R.id.lin)
    LinearLayout lin;
    private List<OrderItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_home_cycle);
        ButterKnife.bind(this);
        setSharedPreferences(HomeCycle.this);
        if (LoadData(HomeCycle.this, USER_TYPE).equals("client")) {
            ReplaceFragment(getSupportFragmentManager(), new HomeFragment(), R.id.homecycle
                    , null, "medo");
            addtostore.setBackgroundResource( R.drawable.ic_local_grocery_store_black_24dp);


        } else if (LoadData(HomeCycle.this, USER_TYPE).equals("resturant")) {
            ReplaceFragment(getSupportFragmentManager(), new RestaurantHomeFragment(), R.id.homecycle
                    , null, "medo");
            addtostore.setBackgroundResource(R.drawable.ic_border_clear_black_24dp);

        }


    }

    @OnClick({R.id.homecycle_homeitem, R.id.homecycle_profileitem, R.id.homecycle_categoriesitem, R.id.homecycle_moreitem, R.id.addtostore, R.id.notifications})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.homecycle_homeitem:
                if (LoadData(HomeCycle.this, USER_TYPE).equals("client")) {
                    ReplaceFragment(getSupportFragmentManager(), new HomeFragment(), R.id.homecycle
                            , null, "medo");

                } else if (LoadData(HomeCycle.this, USER_TYPE).equals("resturant")) {
                    ReplaceFragment(getSupportFragmentManager(), new RestaurantHomeFragment(), R.id.homecycle
                            , null, "medo");

                }

                break;
            case R.id.homecycle_profileitem:

                if (LoadData(HomeCycle.this, USER_TYPE).equals("client")) {
                    ReplaceFragment(getSupportFragmentManager(), new Profile(), R.id.homecycle
                            , null, "medo");

                } else if (LoadData(HomeCycle.this, USER_TYPE).equals("resturant")) {
                    ReplaceFragment(getSupportFragmentManager(), new RestaurantProfileFragment(), R.id.homecycle
                            , null, "medo");

                }
                break;
            case R.id.homecycle_categoriesitem:

                if (LoadData(HomeCycle.this, USER_TYPE).equals("client")) {
                    ReplaceFragment(getSupportFragmentManager(), new MyOrders(), R.id.homecycle
                            , null, "medo");
                }//
                else if (LoadData(HomeCycle.this, USER_TYPE).equals("resturant")) {
                    ReplaceFragment(getSupportFragmentManager(), new RestaurantOrdersFragment(), R.id.homecycle
                            , null, "medo");

                }
                break;
            case R.id.homecycle_moreitem:
                ReplaceFragment(getSupportFragmentManager(), new More(), R.id.homecycle
                        , null, "medo");
                break;
            case R.id.addtostore:
                if (LoadData(HomeCycle.this, USER_TYPE).equals("client")) {

                    StoreFragment storeFragment = new StoreFragment();
                    roomDao = getInistance(HomeCycle.this).roomDao();

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            data = roomDao.getAll();
                            storeFragment.data2 = data;


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ReplaceFragment(getSupportFragmentManager(), storeFragment, R.id.homecycle
                                            , null, "medo");

                                }
                            });
                        }

                    });
                }
                else if (LoadData(HomeCycle.this, USER_TYPE).equals("resturant"))
                {
                    ReplaceFragment(getSupportFragmentManager(), new CommissionFragment(), R.id.homecycle
                            , null, "medo");

                }


                break;
            case R.id.notifications:
                Intent intent = new Intent(HomeCycle.this, NotificationsActivity.class);
                startActivity(intent);
                break;
        }
    }


}
