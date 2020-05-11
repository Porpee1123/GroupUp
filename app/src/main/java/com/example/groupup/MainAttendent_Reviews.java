package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainAttendent_Reviews extends AppCompatActivity {
    String id = "", eId = "", eName = "", email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_reviews);
        id = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
    }
    public void backSum(View v) {
        finish();
//        Intent intent = new Intent(JobSummary.this, MainAttendent.class);
//        intent.putExtra("id",id+"");
//        intent.putExtra("eid",eId+"");
//        intent.putExtra("email",email+"");
//        intent.putExtra("nameEvent",eName+"");
//        intent.putExtra("tab",1+"");
//        startActivity(intent);
    }
}
