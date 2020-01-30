package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

public class Manage_calendar extends AppCompatActivity {
    private final int INVALID_VAL = -1;
    int mHour, mMinute;
    String mRecurrenceOption, mRecurrenceRule;
    CalendarPickerView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_calendar);
        Calendar nextYear = Calendar.getInstance();
        calendar = (CalendarPickerView) findViewById(R.id.calendarPickerView);
        nextYear.add(Calendar.MONTH, 3);

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .inMode(MULTIPLE);
        calendar.selectDate(today);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //Do something when dates get selected
            }

            @Override
            public void onDateUnselected(Date date) {
                // when dates didnt get selected
            }
        });

    }
}
