package com.example.sofra.view.dialoge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.restaurants.Restaurant;
import com.example.sofra.helper.MediaLoader;
import com.example.sofra.view.fragment.RestaurantHomeFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class NewCategoryDialoge extends DialogFragment {
    @BindView(R.id.newcategorydialog_tvcategorytext)
    TextView newcategorydialogTvcategorytext;
    @BindView(R.id.newcategorydialog_ivcategoryimage)
    CircularImageView newcategorydialogIvcategoryimage;
    @BindView(R.id.newcategorydialog_edcategoryname)
    EditText newcategorydialogEdcategoryname;
    @BindView(R.id.newcategorydialog_btnadd)
    Button newcategorydialogBtnadd;
    RestaurantHomeFragment restaurantHomeFragment;

    public NewCategoryDialoge(RestaurantHomeFragment restaurantHomeFragment) {
        this.restaurantHomeFragment = restaurantHomeFragment;
    }

    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String profile;
    private MultipartBody.Part category_pic;
    UserApi userApi;
    RequestBody name, api_token;
    public Category data;
    RequestBody category_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_category_dialog, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        if (LoadData(getActivity(), "edit").equals("edit")) {
            newcategorydialogBtnadd.setText(getString(R.string.edit));
            Glide.with(getActivity()).load(data.getPhotoUrl()).into(newcategorydialogIvcategoryimage);
            newcategorydialogEdcategoryname.setText(data.getName());
        } else if (LoadData(getActivity(), "edit").equals("add")) {

        }
        return root;
    }

    @OnClick({R.id.newcategorydialog_ivcategoryimage, R.id.newcategorydialog_btnadd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newcategorydialog_ivcategoryimage:
                openGallery();
                break;
            case R.id.newcategorydialog_btnadd:
                if (LoadData(getActivity(), "edit").equals("edit")) {
                    updateCategory();
                    getDialog().dismiss();
                    ReplaceFragment(getActivity().getSupportFragmentManager(), new RestaurantHomeFragment(), R.id.homecycle
                        , null, "medo");

                } else if (LoadData(getActivity(), "edit").equals("add")) {

                    addCategory();
                    getDialog().dismiss();
                    restaurantHomeFragment.getMyCategories();
                }

//                R

                break;
        }
    }

    private void openGallery() {
        Album.initialize(AlbumConfig.newBuilder(getActivity())
                .setAlbumLoader(new MediaLoader()).build());

        Album.image(this) // Image selection.
                .multipleChoice()
                .camera(true)
                .columnCount(3)
                .selectCount(1)
                .checkedList(mAlbumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        profile = result.get(0).getPath();

                        Glide.with(getActivity()).load(profile).into(newcategorydialogIvcategoryimage);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }

    public void addCategory() {
        convertData();

        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.addCategory(name, category_pic, api_token).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
//                        showPositiveToast(getActivity(), response.body().getMsg());
                    } else {
//                        showNegativeToast(getActivity(), response.body().getMsg());

                    }

                } catch (Exception e) {


                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                showNegativeToast(getActivity(), t.toString());


            }
        });


    }

    public void convertData() {
        if (LoadData(getActivity(), "edit").equals("edit")) {
            category_pic = convertFileToMultipart(profile, "photo");
            name = convertToRequestBody(newcategorydialogEdcategoryname.getText().toString());
            api_token = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
            category_id = convertToRequestBody(String.valueOf(data.getId()));
        } else if (LoadData(getActivity(), "edit").equals("add")) {

            category_pic = convertFileToMultipart(profile, "photo");
            name = convertToRequestBody(newcategorydialogEdcategoryname.getText().toString());
            api_token = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
            category_id = null;

        }

    }

    public void updateCategory() {
        convertData();
        showProgressDialog(getActivity(), "please wait");
        userApi.updateCategory(name, category_pic, api_token, category_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                    } else {
                        showNegativeToast(getActivity(), response.body().getMsg());

                    }

                } catch (Exception e) {


                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                showNegativeToast(getActivity(), t.toString());


            }
        });


    }

}
