package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeHead_Appointment_SetItem extends AppCompatActivity {
    Button inviteFriend , selectTheme;
    String id,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_set_item);
        inviteFriend = findViewById(R.id.select_friend);
        selectTheme = findViewById(R.id.selectTheme);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
    }

    public void selectFriend(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, InviteFriend.class);
        intent.putExtra("email",email);
        intent.putExtra("id",id);
        intent.putExtra("eid",eid);
        intent.putExtra("nameEvent",nameE);
        intent.putExtra("mStart",monS);
        intent.putExtra("mEnd",monE);
        startActivity(intent);
    }

    public void selectTheme(View v) {
        Intent intent = new Intent(HomeHead_Appointment_SetItem.this, HomeHead_Theme.class);
        startActivity(intent);
    }
}
