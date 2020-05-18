package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

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

public class HomeHead_Appointment_Date_And_Place extends AppCompatActivity {
    String id, eid, nameE, monS, monE, email, wait;
    ToggleButton btn_closeTime, btn_closePlace;
    Button btn_select_dateTime, btn_select_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appoint_date_and_place);
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        email = getIntent().getStringExtra("email");
        wait = getIntent().getStringExtra("wait");
        btn_closeTime = findViewById(R.id.btn_closeVoteTime);
        btn_closePlace = findViewById(R.id.btn_closeVotePlace);
        btn_select_dateTime = findViewById(R.id.select_date_time);
        btn_select_place = findViewById(R.id.select_place);
        btn_closeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTime();
                btn_closeTime.setEnabled(false);
                Intent in = new Intent(HomeHead_Appointment_Date_And_Place.this, Home.class);
                in.putExtra("email", email + "");
                in.putExtra("id", id + "");
                startActivity(in);
            }
        });
        btn_closePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePlace();
                btn_closePlace.setEnabled(false);
                Intent in = new Intent(HomeHead_Appointment_Date_And_Place.this, Home.class);
                in.putExtra("email", email + "");
                in.putExtra("id", id + "");
                startActivity(in);
            }
        });
        Log.d("appoint", email + "/" + id + "/" + eid + "/" + nameE + "/" + monS + "/" + monE);
        checkvisibleButton(id, eid, "2");
    }

    public void selectDateTime(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, Head_Date.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }

    public void selectPlace(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, Head_Place.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }

    public void closePlace() {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closeVotePlace.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Extend_MyHelper.sentInviteFCMPerson("0",eid,"3","โหวตสถานที่ปิดแล้ว","กรุณายืนยันการเข้าร่วม","OPEN_ACTIVITY_1",HomeHead_Appointment_Date_And_Place.this);
                        Extend_MyHelper.UpdateAllState(eid, "11", "3", HomeHead_Appointment_Date_And_Place.this);
                        Extend_MyHelper.UpdateAllState(eid, "10", "2", HomeHead_Appointment_Date_And_Place.this);

                        Toast.makeText(HomeHead_Appointment_Date_And_Place.this, "Close Place Vote Complete", Toast.LENGTH_SHORT).show();
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

    public void closeTime() {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closevotetime.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Extend_MyHelper.sentInviteFCMPerson("0",eid,"2","โหวตเวลาปิดแล้ว ถึงเวลาเลือกสถานที่","วันที่","OPEN_ACTIVITY_1",HomeHead_Appointment_Date_And_Place.this);
                        Extend_MyHelper.UpdateAllState(eid, "8", "2", HomeHead_Appointment_Date_And_Place.this);
                        Toast.makeText(HomeHead_Appointment_Date_And_Place.this, "Close Time Vote Complete", Toast.LENGTH_SHORT).show();
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

    public void checkvisibleButton(String uid, String eid, String pid) {
        Log.d("checktrans", "id : " + uid + " eid : " + eid + " pid : " + pid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gettransid.php";
        url += "?uId=" + uid;
        url += "&eId=" + eid;
        url += "&pId=" + pid;
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
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("pri_id", c.getString("pri_id"));
                                MyArrList.add(map);
                            }
                            String state = MyArrList.get(0).get("states_id");
                            int stateId = Integer.parseInt(state);
                            Log.d("checktrans", "state " + state);
                            if (stateId == 5) {
                                btn_select_dateTime.setEnabled(true);
                                btn_select_place.setEnabled(false);
                                btn_select_dateTime.setAlpha(1);
                                btn_select_place.setAlpha((float) 0.5);
                                btn_closeTime.setVisibility(View.GONE);
                                btn_closePlace.setVisibility(View.GONE);
                                btn_select_dateTime.setVisibility(View.VISIBLE);
                                btn_select_place.setVisibility(View.VISIBLE);

                            } else if (stateId == 6) {
                                btn_select_dateTime.setEnabled(true);
                                btn_select_dateTime.setAlpha(1);
                                btn_select_dateTime.setVisibility(View.GONE);
                                btn_select_place.setEnabled(false);
                                btn_select_place.setAlpha((float) 0.5);
                                btn_closeTime.setVisibility(View.VISIBLE);
                                btn_closePlace.setVisibility(View.GONE);
                                btn_select_place.setVisibility(View.VISIBLE);
                            } else if (stateId == 8) {
                                btn_select_place.setEnabled(true);
                                btn_select_place.setAlpha(1);
                                btn_select_place.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setEnabled(false);
                                btn_select_dateTime.setAlpha((float) 0.5);
                                btn_closeTime.setVisibility(View.GONE);
                                btn_closePlace.setVisibility(View.GONE);
                            } else if (stateId == 9) {
                                btn_closePlace.setEnabled(true);
                                btn_closePlace.setAlpha(1);
                                btn_closePlace.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setEnabled(false);
                                btn_select_dateTime.setAlpha((float) 0.5);
                                btn_closeTime.setVisibility(View.GONE);
                                btn_select_place.setVisibility(View.GONE);
                            } else {
                                btn_select_place.setEnabled(true);
                                btn_select_place.setAlpha((float) 0.5);
                                btn_select_place.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setVisibility(View.VISIBLE);
                                btn_select_dateTime.setEnabled(false);
                                btn_select_dateTime.setAlpha((float) 0.5);
                                btn_closeTime.setVisibility(View.GONE);
                                btn_closePlace.setVisibility(View.GONE);
                            }
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
