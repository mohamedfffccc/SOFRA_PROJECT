package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.notifications.NotificationsData;
import com.example.sofra.view.activity.BaseActivity;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;


/**
 * Created by medo on 13/11/2016.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.CategoriesViewHolder> {



    private Context context;
    private BaseActivity activity;
    private List<NotificationsData> notificationsList = new ArrayList<>();
    private Typeface type;
    private UserApi userapi;
    private Date date;

    public NotificationsAdapter(Context context, BaseActivity activity, List<NotificationsData> notificationsList) {
        this.context = context;
        this.activity = activity;
        this.notificationsList = notificationsList;
        userapi = GetClient().create(UserApi.class);
        setSharedPreferences(activity);
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        NotificationsData notificationdata = notificationsList.get(position);
        holder.notificationitemTvtext.setText(notificationdata.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
             date = sdf.parse(notificationdata.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime();
        String timeago = prettyTime.format(date);
        holder.notificationitemTvtime.setText(timeago);


    }


    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notificationitem_ivimage)
        ImageView notificationitemIvimage;
        @BindView(R.id.notificationitem_tvtext)
        TextView notificationitemTvtext;
        @BindView(R.id.notificationitem_tvtime)
        TextView notificationitemTvtime;


        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
