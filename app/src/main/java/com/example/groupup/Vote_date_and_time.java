package com.example.groupup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

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
import java.util.StringTokenizer;

public class Vote_date_and_time extends AppCompatActivity {
    String id = "", eId = "", eName = "", email;
    ArrayList<String> dateDB,timeDB ;
    Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_vote_date_and_time);
        id = getIntent().getStringExtra("id");
        eId = getIntent().getStringExtra("eid");
        eName = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        btn1 = findViewById(R.id.vote1);
        btn2 = findViewById(R.id.vote2);
        btn3 = findViewById(R.id.vote3);
        btn4 = findViewById(R.id.vote4);
        dateDB = new ArrayList<>();
        timeDB = new ArrayList<>();
        getDate();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                        viewDetail.setTitle(R.string.con_vote);
                        viewDetail.setMessage(btn1.getText().toString());
                        viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เลือกอันอื่น", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VoteDate(dateDB.get(0),timeDB.get(0));
                                backVote();

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
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn1.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เลือกอันอื่น", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(1),timeDB.get(1));
                        backVote();

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
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn1.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เลือกอันอื่น", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(2),timeDB.get(2));
                        backVote();

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
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn1.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เลือกอันอื่น", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(3),timeDB.get(3));
                        backVote();

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

    public void backVote() {
        Intent intent = new Intent(Vote_date_and_time.this, MainAttendent.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("email",email+"");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("tab",0+"");
        startActivity(intent);
    }
    public void backVote(View v) {
        Intent intent = new Intent(Vote_date_and_time.this, MainAttendent.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("email",email+"");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("tab",0+"");
        startActivity(intent);
    }
    public void getDate() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getdateforvote.php";
        url += "?eId=" + eId;
        Log.d("getdateforvote",url);
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
                                map.put("time", c.getString("time"));
                                map.put("timerange", c.getString("timerange"));
                                map.put("status", c.getString("status"));
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

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                StringTokenizer st = new StringTokenizer(MyArrList.get(0).get("time"),"/");
                StringTokenizer st1 = new StringTokenizer(MyArrList.get(1).get("time"),"/");
                StringTokenizer st2 = new StringTokenizer(MyArrList.get(2).get("time"),"/");
                while (st.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st.nextToken();
                    m= st.nextToken();
                    y= st.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    btn1.setText(date+"\n ช่วงเวลา "+MyArrList.get(0).get("timerange"));
                    btn1.setTextSize(18);
                }
                while (st1.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st1.nextToken();
                    m= st1.nextToken();
                    y= st1.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    btn2.setText(date+"\n ช่วงเวลา "+MyArrList.get(1).get("timerange"));
                    btn2.setTextSize(18);

                }
                while (st2.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st2.nextToken();
                    m= st2.nextToken();
                    y= st2.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    btn3.setText(date+"\n ช่วงเวลา "+MyArrList.get(2).get("timerange"));
                    btn3.setTextSize(18);
                }
                String[] array = getResources().getStringArray(R.array.random);
                btn4.setText(array[0]);
                btn4.setTextSize(24);
                dateDB.add(MyArrList.get(0).get("time"));
                dateDB.add(MyArrList.get(1).get("time"));
                dateDB.add(MyArrList.get(2).get("time"));
                dateDB.add(MyArrList.get(3).get("time"));
                timeDB.add(MyArrList.get(0).get("timerange"));
                timeDB.add(MyArrList.get(1).get("timerange"));
                timeDB.add(MyArrList.get(2).get("timerange"));
                timeDB.add(MyArrList.get(3).get("timerange"));
            }
        }.start();
    }
    public void VoteDate(String s,String time){
        Log.d("votedate",s+" : "+time);
        String url = "http://www.groupupdb.com/android/addpointvotetime.php";
        url += "?vdid=" + s;
        url += "&vtid=" + time;
        url += "&eId=" + eId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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
