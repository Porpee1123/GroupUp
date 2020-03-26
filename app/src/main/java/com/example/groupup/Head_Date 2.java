package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Head_Date extends AppCompatActivity {

    String uid,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head__date);
    }
    public void backAppoint(View v) {
        Intent intent = new Intent(Head_Date.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid+"");
        intent.putExtra("email", email+"");
        intent.putExtra("eid",eid);
        intent.putExtra("nameEvent",nameE);
        intent.putExtra("mStart",monS);
        intent.putExtra("mEnd",monE);
        startActivity(intent);
    }
}
