package com.attendance.checkin.checkinforwork;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.attendance.checkin.checkinforwork.Fragment.FragmentLogin;

public class AuthenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayPrimaryColorGreen, true);
        setContentView(R.layout.activity_authen);
        getSupportActionBar().hide();
        fragmentTran(new FragmentLogin());

    }

    public void fragmentTran(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }
}
