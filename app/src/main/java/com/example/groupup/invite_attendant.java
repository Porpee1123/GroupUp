package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class invite_attendant extends AppCompatActivity {
    Button bu_slide;
    View table_row;
    boolean isUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_attendant);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        linearLayout.setBackgroundColor(Color.parseColor("#00b06b"));
        bu_slide = findViewById(R.id.slide);
        table_row = findViewById(R.id.invite_table_friend);
        //set up
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        table_row.setVisibility(View.INVISIBLE);
        isUp = true;
    }
    // slide the view from below itself to the current position
    public void slideUp(View view) {
        Log.d("slide", "Hello up");
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        table_row.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        Log.d("slide", "Hello down");
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_black_24dp, 0);
        table_row.setVisibility(View.VISIBLE);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(table_row);
            bu_slide.setText("Slide up");
        } else {
            slideUp(table_row);
            bu_slide.setText("Slide down");

        }
        isUp = !isUp;
    }
}
