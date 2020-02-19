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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
    ArrayList<String> newDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_calendar);
        btnGetCalen = (Button) findViewById(R.id.btnGetCalendar);
        calendarPicker = findViewById(R.id.calendarPickerView);
        calendarPicker.highlightDates(date);
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

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);

                Toast.makeText(Manage_calendar.this, selectedDate, Toast.LENGTH_SHORT).show();
                String s = selectedDate;
//                allDaySelect.add(s);
                day.add(s);
//                txt.setText(day.toString());
            }

            @Override
            public void onDateUnselected(Date date) {
                Calendar calUnSelected = Calendar.getInstance();
                calUnSelected.setTime(date);

                String selectedDate = "" + calUnSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calUnSelected.get(Calendar.MONTH) + 1)
                        + " " + calUnSelected.get(Calendar.YEAR);

                Toast.makeText(Manage_calendar.this, selectedDate, Toast.LENGTH_SHORT).show();
                String s = selectedDate;
                String del = "";
                for (int i = 0; i < day.size(); i++) {
                    if (s.equals(day.get(i))) {
                        del = i + "";
                    }
                }
//                allDaySelect.remove(Integer.parseInt(del));
                day.remove(Integer.parseInt(del));
//                txt.setText(day.toString());


//                txt.setText(txt.getText()+" | "+s);
            }

        });
        //Change format date
        newDate = new ArrayList();
        for (int i = 0; i < date.size(); i++) {
            DateFormat simple = new SimpleDateFormat("dd MM yyyy");
            newDate.add(simple.format(date.get(i).getTime()));
        }

        allDaySelect = new ArrayList();
        //clone Arraylist


//        Button btn = (Button) findViewById(R.id.button);
//        allDaySelect.add("ZERO");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                allDaySelect = (ArrayList<String>) newDate.clone(); //วันที่มีมาร์คในปฏิทิน
//
//                allDaySelect.set(0, txt.getText().toString());
//                txt.setText(allDaySelect.toString());
//            }
//        });
        btnGetCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Manage_calendar.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Manage_calendar.this, "You have already permission", Toast.LENGTH_SHORT).show();
                    readCalendarEvent(Manage_calendar.this);
                    calendarPicker.highlightDates(date);


                } else {
                    requestCalendarPermission();
                    readCalendarEvent(Manage_calendar.this);
                    calendarPicker.highlightDates(date);
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
                                "dtstart", "dtend", "eventLocation"}, selection,
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
            c2.setTimeInMillis(Long.parseLong(s3) - 25201000); //25200100 millisec = 252001 sec
            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            all += s1 + "\n\t\t" + simple.format(c1.getTime()) + "---" + simple.format(c2.getTime()) + "\n\n";

            d.setTime(cursor.getLong(3));
            dend.setTime((cursor.getLong(4) - 25201000));


//            Log.i("@calendar", "Calendar Name : " + d+" - "+dend);
            date.add(d);
            dateString.add(d + "");
            date.add(dend);
            dateString.add(dend + "");
            if (d.getDate() != dend.getDate()) {
                int dif = 0;
                long oneday = 86400000;
                dif = Integer.parseInt(dend.getDate() + "") - Integer.parseInt(d.getDate() + "");
                for (int num = 1; num < dif; num++) {
                    ddiff.setTime(d.getTime() + (oneday * num));
                    date.add(ddiff);
                    Log.i("@resultBefore", num + "");
                    Log.i("@result", ddiff + "");
                    dateString.add(ddiff + "");
                }
                for (int a = date.size() - 4; a < date.size(); a++) {
                    Log.i("@resultString/////", dateString.get(a) + "");
                    Log.i("@resultDate/////", date.get(a) + "");
                }
            }
            Log.d("@Lastcalendar ", "Calendar Name : " + d + " - " + dend + " + " + ddiff);
            cursor.moveToNext();
//            1572307200000     //29/10/2019
//            1572393600000     //30/10/2019
//            1572541199000     //31/10/2019

        }
//        txt.setText(all);
//        txt2.setText(date.toString());
        return calendars;
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

    public void backHome(View v){
        Intent in = new Intent(this,home.class);
        startActivity(in);
    }
}
