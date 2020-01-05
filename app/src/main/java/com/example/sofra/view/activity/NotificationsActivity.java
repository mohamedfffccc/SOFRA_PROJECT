package com.example.sofra.view.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.NotificationsAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.model.notifications.Notifications;
import com.example.sofra.data.model.notifications.NotificationsData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;

public class NotificationsActivity extends AppCompatActivity {

    @BindView(R.id.notifications_vlist)
    RecyclerView notificationsVlist;
    UserApi userApi;
    NotificationsAdapter adapter;
    ArrayList<NotificationsData> datalist;
    LinearLayoutManager linearLayoutManager;
    private BaseActivity baseactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        baseactivity = new BaseActivity();
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(NotificationsActivity.this);
        linearLayoutManager=new LinearLayoutManager(this);
        notificationsVlist.setLayoutManager(linearLayoutManager);
        datalist=new ArrayList<>();
        if (LoadData(baseactivity , USER_TYPE).equals("client")) {


            getNotifications();

        }
        else if (LoadData(NotificationsActivity.this , USER_TYPE).equals("resturant"))
        {
            getRestNotifications();

        }
    }

    private void getNotifications() {
       userApi.getNotifications(LoadData(baseactivity ,API_TOKEN)).enqueue(new Callback<Notifications>() {
           @Override
           public void onResponse(Call<Notifications> call, Response<Notifications> response) {
               try {
                   if (response.body().getStatus()==1) {
                       datalist.addAll(response.body().getData().getData());
                       adapter=new NotificationsAdapter(NotificationsActivity.this , baseactivity , datalist);
                       notificationsVlist.setAdapter(adapter);
                   }

               }
               catch (Exception e)
               {

               }
           }

           @Override
           public void onFailure(Call<Notifications> call, Throwable t) {

           }
       });
    }
    private void getRestNotifications() {
        userApi.getRestNotifications(LoadData(baseactivity ,REST_API_TOKEN)).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                try {
                    if (response.body().getStatus()==1) {
                        datalist.addAll(response.body().getData().getData());
                        adapter=new NotificationsAdapter(NotificationsActivity.this , baseactivity , datalist);
                        notificationsVlist.setAdapter(adapter);
                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
