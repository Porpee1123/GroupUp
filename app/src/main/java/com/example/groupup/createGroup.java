package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class createGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //Spinner
        Spinner sp = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        //Spinner month start
        Spinner spms = findViewById(R.id.spinner7);
        Spinner spme = findViewById(R.id.spinner8);
        ArrayAdapter<CharSequence> adm = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spms.setAdapter(adm);
        spme.setAdapter(adm);

    }
    public void inviteFriend(View v){
        Intent intent = new Intent(createGroup.this,inviteFriends.class);
        startActivity(intent);
    }

}
