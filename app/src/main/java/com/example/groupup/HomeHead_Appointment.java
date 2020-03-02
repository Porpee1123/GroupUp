package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeHead_Appointment extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    String id,eid,nameE,monS,monE,email;
    TextView tName,mStart,mEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        tName = findViewById(R.id.nameEvent);
        mStart = findViewById(R.id.startMonth);
        mEnd = findViewById(R.id.endMonth);
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        email = getIntent().getStringExtra("email");
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tName.setText(nameE);
        mStart.setText(monS);
        mEnd.setText(monE);
        getEvent();
        tabHost.setup(mLocalActivityManager);
        Intent intentS = new Intent(this,HomeHead_Appointment_SetItem.class);
        intentS.putExtra("id", id+"");
        intentS.putExtra("email", email+"");
        intentS.putExtra("nEvent", nameE+"");
        intentS.putExtra("mStart", monS+"");
        intentS.putExtra("mEnd", monE+"");
        intentS.putExtra("eid", eid+"");
        Intent intentdp  = new Intent(this,HomeHead_Appointment_Date_And_Place.class);
        intentdp.putExtra("id", id+"");
        intentdp.putExtra("email", email+"");
        intentdp.putExtra("nEvent", nameE+"");
        intentdp.putExtra("mStart", monS+"");
        intentdp.putExtra("mEnd", monE+"");
        intentdp.putExtra("eid", eid+"");
        Intent intentslip  = new Intent(this,HomeHead_Appointment_SlipCheck.class);
        intentslip.putExtra("id", id+"");
        intentslip.putExtra("email", email+"");
        intentslip.putExtra("nEvent", nameE+"");
        intentslip.putExtra("mStart", monS+"");
        intentslip.putExtra("mEnd", monE+"");
        intentslip.putExtra("eid", eid+"");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("กำหนดรายการ ")
                .setContent(intentS);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("วันที่เวลา/สถานที่")
                .setContent(intentdp);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
                .setIndicator("ตรวจสอบสลิป")
                .setContent(intentslip);

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
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
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
    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + id;//รอเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                MyArrList.add(map);
                            }
                            tName.setText(MyArrList.get(0).get("events_name"));
                            mStart.setText(MyArrList.get(0).get("events_month_start"));
                            mEnd.setText(MyArrList.get(0).get("events_month_end"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void backHomepage(View v) {
        Intent intent = new Intent(HomeHead_Appointment.this, Home.class);
        startActivity(intent);
    }


}


