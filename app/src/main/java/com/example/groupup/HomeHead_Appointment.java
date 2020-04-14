package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HomeHead_Appointment extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    String id,eid,nameE,monS,monE,email;
    int tab=0;
    TextView tName,mStart,mEnd,headAppoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appointment);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        headAppoint = findViewById(R.id.appointment);
        tName = findViewById(R.id.nameEvent);
        mStart = findViewById(R.id.startMonth);
        mEnd = findViewById(R.id.endMonth);
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        email = getIntent().getStringExtra("email");
        tab =Integer.parseInt(getIntent().getStringExtra("tab")+"") ;
        Log.d("tab","tab "+tab);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tName.setText(nameE);
        mStart.setText(monS);
        mEnd.setText(monE);
        headAppoint.setText(nameE);

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
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("กำหนดรายการ ")
                .setContent(intentS);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("วันที่เวลา/สถานที่")
                .setContent(intentdp);


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
    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        eventData();
        tabHost.setCurrentTab(tab);
        mLocalActivityManager.dispatchResume();
    }

    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + id;//ร  อเอาIdหรือ email จากfirebase
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
                            nameE = MyArrList.get(0).get("events_name");
                            monS = MyArrList.get(0).get("events_month_start");
                            monE = MyArrList.get(0).get("events_month_end");
                            tName.setText(nameE);
                            mStart.setText(monS);
                            mEnd.setText(monE);
                            writeFile(id,eid,nameE,monS,monS,email);
//                            Log.d("appoint","home appoint "+email+"/"+id+"/"+eid+"/"+nameE+"/"+monS+"/"+monE);
//                            Log.d("appoint","home appoint "+email+"/"+id+"/"+eid+"/"+nameE+"/"+monS+"/"+monE);
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
    public void backHomepage(View v) {
        Intent intent = new Intent(HomeHead_Appointment.this, Home.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        startActivity(intent);
    }

    public void writeFile(String id,String eid,String nameE,String monS,String monE,String email) {
    String filename = "eventData.txt";
    String sid = id+ ":";
    String seid = eid+ ":";
    String snameE = nameE+ ":";
    String smonS = monS+ ":";
    String smonE = monE+ ":";
    String semail = email + "\n";
    FileOutputStream outputStream;
    try {
        outputStream = openFileOutput(filename, MODE_PRIVATE);
        outputStream.write(sid.getBytes());
        outputStream.write(seid.getBytes());
        outputStream.write(snameE.getBytes());
        outputStream.write(smonS.getBytes());
        outputStream.write(smonE.getBytes());
        outputStream.write(semail.getBytes());
        outputStream.close();
//            Log.d("AddFriend","write file : id "+sid +" / status "+ semail);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void eventData(){
        String filename = "eventData.txt";
        try {
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(openFileInput(filename)));

            ArrayList<String> his = new ArrayList<>();
            String line = "";
            while ((line = inputReader.readLine()) != null) {
                his.add(line);
            }
            for (int i = 0; i < his.size(); i++) {
                StringTokenizer st = new StringTokenizer(his.get(i), ":");
                id = st.nextToken();
                eid =st.nextToken();
                nameE = st.nextToken();
                monS = st.nextToken();
                monE = st.nextToken();
                email = st.nextToken();
            }
            Log.d("appoint","readfile home appoint "+email+"/"+id+"/"+eid+"/"+nameE+"/"+monS+"/"+monE);
            headAppoint.setText(nameE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


