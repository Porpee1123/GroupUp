package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainAttendent_Vote extends AppCompatActivity {
    String id="",eId="",eName="",email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_vote);
        id = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");

    }
    public void voteDate(View v) {
        Intent intent = new Intent(MainAttendent_Vote.this, Vote_date_and_time.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("nameEvent",eName+"");
        startActivity(intent);
    }

    public void votePlace(View v) {
        Intent intent = new Intent(MainAttendent_Vote.this, Vote_place.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("nameEvent",eName+"");
        startActivity(intent);
    }
}
