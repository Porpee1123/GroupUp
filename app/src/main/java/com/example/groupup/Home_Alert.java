package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

public class Home_Alert extends AppCompatActivity {
    String id="";
    ListView listViewInvite;
    Home_Alert.ResponseStr responseStr = new Home_Alert.ResponseStr();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        id = getIntent().getStringExtra("id");
        listViewInvite = findViewById(R.id.listView_invite);
        getEventInvitation();
    }
    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        startActivity(in);
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void getEventInvitation() {
        responseStr = new Home_Alert.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "id" + id);
        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
        url += "?sId=" + id;//รอเอาIdจากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText("Response is: "+ response.toString());

                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("events_id", c.getString("events_id"));
                                map.put("event_creater", c.getString("event_creater"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("pri_id", c.getString("pri_id"));
                                map.put("pri_name", c.getString("pri_name"));

                                //map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
                            Log.d("query", MyArrList.size() + "");
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
                // Execute your code here
                listViewInvite.setVisibility(View.VISIBLE);
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(Home_Alert.this, MyArrList, R.layout.activity_invitation_home,
                        new String[]{"event_creater", "events_name", "events_month_start", "events_month_end","pri_name"}, new int[]{R.id.col_head, R.id.col_name_header, R.id.col_time, R.id.col_time_end,R.id.col_pri});
                listViewInvite.setAdapter(sAdap);
                final AlertDialog.Builder viewDetail = new AlertDialog.Builder(Home_Alert.this);

                listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String sCreater = MyArrList.get(position).get("event_creater").toString();
                        String sName = MyArrList.get(position).get("events_name").toString();
                        String sSta = MyArrList.get(position).get("events_month_start").toString();
                        String sEnd = MyArrList.get(position).get("events_month_end").toString();
                        String sPri = MyArrList.get(position).get("pri_name").toString();
                        String sTim = sSta + " - " + sEnd;
                        viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                        viewDetail.setTitle("รายละเอียด");
                        viewDetail.setMessage("ผู้เชิญ : " + sCreater + "\n"
                                + "ชื่อการนัดหมาย : " + sName + "\n" + "ช่วงเวลา : " + sTim + "\n"
                                +"สถานะ : " + sPri + "\n");
                        viewDetail.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        viewDetail.setNegativeButton("ไม่เข้าร่วม", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        viewDetail.setPositiveButton("เข้าร่วม", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        viewDetail.show();

                    }
                });

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }
}
