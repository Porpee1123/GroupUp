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

import java.util.ArrayList;
import java.util.Locale;


public class InviteFriend extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    static  TabHost tabHost;
    String uid,eid,nameE,monS,monE,email;
    static ArrayList<String> nameHead =new ArrayList<>();
    static ArrayList<String> nameAttend =new ArrayList<>();
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_invite);
        searchText = findViewById(R.id.searchText);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        Log.d("checkIntent","invite "+uid+" "+email+" "+eid+" ");
        createTab();
        search();

    }
    public void createTab(){

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent intentA = new Intent(this,InviteFriend_Attendant.class);
        intentA.putExtra("id", uid+"");
        intentA.putExtra("email", email+"");
        intentA.putExtra("eid", eid+"");
        intentA.putExtra("nameEvent",nameE+"");
        intentA.putExtra("mStart",monS+"");
        intentA.putExtra("mEnd",monE+"");

        Intent intentH = new Intent(this,InviteFriend_Head.class);
        intentH.putExtra("id", uid+"");
        intentH.putExtra("email", email+"");
        intentH.putExtra("eid", eid+"");
        intentH.putExtra("nameEvent",nameE+"");
        intentH.putExtra("mStart",monS+"");
        intentH.putExtra("mEnd",monE+"");
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
                searchHead();
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
    public void backAppoint(View v) {
        Intent intent = new Intent(InviteFriend.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid+"");
        intent.putExtra("email", email+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        intent.putExtra("tab",0+"");
        tabHost.setCurrentTab(1);
        startActivity(intent);
//        finish();
    }
    public void search() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
//                InviteFriend_Head.myItemsListAdapter.filter(text);
                InviteFriend_Attendant.myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void searchHead() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                InviteFriend_Head.myItemsListAdapter.filter(text);
//                InviteFriend_Attendant.myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }
}
