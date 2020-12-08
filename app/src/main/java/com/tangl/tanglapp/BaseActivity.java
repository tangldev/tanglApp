package com.tangl.tanglapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tangl.tanglapp.MainUIElements.HomeFragment;

public class BaseActivity extends FragmentActivity {
    private final String TAG = "TANGL.STANDARD_ACTIVITY";
    private BottomNavigationView mBottomNavigationMenu;
    private ImageButton mBackButton;
    private ImageButton mMenuButton;
    private ImageButton mHomeButton;
    private final FragmentManager mFragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        goHome();
        mMenuButton = findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction menuOpenTransaction = mFragmentManager.beginTransaction();
                menuOpenTransaction.replace(R.id.base_linear_layout,new SettingFragment());
                menuOpenTransaction.addToBackStack("Settings");
                menuOpenTransaction.commit();
            }
        });
        mHomeButton = findViewById(R.id.home_button);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.popBackStackImmediate();
            }
        });
    
        mBottomNavigationMenu = findViewById(R.id.bottom_menu_bar);
        mBottomNavigationMenu.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()) {
                    case R.id.scan_button:
                        Intent scanIntent = new Intent(BaseActivity.this, ScanLabelActivity.class);
                        startActivity(scanIntent);
                        return true;

                    case R.id.home_button:
//Fragment homeFragment = new HomeFragment();
                        //FragmentTransaction homeTransaction = fragmentManager.beginTransaction();
                        // homeTransaction.a
                        return true;
                    case R.id.discussion_button:
                        //Fragment discussionFragment = new DiscussionFragment();
                        //FragmentTransaction discussionTransaction;
                        //discussionTransaction.replace(R.id.standard_frame_layout,discussionFragment);
                        return true;
                    case R.id.service_search_button:
                        openServicesFragment();
                        return true;
                }
                return false;
            }
        });

    }

    private void goHome(){
        FragmentTransaction homeTransaction = mFragmentManager.beginTransaction();
        homeTransaction.replace(R.id.base_linear_layout,new HomeFragment());
        homeTransaction.commit();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        }
        else{
            Log.i(TAG,"Camera Permissions Set");
        }
    }

    private void openServicesFragment(){
        FragmentTransaction serviceSearchTransaction = mFragmentManager.beginTransaction();
        //serviceSearchTransaction.replace(R.id.base_linear_layout,new ServiceSearchFragment());
        serviceSearchTransaction.addToBackStack("ServiceSearch");
        serviceSearchTransaction.commit();

    }
}