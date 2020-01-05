package com.example.sofra.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sofra.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

   public BaseFragment baseFragment;


    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onback();
    }
}
