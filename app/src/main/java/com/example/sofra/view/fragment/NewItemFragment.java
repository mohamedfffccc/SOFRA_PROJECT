package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.restaurants.Category;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.helper.MediaLoader;
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
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewItemFragment extends BaseFragment {
    public Category categorydata;
    public OffersData offersData;
    RequestBody name;
    RequestBody desription;
    RequestBody price;
    RequestBody offer_price;
    RequestBody apitoken;
    String photourl;
    MultipartBody.Part photo;
    @BindView(R.id.newitemfragment_ivimage)
    ImageView newitemfragmentIvimage;
    @BindView(R.id.newitemfragment_edname)
    EditText newitemfragmentEdname;
    @BindView(R.id.newitemfragment_eddesc)
    EditText newitemfragmentEddesc;
    @BindView(R.id.newitemfragment_edprice)
    EditText newitemfragmentEdprice;
    @BindView(R.id.newitemfragment_edofferprice)
    EditText newitemfragmentEdofferprice;
    @BindView(R.id.newitemfragment_btnadd)
    Button newitemfragmentBtnadd;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    UserApi userApi;
    RequestBody category_id;
    RequestBody item_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_item, container, false);
        ButterKnife.bind(this, root);
        setUpActivity();
        setSharedPreferences(getActivity());
        userApi = GetClient().create(UserApi.class);
        if (LoadData(getActivity() , "additem").equals("additem")) {
            category_id = convertToRequestBody(String.valueOf(categorydata.getId()));


        }
        else if (LoadData(getActivity() , "additem").equals("updateitem"))
        {
            item_id=convertToRequestBody(String.valueOf(offersData.getId()));
            category_id=convertToRequestBody(offersData.getCategoryId());
            Glide.with(getActivity()).load(offersData.getPhotoUrl()).into(newitemfragmentIvimage);
            newitemfragmentBtnadd.setText(getActivity().getString(R.string.edit));
            newitemfragmentEdname.setText(offersData.getName());
            newitemfragmentEddesc.setText(offersData.getDescription());
            newitemfragmentEdprice.setText(offersData.getPrice());
            newitemfragmentEdofferprice.setText(offersData.getOfferPrice());
        }
        // Inflate the layout for this fragment
        return root;
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
                        photourl = result.get(0).getPath();

                        Glide.with(getActivity()).load(photourl).into(newitemfragmentIvimage);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }

    public void convertData() {
        apitoken = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
        name = convertToRequestBody(newitemfragmentEdname.getText().toString());
        desription = convertToRequestBody(newitemfragmentEddesc.getText().toString());
        price = convertToRequestBody(newitemfragmentEdprice.getText().toString());
        offer_price = convertToRequestBody(newitemfragmentEdofferprice.getText().toString());
        photo = convertFileToMultipart(photourl, "photo");

    }

    public void addItem() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.addItem(desription, price, name, photo, apitoken, offer_price, category_id)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                showPositiveToast(getActivity(), response.body().getMsg());
                            } else {
                                showPositiveToast(getActivity(), response.body().getMsg());
                            }

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
    }
    public void updateItem() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        userApi.updateItem(desription, price,category_id, name, photo,item_id , apitoken, offer_price)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                showPositiveToast(getActivity(), response.body().getMsg());
                            } else {
                                showPositiveToast(getActivity(), response.body().getMsg());
                            }

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
    }


    @OnClick({R.id.newitemfragment_ivimage, R.id.newitemfragment_btnadd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newitemfragment_ivimage:
                openGallery();
                break;
            case R.id.newitemfragment_btnadd:
                if (LoadData(getActivity() , "additem").equals("additem")) {
                    addItem();

                }
                else if (LoadData(getActivity() , "additem").equals("updateitem"))
                {
                    updateItem();

                }


                break;
        }
    }


    @Override
    public void onback() {
        super.onback();
    }
}
