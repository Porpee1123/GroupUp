package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


public class HomeHead_Appointment_SetItem extends AppCompatActivity {
    Button btn_inviteFriend, btn_selectTheme, btn_summary, btn_checkSlip;
    String id, eid, nameE, monS, monE, email, wait, state, peopleInEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appoint_set_item);
        btn_inviteFriend = findViewById(R.id.select_friend);
        btn_selectTheme = findViewById(R.id.selectTheme);
        btn_summary = findViewById(R.id.summaryHead);
        btn_checkSlip = findViewById(R.id.checkslipHead);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        wait = getIntent().getStringExtra("wait");
        checkPeopleEvent(eid);
        btn_selectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTheme();
            }
        });
        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSummary();
            }
        });
        btn_checkSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSlip();
            }
        });

    }

    public void selectFriend() {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, InviteFriend.class);
        intent.putExtra("email", email + "");
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        startActivity(intent);

    }

    public void selectTheme() {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, HomeHead_Theme.class);
        intent.putExtra("email", email + "");
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }

    public void selectSlip() {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, HomeHead_SlipCheck.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        startActivity(intent);
    }

    public void selectSummary() {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, Head_Summary.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        startActivity(intent);
    }

    public void showFriend() {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, show_invitation.class);
        intent.putExtra("email", email + "");
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        startActivity(intent);
    }

    public void checkStatePeople(String eId, String uId, String pri) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/checkstatepeople.php";
        url += "?uId=" + uId;
        url += "&eId=" + eId;
        url += "&pri=" + pri;
        Log.d("MyHelper", url);
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
                                map.put("states_id", c.getString("states_id"));
                                MyArrList.add(map);
                            }
                            state = MyArrList.get(0).get("states_id");
                            Log.d("MyHelper", "response " + state);

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

    public void checkStatusbutton() {

    }

    public void checkStatusTrans(String eId, String stateId) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/checkpeopleinstate.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        url += "&stId=" + stateId;
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
                                map.put("num", c.getString("num"));
                                MyArrList.add(map);
                            }
//                            sum = MyArrList.get(0).get("num");

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

    public void checkPeopleEvent(String eId) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/checkpeopleinevent.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        Log.d("MyHelper", "url " + url);
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
                                map.put("sumtran", c.getString("sumtran"));
                                MyArrList.add(map);
                            }
                            peopleInEvent = MyArrList.get(0).get("sumtran");
                            Log.d("MyHelper", "peopleInEvent " + peopleInEvent);
                            final int people = Integer.parseInt(peopleInEvent);
                            if (people > 2) {
                                btn_inviteFriend.setText("ดูรายการเพื่อน");
                                btn_inviteFriend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showFriend();
                                    }
                                });
                            } else {
                                btn_inviteFriend.setText(R.string.select_friend);
                                btn_inviteFriend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        selectFriend();
                                    }
                                });
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
