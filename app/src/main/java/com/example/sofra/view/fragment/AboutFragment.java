package com.example.sofra.view.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    UserApi userApi;
    @BindView(R.id.aboutfragment_tvabout)
    TextView aboutfragmentTvabout;
    @BindView(R.id.aboutfragment_tvaboutterms)
    TextView aboutfragmentTvaboutterms;
    private Typeface type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bigfont4.ttf");
        getSetting();
        return root;
    }

    public void getSetting() {
        userApi.getAppSetting().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        aboutfragmentTvabout.setText(response.body().getData().getAboutApp());
                        aboutfragmentTvabout.setTypeface(type);
                        aboutfragmentTvaboutterms.setText(response.body().getData().getTerms());
                        aboutfragmentTvaboutterms.setTypeface(type);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

}
