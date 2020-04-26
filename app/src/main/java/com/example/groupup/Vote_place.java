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

public class Vote_place extends AppCompatActivity {
    String id,eid,nameE,email;
    Vote_place.ResponseStr responseStr = new Vote_place.ResponseStr();

    ListView votePlace ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_vote_place);
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        votePlace = findViewById(R.id.listView_vote_place);

        getVotePlace();
    }
    public void getVotePlace() {
        responseStr = new Vote_place.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomeattend.php";
        url += "?sId=" + id;//รอเอาIdจากfirebase
        Log.d("footer","id : id "+id) ;
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
                        Log.d("footer","arraylist : id "+MyArrList.toString()) ;
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
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                SimpleAdapter sAdap;
                Log.d("slipUpload","12346 : "+MyArrList.toString()) ;
                sAdap = new SimpleAdapter(Vote_place.this, MyArrList, R.layout.activity_place,
                        new String[]{"events_name", "states_name"}, new int[]{R.id.rowPlaceName, R.id.rowDescription});
                votePlace.setAdapter(sAdap);

                votePlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String eName= MyArrList.get(position).get("events_name");
                        String eId= MyArrList.get(position).get("events_id");
                        String eStatus= MyArrList.get(position).get("states_name");
                        Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
                        Intent intent = new Intent(Vote_place.this, MainAttendent.class);
                        intent.putExtra("id",id+"");
                        intent.putExtra("eid",eId+"");
                        intent.putExtra("nameEvent",eName+"");
                        intent.putExtra("email",email);
                        intent.putExtra("tab",0+"");
                        startActivity(intent);
                    }
                });

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
    public void backVote(View v) {
        Intent intent = new Intent(Vote_place.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("email", email+"");
        intent.putExtra("tab",0+"");

        startActivity(intent);
    }
}
