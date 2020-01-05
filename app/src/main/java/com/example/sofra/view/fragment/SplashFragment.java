package com.example.sofra.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.view.activity.AuthActivity;
import com.example.sofra.view.activity.HomeCycle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.USER_TYPE;


public class SplashFragment extends Fragment {

LoginFragment loginFragment;
    @BindView(R.id.splash_logo)
    ImageView splashLogo;
    @BindView(R.id.splash_orderfood)
    Button splashOrderfood;
    @BindView(R.id.splash_sellfood)
    Button splashSellfood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);
        setSharedPreferences(getActivity());
        ButterKnife.bind(this,root);
        loginFragment = new LoginFragment();
        splashOrderfood.setVisibility(View.GONE);
        splashSellfood.setVisibility(View.GONE);
        Animation set = AnimationUtils.loadAnimation(getActivity() , R.anim.splash_animation);
        splashLogo.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashOrderfood.setVisibility(View.VISIBLE);
                splashSellfood.setVisibility(View.VISIBLE); }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }); return root;
    }

    @OnClick({R.id.splash_orderfood, R.id.splash_sellfood})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_orderfood:
                Intent intent = new Intent(getActivity() , HomeCycle.class);
                startActivity(intent);
                SaveData(getActivity(), USER_TYPE, "client");



                break;
            case R.id.splash_sellfood:
                Intent intent2 = new Intent(getActivity() , AuthActivity.class);
                startActivity(intent2);

                SaveData(getActivity(), USER_TYPE, "resturant");
                break;
        }
    }
}
