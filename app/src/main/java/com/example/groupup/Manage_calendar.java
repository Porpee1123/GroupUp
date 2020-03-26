package com.example.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class Manage_calendar extends AppCompatActivity {
    CalendarPickerView calendarPicker;
    Button btnGetCalen,btnConfirmCalendar;
    Manage_calendar.ResponseStr responseStr = new Manage_calendar.ResponseStr();
    private int CALENDAR_PERMISSION_CODE = 1;
    ArrayList<Date> date = new ArrayList<>();
    ArrayList<String> dateString = new ArrayList<>();
    ArrayList<String> dateDiffString = new ArrayList<>();
    List<String> calendars = new ArrayList<>();
    ArrayList<String> allDaySelect;
    ArrayList<Date> newDate;
    ArrayList<String> dateCalGet = new ArrayList<>();
    ArrayList<String> timeCalGet = new ArrayList<>();
    ArrayList<String> dateCalGetEnd = new ArrayList<>();
    ArrayList<String> timeCalGetEnd = new ArrayList<>();
    ArrayList<String> dateCalGetDiff = new ArrayList<>();
    ArrayList<String> timeCalGetDiff = new ArrayList<>();
    ArrayList<String> startDateTime = new ArrayList<>();
    ArrayList<String> dateCalGetAllDay = new ArrayList<>();
    ArrayList<String> endDateTime = new ArrayList<>();
    ArrayList<String> diffDateTime = new ArrayList<>();
    ArrayList<String> cbStartcalenFromDB = new ArrayList<>();
    ArrayList<String> cbEndcalenFromDB = new ArrayList<>();
    ArrayList<String> dateFromDB = new ArrayList<>();
    ArrayList<String> dateDiffFromDB = new ArrayList<>();
    ArrayList<String> AllDateTime = new ArrayList<>();
    String uid, email;
    String date1,date2;
    //checkbox
    LinearLayout linearCheckbox;
    boolean checkVisible;
    CheckBox cbMorninig, cbLate, cbAfternoon, cbEvening;
    TextView tvDateTime;
    TypedArray arr_month;
    ProgressDialog progressDialog ,saveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_calendar);
        btnGetCalen = (Button) findViewById(R.id.btnGetCalendar);
        calendarPicker = findViewById(R.id.calendarPickerView);
        linearCheckbox = findViewById(R.id.linear_Checkbox_Calendar);
        calendarPicker.highlightDates(date);
        btnConfirmCalendar =findViewById(R.id.btn_confirmCalendar);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        //checkbox
        linearCheckbox.setVisibility(View.GONE);
        checkVisible = true;//close custom
        newDate = new ArrayList();

        tvDateTime = findViewById(R.id.cb_dateTime);
        cbMorninig = findViewById(R.id.cb_time1);
        cbLate = findViewById(R.id.cb_time2);
        cbAfternoon = findViewById(R.id.cb_time3);
        cbEvening = findViewById(R.id.cb_time4);
        cbMorninig.setText(R.string.time_morning);
        cbLate.setText(R.string.time_late);
        cbAfternoon.setText(R.string.time_afternoon);
        cbEvening.setText(R.string.time_evening);
        arr_month = getResources().obtainTypedArray(R.array.month12);
        //get date from DB
        progressDialog = new ProgressDialog(Manage_calendar.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        saveDialog = new ProgressDialog(Manage_calendar.this);
        saveDialog.setMessage("กำลังบันทึกข้อมูล....");
        saveDialog.setTitle("กรุณารอซักครู่");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getcalendarFromDB();
                getDateFromDB();
                getDateDiffFromDB();
            }
        }).start();

        new CountDownTimer(500, 500) {
            public void onFinish() {
//                Log.d("newDate","dateFromDB "+dateFromDB.toString());
//                Log.d("newDate","dateString "+dateString.toString());
                for (int i = 0; i < dateFromDB.size(); i++) {
                    long date = Date.parse(dateFromDB.get(i));
                    Date d = new Date(date);
                    newDate.add(d);
                }
                calendarPicker.highlightDates(newDate);
                cutStringDate(cbStartcalenFromDB,cbEndcalenFromDB);
                Log.d("dateAll ","cbStartcalenFromDB : "+ cbStartcalenFromDB.toString());
                Log.d("dateAll ","cbEndcalenFromDB : "+ cbEndcalenFromDB.toString());
                DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy:HH");
//                Log.d("newDate","newDate "+simple.format(newDate.get(0).getTime())+"size: "+newDate.size());
//                Log.d("newDate","newDate "+checkCalendarDate(newDate.get(0).getDate()+"")+"/"+checkCalendar(newDate.get(0).getMonth()+"")+"/"+newDate.get(0).getYear()+"");
//                Log.d("newDate","newDate "+dateCalGet.get(0));
                Log.d("datediffer","newDate "+dateDiffFromDB.toString()+"size : "+ dateDiffFromDB.size());
                ArrayList<Date> pres = new ArrayList<>();
                for (int i=0;i<dateDiffFromDB.size();i++){
                    long date = Date.parse(dateFromDB.get(i));
                    Date d = new Date(date);
                    pres.add(d);
                    diffDateTime.add(simpleHour.format(pres.get(i))+"");
                    cutStringDateDiff(diffDateTime);
                }
                Log.d("dateAll ","diffDateTime : "+ diffDateTime.toString());
                handler.sendEmptyMessage(0);

            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();

//        Log.d("newDate","cbStartcalenFromDB "+cbStartcalenFromDB.toString());
//        Log.d("newDate","dateCalGet "+dateCalGet.toString());
        //checkbox
        btnConfirmCalendar.setVisibility(View.GONE);
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 4); // ใช้setว่าจะให้แสดงปฏิทินยังไง กี่เดือน
        calendarPicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);//เซ็ตการเลือกว่าจะให้เลือกเป็นวัน เป็นช่วง เป็นหลายๆวัน
        calendarPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            ArrayList<String> day = new ArrayList<>();

            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                clearCheckBox();
                btnConfirmCalendar.setVisibility(View.VISIBLE);
                linearCheckbox.setVisibility(View.VISIBLE);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);
                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);
                String selectedDateCompare = "" + checkCalendarDate(calSelected.get(Calendar.DAY_OF_MONTH) + "") + "/";
                selectedDateCompare += checkCalendar(calSelected.get(Calendar.MONTH) + "") + "/";
                selectedDateCompare += calSelected.get(Calendar.YEAR);
                tvDateTime.setText(calSelected.get(Calendar.DAY_OF_MONTH) + " " + (arr_month.getString(calSelected.get(Calendar.MONTH))) + " " + calSelected.get(Calendar.YEAR));
//                Toast.makeText(Manage_calendar.this, selectedDateCompare, Toast.LENGTH_SHORT).show();
                String s = selectedDate;
                day.add(s);
                Log.d("newDate","dateCalGet "+dateCalGet.toString()+"---"+dateCalGetEnd.toString());
                for (int i = 0; i < dateCalGet.size(); i++) {
//                    Log.d("dateTime", "dateCalGet : " + dateCalGet.get(i) + " " + selectedDateCompare);
                    if (selectedDateCompare.equals(dateCalGet.get(i))) {
//                        Log.d("dateTime", "checkbox : " + Integer.parseInt(timeCalGet.get(i)) + " " + Integer.parseInt(timeCalGetEnd.get(i)) + " " + dateCalGet.get(i) + " " + dateCalGetEnd.get(i));
                        Log.d("dateTime123", "selectedDateCompare : " + selectedDateCompare);
                        clickCheckboxDateTime(Integer.parseInt(timeCalGet.get(i)), Integer.parseInt(timeCalGetEnd.get(i)), dateCalGet.get(i), dateCalGetEnd.get(i));
                    } else if (selectedDateCompare.equals(dateCalGetEnd.get(i))) {
                        clickCheckboxDiffDTLastDay(Integer.parseInt(timeCalGetEnd.get(i)));
                    }
                }
                for (int i = 0; i < dateCalGetDiff.size(); i++) {
//                    Log.d("diffDateTime", "dateCalGetDiff : " + dateCalGetDiff.toString());
                    if (selectedDateCompare.equals(dateCalGetDiff.get(i))) {
                        cbMorninig.setChecked(true);
                        cbLate.setChecked(true);
                        cbAfternoon.setChecked(true);
                        cbEvening.setChecked(true);
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                clearCheckBox();
                linearCheckbox.setVisibility(View.GONE);
                Calendar calUnSelected = Calendar.getInstance();
                calUnSelected.setTime(date);

                String selectedDate = "" + calUnSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calUnSelected.get(Calendar.MONTH) + 1)
                        + " " + calUnSelected.get(Calendar.YEAR);

//                Toast.makeText(Manage_calendar.this, selectedDate, Toast.LENGTH_SHORT).show();
                String s = selectedDate;
                String del = "";
                for (int i = 0; i < day.size(); i++) {
                    if (s.equals(day.get(i))) {
                        del = i + "";
                    }//time == time diff click all day
                }
//                allDaySelect.remove(Integer.parseInt(del));
                day.remove(Integer.parseInt(del));
            }
        });
//        //Change format date
//        newDate = new ArrayList();
//        for (int i = 0; i < dateString.size(); i++) {
////            DateFormat simple = new SimpleDateFormat();
//            long date = Date.parse(dateString.get(i));
//            Date d = new Date(date * 1000);
//            newDate.add(d);
//        }


        allDaySelect = new ArrayList();
        btnGetCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Manage_calendar.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Manage_calendar.this, "You have already permission", Toast.LENGTH_SHORT).show();
                    readCalendarEvent(Manage_calendar.this);
                    btnConfirmCalendar.setVisibility(View.VISIBLE);

                    for (int i = 0; i < dateString.size(); i++) {
                        long date = Date.parse(dateString.get(i));
                        Date d = new Date(date);
                        newDate.add(d);
                    }
                    calendarPicker.highlightDates(newDate);
//                    Log.d("newDate","newDate123"+dateString.toString());


                } else {
                    requestCalendarPermission();
//                    readCalendarEvent(Manage_calendar.this);
//                    newDate = new ArrayList();
//                    for (int i = 0; i < dateString.size(); i++) {
//                        long date = Date.parse(dateString.get(i));
//                        Date d = new Date(date);
//                        newDate.add(d);
//                    }
//                    calendarPicker.highlightDates(newDate);
                }
            }
        });
        btnConfirmCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog.show();
                final android.app.AlertDialog viewDetail = new android.app.AlertDialog.Builder(Manage_calendar.this).create();
                viewDetail.setTitle("ยืนยันการเพิ่มวันที่");

                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE,"ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE,"ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (startDateTime!=null||endDateTime!=null||dateString!=null){
                            for (int i=0;i<startDateTime.size();i++){
                                sentCalendarToDB(startDateTime.get(i),endDateTime.get(i));
                            }
                            for (int i=0;i<dateString.size();i++){
                                sentDateToDB(dateString.get(i));
                            }
                            for(int i=0;i<dateDiffString.size();i++){
                                sentDateDiffToDB(dateDiffString.get(i));
                            }
                            handlerSave.sendEmptyMessage(0);
                        }else{

                        }

                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
        }
    };
    Handler handlerSave = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            saveDialog.dismiss();
        }
    };
    public List<String> readCalendarEvent(Context context) {

        Calendar startTime = Calendar.getInstance();

        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MONTH, 4); //set ว่าให้search กี่เดือน

        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ) AND ( deleted != 1 ))";
//        Cursor cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, null, null);
        Log.i("@startTime", "Calendar startTime : " + startTime);
        Log.i("@endTime", "Calendar endTime : " + endTime);


        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation", CalendarContract.Events.ALL_DAY}, selection,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id

        calendars.clear();

        Log.d("cnameslength", "" + CNames.length);
        if (CNames.length == 0) {
            Toast.makeText(context, "No event exists in calendar", Toast.LENGTH_LONG).show();
        }
//        Log.i("@calendar", "Cursor count " + cursor.getCount());
        String s1 = "", s2 = "", s3 = "", all = "";
//        TextView txt = (TextView) findViewById(R.id.textView);
        for (int i = 0; i < CNames.length; i++) {

            calendars.add(cursor.getString(1));
            Date d = new Date();
            Date dend = new Date();
            Date ddiff = new Date();
            long oneday = 86400000;
            CNames[i] = cursor.getString(1);
            s1 = cursor.getString(1);
            s2 = cursor.getString(3);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            if (cursor.getString(4) == null) {
                s3 = cursor.getString(3);
                c2.setTimeInMillis(Long.parseLong(s3) + oneday);
            } else {
                s3 = cursor.getString(4);
                c2.setTimeInMillis(Long.parseLong(s3));
            }
            c1.setTimeInMillis(Long.parseLong(s2));

            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy:HH"); //dd/MM/yyyy/HH/mm
            all += s1 + "\n\t\t" + simple.format(c1.getTime()) + "---" + simple.format(c2.getTime()) + "\n\n";
//            Log.d("dateTime ", "Calendar Name long : " + s1 + " - " + simple.format(c1.getTime()) + " + " + simple.format(c2.getTime()) + " : " + cursor.getString(6));
            startDateTime.add(simple.format(c1.getTime()));
            endDateTime.add(simple.format(c2.getTime()));
            dateCalGetAllDay.add(cursor.getString(6));
            cutStringDate(startDateTime, endDateTime);
            Log.d("arraydb ", "size : " + startDateTime.size() + " dateString : " + c1.getTime());
            Log.d("arraydb ", "size : " + endDateTime.size() + " dateString : " + endDateTime.toString());
            d.setTime(cursor.getLong(3));
            dend.setTime(Long.parseLong(s3));
            date.add(d);
            dateString.add(d.toString());
            if (cursor.getString(6).equals("0")) {
                date.add(dend);
                dateString.add(dend.toString());
            }
            if (d.getDate() != dend.getDate()) {
//                Log.d("checkTime ", "d : " + d.getDate()+"---"+dend.getDate());
                int dif = 0;
                dif = Integer.parseInt(dend.getDate() + "") - Integer.parseInt(d.getDate() + "");
                Log.d("dateTime ", "d : " + d.getDate() + "---" + dend.getDate());
                Log.d("dateTime ", "dif : " + dif);
                for (int num = 1; num < dif; num++) {
                    ddiff.setTime(d.getTime() + (oneday) * num);
                    Log.d("checkTime ", "checkTime : " + ddiff);
                    Log.d("dateTime ", "ddiff : " + ddiff);
                    date.add(ddiff);
                    dateString.add(ddiff.toString());
                    dateDiffString.add(ddiff.toString());
                    diffDateTime.add(simple.format(ddiff));
                    Log.d("checkTime ", "date : " + dateDiffString.get(dateDiffString.size()-1)+" date size: "+dateDiffString.size());
                }
                cutStringDateDiff(diffDateTime);
            }

            Log.d("arraydb14547 ", "size : " + dateString.size() + " dateString : " + dateString.toString());
            cursor.moveToNext();
        }
//        Log.d("newDate","dateString "+dateString.toString());
        return calendars;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALENDAR_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Access", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void requestCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the calendar")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Manage_calendar.this, new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_CODE);
                        }
                    }).setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_CODE);
        }
    }

    public void clickCheckboxDateTime(int tiStart, int tiEnd, String daStart, String daEnd) {
        if (!daStart.equals(daEnd) && tiStart == tiEnd) {//date same day all day
            Log.d("calendar123", "date123 : " + daStart + " - " + daEnd);
            cbMorninig.setChecked(true);
            cbLate.setChecked(true);
            cbAfternoon.setChecked(true);
            cbEvening.setChecked(true);
        } else {
            Log.d("calendar123", "date : " + daStart + " - " + daEnd);
            if (!daStart.equals(daEnd) && tiStart != tiEnd) {//no same dat No all time
                Log.d("calendar123", "date : " + daStart + " - " + daEnd);
                if (tiStart >= 0 && tiStart < 11) {
                    //0
                } else if (tiStart >= 11 && tiStart < 14) {
                    //1234
                    cbMorninig.setChecked(true);
                    cbLate.setChecked(true);
                    cbAfternoon.setChecked(true);
                    cbEvening.setChecked(true);
                } else if (tiStart >= 14 && tiStart < 17) {
                    //234
                    cbLate.setChecked(true);
                    cbAfternoon.setChecked(true);
                    cbEvening.setChecked(true);
                } else if (tiStart >= 17 && tiStart < 20) {
                    //34
                    cbAfternoon.setChecked(true);
                    cbEvening.setChecked(true);
                } else if (tiStart >= 20 && tiStart < 24) {
                    //4
                    cbEvening.setChecked(true);
                }
            } else {
                if (tiStart >= 0 && tiStart < 14) {
                    if (tiEnd >= 0 && tiEnd < 11) {
                        //0
                    } else if (tiEnd >= 11 && tiEnd < 14) {
                        //1
                        cbMorninig.setChecked(true);
                    } else if (tiEnd >= 14 && tiEnd < 17) {
                        //12
                        cbMorninig.setChecked(true);
                        cbLate.setChecked(true);
                    } else if (tiEnd >= 17 && tiEnd < 20) {
                        //123
                        cbMorninig.setChecked(true);
                        cbLate.setChecked(true);
                        cbAfternoon.setChecked(true);
                    } else if (tiEnd >= 20 && tiEnd < 24) {
                        //1234
                        cbMorninig.setChecked(true);
                        cbLate.setChecked(true);
                        cbAfternoon.setChecked(true);
                        cbEvening.setChecked(true);
                    }
                } else if (tiStart >= 14 && tiStart < 17) {
                    if (tiEnd >= 14 && tiEnd < 17) {
                        //2
                        cbLate.setChecked(true);
                    } else if (tiEnd >= 17 && tiEnd < 20) {
                        //23
                        cbLate.setChecked(true);
                        cbAfternoon.setChecked(true);
                    } else if (tiEnd >= 20 && tiEnd < 24) {
                        //234
                        cbLate.setChecked(true);
                        cbAfternoon.setChecked(true);
                        cbEvening.setChecked(true);
                    }
                } else if (tiStart >= 17 && tiStart < 20) {
                    if (tiEnd >= 17 && tiEnd < 20) {
                        //3
                        cbAfternoon.setChecked(true);
                    } else if (tiEnd >= 20 && tiEnd < 24) {
                        //34
                        cbAfternoon.setChecked(true);
                        cbEvening.setChecked(true);
                    }
                } else if (tiStart >= 20 && tiStart < 24) {
                    if (tiEnd >= 20 && tiEnd < 24) {
                        //4
                        cbEvening.setChecked(true);
                    }
                }
            }

        }

    }

    public void clickCheckboxDiffDTLastDay(int tiEnd) {
        if (tiEnd >= 0 && tiEnd < 11) {
            //0
        } else if (tiEnd >= 11 && tiEnd < 14) {
            //1
            cbMorninig.setChecked(true);
        } else if (tiEnd >= 14 && tiEnd < 17) {
            //12
            cbMorninig.setChecked(true);
            cbLate.setChecked(true);
        } else if (tiEnd >= 17 && tiEnd < 20) {
            //123
            cbMorninig.setChecked(true);
            cbLate.setChecked(true);
            cbAfternoon.setChecked(true);
            ;
        } else if (tiEnd >= 20 && tiEnd < 24) {
            //1234
            cbMorninig.setChecked(true);
            cbLate.setChecked(true);
            cbAfternoon.setChecked(true);
            cbEvening.setChecked(true);
        }
    }

    public void clearCheckBox() {
        cbMorninig.setChecked(false);
        cbLate.setChecked(false);
        cbAfternoon.setChecked(false);
        cbEvening.setChecked(false);
    }

    public void cutStringDate(ArrayList dt, ArrayList edt) {
        for (int i = 0; i < dt.size(); i++) {
            StringTokenizer st = new StringTokenizer(dt.get(i).toString(), ":");
            while (st.hasMoreTokens()) {
                dateCalGet.add(st.nextToken());
                timeCalGet.add(st.nextToken());
            }
        }
        for (int i = 0; i < edt.size(); i++) {
            StringTokenizer st = new StringTokenizer(edt.get(i).toString(), ":");
            while (st.hasMoreTokens()) {
                dateCalGetEnd.add(st.nextToken());
                timeCalGetEnd.add(st.nextToken());
            }
        }

    }

    public void cutStringDateDiff(ArrayList dt) {
        for (int i = 0; i < dt.size(); i++) {
            StringTokenizer st = new StringTokenizer(dt.get(i).toString(), ":");
            while (st.hasMoreTokens()) {
                dateCalGetDiff.add(st.nextToken());
                timeCalGetDiff.add(st.nextToken());
            }
        }
    }

    public String checkCalendar(String monthNumber) {
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
    public void sentCalendarToDB(String startDate, String endDate) {
        String url = "http://www.groupupdb.com/android/addcalendar.php";
        url += "?sId=" + uid;
        url += "&sdt=" + startDate;
        url += "&edt=" + endDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
                        try {
                            String strStatusID = "0";
                            String strError = "Unknow Status!";
                            JSONObject c;
                            JSONArray data = new JSONArray("[" + response.toString() + "]");
                            for (int i = 0; i < data.length(); i++) {
                                c = data.getJSONObject(i);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            }
                            if (strStatusID.equals("0")) {

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(InviteFriend_Attendant.this, "Submission Error!", Toast.LENGTH_LONG).show();
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
    public void getcalendarFromDB(){
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getcalendar.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
                                map.put("usercalendar_id", c.getString("usercalendar_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("startdatetime", c.getString("startdatetime"));
                                map.put("enddatetime", c.getString("enddatetime"));
                                //map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
//                            Log.d("newDate","MyArrList"+MyArrList.toString());
                            for (int i =0;i<MyArrList.size();i++){
                                cbStartcalenFromDB.add(MyArrList.get(i).get("startdatetime"));
                                cbEndcalenFromDB.add(MyArrList.get(i).get("enddatetime"));
//                                Log.d("newDate","MyArrList"+MyArrList.get(i).get("startdatetime"));
                            }
//                            Log.d("newDate","cbStartcalenFromDB2"+cbStartcalenFromDB.toString());
                            Log.d("query", MyArrList.toString() + "");
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
    public void sentDateToDB(String date) {
        date1="";
        date2="";
        CutStringDateForSaveDB(date);
        String url = "http://www.groupupdb.com/android/adddate.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
                        try {
                            String strStatusID = "0";
                            String strError = "Unknow Status!";
                            JSONObject c;
                            JSONArray data = new JSONArray("[" + response.toString() + "]");
                            for (int i = 0; i < data.length(); i++) {
                                c = data.getJSONObject(i);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            }
                            if (strStatusID.equals("0")) {

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(InviteFriend_Attendant.this, "Submission Error!", Toast.LENGTH_LONG).show();
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
    public void getDateFromDB(){
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getdate.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
                                map.put("userdateid", c.getString("userdateid"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("date", c.getString("date"));
                                map.put("datelast", c.getString("datelast"));

                                //map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
                            for (int i =0;i<MyArrList.size();i++){
                                dateFromDB.add(MyArrList.get(i).get("date")+"+"+MyArrList.get(i).get("datelast"));
                            }
//                            Log.d("newDate","dateFromDB"+dateFromDB.toString());
                            Log.d("query", MyArrList.toString() + "");
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
    public void sentDateDiffToDB(String date) {
        date1="";
        date2="";
        CutStringDateForSaveDB(date);
        String url = "http://www.groupupdb.com/android/adddatediff.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
                        try {
                            String strStatusID = "0";
                            String strError = "Unknow Status!";
                            JSONObject c;
                            JSONArray data = new JSONArray("[" + response.toString() + "]");
                            for (int i = 0; i < data.length(); i++) {
                                c = data.getJSONObject(i);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            }
                            if (strStatusID.equals("0")) {

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(InviteFriend_Attendant.this, "Submission Error!", Toast.LENGTH_LONG).show();
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
    public void getDateDiffFromDB(){
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getdatediff.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
                                map.put("udfid", c.getString("udfid"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("datediff", c.getString("datediff"));
                                map.put("datediffend", c.getString("datediffend"));

                                //map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
                            for (int i =0;i<MyArrList.size();i++){
                                dateDiffFromDB.add(MyArrList.get(i).get("datediff")+"+"+MyArrList.get(i).get("datediffend"));
                            }
//                            Log.d("newDate","dateDiffFromDB"+dateDiffFromDB.toString());
//                            Log.d("query", MyArrList.toString() + "");
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
    public void CutStringDateForSaveDB(String s){
        StringTokenizer st = new StringTokenizer(s,"+");
        while (st.hasMoreTokens()){
            date1 = st.nextToken();
            date2 = st.nextToken();
        }
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
}
