package com.example.sofra.view.dialoge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sofra.R;
import com.example.sofra.view.activity.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutDialoge extends DialogFragment {
    @BindView(R.id.logout_ivyes)
    ImageView logoutIvyes;
    @BindView(R.id.logout_ivno)
    ImageView logoutIvno;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.logoutdialoge, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick({R.id.logout_ivyes, R.id.logout_ivno})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.logout_ivyes:
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.logout_ivno:
                getDialog().dismiss();
                break;
        }
    }
}
