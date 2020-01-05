package com.example.sofra.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.helper.MediaLoader;
import com.example.sofra.helper.ResturantDataSave;
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
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


public class RegisterResturantStep2Fragment extends Fragment {
    ResturantDataSave resturantData;
    static final int GALLERY_REQUEST = 1888;
    @BindView(R.id.registerfresturantragment2_tvcomm)
    TextView registerfresturantragment2Tvcomm;
    @BindView(R.id.registerfresturantragment2_edphone)
    EditText registerfresturantragment2Edphone;
    @BindView(R.id.registerfresturantragment2_lin1)
    LinearLayout registerfresturantragment2Lin1;
    @BindView(R.id.registerfresturantragment2_edwhats)
    EditText registerfresturantragment2Edwhats;
    @BindView(R.id.registerfresturantragment2_lin2)
    LinearLayout registerfresturantragment2Lin2;
    @BindView(R.id.registerfresturantragment2_tvchoosepicture)
    TextView registerfresturantragment2Tvchoosepicture;
    @BindView(R.id.registerfresturantragment2_tvrestaurantepicture)
    ImageView registerfresturantragment2Tvrestaurantepicture;
    @BindView(R.id.registerfresturantragment2_lin3)
    LinearLayout registerfresturantragment2Lin3;
    @BindView(R.id.registerfresturantragment2_btnregister)
    Button registerfresturantragment2Btnregister;
    private Bitmap bitmap;
    RequestBody name;
    RequestBody email;
    RequestBody phone;
    RequestBody region_id;
    RequestBody password;
    RequestBody password_confirmation;
    RequestBody delivery_cost;
    RequestBody minimum_order;
    RequestBody whatsup;
    RequestBody deliverytime;
    MultipartBody.Part image;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String photo;
UserApi userApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_resturant_fragment2, container, false);
        ;
        // Inflate the layout for this fragment
        ButterKnife.bind(this, root);
       userApi=GetClient().create(UserApi.class);
        return root;
    }



    @OnClick({R.id.registerfresturantragment2_tvrestaurantepicture, R.id.registerfresturantragment2_btnregister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerfresturantragment2_tvrestaurantepicture:
                openGallery();
                break;
            case R.id.registerfresturantragment2_btnregister:
                registerResturant();
                break;
        }
    }
    public void convertData()
    {
        name = convertToRequestBody(resturantData.resturant_name);
        email = convertToRequestBody(resturantData.email);
        phone = convertToRequestBody(registerfresturantragment2Edphone.getText().toString());
        region_id = convertToRequestBody(String.valueOf(resturantData.regionid));
        password = convertToRequestBody(resturantData.password);
        password_confirmation = convertToRequestBody(resturantData.password_confirmation);
        whatsup=convertToRequestBody(registerfresturantragment2Edwhats.getText().toString());
        delivery_cost=convertToRequestBody(resturantData.deliver_cost);
        minimum_order=convertToRequestBody(resturantData.minimum_order);
        deliverytime = convertToRequestBody(resturantData.delivery_time);
        image = convertFileToMultipart(photo, "photo");
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
                        photo = result.get(0).getPath();
                        Glide.with(getActivity()).load(photo).into(registerfresturantragment2Tvrestaurantepicture);

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }
    public void registerResturant()
    {
        convertData();
        showProgressDialog(getActivity() , getString(R.string.please_wait));
        userApi.registerRestaurant(name,email,password,password_confirmation,phone,whatsup,
                region_id,delivery_cost,minimum_order,image,deliverytime)
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus()==1) {
                                showPositiveToast(getActivity(), response.body().getMsg());
                            }
                            else
                            {
                                showNegativeToast(getActivity(),response.body().getMsg());
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
}
