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

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.fragment.NewItemFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


/**
 * Created by medo on 13/11/2016.
 */

public class CategoriesFoodItemsAdapter extends RecyclerView.Adapter<CategoriesFoodItemsAdapter.CategoriesFoodViewHolder> {



    private Context context;
    private BaseActivity activity;
    private List<OffersData> categoryfoodList = new ArrayList<>();
    private Typeface type;
    private Typeface type2;
    UserApi userApi;
    private int position;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public CategoriesFoodItemsAdapter(Context context, BaseActivity activity, List<OffersData> categoryfoodList) {
        this.context = context;
        this.activity = activity;
        this.categoryfoodList = categoryfoodList;
        setSharedPreferences(activity);
        userApi = GetClient().create(UserApi.class);
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @NonNull
    @Override
    public CategoriesFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoryfood_item, parent, false);
        return new CategoriesFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesFoodViewHolder holder, int position) {
        OffersData categoryfooddata = categoryfoodList.get(position);
        viewBinderHelper.bind(holder.categoryfoodsSwipe1, String.valueOf(categoryfooddata.getId()));

        Glide.with(context).load(categoryfooddata.getPhotoUrl()).into(holder.categoriesfooditemCategoryimage);
        holder.categoryfooditemCategoryname.setText(categoryfooddata.getName());
        holder.categoriesfooditemTvdesc.setText(categoryfooddata.getDescription());
        holder.categoriesfooditemCategoryprice.setText(categoryfooddata.getPrice() + " $");
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont3.otf");
        type2 = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont2.ttf");

        holder.categoryfooditemCategoryname.setTypeface(type);
        holder.categoriesfooditemTvdesc.setTypeface(type2);
        holder.categoriesfooditemCategoryprice.setTypeface(type2);
        holder.categoryfooditemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(categoryfoodList.get(position).getId());

            }
        });
        holder.categoryfooditemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData(activity, "additem", "updateitem");
                NewItemFragment newItemFragment = new NewItemFragment();
                newItemFragment.offersData = categoryfoodList.get(position);
                ReplaceFragment(activity.getSupportFragmentManager(), newItemFragment, R.id.homecycle
                        , null, "medo");
            }
        });


    }

    public void deleteItem(int id) {
        showProgressDialog(activity, activity.getString(R.string.please_wait));
        userApi.deleteItem(id, LoadData(activity, REST_API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(activity, response.body().getMsg());
                        categoryfoodList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        showNegativeToast(activity, response.body().getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryfoodList.size();
    }

    public class CategoriesFoodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryfooditem_delete)
        ImageView categoryfooditemDelete;
        @BindView(R.id.categoryfooditem_edit)
        ImageView categoryfooditemEdit;
        @BindView(R.id.categoriesfooditem_categoryimage)
        ImageView categoriesfooditemCategoryimage;
        @BindView(R.id.categoryfooditem_categoryname)
        TextView categoryfooditemCategoryname;
        @BindView(R.id.categoriesfooditem_tvprice)
        TextView categoriesfooditemTvprice;
        @BindView(R.id.categoriesfooditem_categoryprice)
        TextView categoriesfooditemCategoryprice;

        @BindView(R.id.categoriesfooditem_tvdesc)
        TextView categoriesfooditemTvdesc;
        @BindView(R.id.categoryfoods_swipe1)
        SwipeRevealLayout categoryfoodsSwipe1;

        public CategoriesFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
