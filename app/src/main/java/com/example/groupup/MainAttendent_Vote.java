package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class MainAttendent_Vote extends AppCompatActivity {
    String id = "", eId = "", eName = "", email;
    Button btn_time, btn_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_vote);
        id = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        btn_time = findViewById(R.id.select_date_time);
        btn_place = findViewById(R.id.select_place);
        checkvisibleButton(id,eId,"3");
    }

    public void voteDate(View v) {
        Intent intent = new Intent(MainAttendent_Vote.this, Vote_date_and_time.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("tab", 0 + "");
        startActivity(intent);
    }

    public void votePlace(View v) {
        Intent intent = new Intent(MainAttendent_Vote.this, Vote_place.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("tab", 0 + "");
        startActivity(intent);
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
                            Log.d("checktrans", "state" + state);
                            if (stateId == 6 ){
                                btn_time.setEnabled(true);
                                btn_place.setEnabled(false);
                                btn_time.setAlpha(1);
                                btn_place.setAlpha((float) 0.5);
                            }else if (stateId == 9 ){
                                btn_place.setEnabled(true);
                                btn_time.setEnabled(false);
                                btn_place.setAlpha(1);
                                btn_time.setAlpha((float) 0.5);
                            }else{
                                btn_time.setEnabled(false);
                                btn_place.setEnabled(false);
                                btn_time.setAlpha((float) 0.5);
                                btn_place.setAlpha((float) 0.5);
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
