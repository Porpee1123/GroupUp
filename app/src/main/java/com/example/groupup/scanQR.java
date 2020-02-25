package com.example.groupup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class scanQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentMainFragment,new scanQrFragment()).commit();
        }
    }
}
