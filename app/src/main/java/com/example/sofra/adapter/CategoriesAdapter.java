package com.example.sofra.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.view.activity.BaseActivity;
import com.example.sofra.view.dialoge.NewCategoryDialoge;
import com.example.sofra.view.fragment.CategoryFoodItems;
import com.example.sofra.view.fragment.RestaurantHomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<Category> categoryList = new ArrayList<>();
    private Typeface type;
    private UserApi userapi;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    RestaurantHomeFragment restaurantHomeFragment=new RestaurantHomeFragment();

    public CategoriesAdapter(Context context, BaseActivity activity, List<Category> categoryList) {
        this.context = context;
        this.activity = activity;
        this.categoryList = categoryList;
        userapi = GetClient().create(UserApi.class);
        setSharedPreferences(activity);
        viewBinderHelper.setOpenOnlyOne(true);


    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoriesitem, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category categorydata = categoryList.get(position);
        viewBinderHelper.bind(holder.swipe1, String.valueOf(categorydata.getId()));

        holder.categoryitemCategoryname.setText(categorydata.getName());
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont3.otf");
        holder.categoryitemCategoryname.setTypeface(type);
        Glide.with(context).load(categorydata.getPhotoUrl()).into(holder.categoriesitemCategoryimage);
        holder.categoriesitemCategoryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFoodItems categoryFoodItems = new CategoryFoodItems();
                Toast.makeText(context, "hhh", Toast.LENGTH_SHORT).show();
                Category categorydata2 = categoryList.get(position);
                categoryFoodItems.category = categorydata2;
                ReplaceFragment(activity.getSupportFragmentManager(), categoryFoodItems, R.id.homecycle
                        , null, "medo");
            }
        });
        holder.categoryitemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(categoryList.get(position).getId(), position);

            }
        });
        holder.categoryitemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category EditCat = categoryList.get(position);
                FragmentManager manager = activity.getSupportFragmentManager();
                NewCategoryDialoge dialoge = new NewCategoryDialoge(restaurantHomeFragment);
                dialoge.data = EditCat;
                dialoge.show(manager, activity.getString(R.string.new_category));
                SaveData(activity, "edit", "edit");
            }
        });

    }

    private void deleteCategory(int id, int position) {
        showProgressDialog(activity, context.getString(R.string.please_wait));
        userapi.deleteCategory(LoadData(activity, REST_API_TOKEN), id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(activity, response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        categoryList.remove(position);
                        notifyDataSetChanged();
                        showPositiveToast(activity, response.body().getMsg());
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
        return categoryList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.categoryitem_delete)
        ImageView categoryitemDelete;
        @BindView(R.id.categoryitem_edit)
        ImageView categoryitemEdit;
        @BindView(R.id.categoriesitem_categoryimage)
        ImageView categoriesitemCategoryimage;
        @BindView(R.id.categoryitem_categoryname)
        TextView categoryitemCategoryname;

        @BindView(R.id.swipe1)
        SwipeRevealLayout swipe1;


        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
