package com.example.sofra.view.activity.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sofra.R;
import com.example.sofra.view.activity.SplashActivity;
import com.example.sofra.view.dialoge.LogoutDialoge;
import com.example.sofra.view.fragment.AboutFragment;
import com.example.sofra.view.fragment.ChangePasswordFragment;
import com.example.sofra.view.fragment.ContactFragment;
import com.example.sofra.view.fragment.NewOffersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.helper.HelperMethod.ReplaceFragment;


public class More extends Fragment {

    @BindView(R.id.morefragment_ivofferimage)
    ImageView morefragmentIvofferimage;
    @BindView(R.id.morefragment_ivcontactimage)
    ImageView morefragmentIvcontactimage;
    @BindView(R.id.morefragment_ivaboutimage)
    ImageView morefragmentIvaboutimage;
    @BindView(R.id.morefragment_ivchangepassimage)
    ImageView morefragmentIvchangepassimage;
    @BindView(R.id.morefragment_ivcommenttimage)
    ImageView morefragmentIvcommenttimage;
    @BindView(R.id.morefragment_ivsignoutimage)
    ImageView morefragmentIvsignoutimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, root);
        // Inflate the layout for this fragment
        return root;
    }


    @OnClick({R.id.morefragment_ivofferimage, R.id.morefragment_ivcontactimage, R.id.morefragment_ivaboutimage, R.id.morefragment_ivchangepassimage, R.id.morefragment_ivcommenttimage, R.id.morefragment_ivsignoutimage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.morefragment_ivofferimage:
                ReplaceFragment(getActivity().getSupportFragmentManager(), new NewOffersFragment(), R.id.homecycle
                        , null, "medo");

                break;
            case R.id.morefragment_ivcontactimage:
                ReplaceFragment(getActivity().getSupportFragmentManager(), new ContactFragment(), R.id.homecycle
                        , null, "medo");

                break;
            case R.id.morefragment_ivaboutimage:
                ReplaceFragment(getActivity().getSupportFragmentManager(), new AboutFragment(), R.id.homecycle
                        , null, "medo");


                break;
            case R.id.morefragment_ivchangepassimage:
                ReplaceFragment(getActivity().getSupportFragmentManager(), new ChangePasswordFragment(), R.id.homecycle
                        , null, "medo");

                break;
            case R.id.morefragment_ivcommenttimage:
                break;
            case R.id.morefragment_ivsignoutimage:
                showDialoge();
                break;
        }
    }
    public void showDialoge()
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        LogoutDialoge logoutDialoge = new LogoutDialoge();
        logoutDialoge.show(manager , "warning");



    }
}
