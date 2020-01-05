package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.offers.OffersData;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.helper.DateModel;
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
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showCalender;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOfferFragment extends BaseFragment {
    public OffersData offersData;


    @BindView(R.id.addofferfragment_ivimage)
    ImageView addofferfragmentIvimage;
    @BindView(R.id.addofferfragment_tvname)
    EditText addofferfragmentTvname;
    @BindView(R.id.addofferfragment_tvdesc)
    EditText addofferfragmentTvdesc;
    @BindView(R.id.addofferfragment_tvfoom)
    TextView addofferfragmentTvfoom;
    @BindView(R.id.addofferfragment_btndatefrom)
    ImageView addofferfragmentBtndatefrom;
    @BindView(R.id.addofferfragment_btndateto)
    ImageView addofferfragmentBtndateto;
    @BindView(R.id.addofferfragment_tvto)
    TextView addofferfragmentTvto;
    @BindView(R.id.scrool)
    ScrollView scrool;
    @BindView(R.id.addofferfragment_btnadd)
    Button addofferfragmentBtnadd;
    @BindView(R.id.addofferfragment_tvadd)
    TextView addofferfragmentTvadd;
    @BindView(R.id.addofferfragment_tvprice)
    EditText addofferfragmentTvprice;
    private ArrayList<AlbumFile> mAlbumFiles;
    private String profile;
    RequestBody name;
    RequestBody desc;
    RequestBody from;
    RequestBody to;
    RequestBody api_token;
    RequestBody price;
    MultipartBody.Part offerimage;
    RequestBody offer_id;
    UserApi userApi;
    DateModel dateModel;
    DateModel dateModel1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_offer, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());
        setUpActivity();

        dateModel = new DateModel("01","01","2020","01-01-2020");
        dateModel1 = new DateModel("01","01","2020","01-01-2020");

        if (LoadData(getActivity(), "updateoffer").equals("updateoffer")) {
            Glide.with(getActivity()).load(offersData.getPhotoUrl()).into(addofferfragmentIvimage);
            addofferfragmentTvname.setText(offersData.getName());
            addofferfragmentTvdesc.setText(offersData.getDescription());
            addofferfragmentTvprice.setText(offersData.getPrice());
            addofferfragmentTvfoom.setText(offersData.getStartingAt());
            addofferfragmentTvto.setText(offersData.getEndingAt());
            addofferfragmentBtnadd.setText(getActivity().getString(R.string.edit));

        } else if (LoadData(getActivity(), "updateoffer").equals("addoffer")) {

        }
        // Inflate the layout for this fragment
        return root;
    }


    @OnClick({R.id.addofferfragment_ivimage, R.id.addofferfragment_btndatefrom, R.id.addofferfragment_btndateto, R.id.addofferfragment_btnadd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addofferfragment_ivimage:
                openGallery();
                break;
            case R.id.addofferfragment_btndatefrom:
                showCalender(getActivity(), getString(R.string.select_date), addofferfragmentTvfoom, dateModel);
                break;
            case R.id.addofferfragment_btndateto:
                showCalender(getActivity(), getString(R.string.select_date), addofferfragmentTvto, dateModel1);
                break;
            case R.id.addofferfragment_btnadd:
                if (LoadData(getActivity(), "updateoffer").equals("updateoffer")) {
                    offer_id = convertToRequestBody(String.valueOf(offersData.getId()));
                    updateOffer(offer_id);

                }
         else if (LoadData(getActivity(), "updateoffer").equals("addoffer")) {
                    addOffer();

        }
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

                        Glide.with(getActivity()).load(profile).into(addofferfragmentIvimage);

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
        name = convertToRequestBody(addofferfragmentTvname.getText().toString());
        desc = convertToRequestBody(addofferfragmentTvdesc.getText().toString());
        from = convertToRequestBody(addofferfragmentTvfoom.getText().toString());
        to = convertToRequestBody(addofferfragmentTvto.getText().toString());
        api_token = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
        price = convertToRequestBody(addofferfragmentTvprice.getText().toString());

        offerimage = convertFileToMultipart(profile, "photo");

    }

    public void addOffer() {
        convertData();
        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.addOffer(desc, price, from, name, offerimage, to, api_token, price).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                    }
                    else
                    {
                        showNegativeToast(getActivity() , response.body().getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }
    public void updateOffer(RequestBody id) {
        convertData();
        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.updateOffer(desc, price, from, name, offerimage, to,id, api_token, price).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                showPositiveToast(getActivity(), response.body().getMsg());

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
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

    @Override
    public void onback() {
        super.onback();
    }

}
