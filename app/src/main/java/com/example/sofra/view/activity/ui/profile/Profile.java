package com.example.sofra.view.activity.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.generalresponse.RegionsData;
import com.example.sofra.helper.MediaLoader;
import com.example.sofra.view.fragment.BaseFragment;
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
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.CITY_ID;
import static com.example.sofra.data.local.SofraConstants.EMAIL;
import static com.example.sofra.data.local.SofraConstants.NAME;
import static com.example.sofra.data.local.SofraConstants.PASSWORD;
import static com.example.sofra.data.local.SofraConstants.PHONE;
import static com.example.sofra.data.local.SofraConstants.PROFILE_IMAGE;
import static com.example.sofra.data.local.SofraConstants.REGION_ID;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class Profile extends BaseFragment {
    RequestBody name;
    RequestBody email;
    RequestBody phone;
    RequestBody region_id;
    RequestBody password;
    RequestBody password_confirmation;
    RequestBody api_token;
    MultipartBody.Part profile_picture;


    @BindView(R.id.userprofile_ivpersonalimage)
    ImageView userprofileIvpersonalimage;
    @BindView(R.id.userprofile_edname)
    EditText userprofileEdname;
    @BindView(R.id.userprofile_edemail)
    EditText userprofileEdemail;
    @BindView(R.id.userprofile_edphone)
    EditText userprofileEdphone;
    @BindView(R.id.userprofile_spcity)
    Spinner userprofileSpcity;
    @BindView(R.id.userprofile_spvillage)
    Spinner userprofileSpvillage;

    @BindView(R.id.userprofile_btnedit)
    Button userprofileBtnedit;
    GeneralResponseAdapter adapter;
    ArrayList<RegionsData> city, region;
    UserApi userApi;
    @BindView(R.id.userprofile_edpassword)
    EditText userprofileEdpassword;
    @BindView(R.id.userprofile_edpasswordconfirmation)
    EditText userprofileEdpasswordconfirmation;
    private ArrayList<AlbumFile> mAlbumFiles=new ArrayList<>();
    private String profile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.bind(this, root);
        setSharedPreferences(getActivity());

        userApi = GetClient().create(UserApi.class);
        addCity();


        adapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.select_city));
        userprofileSpcity.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        setProfileData();


        return root;
    }

    public void addCity() {
        city = new ArrayList<>();
        userApi.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {


                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());

                        city.addAll(response.body().getData().getData());
                        userprofileSpcity.setSelection(Integer.parseInt(LoadData(getActivity(), CITY_ID)));
                        addRegion(userprofileSpcity.getSelectedItemPosition());

                        adapter = new GeneralResponseAdapter(getActivity(), region, getActivity().getString(R.string.sel_reg));
                        userprofileSpvillage.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }
                catch (Exception e)
                {

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
                    userprofileSpvillage.setSelection(Integer.parseInt(LoadData(getActivity(), REGION_ID)));

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    public void setProfileData() {
        Glide.with(getActivity()).load(LoadData(getActivity(), PROFILE_IMAGE)).into(userprofileIvpersonalimage);

        userprofileEdname.setText(LoadData(getActivity(), NAME));
        userprofileEdemail.setText(LoadData(getActivity(), EMAIL));
        userprofileEdphone.setText(LoadData(getActivity(), PHONE));
        userprofileEdpassword.setText(LoadData(getActivity(), PASSWORD));
        userprofileEdpasswordconfirmation.setText(LoadData(getActivity(), PASSWORD));
    }

    public void convertData() {
        name = convertToRequestBody(userprofileEdname.getText().toString());
        email = convertToRequestBody(userprofileEdemail.getText().toString());
        phone = convertToRequestBody(userprofileEdphone.getText().toString());
        region_id = convertToRequestBody(String.valueOf(userprofileSpvillage.getSelectedItemPosition()));
        password = convertToRequestBody(userprofileEdpassword.getText().toString());
        password_confirmation = convertToRequestBody(userprofileEdpasswordconfirmation.getText().toString());
        api_token=convertToRequestBody(LoadData(getActivity() , API_TOKEN));
        profile_picture = convertFileToMultipart(profile, "profile_image");

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
                        Glide.with(getActivity()).load(profile).into(userprofileIvpersonalimage);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }
    public void editProfile()
    {
        convertData();
        showProgressDialog(getActivity() , getActivity().getString(R.string.please_wait));
        userApi.editUserProfile(api_token,name,phone,email,
                password,password_confirmation,region_id,profile_picture)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus()==1) {
                                showPositiveToast(getActivity(),response.body().getMsg());
                            }

                        }
                        catch (Exception e)
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
    }



    @OnClick({R.id.userprofile_ivpersonalimage, R.id.userprofile_btnedit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userprofile_ivpersonalimage:
                openGallery();
                break;
            case R.id.userprofile_btnedit:
                editProfile();
                break;
        }
    }
}