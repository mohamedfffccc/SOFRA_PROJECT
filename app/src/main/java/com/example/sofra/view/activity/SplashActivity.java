package com.example.sofra.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sofra.R;
import com.example.sofra.view.fragment.auth.SplashFragment;

import static com.example.sofra.helper.HelperMethod.ReplaceFragment;

public class SplashActivity extends AppCompatActivity {
    SplashFragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_main);
        splashFragment = new SplashFragment();
        ReplaceFragment( getSupportFragmentManager(), splashFragment, R.id.splash_cycle_activity
            , null, "medo");
    }
}
