package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class HomeHead_Appointment_SetItem extends AppCompatActivity {
    Button inviteFriend , selectTheme,seeInviteFriend;
    String id,eid,nameE,monS,monE,email,wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appoint_set_item);
        inviteFriend = findViewById(R.id.select_friend);
        selectTheme = findViewById(R.id.selectTheme);
        seeInviteFriend = findViewById(R.id.show_inviteFriend);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        wait = getIntent().getStringExtra("wait");
//        Log.d("appoint","home appoint "+email+"/"+id+"/"+eid+"/"+nameE+"/"+monS+"/"+monE);
        seeInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeHead_Appointment_SetItem.this, show_invitation.class);
                intent.putExtra("email",email+"");
                intent.putExtra("id",id+"");
                intent.putExtra("eid",eid+"");
                intent.putExtra("nameEvent",nameE+"");
                intent.putExtra("mStart",monS+"");
                intent.putExtra("mEnd",monE+"");
                startActivity(intent);
            }
        });
    }

    public void selectFriend(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, InviteFriend.class);
        intent.putExtra("email",email+"");
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        startActivity(intent);

    }

    public void selectTheme(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, HomeHead_Theme.class);
        intent.putExtra("email",email+"");
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }
    public void selectSlip(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, HomeHead_SlipCheck.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        startActivity(intent);
    }
    public void selectSummary(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, Head_Summary.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        startActivity(intent);
    }

}
