package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Vote_date_and_time extends AppCompatActivity {
    String id="",eId="",eName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_date_and_time);
        id = getIntent().getStringExtra("id");
        eId = getIntent().getStringExtra("eid");
        eName = getIntent().getStringExtra("nameEvent");
    }
    public void backVote(View v) {
        Intent intent = new Intent(Vote_date_and_time.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("nameEvent",eName+"");
        startActivity(intent);
    }
}
