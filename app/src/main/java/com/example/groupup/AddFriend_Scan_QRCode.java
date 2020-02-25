package com.example.groupup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddFriend_Scan_QRCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend_scan_qrcode);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentMainFragment,new Extend_Fragment_ScanQRCode()).commit();
        }
    }
}
