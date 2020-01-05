package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    String name, phone, email, content, type;
    @BindView(R.id.contactfragment_edname)
    EditText contactfragmentEdname;
    @BindView(R.id.contactfragment_edemail)
    EditText contactfragmentEdemail;
    @BindView(R.id.contactfragment_edphone)
    EditText contactfragmentEdphone;
    @BindView(R.id.contactfragment_edtext)
    EditText contactfragmentEdtext;
    @BindView(R.id.contactfragment_rbcomplainant)
    RadioButton contactfragmentRbcomplainant;
    @BindView(R.id.contactfragment_rbsuggession)
    RadioButton contactfragmentRbsuggession;
    @BindView(R.id.contactfragment_rbinquiry)
    RadioButton contactfragmentRbinquiry;
    @BindView(R.id.contactfragment_btnsend)
    Button contactfragmentBtnsend;
    @BindView(R.id.contactfragment_rgtype)
    RadioGroup contactfragmentRgtype;
    UserApi userApi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        contactfragmentRgtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.contactfragment_rbcomplainant:
                        type = "complaint";


                        break;
                    case R.id.contactfragment_rbinquiry:
                        type = "inquiry";
                        break;
                    case R.id.contactfragment_rbsuggession:
                        type = "suggestion";
                        break;
                }
            }
        });

        return root;

    }

    public void contactUs(String n, String e, String p, String ty, String text) {
        showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        userApi.contactUs(n, e, p, ty, text).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();

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

    @OnClick(R.id.contactfragment_btnsend)
    public void onViewClicked() {
        name=contactfragmentEdname.getText().toString();
        email=contactfragmentEdemail.getText().toString();
        phone=contactfragmentEdphone.getText().toString();
        content=contactfragmentEdtext.getText().toString();

        contactUs(name,email,phone,type,content);
    }
}
