package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class MainAttendent extends AppCompatActivity {
    String nEvent="",id="",eid="",email="";
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    TextView nHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        nHead = findViewById(R.id.inviteFriends);
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        nHead.setText(nEvent);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent intentV = new Intent(this,MainAttendent_Vote.class);
        intentV.putExtra("id", id+"");
        intentV.putExtra("email", email+"");
        intentV.putExtra("nameEvent", nEvent+"");
        intentV.putExtra("eid", eid+"");
        Intent intentS = new Intent(this,MainAttendent_Summary.class);
        intentS.putExtra("id", id+"");
        intentS.putExtra("email", email+"");
        intentS.putExtra("nameEvent", nEvent+"");
        intentS.putExtra("eid", eid+"");
        Intent intentR = new Intent(this,MainAttendent_Reviews.class);
        intentR.putExtra("id", id+"");
        intentR.putExtra("email", email+"");
        intentR.putExtra("nameEvent", nEvent+"");
        intentR.putExtra("eid", eid+"");

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("การโหวต")
                .setContent(intentV);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("สรุปงาน")
                .setContent(intentS);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
                .setIndicator("รีวิว")
                .setContent(intentR);

        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
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

    @Override
    protected void onResume() {
        super.onResume();
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        nHead.setText(nEvent);
    }

    public void backHome(View v) {
        Intent intent = new Intent(MainAttendent.this, Home.class);
        intent.putExtra("email", email+"");
        startActivity(intent);
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
}
