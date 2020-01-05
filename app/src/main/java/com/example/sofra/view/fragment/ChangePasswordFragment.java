package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.api.UserApi;
import com.example.sofra.data.model.generalresponse.GeneralResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.ClientApi.GetClient;
import static com.example.sofra.data.local.Saveddata.showNegativeToast;
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    UserApi userApi;
    @BindView(R.id.changepassword_etold)
    EditText changepasswordEtold;
    @BindView(R.id.changepassword_etnew)
    EditText changepasswordEtnew;
    @BindView(R.id.changepassword_etconfirmnew)
    EditText changepasswordEtconfirmnew;
    @BindView(R.id.changepassword_btnchange)
    Button changepasswordBtnchange;
    String api_token,old_password,new_password,confirm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        setSharedPreferences(getActivity());

        // Inflate the layout for this fragment
        return root;
    }

    public void changeClientPass(String token, String oldp, String newp, String confirm) {
        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.changeClientPassword(token, oldp, newp, confirm).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
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

            }
        });
    }

    public void changeRestPass(String token, String oldp, String newp, String confirm) {
        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.changeRestPassword(token, oldp, newp, confirm).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
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

            }
        });
    }

    @OnClick(R.id.changepassword_btnchange)
    public void onViewClicked() {
        Init();
        if (LoadData(getActivity(),USER_TYPE).equals("client")) {
            api_token=LoadData(getActivity(),API_TOKEN);
            changeClientPass(api_token,old_password,new_password,confirm);
        }
        else if (LoadData(getActivity(),USER_TYPE).equals("resturant")) {
            api_token=LoadData(getActivity(),REST_API_TOKEN);
            changeRestPass(api_token,old_password,new_password,confirm);
        }
    }
    public  void Init()
    {
        old_password=changepasswordEtold.getText().toString();
        new_password=changepasswordEtnew.getText().toString();
        confirm=changepasswordEtconfirmnew.getText().toString();

    }
}
