package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class AddFriend_QRCode extends AppCompatActivity {
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend_qrcode);
        email = getIntent().getStringExtra("email");
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent inMyQr = new Intent(AddFriend_QRCode.this, AddFriend_My_QRCode.class);
        inMyQr.putExtra("email", email+"");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("สแกนคิวอาร์โค้ด")
                .setContent(new Intent(this, AddFriend_Scan_QRCode.class));

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("คิวอาร์โค้ดของฉัน")
                .setContent(inMyQr);


        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
        tabHost.getTabWidget()
                .getChildAt(0)
                .setBackgroundResource(
                        R.drawable.shape_tab);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                updateTabs();
            }
        });
    }
    protected void updateTabs() {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.shape_tab);
            }
            else {

                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.visible);

            }
        }

    }
    public void backFriend(View v){
        Intent in = new Intent(this, Home.class);
        startActivity(in);
    }
}
