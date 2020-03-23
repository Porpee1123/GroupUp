package com.example.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class Manage_calendar extends AppCompatActivity {
    private final int INVALID_VAL = -1;
    int mHour, mMinute;
    String mRecurrenceOption, mRecurrenceRule;
    CalendarPickerView calendarPicker;
    Button btnGetCalen;
    private int CALENDAR_PERMISSION_CODE = 1;
    ArrayList<Date> date = new ArrayList<>();
    ArrayList<String> dateString = new ArrayList<>();
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
    String uid, email;
    //checkbox
    LinearLayout linearCheckbox;
    boolean checkVisible;
    CheckBox cbMorninig, cbLate, cbAfternoon, cbEvening;
    TextView tvDateTime;
    TypedArray arr_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_calendar);
        btnGetCalen = (Button) findViewById(R.id.btnGetCalendar);
        calendarPicker = findViewById(R.id.calendarPickerView);
        linearCheckbox = findViewById(R.id.linear_Checkbox_Calendar);
        calendarPicker.highlightDates(date);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        //checkbox
        linearCheckbox.setVisibility(View.GONE);
        checkVisible = true;//close custom
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
        //checkbox
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
//                Log.d("dateTime",date.toString());

                for (int i = 0; i < dateCalGet.size(); i++) {
//                    Log.d("dateTime", "dateCalGet : " + dateCalGet.get(i) + " " + selectedDateCompare);
                    if (selectedDateCompare.equals(dateCalGet.get(i))) {
//                        Log.d("dateTime", "checkbox : " + Integer.parseInt(timeCalGet.get(i)) + " " + Integer.parseInt(timeCalGetEnd.get(i)) + " " + dateCalGet.get(i) + " " + dateCalGetEnd.get(i));
                        clickCheckboxDateTime(Integer.parseInt(timeCalGet.get(i)), Integer.parseInt(timeCalGetEnd.get(i)), dateCalGet.get(i), dateCalGetEnd.get(i));
                    }
                }
                for (int i = 0; i < dateCalGetDiff.size(); i++) {
//                    Log.d("dateTime", "dateCalGetDiff : " + dateCalGetDiff.toString());
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
        newDate = new ArrayList();
        for (int i = 0; i < dateString.size(); i++) {
//            DateFormat simple = new SimpleDateFormat();
            long date = Date.parse(dateString.get(i));
            Date d = new Date(date * 1000);
            newDate.add(d);
        }

        allDaySelect = new ArrayList();
        btnGetCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Manage_calendar.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Manage_calendar.this, "You have already permission", Toast.LENGTH_SHORT).show();
                    readCalendarEvent(Manage_calendar.this);
//                    Log.d("dateTime", "calendarPicker : "+ date.toString());
//                    for (int i = 0;i<dateString.size();i++){
//                        Log.d("calendar123", "date : " + dateString.get(i)+"\n");
//                    }
//                    Log.d("calendar123", "date : " + date);
//                    calendarPicker.highlightDates(date);
                    newDate = new ArrayList();
                    for (int i = 0; i < dateString.size(); i++) {
                        long date = Date.parse(dateString.get(i));
                        Date d = new Date(date);
                        newDate.add(d);
                    }
                    calendarPicker.highlightDates(newDate);


                } else {
                    requestCalendarPermission();
                    readCalendarEvent(Manage_calendar.this);
                    Log.d("calendar123", "date : " + date);
                    calendarPicker.highlightDates(date);
//                    calendarPicker.highlightDates(dateDifference);
                    ;
                }
            }
        });
    }

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
            CNames[i] = cursor.getString(1);
            s1 = cursor.getString(1);
            s2 = cursor.getString(3);
            s3 = cursor.getString(4);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTimeInMillis(Long.parseLong(s2));
            c2.setTimeInMillis(Long.parseLong(s3)); //25200100 millisec = 252001 sec
            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy:HH"); //dd/MM/yyyy/HH/mm
            all += s1 + "\n\t\t" + simple.format(c1.getTime()) + "---" + simple.format(c2.getTime()) + "\n\n";
            Log.d("dateTime ", "Calendar Name long : " + s1 + " - " + simple.format(c1.getTime()) + " + " + simple.format(c2.getTime()) + " : " + cursor.getString(6));
            startDateTime.add(simple.format(c1.getTime()));
            endDateTime.add(simple.format(c2.getTime()));
            dateCalGetAllDay.add(cursor.getString(6));
            cutStringDate(startDateTime, endDateTime);
            d.setTime(cursor.getLong(3));
            dend.setTime((cursor.getLong(4)));
            date.add(d);
            dateString.add(d.toString());
            if (cursor.getString(6).equals("0")){
                date.add(dend);
                dateString.add(dend.toString());
            }
            if (d.getDate() != dend.getDate()) {
//                Log.d("checkTime ", "d : " + d.getDate()+"---"+dend.getDate());
                int dif = 0;
                long oneday = 86400000;
                dif = Integer.parseInt(dend.getDate() + "") - Integer.parseInt(d.getDate() + "");
                Log.d("dateTime ", "d : " + d.getDate()+"---"+dend.getDate());
                Log.d("dateTime ", "dif : " + dif);
                for (int num = 1; num < dif; num++) {
                    ddiff.setTime(d.getTime() + (oneday)*num);
                    Log.d("checkTime ", "checkTime : " + ddiff);
                    Log.d("dateTime ", "ddiff : " + ddiff);
                    date.add(ddiff);
                    dateString.add(ddiff.toString());
                    diffDateTime.add(simple.format(ddiff));
                    Log.d("checkTime ", "date : " + date.get(date.size() - 1));
//                    Log.d("dateTime ", "diffDateTime : " + diffDateTime.toString());

                }
//                Log.d("dateTime ", "diffDateTime : " + diffDateTime.toString());
                cutStringDateDiff(diffDateTime);
//                Log.d("dateTime ", "all : " +cursor.getString(6));
////                for (int a = date.size() - 4; a < date.size(); a++) {
////                    Log.i("@resultString/////", dateString.get(a) + "");
////                    Log.i("@resultDate/////", date.get(a) + "");
////                }
            }
//            Log.d("@Lastcalendar ", "Calendar Name : " + d + " - " + dend + " + " + ddiff);
            cursor.moveToNext();
//            1572307200000     //29/10/2019
//            1572393600000     //30/10/2019
//            1572541199000     //31/10/2019

        }
//        Log.d("dateTime",date.toString());
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
            cbMorninig.setChecked(true);
            cbLate.setChecked(true);
            cbAfternoon.setChecked(true);
            cbEvening.setChecked(true);
        } else {
            if (!daStart.equals(daEnd) && tiStart != tiEnd) {

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
}
