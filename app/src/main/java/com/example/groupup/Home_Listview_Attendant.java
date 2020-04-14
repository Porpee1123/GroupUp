package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class Home_Listview_Attendant extends AppCompatActivity {
    Home_Listview_Attendant.ResponseStr responseStr = new Home_Listview_Attendant.ResponseStr();
    String name = "", id = "",email="";
    ListView listViewAttend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attendant);
        listViewAttend = findViewById(R.id.listView_attend);
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        Log.d("footer","attend : id "+id );
        getEventAttend();
    }
    public void getEventAttend() {
        responseStr = new Home_Listview_Attendant.ResponseStr();

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

        new CountDownTimer(300, 300) {
            public void onFinish() {
                // When timer is finished
                listViewAttend.setVisibility(View.VISIBLE);
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(Home_Listview_Attendant.this, MyArrList, R.layout.activity_attend_home,
                        new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_attend, R.id.col_status_attend});
                listViewAttend.setAdapter(sAdap);
                Home.handlerHome.sendEmptyMessage(0);
                listViewAttend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String eName= MyArrList.get(position).get("events_name");
                        String eId= MyArrList.get(position).get("events_id");
                        String eStatus= MyArrList.get(position).get("states_name");
                        Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
                        Intent intent = new Intent(Home_Listview_Attendant.this, MainAttendent.class);
                        intent.putExtra("id",id+"");
                        intent.putExtra("eid",eId+"");
                        intent.putExtra("nameEvent",eName+"");
                        intent.putExtra("email",email);
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
}
