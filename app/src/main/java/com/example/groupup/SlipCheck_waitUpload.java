package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class SlipCheck_waitUpload extends AppCompatActivity {
    String id,eid,nameE,monS,monE,email;
    SlipCheck_waitUpload.ResponseStr responseStr = new SlipCheck_waitUpload.ResponseStr();

    ListView slipWait ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_slip_check_wait_upload);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        slipWait = findViewById(R.id.listView_slipUpload);
        getSlipUpload();
        Log.d("slipUpload","wait : ") ;
    }
    public void getSlipUpload() {
        responseStr = new SlipCheck_waitUpload.ResponseStr();

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
                            Log.d("slipUpload","slipUpload : "+MyArrList.toString()) ;
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


        new CountDownTimer(500, 500){
            @Override
            public void onTick(long millisUntilFinished) {            }

            @Override
            public void onFinish() {
                SimpleAdapter sAdap;
                Log.d("slipUpload","12346 : "+MyArrList.toString()) ;
                sAdap = new SimpleAdapter(SlipCheck_waitUpload.this, MyArrList, R.layout.activity_slip_upload,
                        new String[]{"events_name", "states_name"}, new int[]{R.id.rowTextViewName, R.id.rowTextViewTag});
                slipWait.setAdapter(sAdap);

                slipWait.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String eName= MyArrList.get(position).get("events_name");
                        String eId= MyArrList.get(position).get("events_id");
                        String eStatus= MyArrList.get(position).get("states_name");
                        Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
                        Intent intent = new Intent(SlipCheck_waitUpload.this, MainAttendent.class);
                        intent.putExtra("id",id+"");
                        intent.putExtra("eid",eId+"");
                        intent.putExtra("nameEvent",eName+"");
                        intent.putExtra("email",email);
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
    //delay
//    new CountDownTimer(500, 500){
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    }.start();
}
