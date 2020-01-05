package com.example.sofra.view.fragment;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.generalresponse.RegionsData;
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
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


public class ClientRegisterFragment extends Fragment {

    RequestBody name;
    RequestBody email;
    RequestBody phone;
    RequestBody region_id;
    RequestBody password;
    RequestBody password_confirmation;
    Uri selectedImage;


    GeneralResponseAdapter adapter;
    ArrayList<RegionsData> city, region;
    UserApi userApi;
    @BindView(R.id.userregister_ivpersonalimage)
    ImageView userregisterIvpersonalimage;
    @BindView(R.id.registerfragment_edname)
    EditText registerfragmentEdname;

    @BindView(R.id.registerfragment_edphone)
    EditText registerfragmentEdphone;
    @BindView(R.id.registerfragment_spcity)
    Spinner registerfragmentSpcity;
    @BindView(R.id.registerfragment_spvillage)
    Spinner registerfragmentSpvillage;
    @BindView(R.id.registerfragment_edpassword)
    EditText registerfragmentEdpassword;
    @BindView(R.id.registerfragment_edpasswordconfirmation)
    EditText registerfragmentEdpasswordconfirmation;
    @BindView(R.id.userregister_scroll)
    ScrollView userregisterScroll;
    @BindView(R.id.registerfragment_btnregister)
    Button registerfragmentBtnregister;
    static final int GALLERY_REQUEST = 1888;
    @BindView(R.id.regisetrfragment_edemail)
    EditText regisetrfragmentEdemail;

    private Bitmap bitmap;
    private String profile;
    MultipartBody.Part profile_picture;
    private ArrayList<AlbumFile> mAlbumFiles=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_register, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        addCity();

        adapter = new GeneralResponseAdapter(getActivity(), city, "select city");
        registerfragmentSpcity.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerfragmentSpcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addRegion(position);
                adapter = new GeneralResponseAdapter(getActivity(), region, "select region");
                registerfragmentSpvillage.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //TODO REGION


        return root;
    }

    @OnClick({R.id.userregister_ivpersonalimage, R.id.registerfragment_btnregister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userregister_ivpersonalimage:
                openGallery();
                break;
            case R.id.registerfragment_btnregister:
                registerUser();
                break;
        }
    }

    public void addCity() {
        city = new ArrayList<>();
        userApi.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus() == 1) {
                    showPositiveToast(getActivity(), response.body().getMsg());

                    city.addAll(response.body().getData().getData());
                    Toast.makeText(getActivity(), city.toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void addRegion(int city_id) {
        region = new ArrayList<>();
        userApi.getRegions(city_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    showPositiveToast(getActivity(), response.body().getMsg());

                    region.addAll(response.body().getData().getData());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

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
                        Glide.with(getActivity()).load(profile).into(userregisterIvpersonalimage);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }



    public void getData() {
        name = convertToRequestBody(registerfragmentEdname.getText().toString());
        email = convertToRequestBody(regisetrfragmentEdemail.getText().toString());
        phone = convertToRequestBody(registerfragmentEdphone.getText().toString());
        region_id = convertToRequestBody(String.valueOf(registerfragmentSpvillage.getSelectedItemPosition()));
        password = convertToRequestBody(registerfragmentEdpassword.getText().toString());
        password_confirmation = convertToRequestBody(registerfragmentEdpasswordconfirmation.getText().toString());
        profile_picture = convertFileToMultipart(profile, "profile_image");
    }

    public void registerUser() {
        getData();
        showProgressDialog(getActivity(),"please wait");

        userApi.registerUser(name, email, password, password_confirmation, phone, region_id, profile_picture)
                .enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        dismissProgressDialog();
                        showPositiveToast(getActivity(), response.body().getMsg());
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {

                    }
                });
    }
}
//