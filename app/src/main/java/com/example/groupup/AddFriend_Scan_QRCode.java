package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AddFriend_Scan_QRCode extends AppCompatActivity {
    String email,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        Log.d("QrTest","AddFriend_Scan_QRCode");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentMainFragment,new Extend_Fragment_ScanQRCode()).commit();
        }
    }
}
