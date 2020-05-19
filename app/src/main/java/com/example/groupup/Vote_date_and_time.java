package com.example.groupup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Vote_date_and_time extends AppCompatActivity {
    String id = "", eId = "", eName = "", email,transId="";
    ArrayList<String> dateDB, timeDB;
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
        getTransIDByTrans(id,eId,"3");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn1.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(0), timeDB.get(0));
                        backVote();

                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn2.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(1), timeDB.get(1));
                        backVote();

                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn3.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate(dateDB.get(2), timeDB.get(2));
                        backVote();

                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Vote_date_and_time.this).create();

                viewDetail.setTitle(R.string.con_vote);
                viewDetail.setMessage(btn4.getText().toString());
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VoteDate("random", "random");
                        backVote();

                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
    }

    public void backVote() {
        Extend_MyHelper.UpdateStateToDb(transId, "7", Vote_date_and_time.this);
        Extend_MyHelper.sentInviteFCMPerson(id,eId,"3","การโหวตวันที่เวลาเสร็จสิ้น","กรุณารอแม่งานทำการปิดโหวต","OPEN_ACTIVITY_1",Vote_date_and_time.this);
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        in.putExtra("id", id + "");
        startActivity(in);
//        Intent intent = new Intent(Vote_date_and_time.this, MainAttendent.class);
//        intent.putExtra("id", id + "");
//        intent.putExtra("eid", eId + "");
//        intent.putExtra("email", email + "");
//        intent.putExtra("nameEvent", eName + "");
//        intent.putExtra("tab", 0 + "");
//        startActivity(intent);
    }

    public void backVote(View v) {
        Intent intent = new Intent(Vote_date_and_time.this, MainAttendent.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("tab", 0 + "");
        startActivity(intent);
    }

    public void getDate() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getdateforvote.php";
        url += "?eId=" + eId;
        Log.d("getdateforvote", url);
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
                ArrayList<String> day = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                for (int i = 0; i < MyArrList.size(); i++) {
                    if (!MyArrList.get(i).get("time").equalsIgnoreCase("random")) {
                        day.add(MyArrList.get(i).get("time"));
                        time.add(MyArrList.get(i).get("timerange"));
                    }
                }
                day.add("random");
                time.add("random");
                Log.d("getdateforvote", day.toString());
                Log.d("getdateforvote", time.toString());

                Log.d("getdateforvote",day.get(0)+"----"+Extend_MyHelper.getDayFromDateString(day.get(0),"dd/MM/yyyy")+"");
                Log.d("getdateforvote",day.get(1)+"----"+Extend_MyHelper.getDayFromDateString(day.get(1),"dd/MM/yyyy")+"");
                Log.d("getdateforvote",day.get(2)+"----"+Extend_MyHelper.getDayFromDateString(day.get(2),"dd/MM/yyyy")+"");
                for (int i = 0; i < day.size()-1; i++) {
                    StringTokenizer st = new StringTokenizer(day.get(i), "/");
                    String dayString = Extend_MyHelper.getDayFromDateString(day.get(i),"dd/MM/yyyy");
                    while (st.hasMoreTokens()) {
                        String d, m, y, date;
                        String[] some_array = getResources().getStringArray(R.array.month);
                        d = st.nextToken();
                        m = st.nextToken();
                        y = st.nextToken();
                        date = "วัน"+dayString+"\n " + d + " " + some_array[Integer.parseInt(m)] + " " + y;
                        dateDB.add(day.get(i));
                        timeDB.add(time.get(i));
                        if (i==0){
                            btn1.setText(date + "\n เวลา " + time.get(i));
                            btn1.setTextSize(18);
                        }else if (i==1){
                            btn2.setText(date + "\n เวลา " + time.get(i));
                            btn2.setTextSize(18);
                        }
                        else if (i==2){
                            btn3.setText(date + "\n เวลา " + time.get(i));
                            btn3.setTextSize(18);
                        }

                    }
                }

                btn4.setText("วันใดก็ได้");
                btn4.setTextSize(24);
            }
        }.start();
    }

    public void VoteDate(String s, String time) {
        Log.d("votedate", s + " : " + time);
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
    public void getTransIDByTrans(String uid, String eid, String pid) {
        Log.d("themeSelect", "id : " + uid + " eid : " + eid + " pid : " + pid);
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
                            transId = MyArrList.get(0).get("trans_id");
//                            Log.d("themeSelect","myarr : "+MyArrList.toString());
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
