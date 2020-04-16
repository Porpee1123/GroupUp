package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

public class HomeHead_SlipCheck extends AppCompatActivity {
    String id,eid,nameE,monS,monE,email;
    TabHost tabHost;
    LocalActivityManager mLocalActivityManager;
    EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appoint_slip_check);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        searchText = findViewById(R.id.searchText);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent intentWU = new Intent(this,SlipCheck_waitUpload.class);
        intentWU.putExtra("id", id+"");
        intentWU.putExtra("email", email+"");
        intentWU.putExtra("nEvent", nameE+"");
        intentWU.putExtra("mStart", monS+"");
        intentWU.putExtra("mEnd", monE+"");
        intentWU.putExtra("eid", eid+"");
        Intent intentWC = new Intent(this,SlipCheck_waitCheck.class);
        intentWC.putExtra("id", id+"");
        intentWC.putExtra("email", email+"");
        intentWC.putExtra("nEvent", nameE+"");
        intentWC.putExtra("mStart", monS+"");
        intentWC.putExtra("mEnd", monE+"");
        intentWC.putExtra("eid", eid+"");
        Intent intentFi  = new Intent(this, SlipCheck_finish.class);
        intentFi.putExtra("id", id+"");
        intentFi.putExtra("email", email+"");
        intentFi.putExtra("nEvent", nameE+"");
        intentFi.putExtra("mStart", monS+"");
        intentFi.putExtra("mEnd", monE+"");
        intentFi.putExtra("eid", eid+"");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("รอดำเนินการ\nอัปโหลด")
                .setContent(intentWU);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("รอดำเนินการ\nตรวจสอบ")
                .setContent(intentWC);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab2")
                .setIndicator("อัปโหลด\nเสร็จสิ้น")
                .setContent(intentFi);

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
        search();
    }
    protected void updateTabs() {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.shape_tab);
                searchText.setText("");
            }
            else {

                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.visible);
                searchText.setText("");

            }
        }

    }
    public void search() {
//        Log.d("arry",nameAttend.toString());

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("arry",nameAttend.toString());
                Log.d("arry","s "+s);
                SlipCheck_waitUpload.sAdap.getFilter().filter(s);
                SlipCheck_waitCheck.sAdap.getFilter().filter(s);
                SlipCheck_finish.sAdap.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    public void backAppoint(View v) {
        Intent intent = new Intent(HomeHead_SlipCheck.this, HomeHead_Appointment.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        intent.putExtra("tab",0+"");
        startActivity(intent);
    }
}
