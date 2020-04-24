package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
    String id = "", email = "";
    ListView listViewInvite;
    Home_Alert.ResponseStr responseStr = new Home_Alert.ResponseStr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_alert);
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        listViewInvite = findViewById(R.id.listView_slipCheck);
        getEventInvitation();
    }

    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        startActivity(in);
//        addNotification();
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
                                map.put("trans_id", c.getString("trans_id"));
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
                        new String[]{"event_creater", "events_name", "events_month_start", "events_month_end", "pri_name"}, new int[]{R.id.col_head, R.id.col_name_header, R.id.col_time, R.id.col_time_end, R.id.col_pri});
                listViewInvite.setAdapter(sAdap);
                final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();

                listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String sCreater = MyArrList.get(position).get("event_creater").toString();
                        final String sName = MyArrList.get(position).get("events_name").toString();
                        final String eid = MyArrList.get(position).get("events_id").toString();
                        String sSta = MyArrList.get(position).get("events_month_start").toString();
                        String sEnd = MyArrList.get(position).get("events_month_end").toString();
                        String sPri = MyArrList.get(position).get("pri_name").toString();
                        String sTim = sSta + " - " + sEnd;
                        final String tranId = MyArrList.get(position).get("trans_id").toString();
                        viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                        viewDetail.setTitle("รายละเอียด" + tranId);
                        viewDetail.setMessage("ผู้เชิญ : " + sCreater + "\n"
                                + "ชื่อการนัดหมาย : " + sName + "\n" + "ช่วงเวลา : " + sTim + "\n"
                                + "สถานะ : " + sPri + "\n");


                        viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เอาไว้ก่อน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "เข้าร่วม", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Extend_MyHelper.UpdateStateToDb(tranId, 3 + "",Home_Alert.this);

                                addEventFriend(id,eid,sName);

                            }
                        });
                        viewDetail.show();
                        Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                        layoutParams.weight = 10;
                        btnPositive.setLayoutParams(layoutParams);
                        btnNegative.setLayoutParams(layoutParams);
                    }
                });

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }

    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setContentTitle("การเชิญเพื่อน")
                .setContentText("คุณนิวได้เชิญคุณเข้าร่วมนัดหมาย");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, Home_Alert.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void addEventFriend(String id, String eid,String eName) {
        String url = "http://www.groupupdb.com/android/adduserevent.php";
        url += "?sId=" + id;
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updateeventfrienddb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(Home_Alert.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",eName+"");
        intent.putExtra("email",email);
        startActivity(intent);
    }
}
