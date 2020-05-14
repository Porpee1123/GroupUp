package com.example.groupup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Head_Date extends AppCompatActivity {
    Head_Date.ResponseStr responseStr = new Head_Date.ResponseStr();
    String uid, eid, nameE, monS, monE, email,wait;
    CheckBox cb1, cb2, cb3, cb4, cb6;
    CalendarView cv;
    ArrayList<String> dataDB, dateSelect, timeDB, timeSelect;
    Button conDateVote;
    EditText edt_mshowCustomDate;
    Spinner spCusTime;
    int cb6Click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_head__date);
        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb6 = findViewById(R.id.checkBox6);
        cv = findViewById(R.id.calendarView);
        edt_mshowCustomDate = findViewById(R.id.dateCustomShow);
        edt_mshowCustomDate.setFocusable(false);
        conDateVote = findViewById(R.id.confirmDateVote);
        spCusTime = findViewById(R.id.sp_customTime);
        spCusTime.setEnabled(false);
        dataDB = new ArrayList<>();
        dateSelect = new ArrayList<>();
        timeDB = new ArrayList<>();
        timeSelect = new ArrayList<>();
        cb6Click = 99;
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        wait = getIntent().getStringExtra("wait");
        conDateVote.setVisibility(View.INVISIBLE);
        getEvent();
//        getDate();
        checkPeopleEvent(eid);

        Log.d("dateselect",uid+" "+email+" "+eid+" "+wait+" "+monS+" "+monE);
        final int[] count = {0};
        final int maxLimit = 3;
        //set spinner time
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.range_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCusTime.setAdapter(adapter);
        spCusTime.setSelection(0);
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0] == maxLimit - 1) {
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(0));
                        timeSelect.add(timeDB.get(0));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(0));
//                        removeTime(timeDB.get(0));
                        count[0]--;
                    }
                } else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(0));
                        timeSelect.add(timeDB.get(0));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(0));
//                        removeTime(timeDB.get(0));
                        count[0]--;
                    }
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0] == maxLimit - 1) {
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(1));
                        timeSelect.add(timeDB.get(1));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(1));
//                        removeTime(timeDB.get(1));
                        count[0]--;
                    }
                } else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(1));
                        timeSelect.add(timeDB.get(1));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(1));
//                        removeTime(timeDB.get(1));
                        count[0]--;
                    }
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0] == maxLimit - 1) {
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(2));
                        timeSelect.add(timeDB.get(2));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(2));
//                        removeTime(timeDB.get(2));
                        count[0]--;
                    }
                } else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(2));
                        timeSelect.add(timeDB.get(2));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(2));
//                        removeTime(timeDB.get(2));
                        count[0]--;
                    }
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0] == maxLimit - 1) {
                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(3));
                        timeSelect.add(timeDB.get(3));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(3));
//                        removeTime(timeDB.get(3));
                        count[0]--;
                    }
                } else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        count[0]++;
                        dateSelect.add(dataDB.get(3));
                        timeSelect.add(timeDB.get(3));
                    } else if (!isChecked) {
                        removeDate(dataDB.get(3));
//                        removeTime(timeDB.get(3));
                        count[0]--;
                    }
                }
            }
        });
        final String[] datecus = {""};
        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (count[0] == maxLimit - 1) {

                    conDateVote.setVisibility(View.VISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        spCusTime.setEnabled(true);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Head_Date.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String date = checkCalendarDate(dayOfMonth + "");
                                        String month = checkCalendar((monthOfYear) + "");
                                        edt_mshowCustomDate.setText(date + "/" + month + "/" + year);

                                        datecus[0] = date + "/" + month + "/" + year;
                                        dateSelect.add(datecus[0]);
                                        timeSelect.add(spCusTime.getSelectedItem().toString());
//                                        timeSelect.add(timeDB.get(3));
                                        cb6Click = dateSelect.size() - 1;
                                    }
                                }, mYear, mMonth, mDay);

                        datePickerDialog.show();
                        count[0]++;

                    } else if (!isChecked) {
                        spCusTime.setEnabled(false);
                        cb6Click = 99;
                        if (!edt_mshowCustomDate.getText().toString().equals("")) {
                            removeDate(datecus[0]);
                        }
                        edt_mshowCustomDate.setText("");

//                        removeTime(timeDB.get(4));
                        count[0]--;
                    }
                } else {
                    conDateVote.setVisibility(View.INVISIBLE);
                    if (count[0] == maxLimit && isChecked) {
                        conDateVote.setVisibility(View.VISIBLE);
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "สามารถเลือกได้สูงสุด 3 วัน", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {
                        spCusTime.setEnabled(true);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Head_Date.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String date = checkCalendarDate(dayOfMonth + "");
                                        String month = checkCalendar((monthOfYear) + "");
                                        edt_mshowCustomDate.setText(date + "/" + month + "/" + year);
                                        datecus[0] = date + "/" + month + "/" + year;
                                        dateSelect.add(datecus[0]);
                                        timeSelect.add(spCusTime.getSelectedItem().toString());
                                        cb6Click = dateSelect.size() - 1;
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();

                        count[0]++;
                    } else if (!isChecked) {
                        spCusTime.setEnabled(false);
                        cb6Click = 99;
                        if (!edt_mshowCustomDate.getText().toString().equals("")) {
                            removeDate(datecus[0]);
                        }
                        edt_mshowCustomDate.setText("");
                        count[0]--;
                    }
                }

            }
        });
        conDateVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wait",wait+"");
                Log.d("dateSelect", dateSelect.toString() + " " + timeSelect.toString());
                if (cb6Click == 99) {
                    Log.d("cb6Click", cb6Click + "");
                    Log.d("cb6Click", dateSelect.toString());
                    Log.d("cb6Click", timeSelect.toString());
                    for (int i = 0; i < dateSelect.size(); i++) {
                        setVoteDateforUser(dateSelect.get(i), timeSelect.get(i));
                    }
                    setVoteDateRandomforUser();
                } else {

                    Log.d("cb6Click", spCusTime.getSelectedItem().toString());
                    Log.d("cb6Click", dateSelect.toString());
                    timeSelect.set(cb6Click, spCusTime.getSelectedItem().toString());
                    Log.d("cb6Click", timeSelect.toString());
                    for (int i = 0; i < dateSelect.size(); i++) {
                        setVoteDateforUser(dateSelect.get(i), timeSelect.get(i));
                    }
                    setVoteDateRandomforUser();
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
//        finish();
    }

    public void backAppoint() {
        Extend_MyHelper.UpdateAllState(eid,"6","3",this);
        Extend_MyHelper.UpdateAllState(eid,"6","2",this);
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
        String url = "http://www.groupupdb.com/android/showtimeforvote.php";
        url += "?eid=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {//0 = id,1 = max people,2 = date
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("0", c.getString("0"));
                                map.put("1", c.getString("1"));
                                map.put("2", c.getString("2"));
                                MyArrList.add(map);
                            }
                            for (int i = 0; i < MyArrList.size(); i++) {
                                dataDB.add(MyArrList.get(i).get("2"));
                            }
                            timeDB.add("11:00 - 13:59");
                            timeDB.add("14:00 - 16:59");
                            timeDB.add("17:00 - 19:59");
                            timeDB.add("20:00 - 23:59");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("dateselect", MyArrList.toString());
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
                for (int i =0;i<dataDB.size();i++){
                    StringTokenizer st = new StringTokenizer(dataDB.get(i), "/");
                    String dayString = Extend_MyHelper.getDayFromDateString(dataDB.get(i),"dd/MM/yyyy");
                    while (st.hasMoreTokens()) {
                        String d, m, y, date;
                        String[] some_array = getResources().getStringArray(R.array.month);
                        d = st.nextToken();
                        m = st.nextToken();
                        y = st.nextToken();
                        date = "วัน"+dayString+"ที่ " + d + " " + some_array[Integer.parseInt(m)] + " " + y;
                        if (i==0){
                            cb1.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==1){
                            cb2.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==2){
                            cb3.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==3){
                            cb4.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }

                    }
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
        timeSelect.remove(Integer.parseInt(number));
        Log.d("dateselect", dateSelect.toString());
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void getHeadSelectCal() {
        //add date for Header select
        responseStr = new Head_Date.ResponseStr();
        String url = "http://www.groupupdb.com/android/getheaderselectcal.php";
        url += "?eid=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
                        Log.d("eventSelect", "getHeadSelectCal " + response);
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

    public void setVoteDateforUser(String s, String time) {
        String url = "http://www.groupupdb.com/android/addvotedateforuser.php";
        url += "?vdid=" + s;//date
        url += "&vtid=" + time;//time
        url += "&eId=" + eid;
        url += "&dLw=" + calwait(Integer.parseInt(wait))+"";
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
    public void setVoteDateRandomforUser() {
        String url = "http://www.groupupdb.com/android/addvotedateforuser.php";
        url += "?vdid=" + "random";//date
        url += "&vtid=" + "random";//time
        url += "&eId=" + eid;
        url += "&dLw=" + calwait(Integer.parseInt(wait))+"";
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

    public String checkCalendar(String monthNumber) {
        Log.d("dateselect", monthNumber);
        switch (monthNumber) {
            case "0":
                return "01";
            case "1":
                return "02";
            case "2":
                return "03";
            case "3":
                return "04";
            case "4":
                return "05";
            case "5":
                return "06";
            case "6":
                return "07";
            case "7":
                return "08";
            case "8":
                return "09";
            case "9":
                return "10";
            case "10":
                return "11";
            case "11":
                return "12";
            default:
                return "00";
        }
    }

    public String checkCalendarDate(String dateNumber) {
        switch (dateNumber) {
            case "1":
                return "01";
            case "2":
                return "02";
            case "3":
                return "03";
            case "4":
                return "04";
            case "5":
                return "05";
            case "6":
                return "06";
            case "7":
                return "07";
            case "8":
                return "08";
            case "9":
                return "09";
            default:
                return dateNumber;
        }
    }
    public String calwait(int wait){
        Calendar cal = Calendar.getInstance();
        Date today1 = cal.getTime();
        cal.add(Calendar.DATE, wait); // to get previous year add -1
        Date nextYear1 = cal.getTime();
        DateFormat simpleNoHour = new SimpleDateFormat("yyyy-MM-dd");
        simpleNoHour.format(nextYear1);
        Log.d("wait",nextYear1+"");
        return simpleNoHour.format(nextYear1)+"";
    }

    public void getnumcal(final int people) {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/getNumdateeventcal.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("numdate", c.getString("numdate"));
                                map.put("numdatemax", c.getString("numdatemax"));
                                MyArrList.add(map);
                            }
                            String numcal = MyArrList.get(0).get("numdate");
                            String numcalmax = MyArrList.get(0).get("numdatemax");
                            Log.d("tab","numcalb : "+ numcal);
                            int waitTime = (Integer.parseInt(numcal)*4)+4;
                            Log.d("tab","numcal : "+ waitTime + " "+ numcalmax);
                            if (people > 2) {
                                Log.d("MyHelper","people > 5");
                                getDate();
                            } else {
                                Log.d("MyHelper","people < 5");
                                getDateless5(waitTime+"",numcalmax);
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
    }
    public void getDateless5(String numWait,String max) {
        responseStr = new Head_Date.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/showtimeforvotelessfive.php";
        url += "?eid=" + eid;
        url += "&wait=" + numWait+"";
        url += "&max=" + max+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {//0 = id,1 = max people,2 = date
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("daforcheck", c.getString("daforcheck"));
                                MyArrList.add(map);
                            }
                            for (int i = 0; i < MyArrList.size(); i++) {
                                dataDB.add(MyArrList.get(i).get("daforcheck"));
                            }
                            timeDB.add("11:00 - 13:59");
                            timeDB.add("14:00 - 16:59");
                            timeDB.add("17:00 - 19:59");
                            timeDB.add("20:00 - 23:59");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("dateselect", "lessfive "+MyArrList.toString());
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
                for (int i =0;i<dataDB.size();i++){
                    StringTokenizer st = new StringTokenizer(dataDB.get(i), "/");
                    String dayString = Extend_MyHelper.getDayFromDateString(dataDB.get(i),"dd/MM/yyyy");
                    while (st.hasMoreTokens()) {
                        String d, m, y, date;
                        String[] some_array = getResources().getStringArray(R.array.month);
                        d = st.nextToken();
                        m = st.nextToken();
                        y = st.nextToken();
                        date = "วัน"+dayString+"ที่ " + d + " " + some_array[Integer.parseInt(m)] + " " + y;
                        if (i==0){
                            cb1.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==1){
                            cb2.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==2){
                            cb3.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }else if (i==3){
                            cb4.setText(date + " ช่วงเวลา " + timeDB.get(i));
                        }

                    }
                }
            }
        }.start();
    }
    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + uid;//ร  อเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("events_detail", c.getString("events_detail"));
                                map.put("events_note", c.getString("events_note"));
                                map.put("events_wait", c.getString("events_wait"));

                                MyArrList.add(map);
                            }
                            wait = MyArrList.get(0).get("events_wait");
                            Log.d("tab","wait : "+ wait);
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
    public void checkPeopleEvent(String eId) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/checkpeopleinevent.php";
        url += "?eId=" + eId;//attend only
        Log.d("MyHelper", "url " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("sumtran", c.getString("sumtran"));
                                MyArrList.add(map);
                            }
                           String peopleInEvent = MyArrList.get(0).get("sumtran");
                            Log.d("MyHelper", "peopleInEvent " + peopleInEvent);
                            final int people = Integer.parseInt(peopleInEvent);
                            getnumcal(people);
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
