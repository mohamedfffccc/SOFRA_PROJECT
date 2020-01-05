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
import static com.example.sofra.data.local.Saveddata.showPositiveToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;


public class NewPasswordFragment extends Fragment {


    @BindView(R.id.newpasswordfragment_edcode)
    EditText newpasswordfragmentEdcode;
    @BindView(R.id.newpasswordfragment_ednewpassword)
    EditText newpasswordfragmentEdnewpassword;
    @BindView(R.id.newpasswordfragment_ednewpasswordconfirm)
    EditText newpasswordfragmentEdnewpasswordconfirm;
    @BindView(R.id.newpasswordfragment_btnsend)
    Button newpasswordfragmentBtnsend;
    UserApi userApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_password, container, false);
        ButterKnife.bind(this, root);
        userApi=GetClient().create(UserApi.class);


        // Inflate the layout for this fragment
        return root;
    }

    @OnClick(R.id.newpasswordfragment_btnsend)
    public void onViewClicked() {
        setNewPassword(newpasswordfragmentEdcode.getText().toString(),
                newpasswordfragmentEdnewpassword.getText().toString(),
                newpasswordfragmentEdnewpasswordconfirm.getText().toString());
    }
    public void setNewPassword(String code,String  password,String passwordconfirmation)
    {
        showProgressDialog(getActivity(), "please wait");

        userApi.newPassword(code,password,passwordconfirmation).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                showPositiveToast(getActivity(), response.body().getMsg());

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
}
