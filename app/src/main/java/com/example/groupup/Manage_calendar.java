package com.example.groupup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Manage_calendar extends AppCompatActivity {
    CalendarPickerView calendarPicker;
    Button btnGetCalen, btnConfirmCalendar, btnDelCalendar;
    Manage_calendar.ResponseStr responseStr = new Manage_calendar.ResponseStr();
    private int CALENDAR_PERMISSION_CODE = 1;
    private RequestQueue requestQueue;
    ArrayList<String> dateString;
    ArrayList<String> dateDiffString;
    List<String> calendars = new ArrayList<>();
    ArrayList<String> allDaySelect;
    ArrayList<Date> newDate;
    ArrayList<String> dateCalGet = new ArrayList<>();
    ArrayList<String> timeCalGet = new ArrayList<>();
    ArrayList<String> dateCalGetEnd = new ArrayList<>();
    ArrayList<String> timeCalGetEnd = new ArrayList<>();
    ArrayList<String> dateCalGetDiff = new ArrayList<>();
    ArrayList<String> timeCalGetDiff = new ArrayList<>();
    ArrayList<String> dateCalGetAllDay = new ArrayList<>();
    ArrayList<String> startDateTime;
    ArrayList<String> diffRangeDateTimeStart;
    ArrayList<String> diffRangeDateTimeEnd;
    ArrayList<String> endDateTimeGetCalen;
    ArrayList<String> startDateTimeGetCalen;
    ArrayList<String> endDateTime;
    ArrayList<String> startDateTimeDel;
    ArrayList<String> endDateTimeDel;
    ArrayList<String> diffDateTime = new ArrayList<>();
    ArrayList<String> cbStartcalenFromDB = new ArrayList<>();
    ArrayList<String> cbEndcalenFromDB = new ArrayList<>();
    ArrayList<String> dateFromDB = new ArrayList<>();
    ArrayList<String> dateDiffFromDB = new ArrayList<>();
    ArrayList<String> dateForDB = new ArrayList<>();
    //Array for 1 year
    static ArrayList<String> dateInYear; // real Tue Sep 01 20:44:10 GMT+07:00 2020
    String uid, email;
    String date1, date2;
    //checkbox
    LinearLayout linearCheckbox;
    boolean checkVisible;
    CheckBox cbMorninig, cbLate, cbAfternoon, cbEvening;
    TextView tvDateTime;
    TypedArray arr_month;
    ProgressDialog progressDialog, saveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_manage_calendar);
        btnGetCalen = findViewById(R.id.btnGetCalendar);
        calendarPicker = findViewById(R.id.calendarPickerView);
        linearCheckbox = findViewById(R.id.linear_Checkbox_Calendar);
//        calendarPicker.highlightDates(date);
        btnConfirmCalendar = findViewById(R.id.btn_confirmCalendar);
        btnDelCalendar = findViewById(R.id.btnDelCalendar);
        dateInYear = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        //checkbox
        linearCheckbox.setVisibility(View.GONE);
        checkVisible = true;//close custom
        newDate = new ArrayList();
        startDateTime = new ArrayList<>();
        startDateTimeGetCalen = new ArrayList<>();
        endDateTimeGetCalen = new ArrayList<>();
        diffRangeDateTimeStart = new ArrayList<>();
        diffRangeDateTimeEnd = new ArrayList<>();
        endDateTime = new ArrayList<>();
        startDateTimeDel = new ArrayList<>();
        endDateTimeDel = new ArrayList<>();
        dateString = new ArrayList<>();
        dateDiffString = new ArrayList<>();
        allDaySelect = new ArrayList();
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
        btnConfirmCalendar.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDateFromDB();
                getDateDiffFromDB();
                getcalendarFromDB();
            }
        }).start();


        functionInCreate();
        calendarPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
//                Log.d("dateSelectClick","date : "+date);
                btnGetCalen.setEnabled(false);
                clearCheckBox();
                btnConfirmCalendar.setVisibility(View.VISIBLE);
                linearCheckbox.setVisibility(View.VISIBLE);
                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);
                String selectedDateCompare = "" + checkCalendarDate(calSelected.get(Calendar.DAY_OF_MONTH) + "") + "/";
                selectedDateCompare += checkCalendar(calSelected.get(Calendar.MONTH) + "") + "/";
                selectedDateCompare += calSelected.get(Calendar.YEAR);
//                Log.d("dateSelectClick","selectedDate : "+selectedDateCompare);
                Log.d("checkdiff", "selectedDateCompare : " + selectedDateCompare);
                dateForDB.add(date.toString());
                final String finalSelectedDateCompare = selectedDateCompare;
                final int[] countMor = {1};
                final int[] countLat = {1};
                final int[] countAft = {1};
                final int[] countEve = {1};
                cbMorninig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countMor[0] % 2 != 0) {
                            if (cbMorninig.isChecked()) {
                                startDateTime.add(finalSelectedDateCompare + ":11");
                                endDateTime.add(finalSelectedDateCompare + ":13");
                                removeTime(finalSelectedDateCompare + ":11", startDateTimeDel);
                                removeTime(finalSelectedDateCompare + ":13", endDateTimeDel);

                            } else {
                                startDateTimeDel.add(finalSelectedDateCompare + ":11");
                                endDateTimeDel.add(finalSelectedDateCompare + ":13");
                                removeTime(finalSelectedDateCompare + ":11", startDateTime);
                                removeTime(finalSelectedDateCompare + ":13", endDateTime);
                            }
                        } else {
                            removeTime(finalSelectedDateCompare + ":11", startDateTime);
                            removeTime(finalSelectedDateCompare + ":13", endDateTime);
                            removeTime(finalSelectedDateCompare + ":11", startDateTimeDel);
                            removeTime(finalSelectedDateCompare + ":13", endDateTimeDel);
                        }
                        countMor[0]++;
                    }
                });
                cbLate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countLat[0] % 2 != 0) {
                            if (cbLate.isChecked()) {
                                startDateTime.add(finalSelectedDateCompare + ":14");
                                endDateTime.add(finalSelectedDateCompare + ":16");
                                removeTime(finalSelectedDateCompare + ":14", startDateTimeDel);
                                removeTime(finalSelectedDateCompare + ":16", endDateTimeDel);

                            } else {
                                startDateTimeDel.add(finalSelectedDateCompare + ":14");
                                endDateTimeDel.add(finalSelectedDateCompare + ":16");
                                removeTime(finalSelectedDateCompare + ":14", startDateTime);
                                removeTime(finalSelectedDateCompare + ":16", endDateTime);
                            }
                        } else {
                            removeTime(finalSelectedDateCompare + ":14", startDateTime);
                            removeTime(finalSelectedDateCompare + ":16", endDateTime);
                            removeTime(finalSelectedDateCompare + ":14", startDateTimeDel);
                            removeTime(finalSelectedDateCompare + ":16", endDateTimeDel);
                        }
                        countLat[0]++;
                    }
                });
                cbAfternoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countAft[0] % 2 != 0) {
                            if (cbAfternoon.isChecked()) {
                                startDateTime.add(finalSelectedDateCompare + ":17");
                                endDateTime.add(finalSelectedDateCompare + ":19");
                                removeTime(finalSelectedDateCompare + ":17", startDateTimeDel);
                                removeTime(finalSelectedDateCompare + ":19", endDateTimeDel);

                            } else {
                                startDateTimeDel.add(finalSelectedDateCompare + ":17");
                                endDateTimeDel.add(finalSelectedDateCompare + ":19");
                                removeTime(finalSelectedDateCompare + ":17", startDateTime);
                                removeTime(finalSelectedDateCompare + ":19", endDateTime);
                            }
                        } else {
                            removeTime(finalSelectedDateCompare + ":17", startDateTime);
                            removeTime(finalSelectedDateCompare + ":19", endDateTime);
                            removeTime(finalSelectedDateCompare + ":17", startDateTimeDel);
                            removeTime(finalSelectedDateCompare + ":19", endDateTimeDel);
                        }
                        countAft[0]++;
                    }
                });
                cbEvening.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countEve[0] % 2 != 0) {
                            if (cbEvening.isChecked()) {
                                startDateTime.add(finalSelectedDateCompare + ":20");
                                endDateTime.add(finalSelectedDateCompare + ":23");
                                removeTime(finalSelectedDateCompare + ":20", startDateTimeDel);
                                removeTime(finalSelectedDateCompare + ":23", endDateTimeDel);

                            } else {
                                startDateTimeDel.add(finalSelectedDateCompare + ":20");
                                endDateTimeDel.add(finalSelectedDateCompare + ":23");
                                removeTime(finalSelectedDateCompare + ":20", startDateTime);
                                removeTime(finalSelectedDateCompare + ":23", endDateTime);
                            }
                        } else {
                            removeTime(finalSelectedDateCompare + ":20", startDateTime);
                            removeTime(finalSelectedDateCompare + ":23", endDateTime);
                            removeTime(finalSelectedDateCompare + ":20", startDateTimeDel);
                            removeTime(finalSelectedDateCompare + ":23", endDateTimeDel);
                        }
                        countEve[0]++;
                    }
                });
                Log.d("dateSelectClick", "dateForDB : " + dateForDB.toString());
                Log.d("dateSelectClick", "startDateTime : " + startDateTime.toString());
                Log.d("dateSelectClick", "endDateTime : " + endDateTime.toString());
                Log.d("dateSelectClick", "startDateTimeDel : " + startDateTimeDel.toString());
                Log.d("dateSelectClick", "endDateTimeDel : " + endDateTimeDel.toString());
                tvDateTime.setText(calSelected.get(Calendar.DAY_OF_MONTH) + " " + (arr_month.getString(calSelected.get(Calendar.MONTH))) + " " + calSelected.get(Calendar.YEAR));
                Log.d("newDate", "dateCalGet " + dateCalGet.toString() + "---" + dateCalGetEnd.toString());
                for (int i = 0; i < dateCalGet.size(); i++) {
//                    Log.d("dateTime", "dateCalGet : " + dateCalGet.get(i) + " " + selectedDateCompare);
                    if (selectedDateCompare.equals(dateCalGet.get(i))) {
//                        Log.d("dateTime", "checkbox : " + Integer.parseInt(timeCalGet.get(i)) + " " + Integer.parseInt(timeCalGetEnd.get(i)) + " " + dateCalGet.get(i) + " " + dateCalGetEnd.get(i));
                        Log.d("dateTime123", "selectedDateCompare : " + selectedDateCompare);
                        clickCheckboxDateTime(Integer.parseInt(timeCalGet.get(i)), Integer.parseInt(timeCalGetEnd.get(i)), dateCalGet.get(i), dateCalGetEnd.get(i));

                    }
//                    else if (selectedDateCompare.equals(dateCalGetEnd.get(i))) {
//                        clickCheckboxDiffDTLastDay(Integer.parseInt(timeCalGetEnd.get(i)));
//                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                clearCheckBox();
                linearCheckbox.setVisibility(View.GONE);
                Calendar calUnSelected = Calendar.getInstance();
                calUnSelected.setTime(date);
                String unSelectedDateCompare = "" + checkCalendarDate(calUnSelected.get(Calendar.DAY_OF_MONTH) + "") + "/";
                unSelectedDateCompare += checkCalendar(calUnSelected.get(Calendar.MONTH) + "") + "/";
                unSelectedDateCompare += calUnSelected.get(Calendar.YEAR);

//                Toast.makeText(Manage_calendar.this, selectedDate, Toast.LENGTH_SHORT).show();
                removeDate(date.toString(), dateForDB);
                final String finalUnSelectedDateCompare = unSelectedDateCompare;
                removeTime(finalUnSelectedDateCompare + ":11", startDateTime);
                removeTime(finalUnSelectedDateCompare + ":13", endDateTime);
                removeTime(finalUnSelectedDateCompare + ":14", startDateTime);
                removeTime(finalUnSelectedDateCompare + ":16", endDateTime);
                removeTime(finalUnSelectedDateCompare + ":17", startDateTime);
                removeTime(finalUnSelectedDateCompare + ":19", endDateTime);
                removeTime(finalUnSelectedDateCompare + ":20", startDateTime);
                removeTime(finalUnSelectedDateCompare + ":23", endDateTime);
                removeTime(finalUnSelectedDateCompare + ":11", startDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":13", endDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":14", startDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":16", endDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":17", startDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":19", endDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":20", startDateTimeDel);
                removeTime(finalUnSelectedDateCompare + ":23", endDateTimeDel);
//                removeTimeSame2Array(startDateTime,startDateTimeDel); // for test
                Log.d("dateSelectClick", "RemovedateForDB : " + dateForDB.toString());
                Log.d("dateSelectClick", "RemovestartDateTime : " + startDateTime.toString());
                Log.d("dateSelectClick", "RemoveendDateTime : " + endDateTime.toString());
                Log.d("dateSelectClick", "RemovestartDateTimeDel : " + startDateTimeDel.toString());
                Log.d("dateSelectClick", "RemoveendDateTimeDel : " + endDateTimeDel.toString());

                if (dateForDB.size() == 0) {
                    btnGetCalen.setEnabled(true);
                } else {
                    btnGetCalen.setEnabled(false);
                }
            }
        });


        btnGetCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Manage_calendar.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(Manage_calendar.this, "You have already permission", Toast.LENGTH_SHORT).show();
                    SaveCalenGettoDB();

                } else {
                    requestCalendarPermission();

                }
            }
        });
        btnConfirmCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog viewDetail = new android.app.AlertDialog.Builder(Manage_calendar.this).create();
                viewDetail.setTitle("ยืนยันการเพิ่มวันที่");
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                            @Override
                            protected void onPreExecute() {

                                super.onPreExecute();

                                progressDialog = ProgressDialog.show(Manage_calendar.this, "กำลังอัปโหลดปฏิทิน", "กรุณารอซักครู่", false, false);

                            }

                            @Override
                            protected void onPostExecute(String string1) {

                                super.onPostExecute(string1);
//                                requestQueue = Volley.newRequestQueue(Manage_calendar.this);

                                // Dismiss the progress dialog after done uploading.
//                                requestQueue.stop();
                                Log.d("catchVolley", "catch : " + requestQueue.toString());
                                progressDialog.dismiss();
                                // Printing uploading success message coming from server on android app.
                                Toast.makeText(Manage_calendar.this, string1, Toast.LENGTH_LONG).show();
                                Intent in = new Intent(Manage_calendar.this, Home.class);
                                in.putExtra("email", email + "");
                                in.putExtra("id", uid + "");
//                                Manage_calendar.this.finish();
                                startActivity(in);

                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                checkdateforcal(dateForDB, dateInYear);
                                if (startDateTime != null || endDateTime != null || dateString != null) {
                                    for (int i = 0; i < startDateTime.size(); i++) {
                                        Log.d("checkDBtest ", "startDateTimeSentToDB : " + startDateTime.toString());//[13/04/2020:11, 13/04/2020:17, 13/04/2020:14, 13/04/2020:20]
                                        sentCustomDateToCalendar(startDateTime.get(i), endDateTime.get(i));
                                    }
                                    for (int i = 0; i < startDateTimeDel.size(); i++) { // date fo delete
                                        Log.d("checkDBtest ", "startDateTimeEndSentToDB : " + startDateTimeDel.toString());//[13/04/2020:11, 13/04/2020:17, 13/04/2020:14, 13/04/2020:20]
                                        sentDelCustomDateToCalendar(startDateTimeDel.get(i), endDateTimeDel.get(i));
                                    }
                                    for (int i = 0; i < dateForDB.size(); i++) {
                                        Log.d("checkDBtest ", "dateForDB : " + dateForDB.toString());//[Mon Apr 20 00:00:00 GMT+07:00 2020]
                                        sentDateToDB(dateForDB.get(i));
                                    }
                                    for (int i = 0; i < dateInYear.size(); i++) {
                                        sentDateForCalToDB(dateInYear.get(i), "1", "1", "1", "1", "1");
                                    }
                                    sentdateforcalcustom(dateForDB, startDateTime);
                                    sentDelDateforcalcustom(dateForDB, startDateTimeDel);
                                    handlerSave.sendEmptyMessage(0);

                                }
                                return "Finish";
                            }
                        }
                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                        AsyncTaskUploadClassOBJ.execute();
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
        btnDelCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog viewDetail = new android.app.AlertDialog.Builder(Manage_calendar.this).create();
                viewDetail.setTitle("ยืนยันการลบปฏิทิน");
//
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                            @Override
                            protected void onPreExecute() {

                                super.onPreExecute();

                                progressDialog = ProgressDialog.show(Manage_calendar.this, "กำลังอัปโหลดปฏิทิน", "กรุณารอซักครู่", false, false);
                            }

                            @Override
                            protected void onPostExecute(String string1) {

                                super.onPostExecute(string1);

                                // Dismiss the progress dialog after done uploading.
//                                requestQueue = Volley.newRequestQueue(Manage_calendar.this);
                                progressDialog.dismiss();
                                calendarPicker.clearHighlightedDates();
                                startActivity(getIntent());

                                // Printing uploading success message coming from server on android app.
                                Toast.makeText(Manage_calendar.this, string1, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                deleteDateInDb();
                                return "Finish";
                            }
                        }
                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                        AsyncTaskUploadClassOBJ.execute();
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
        String[] CNames = new String[cursor.getCount()];

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
            DateFormat simpleNoHour = new SimpleDateFormat("dd/MM/yyyy"); //dd/MM/yyyy/HH/mm
            all += s1 + "\n\t\t" + simple.format(c1.getTime()) + "---" + simple.format(c2.getTime()) + "\n\n";
//            Log.d("dateTime ", "Calendar Name long : " + s1 + " - " + simple.format(c1.getTime()) + " + " + simple.format(c2.getTime()) + " : " + cursor.getString(6));
            cutStringForCalendarDB(simple.format(c1.getTime()), simple.format(c2.getTime()), cursor.getString(6));
            startDateTime.add(simple.format(c1.getTime()));
            endDateTime.add(simple.format(c2.getTime()));
            dateCalGetAllDay.add(cursor.getString(6));
            cutStringDate(startDateTime, endDateTime);
            Log.d("arraydb ", "size : " + startDateTime.size() + " dateString : " + c1.getTime());
            Log.d("arraydb ", "size : " + endDateTime.size() + " dateString : " + endDateTime.toString());
            d.setTime(cursor.getLong(3));
            dend.setTime(Long.parseLong(s3));
            dateString.add(d.toString());
            if (cursor.getString(6).equals("0")) {
                dateString.add(dend.toString());
                Log.d("checkyear ", "dend : " + dend.toString());
            }
//            Log.d("checkyear ", dateString.toString());
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
                    dateString.add(ddiff.toString());
                    dateDiffString.add(ddiff.toString());
                    diffDateTime.add(simple.format(ddiff));
                    diffRangeDateTimeStart.add(simpleNoHour.format(ddiff) + ":11");
                    diffRangeDateTimeEnd.add(simpleNoHour.format(ddiff) + ":13");
                    diffRangeDateTimeStart.add(simpleNoHour.format(ddiff) + ":14");
                    diffRangeDateTimeEnd.add(simpleNoHour.format(ddiff) + ":16");
                    diffRangeDateTimeStart.add(simpleNoHour.format(ddiff) + ":17");
                    diffRangeDateTimeEnd.add(simpleNoHour.format(ddiff) + ":19");
                    diffRangeDateTimeStart.add(simpleNoHour.format(ddiff) + ":20");
                    diffRangeDateTimeEnd.add(simpleNoHour.format(ddiff) + ":23");
                    Log.d("checkTime ", "date : " + dateDiffString.get(dateDiffString.size() - 1) + " date size: " + dateDiffString.size());
                    Log.d("diffRange ", "date : " + diffRangeDateTimeStart.toString());
                    Log.d("diffRange ", "date end: " + diffRangeDateTimeEnd.toString());
                }
                cutStringDateDiff(diffDateTime);
            }

            Log.d("arraydb14547 ", "size : " + dateString.size() + " dateString : " + dateString.toString());
            cursor.moveToNext();
        }
        checkdateforcal(dateString, dateInYear);
//        Log.d("newDate","dateString "+dateString.toString());

        return calendars;
    }

    public void cutStringForCalendarDB(String dt, String edt, String fullday) {
        Log.d("cutStringForCalendarDB", "dt before : " + dt.toString());
        Log.d("cutStringForCalendarDB", "edt before : " + edt.toString());
        String time1 = "", time2 = "";
        int timeStart = 0, timeEnd = 0;
        StringTokenizer st = new StringTokenizer(dt, ":");
        StringTokenizer st2 = new StringTokenizer(edt, ":");
        while (st.hasMoreTokens()) {
            time1 += st.nextToken();
            timeStart = Integer.parseInt(st.nextToken());
        }
        while (st2.hasMoreTokens()) {
            time2 += st2.nextToken();
            timeEnd = Integer.parseInt(st2.nextToken());
        }
        if (fullday.equals("1")) {//full daY
            startDateTimeGetCalen.add(time1 + ":11");
            startDateTimeGetCalen.add(time1 + ":14");
            startDateTimeGetCalen.add(time1 + ":17");
            startDateTimeGetCalen.add(time1 + ":20");
            endDateTimeGetCalen.add(time1 + ":13");
            endDateTimeGetCalen.add(time1 + ":16");
            endDateTimeGetCalen.add(time1 + ":19");
            endDateTimeGetCalen.add(time1 + ":23");
            startDateTimeGetCalen.add(time2 + ":11");
            startDateTimeGetCalen.add(time2 + ":14");
            startDateTimeGetCalen.add(time2 + ":17");
            startDateTimeGetCalen.add(time2 + ":20");
            endDateTimeGetCalen.add(time2 + ":13");
            endDateTimeGetCalen.add(time2 + ":16");
            endDateTimeGetCalen.add(time1 + ":19");
            endDateTimeGetCalen.add(time2 + ":23");
        } else {
            if (time1.equals(time2)) {//date==date
                if (timeStart < 14) {
                    startDateTimeGetCalen.add(time1 + ":11");
                    endDateTimeGetCalen.add(time1 + ":13");
                } else if (timeStart >= 14 && timeStart < 17) {
                    startDateTimeGetCalen.add(time1 + ":11");
                    startDateTimeGetCalen.add(time1 + ":14");
                    endDateTimeGetCalen.add(time1 + ":13");
                    endDateTimeGetCalen.add(time1 + ":16");
                } else if (timeStart >= 17 && timeStart < 20) {
                    startDateTimeGetCalen.add(time1 + ":11");
                    startDateTimeGetCalen.add(time1 + ":14");
                    startDateTimeGetCalen.add(time1 + ":17");
                    endDateTimeGetCalen.add(time1 + ":13");
                    endDateTimeGetCalen.add(time1 + ":16");
                    endDateTimeGetCalen.add(time1 + ":19");
                } else if (timeStart >= 20 && timeStart < 24) {
                    startDateTimeGetCalen.add(time1 + ":11");
                    startDateTimeGetCalen.add(time1 + ":14");
                    startDateTimeGetCalen.add(time1 + ":17");
                    startDateTimeGetCalen.add(time1 + ":20");
                    endDateTimeGetCalen.add(time1 + ":13");
                    endDateTimeGetCalen.add(time1 + ":16");
                    endDateTimeGetCalen.add(time1 + ":19");
                    endDateTimeGetCalen.add(time1 + ":23");
                }
            } else {
                if (timeStart < 14) {
                    startDateTimeGetCalen.add(time1 + ":11");
                    startDateTimeGetCalen.add(time1 + ":14");
                    startDateTimeGetCalen.add(time1 + ":17");
                    startDateTimeGetCalen.add(time1 + ":20");
                    endDateTimeGetCalen.add(time1 + ":13");
                    endDateTimeGetCalen.add(time1 + ":16");
                    endDateTimeGetCalen.add(time1 + ":19");
                    endDateTimeGetCalen.add(time1 + ":23");
                } else if (timeStart >= 14 && timeStart < 17) {
                    startDateTimeGetCalen.add(time1 + ":14");
                    startDateTimeGetCalen.add(time1 + ":17");
                    startDateTimeGetCalen.add(time1 + ":20");
                    endDateTimeGetCalen.add(time1 + ":16");
                    endDateTimeGetCalen.add(time1 + ":19");
                    endDateTimeGetCalen.add(time1 + ":23");
                } else if (timeStart >= 17 && timeStart < 20) {
                    startDateTimeGetCalen.add(time1 + ":17");
                    startDateTimeGetCalen.add(time1 + ":20");
                    endDateTimeGetCalen.add(time1 + ":19");
                    endDateTimeGetCalen.add(time1 + ":23");
                } else if (timeStart >= 20 && timeStart < 24) {
                    startDateTimeGetCalen.add(time1 + ":20");
                    endDateTimeGetCalen.add(time1 + ":23");
                }
                if (timeEnd >= 11 && timeEnd < 14) {
                    startDateTimeGetCalen.add(time2 + ":11");
                    endDateTimeGetCalen.add(time2 + ":13");
                } else if (timeEnd >= 14 && timeEnd < 17) {
                    startDateTimeGetCalen.add(time2 + ":11");
                    startDateTimeGetCalen.add(time2 + ":14");
                    endDateTimeGetCalen.add(time2 + ":13");
                    endDateTimeGetCalen.add(time2 + ":16");
                } else if (timeEnd >= 17 && timeEnd < 20) {
                    startDateTimeGetCalen.add(time2 + ":11");
                    startDateTimeGetCalen.add(time2 + ":14");
                    startDateTimeGetCalen.add(time2 + ":17");
                    endDateTimeGetCalen.add(time2 + ":13");
                    endDateTimeGetCalen.add(time2 + ":16");
                    endDateTimeGetCalen.add(time2 + ":19");
                } else if (timeEnd >= 20 && timeEnd < 24) {
                    startDateTimeGetCalen.add(time2 + ":11");
                    startDateTimeGetCalen.add(time2 + ":14");
                    startDateTimeGetCalen.add(time2 + ":17");
                    startDateTimeGetCalen.add(time2 + ":20");
                    endDateTimeGetCalen.add(time2 + ":13");
                    endDateTimeGetCalen.add(time2 + ":16");
                    endDateTimeGetCalen.add(time1 + ":19");
                    endDateTimeGetCalen.add(time2 + ":23");
                }
            }


        }
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
//        Manage_calendar.this.finish();
        startActivity(in);
    }

    public void requestCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)) {
            new AlertDialog.Builder(this)
                    .setTitle("ต้องการการเข้าถึง")
                    .setMessage("ขออนุญาติเข้าถึงปฏิทินในโทรสัพท์")
                    .setPositiveButton(R.string.btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Manage_calendar.this, new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_CODE);
                            Toast.makeText(Manage_calendar.this, "Plese put Get Calendar Again", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
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

    //    public void clickCheckboxDateTime(int tiStart, int tiEnd, String daStart, String daEnd) {
//        if (!daStart.equals(daEnd) && tiStart == tiEnd) {//date same day all day
//            Log.d("calendar123", "date123 : " + daStart + " - " + daEnd);
//            cbMorninig.setChecked(true);
//            cbLate.setChecked(true);
//            cbAfternoon.setChecked(true);
//            cbEvening.setChecked(true);
//        } else {
//            Log.d("calendar123", "date : " + daStart + " - " + daEnd);
//            if (!daStart.equals(daEnd) && tiStart != tiEnd) {//no same dat No all time
//                Log.d("calendar123", "date : " + daStart + " - " + daEnd);
//                if (tiStart >= 0 && tiStart < 11) {
//                    //0
//                } else if (tiStart >= 11 && tiStart < 14) {
//                    //1234
//                    cbMorninig.setChecked(true);
//                    cbLate.setChecked(true);
//                    cbAfternoon.setChecked(true);
//                    cbEvening.setChecked(true);
//                } else if (tiStart >= 14 && tiStart < 17) {
//                    //234
//                    cbLate.setChecked(true);
//                    cbAfternoon.setChecked(true);
//                    cbEvening.setChecked(true);
//                } else if (tiStart >= 17 && tiStart < 20) {
//                    //34
//                    cbAfternoon.setChecked(true);
//                    cbEvening.setChecked(true);
//                } else if (tiStart >= 20 && tiStart < 24) {
//                    //4
//                    cbEvening.setChecked(true);
//                }
//            } else {
//                if (tiStart >= 0 && tiStart < 14) {
//                    if (tiEnd >= 0 && tiEnd < 11) {
//                        //0
//                    } else if (tiEnd >= 11 && tiEnd < 14) {
//                        //1
//                        cbMorninig.setChecked(true);
//                    } else if (tiEnd >= 14 && tiEnd < 17) {
//                        //12
//                        cbMorninig.setChecked(true);
//                        cbLate.setChecked(true);
//                    } else if (tiEnd >= 17 && tiEnd < 20) {
//                        //123
//                        cbMorninig.setChecked(true);
//                        cbLate.setChecked(true);
//                        cbAfternoon.setChecked(true);
//                    } else if (tiEnd >= 20 && tiEnd < 24) {
//                        //1234
//                        cbMorninig.setChecked(true);
//                        cbLate.setChecked(true);
//                        cbAfternoon.setChecked(true);
//                        cbEvening.setChecked(true);
//                    }
//                } else if (tiStart >= 14 && tiStart < 17) {
//                    if (tiEnd >= 14 && tiEnd < 17) {
//                        //2
//                        cbLate.setChecked(true);
//                    } else if (tiEnd >= 17 && tiEnd < 20) {
//                        //23
//                        cbLate.setChecked(true);
//                        cbAfternoon.setChecked(true);
//                    } else if (tiEnd >= 20 && tiEnd < 24) {
//                        //234
//                        cbLate.setChecked(true);
//                        cbAfternoon.setChecked(true);
//                        cbEvening.setChecked(true);
//                    }
//                } else if (tiStart >= 17 && tiStart < 20) {
//                    if (tiEnd >= 17 && tiEnd < 20) {
//                        //3
//                        cbAfternoon.setChecked(true);
//                    } else if (tiEnd >= 20 && tiEnd < 24) {
//                        //34
//                        cbAfternoon.setChecked(true);
//                        cbEvening.setChecked(true);
//                    }
//                } else if (tiStart >= 20 && tiStart < 24) {
//                    if (tiEnd >= 20 && tiEnd < 24) {
//                        //4
//                        cbEvening.setChecked(true);
//                    }
//                }
//            }
//
//        }
//
//    }
    public void clickCheckboxDateTime(int tiStart, int tiEnd, String daStart, String daEnd) {
        if (tiStart >= 0 && tiStart < 11) {
            //0
        }
        if (tiStart >= 11 && tiStart < 14) {
            //1234
            cbMorninig.setChecked(true);
        }
        if (tiStart >= 14 && tiStart < 17) {
            //234
            cbLate.setChecked(true);
        }
        if (tiStart >= 17 && tiStart < 20) {
            //34
            cbAfternoon.setChecked(true);
        }
        if (tiStart >= 20 && tiStart < 24) {
            //4
            cbEvening.setChecked(true);
        }

    }

//    public void clickCheckboxDiffDTLastDay(int tiEnd) {
//        if (tiEnd >= 0 && tiEnd < 11) {
//            //0
//        } else if (tiEnd >= 11 && tiEnd < 14) {
//            //1
//            cbMorninig.setChecked(true);
//        } else if (tiEnd >= 14 && tiEnd < 17) {
//            //12
//            cbMorninig.setChecked(true);
//            cbLate.setChecked(true);
//        } else if (tiEnd >= 17 && tiEnd < 20) {
//            //123
//            cbMorninig.setChecked(true);
//            cbLate.setChecked(true);
//            cbAfternoon.setChecked(true);
//        } else if (tiEnd >= 20 && tiEnd < 24) {
//            //1234
//            cbMorninig.setChecked(true);
//            cbLate.setChecked(true);
//            cbAfternoon.setChecked(true);
//            cbEvening.setChecked(true);
//        }
//    }

    public void clearCheckBox() {
        cbMorninig.setChecked(false);
        cbLate.setChecked(false);
        cbAfternoon.setChecked(false);
        cbEvening.setChecked(false);
        cbMorninig.setClickable(true);
        cbLate.setClickable(true);
        cbAfternoon.setClickable(true);
        cbEvening.setClickable(true);
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
//        Log.d("dateAll ","dt : "+dt.toString());
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
        Log.d("simpledate", "startDate : " + startDate);
        String dateCheckformatStart = "", dateCheckformatEnd = "";
        StringTokenizer std = new StringTokenizer(startDate, ":");
        StringTokenizer ste = new StringTokenizer(endDate, ":");
        while (std.hasMoreTokens()) {
            String dfs = std.nextToken();
            std.nextToken();
            StringTokenizer st = new StringTokenizer(dfs, "/");
            while (st.hasMoreTokens()) {
                String d = st.nextToken();
                String m = st.nextToken();
                String y = st.nextToken();
                dateCheckformatStart = y + "/" + m + "/" + d;

            }
        }
        while (ste.hasMoreTokens()) {
            String dfe = ste.nextToken();
            ste.nextToken();
            StringTokenizer st = new StringTokenizer(dfe, "/");
            while (st.hasMoreTokens()) {
                String d = st.nextToken();
                String m = st.nextToken();
                String y = st.nextToken();
                dateCheckformatEnd = y + "/" + m + "/" + d;

            }
        }
        Log.d("simpledate", "dateCheckformatStart : " + dateCheckformatStart + "--" + dateCheckformatEnd);
        String url = "http://www.groupupdb.com/android/addcalendar.php";
        url += "?sId=" + uid;
        url += "&sdt=" + startDate;
        url += "&edt=" + endDate;
        url += "&dcfs=" + dateCheckformatStart + "";
        url += "&dcfe=" + dateCheckformatEnd + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        uploadData(stringRequest);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
    }

    public void sentCalendarDiffToDB(String startDate, String endDate) {
        Log.d("simpledate", "startDate : " + startDate);
        String url = "http://www.groupupdb.com/android/addcalendar.php";
        url += "?sId=" + uid;
        url += "&sdt=" + startDate;
        url += "&edt=" + endDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        uploadData(stringRequest);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
    }

    public void getcalendarFromDB() {
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getcalendar.php";
        url += "?sId=" + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response);
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
                            for (int i = 0; i < MyArrList.size(); i++) {
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
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void sentDateToDB(String date) {
        Log.d("checkDB ", "dateString123 : " + date);
        date1 = "";
        date2 = "";
        CutStringDateForSaveDB(date);
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy:HH");
        DateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        long dt = Date.parse(date);
        Date d = new Date(dt);
        String dateCheck = simpleHour.format(d);
        String dateCheckformat = simpledate.format(d) + "";
        Log.d("simpledate", "sentDateToDB : " + dateCheckformat);
        Log.d("checkDB ", "dateCheck : " + dateCheck);
        String url = "http://www.groupupdb.com/android/adddate.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        url += "&sdc=" + dateCheck + "";
        url += "&dcf=" + dateCheckformat + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void getDateFromDB() {
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getdate.php";
        url += "?sId=" + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response);
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
                            for (int i = 0; i < MyArrList.size(); i++) {
                                dateFromDB.add(MyArrList.get(i).get("date") + "+" + MyArrList.get(i).get("datelast"));
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
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void sentDateDiffToDB(String date) {
        date1 = "";
        date2 = "";
        CutStringDateForSaveDB(date);
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy:HH");
        DateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        long dt = Date.parse(date);
        Date d = new Date(dt);
        String dateCheck = simpleHour.format(d) + "";
        String dateCheckformat = simpledate.format(d) + "";
        Log.d("simpledate", "getDateFromDB : " + dateCheckformat);
        String url = "http://www.groupupdb.com/android/adddatediff.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        url += "&sdc=" + dateCheck + "";
        url += "&dcf=" + dateCheckformat + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void getDateDiffFromDB() {
        responseStr = new Manage_calendar.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getdatediff.php";
        url += "?sId=" + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response);
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
                            for (int i = 0; i < MyArrList.size(); i++) {
                                dateDiffFromDB.add(MyArrList.get(i).get("datediff") + "+" + MyArrList.get(i).get("datediffend"));
                            }
//                            Log.d("dateAll","dateDiffFromDB"+dateDiffFromDB.toString());
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
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void CutStringDateForSaveDB(String s) {
        StringTokenizer st = new StringTokenizer(s, "+");
        while (st.hasMoreTokens()) {
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

    public void removeTime(String date, ArrayList arrayList) {
        String number = "";
        for (int i = 0; i < arrayList.size(); i++) {
            if (date.equals(arrayList.get(i))) {
                number = i + "";
            }
        }
        if (!number.equals("")) {
            arrayList.remove(Integer.parseInt(number));
        }

        Log.d("dateSelect", "removeDateTime : " + arrayList.toString());
//        Toast.makeText(Manage_calendar.this, "removeDateTime : "+allDateTime.toString(), Toast.LENGTH_SHORT).show();
    }

    public void removeDate(String date, ArrayList allStartDate) {
        String number = "";
        for (int i = 0; i < allStartDate.size(); i++) {
            if (date.equals(allStartDate.get(i))) {
                number = i + "";
            }
        }
        if (!number.equals("")) {
            allStartDate.remove(Integer.parseInt(number));
        }

        Log.d("dateSelect", "removeDateTime : " + allStartDate.toString());
//        Toast.makeText(Manage_calendar.this, "removeDateTime : "+allDateTime.toString(), Toast.LENGTH_SHORT).show();
    }

    public void SaveCalenGettoDB() {

        final android.app.AlertDialog viewDetail = new android.app.AlertDialog.Builder(Manage_calendar.this).create();
        viewDetail.setTitle("ยืนยันการเพิ่มวันที่เข้าสู่ปฏิทิน");

        viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                readCalendarEvent(Manage_calendar.this);
                btnConfirmCalendar.setVisibility(View.VISIBLE);
                Log.d("checkDB ", "btnGetCalen : " + dateString);
                for (int i = 0; i < dateString.size(); i++) {
                    long date = Date.parse(dateString.get(i));
                    Date d = new Date(date);
                    newDate.add(d);
                }
                calendarPicker.highlightDates(newDate);
                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
//                        checkdateforcal(dateString,dateInYear);
                        progressDialog = ProgressDialog.show(Manage_calendar.this, "กำลังอัปโหลดปฏิทิน", "กรุณารอซักครู่", false, false);
                    }

                    @Override
                    protected void onPostExecute(String string1) {
                        super.onPostExecute(string1);
                        // Dismiss the progress dialog after done uploading.
                        progressDialog.dismiss();

//                        requestQueue = null;
//                        requestQueue = Volley.newRequestQueue(Manage_calendar.this);
                        // Printing uploading success message coming from server on android app.
                        Toast.makeText(Manage_calendar.this, string1, Toast.LENGTH_LONG).show();
                        addDateAvaliableTodb();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        if (startDateTimeGetCalen != null || endDateTimeGetCalen != null || dateString != null) {
//                    Log.d("dateSelect","start"+startDateTime.toString()+" "+dateString.toString()+" "+dateDiffString.toString());
                            for (int i = 0; i < startDateTimeGetCalen.size(); i++) {
                                Log.d("checkDB ", "startDateTime : " + startDateTimeGetCalen.toString());
                                sentCalendarToDB(startDateTimeGetCalen.get(i), endDateTimeGetCalen.get(i));
                            }
                            for (int i = 0; i < diffRangeDateTimeStart.size(); i++) {
                                Log.d("checkDB ", "diffRangeDateTime : " + diffRangeDateTimeStart.toString());
                                sentCalendarDiffToDB(diffRangeDateTimeStart.get(i), diffRangeDateTimeEnd.get(i));
                            }
                            for (int i = 0; i < dateString.size(); i++) {
                                Log.d("checkDB ", "dateString : " + dateString.toString());
                                sentDateToDB(dateString.get(i));
                            }
//                            Log.d("dateAll ","dateDiffString : "+ dateDiffString.toString());
                            for (int i = 0; i < dateDiffString.size(); i++) {
                                Log.d("checkDB ", "dateDiffString : " + dateDiffString.toString());
                                sentDateDiffToDB(dateDiffString.get(i));
                            }
//                            startDateTime.clear();
//                            endDateTime.clear();
//                            dateString.clear();
//                            dateDiffString.clear();
//                    Log.d("dateSelect",startDateTime.toString()+" "+dateString.toString()+" "+dateDiffString.toString());
                        }


                        return "Finish";
                    }
                }
                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                AsyncTaskUploadClassOBJ.execute();

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

    public void sentCustomDateToCalendar(String startDate, String endDate) {
        Log.d("simpledate", "startDate : " + startDate);
        String url = "http://www.groupupdb.com/android/addcustomcalendar.php";
        url += "?sId=" + uid;
        url += "&sdt=" + startDate;
        url += "&edt=" + endDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();
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

    public void sentDelCustomDateToCalendar(String startDate, String endDate) {
        Log.d("simpledate", "startDate : " + startDate);
        String url = "http://www.groupupdb.com/android/removedbcalendar.php";
        url += "?sId=" + uid;
        url += "&sdt=" + startDate;
        url += "&edt=" + endDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();
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

    public void deleteDateInDb() {
        responseStr = new Manage_calendar.ResponseStr();
        String url = "http://www.groupupdb.com/android/deletedatebyuser.php";
        url += "?sId=" + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
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
        startActivity(getIntent());
    }

    public void sentDateForCalToDB(String date, String morning, String late, String afternoon, String evening, String status) {
        Log.d("checkDB ", "dateString123 : " + date);
        date1 = "";
        date2 = "";
        CutStringDateForSaveDB(date);
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        long dt = Date.parse(date);
        Date d = new Date(dt);
        String dateCheck = simpleHour.format(d);
        String dateCheckformat = simpledate.format(d) + "";
        Log.d("simpledate", "sentDateToDB : " + dateCheckformat);
        Log.d("checkDB ", "dateCheck : " + dateCheck);
        String url = "http://www.groupupdb.com/android/adddateforcal.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        url += "&sdc=" + dateCheck + "";
        url += "&dcf=" + dateCheckformat + "";
        url += "&mor=" + morning + "";
        url += "&lat=" + late + "";
        url += "&aft=" + afternoon + "";
        url += "&eve=" + evening + "";
        url += "&sta=" + status + "";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
        uploadData(stringRequest);
    }

    public void DelDateForCalToDB(String date, String morning, String late, String afternoon, String evening, String status) {
        Log.d("checkDB ", "dateString123 : " + date);
        date1 = "";
        date2 = "";
        CutStringDateForSaveDB(date);
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        long dt = Date.parse(date);
        Date d = new Date(dt);
        String dateCheck = simpleHour.format(d);
        String dateCheckformat = simpledate.format(d) + "";
        Log.d("simpledate", "sentDateToDB : " + dateCheckformat);
        Log.d("checkDB ", "dateCheck : " + dateCheck);
        String url = "http://www.groupupdb.com/android/removedbdateforcal.php";
        url += "?sId=" + uid;
        url += "&sdt=" + date1;
        url += "&sdtl=" + date2;
        url += "&sdc=" + dateCheck + "";
        url += "&dcf=" + dateCheckformat + "";
        url += "&mor=" + morning + "";
        url += "&lat=" + late + "";
        url += "&aft=" + afternoon + "";
        url += "&eve=" + evening + "";
        url += "&sta=" + status + "";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
        uploadData(stringRequest);
    }

    ///////////////////////////////////////////////// check range of year
    public static List<Date> getDatesBetweenInYear(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            dateInYear.add(result.toString());
            calendar.add(Calendar.DATE, 1);
        }
        Log.d("checkyear", "dateYearsize " + dateInYear.size());
        return datesInRange;
    }

    public void checkdateforcal(ArrayList<String> dateEvent, ArrayList<String> dateYear) {
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("checkyear", " dateEventsize " + dateEvent.toString());
        Log.d("checkyear", "dateYearsize " + dateYear.size() + " dateEventsize " + dateEvent.size());
        for (int i = 0; i < dateEvent.size(); i++) {
            long dEat = Date.parse(dateEvent.get(i));
            Date dEa = new Date(dEat);
            for (int j = 0; j < dateYear.size(); j++) {
                long dat = Date.parse(dateYear.get(j));
                Date da = new Date(dat);
                if (simpleHour.format(da).equals(simpleHour.format(dEa))) {
                    Log.d("checkyear", " removeDate : " + " " + simpleHour.format(da) + " -> " + simpleHour.format(dEa));
                    dateYear.remove(j);
                }
            }
        }
        Log.d("checkyear", "size " + dateYear.size() + " removeDate : " + dateYear.toString());
    }

    public void sentdateforcalcustom(ArrayList<String> dateEvent, ArrayList<String> dateForTime) {

        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < dateEvent.size(); i++) {//date
            String date = dateEvent.get(i);
            String dateformat = "";
            String morning = "1";//ว่าง
            String late = "1";
            String afternoon = "1";
            String evening = "1";
            String status = "";
            long dEat = Date.parse(dateEvent.get(i));
            Date dEa = new Date(dEat);
            dateformat = simpleHour.format(dEa);
            for (int j = 0; j < dateForTime.size(); j++) {
                if ((dateformat + ":11").equals(dateForTime.get(j))) {
                    morning = "0";//ไม่ว่าง
                }
                if ((dateformat + ":14").equals(dateForTime.get(j))) {
                    late = "0";
                }
                if ((dateformat + ":17").equals(dateForTime.get(j))) {
                    afternoon = "0";
                }
                if ((dateformat + ":20").equals(dateForTime.get(j))) {
                    evening = "0";
                }
            }
            if (morning.equals("0") && late.equals("0") && afternoon.equals("0") && evening.equals("0")) {
                status = "0";
            } else {
                status = "1";
            }
            Log.d("checkcalcustom", " date " + date + " dateformat " + dateformat + " morning " + morning + " late " + late + " afternoon " + afternoon + " evening " + evening + " status " + status);
            Log.d("checkcalcustom", " datesize " + dateInYear.size());
            sentDateForCalToDB(date, morning, late, afternoon, evening, status);
        }
    }

    public void sentDelDateforcalcustom(ArrayList<String> dateEvent, ArrayList<String> dateForTime) {

        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < dateEvent.size(); i++) {//date
            String date = dateEvent.get(i);
            String dateformat = "";
            String morning = "1";//ว่าง
            String late = "1";
            String afternoon = "1";
            String evening = "1";
            String status = "";
            long dEat = Date.parse(dateEvent.get(i));
            Date dEa = new Date(dEat);
            dateformat = simpleHour.format(dEa);

            for (int j = 0; j < dateForTime.size(); j++) {
                if (dateForTime.get(j).equals(dateformat + ":11") || dateForTime.get(j).equals(dateformat + ":14") || dateForTime.get(j).equals(dateformat + ":17") || dateForTime.get(j).equals(dateformat + ":20")) {
                    if ((dateformat + ":11").equals(dateForTime.get(j))) {
                        morning = "0";//ไม่ว่าง
                    }
                    if ((dateformat + ":14").equals(dateForTime.get(j))) {
                        late = "0";
                    }
                    if ((dateformat + ":17").equals(dateForTime.get(j))) {
                        afternoon = "0";
                    }
                    if ((dateformat + ":20").equals(dateForTime.get(j))) {
                        evening = "0";
                    }
                    if (morning.equals("0") && late.equals("0") && afternoon.equals("0") && evening.equals("0")) {
                        status = "0";
                    } else {
                        status = "1";
                    }
                    DelDateForCalToDB(date, morning, late, afternoon, evening, status);
                }
            }

        }
    }

    public void addDateAvaliableTodb() {
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Manage_calendar.this, "กำลังอัปโหลดปฏิทิน", "กรุณารอซักครู่", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

//                dateString
                for (int i = 0; i < dateString.size(); i++) {
                    sentDateForCalToDB(dateString.get(i), "0", "0", "0", "0", "0");
                }
                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
//                requestQueue.stop();
                progressDialog.dismiss();
                Toast.makeText(Manage_calendar.this, string1, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                for (int i = 0; i < dateInYear.size(); i++) {
                    sentDateForCalToDB(dateInYear.get(i), "1", "1", "1", "1", "1");
                }
                for (int i = 0; i < dateString.size(); i++) {
                    sentDateForCalToDB(dateString.get(i), "0", "0", "0", "0", "0");
                }
                return "Finish";
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public void uploadData(StringRequest s) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        } else {
            requestQueue.add(s);
        }
    }

    public void functionInCreate() {
        //////////////////////////////////range date 1 year/////////////////////////////////////
        Calendar cal = Calendar.getInstance();
        Date today1 = cal.getTime();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear1 = cal.getTime();
        getDatesBetweenInYear(today1, nextYear1);
        //////////////////////////////////range date 1 year/////////////////////////////////////
        new CountDownTimer(2000, 2000) {
            public void onFinish() {
                Log.d("newDate", "dateFromDB " + dateFromDB.toString());
                for (int i = 0; i < dateFromDB.size(); i++) {
                    long date = Date.parse(dateFromDB.get(i));
                    Date d = new Date(date);
                    newDate.add(d);
                }
                cutStringDate(cbStartcalenFromDB, cbEndcalenFromDB);
                Log.d("newDate", "cbStartcalenFromDB " + cbStartcalenFromDB.toString());
                Log.d("newDate", "cbEndcalenFromDB " + cbEndcalenFromDB.toString());
                checkdateforcal(dateFromDB, dateInYear);

                DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy:HH");
//                Log.d("dateAll","newDate "+dateDiffFromDB.toString()+"size : "+ dateDiffFromDB.size());
                ArrayList<Date> pres = new ArrayList<>();
                for (int i = 0; i < dateDiffFromDB.size(); i++) {
                    long date = Date.parse(dateDiffFromDB.get(i));
                    Date d = new Date(date);
                    diffDateTime.add(simpleHour.format(d) + "");
                }
                Log.d("dateAll", "diffDateTime : " + diffDateTime.toString());
                cutStringDateDiff(diffDateTime);
                calendarPicker.highlightDates(newDate);
                if (newDate == null) {
                    getDateFromDB();
                    getDateDiffFromDB();
                    getcalendarFromDB();
                }
                Log.d("highDate", "highDate : " + newDate.toString());
                handler.sendEmptyMessage(0);
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1); // ใช้setว่าจะให้แสดงปฏิทินยังไง กี่เดือน

        calendarPicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);//เซ็ตการเลือกว่าจะให้เลือกเป็นวัน เป็นช่วง เป็นหลายๆวัน

    }
}
