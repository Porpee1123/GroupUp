package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class appointment extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("กำหนดรายการ")
                .setContent(new Intent(this, appoint_setItem.class));
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("วันที่เวลา/สถานที่")
                .setContent(new Intent(this, appoint_date_and_place.class));
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
                .setIndicator("ตรวจสอบสลิป")
                .setContent(new Intent(this, appoint_slipCheck.class));

        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }

}


