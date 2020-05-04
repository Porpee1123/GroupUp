package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
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

public class Home_Listview_Head extends AppCompatActivity {
    Home_Listview_Head.ResponseStr responseStr = new Home_Listview_Head.ResponseStr();
    String email = "", id = "";
    static ListView listViewHeader;
    static SimpleAdapter sAdapHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_header);
        listViewHeader = findViewById(R.id.listView_Header);
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        Log.d("listA","idA "+id);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventHeader();
//                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        getEventHeader();


    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void getEventHeader() {
        responseStr = new Home_Listview_Head.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomehead.php";
        url += "?sId=" + id;//รอเอาIdจากfirebase
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
                                map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
                            for (int i =0;i<MyArrList.size();i++){
                                Home.nameHead.add(MyArrList.get(i).get("events_name"));
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
                listViewHeader.setVisibility(View.VISIBLE);//เปิด
                sAdapHead = new SimpleAdapter(Home_Listview_Head.this, MyArrList, R.layout.activity_header_home,
                        new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_header, R.id.col_status_header});
                listViewHeader.setAdapter(sAdapHead);
                listViewHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        // เข้าสู่ event
                        String eName= MyArrList.get(position).get("events_name");
                        String eId= MyArrList.get(position).get("events_id");
                        String eStatus= MyArrList.get(position).get("states_name");
                        String ewait= MyArrList.get(position).get("events_wait");
                        Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
//                        checkCloseVote(eId);
                        Intent intent = new Intent(Home_Listview_Head.this, HomeHead_Appointment.class);
                        intent.putExtra("id",id+"");
                        intent.putExtra("eid",eId+"");
                        intent.putExtra("nameEvent",eName+"");
                        intent.putExtra("email",email+"");
                        intent.putExtra("tab",0+"");
                        intent.putExtra("wait", ewait + "");
                        startActivity(intent);
                    }
                });
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
//    public void checkCloseVote(String eid) {
//        String url = "http://www.groupupdb.com/android/closevotetime.php";
//        url += "?eId=" + eid;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
//                    }
//                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//    }
}
