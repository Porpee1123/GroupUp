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
import java.util.Locale;

public class Show_invitation extends AppCompatActivity {
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    String uid,eid,nameE,monS,monE,email;
    static ArrayList<String> nameHead =new ArrayList<>();
    static ArrayList<String> nameAttend =new ArrayList<>();
    EditText searchText;
    String numAtt="",numHead="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_show_invitation);
        searchText = findViewById(R.id.searchText);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        getMemberAttend();
        search();
    }
    public void createTab(){

        tabHost = (TabHost) findViewById(R.id.tabhostInvite);
        tabHost.setup(mLocalActivityManager);
        Intent intentA = new Intent(this, Show_invitation_attendant.class);
        intentA.putExtra("id", uid+"");
        intentA.putExtra("email", email+"");
        intentA.putExtra("eid", eid+"");
        intentA.putExtra("nameEvent",nameE+"");
        intentA.putExtra("mStart",monS+"");
        intentA.putExtra("mEnd",monE+"");

        Intent intentH = new Intent(this, Show_invitation_head.class);
        intentH.putExtra("id", uid+"");
        intentH.putExtra("email", email+"");
        intentH.putExtra("eid", eid+"");
        intentH.putExtra("nameEvent",nameE+"");
        intentH.putExtra("mStart",monS+"");
        intentH.putExtra("mEnd",monE+"");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("ผู้เข้าร่วมงาน"+"( "+numAtt+" )")
                .setContent(intentA);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("แม่งาน"+"( "+numHead+" )")
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
        Intent intent = new Intent(Show_invitation.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid+"");
        intent.putExtra("email", email+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        intent.putExtra("tab",0+"");
        tabHost.setCurrentTab(1);
        startActivity(intent);
        finish();
    }
    public void editFriend(View v) {
        Intent intent = new Intent(Show_invitation.this, InviteFriend.class);
        intent.putExtra("email",email+"");
        intent.putExtra("id",uid+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        startActivity(intent);

    }
    public void search() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                Show_invitation_attendant.myItemsListAdapterAttend.filter(text);
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
                Show_invitation_head.myItemsListAdapterHead.filter(text);
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
    public void getMemberAttend() {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getmemberinvite.php";
        url += "?eId=" + eid;
        url += "&prId=" + "3";
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("user_email", c.getString("user_email"));
                                MyArrList.add(map);
                            }
                            numAtt = MyArrList.size()+"";
                            getMemberHead();
                            Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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
    public void getMemberHead() {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getmemberinvite.php";
        url += "?eId=" + eid;
        url += "&prId=" + "2";
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("user_email", c.getString("user_email"));
                                MyArrList.add(map);
                            }
                            numHead = MyArrList.size()+"";
                            createTab();
                            Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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
}
