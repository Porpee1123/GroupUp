package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class ManageFriend extends AppCompatActivity {
    ManageFriend.ResponseStr responseStr = new ManageFriend.ResponseStr();
    String uid = "",email="";
    ListView listViewFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friend);
        listViewFriend = findViewById(R.id.listview_friend);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        Log.d("listA","idA "+uid);
        getFriend();
    }
    public void getFriend() {
        responseStr = new ManageFriend.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomeattend.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_name", c.getString("states_name"));
                                MyArrList.add(map);
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

        new CountDownTimer(500, 500) {
            public void onFinish() {
                // When timer is finished
                listViewFriend.setVisibility(View.VISIBLE);
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(ManageFriend.this, MyArrList, R.layout.activity_attend_home,
                        new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_attend, R.id.col_status_attend});
                listViewFriend.setAdapter(sAdap);

                listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String eName= MyArrList.get(position).get("events_name");
                        String eId= MyArrList.get(position).get("events_id");
                        String eStatus= MyArrList.get(position).get("states_name");
                        Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
                        Intent intent = new Intent(ManageFriend.this, MainAttendent.class);
                        intent.putExtra("id",uid);
                        intent.putExtra("eid",eId);
                        intent.putExtra("nameEvent",eName);
                        startActivity(intent);
                    }
                });

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        startActivity(in);
    }
    public void addFriend(View v) {
        Intent in = new Intent(this, ManageFriend_AddFriends.class);
        in.putExtra("id", uid+"");
        in.putExtra("email", email+"");
        startActivity(in);
    }
}
