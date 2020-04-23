package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class Head_Date extends AppCompatActivity {
    Head_Date.ResponseStr responseStr = new Head_Date.ResponseStr();
    String uid, eid, nameE, monS, monE, email;
    CheckBox cb1, cb2, cb3, cb4, cb5;
    CalendarView cv;
    ArrayList<String> dataDB, dateSelect;
    Button conDateVote ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_head__date);
        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cv = findViewById(R.id.calendarView);
        conDateVote = findViewById(R.id.confirmDateVote);
        dataDB = new ArrayList<>();
        dateSelect = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        conDateVote.setVisibility(View.INVISIBLE);
        getDate();
        final int[] count = {0};
        final int maxLimit = 3;
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0]==maxLimit-1){
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(0));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(0));
                        count[0]--;
                    }
                }else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(0));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(0));
                        count[0]--;
                    }
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0]==maxLimit-1){
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(1));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(1));
                        count[0]--;
                    }
                }else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(1));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(1));
                        count[0]--;
                    }
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0]==maxLimit-1){
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(2));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(2));
                        count[0]--;
                    }
                }else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(2));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(2));
                        count[0]--;
                    }
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0]==maxLimit-1){
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(3));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(3));
                        count[0]--;
                    }
                }else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(3));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(3));
                        count[0]--;
                    }
                }
            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0]==maxLimit-1){
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(4));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(4));
                        count[0]--;
                    }
                }else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(4));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(4));
                        count[0]--;
                    }
                }
            }
        });
        conDateVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dateSelect",dateSelect.toString());
                for (int i=0;i< dateSelect.size();i++){
                    setVoteDateforUser(dateSelect.get(i));
                }
                backAppoint();
            }
        });
    }

    public void backAppoint(View v) {
        Intent intent = new Intent(Head_Date.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid + "");
        intent.putExtra("email", email + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("tab", 1 + "");
        startActivity(intent);
    }
    public void backAppoint() {
        Intent intent = new Intent(Head_Date.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid + "");
        intent.putExtra("email", email + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("tab", 1 + "");
        startActivity(intent);
    }

    public void getDate() {
        responseStr = new Head_Date.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getdateforHeaderSelectVote.php";
        url += "?eId=" + eid;
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
                                MyArrList.add(map);
                            }
                            for (int i = 0; i < MyArrList.size(); i++) {
                                dataDB.add(MyArrList.get(i).get("time"));
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
                StringTokenizer st = new StringTokenizer(dataDB.get(0),"/");
                StringTokenizer st1 = new StringTokenizer(dataDB.get(1),"/");
                StringTokenizer st2 = new StringTokenizer(dataDB.get(2),"/");
                StringTokenizer st3 = new StringTokenizer(dataDB.get(3),"/");
                StringTokenizer st4 = new StringTokenizer(dataDB.get(4),"/");
                while (st.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st.nextToken();
                    m= st.nextToken();
                    y= st.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    cb1.setText(date+" ช่วงเวลา 11:00 - 13:59");
                }
                while (st1.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st1.nextToken();
                    m= st1.nextToken();
                    y= st1.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    cb2.setText(date+" ช่วงเวลา 14:00 - 16:59");
                }
                while (st2.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st2.nextToken();
                    m= st2.nextToken();
                    y= st2.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    cb3.setText(date+" ช่วงเวลา 17:00 - 19:59");
                }
                while (st3.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st3.nextToken();
                    m= st3.nextToken();
                    y= st3.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    cb4.setText(date+" ช่วงเวลา 20:00 - 23:59");
                }
                while (st4.hasMoreTokens()){
                    String d,m,y,date;
                    String[] some_array = getResources().getStringArray(R.array.month);
                    d= st4.nextToken();
                    m= st4.nextToken();
                    y= st4.nextToken();
                    date = "วันที่ "+d+" "+some_array[Integer.parseInt(m)]+" "+y;
                    cb5.setText(date+" ช่วงเวลา ทั้งวัน");
                }
            }
        }.start();
    }

    public void removeDate(String id) {
        String number = "";
        for (int i = 0; i < dateSelect.size(); i++) {
            if (id.equals(dateSelect.get(i))) {
                number = i + "";
            }
        }
        dateSelect.remove(Integer.parseInt(number));
        Log.d("dateselect", dateSelect.toString());
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void getHeadSelectCal(){
        //add date for Header select
        responseStr = new Head_Date.ResponseStr();
        String url = "http://www.groupupdb.com/android/getheaderselectcal.php";
        url += "?eid=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
                        Log.d("eventSelect","getHeadSelectCal "+response);
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
    public void setVoteDateforUser(String s){
        String url = "http://www.groupupdb.com/android/addvotedateforuser.php";
        url += "?vdid=" + s;
        url += "&eId=" + eid;
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
