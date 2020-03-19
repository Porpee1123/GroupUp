package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class invitation extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    String uid,eid,nameE,monS,monE,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        createTab();
    }
    public void createTab(){

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent intentA = new Intent(this,Invitation_Attendant.class);
        intentA.putExtra("id", uid+"");
        intentA.putExtra("email", email+"");
        intentA.putExtra("eid", eid+"");
        Intent intentH = new Intent(this,Invitation_Head.class);
        intentH.putExtra("id", uid+"");
        intentH.putExtra("email", email+"");
        intentH.putExtra("eid", eid+"");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("ผู้เข้าร่วมงาน")
                .setContent(intentA);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("แม่งาน")
                .setContent(intentH);
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
