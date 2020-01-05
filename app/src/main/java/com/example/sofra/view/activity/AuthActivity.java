package com.example.sofra.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sofra.R;
import com.example.sofra.view.fragment.LoginFragment;

import static com.example.sofra.helper.HelperMethod.ReplaceFragment;

public class AuthActivity extends AppCompatActivity {
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_user_cycle);
        loginFragment = new LoginFragment();

        ReplaceFragment( getSupportFragmentManager(), loginFragment, R.id.usercycle_activity
                , null, "medo");
        CheckUserPermsions();
    }
    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }


    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // Permission Denied
                    Toast.makeText( AuthActivity.this,"your message" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
