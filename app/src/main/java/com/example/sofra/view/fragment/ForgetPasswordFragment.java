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
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.helper.HelperMethod.ReplaceFragment;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ForgetPasswordFragment extends Fragment {


    @BindView(R.id.forgetpassword_edemail)
    EditText forgetpasswordEdemail;
    @BindView(R.id.forgetpassword_btn_send)
    Button forgetpasswordBtnSend;
    UserApi userApi;
    NewPasswordFragment newPasswordFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, root);
        userApi=GetClient().create(UserApi.class);
         newPasswordFragment = new NewPasswordFragment();

        // Inflate the layout for this fragment
        return root;
    }

    @OnClick(R.id.forgetpassword_btn_send)
    public void onViewClicked() {resetPass(forgetpasswordEdemail.getText().toString());

    }
    public void resetPass(String email)
    {
        showProgressDialog(getActivity(), "please wait");

        userApi.resetPassword(email).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();

                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity(), response.body().getMsg());
                        ReplaceFragment(getActivity().getSupportFragmentManager(), newPasswordFragment, R.id.usercycle_activity
                                , null, "medo");


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
