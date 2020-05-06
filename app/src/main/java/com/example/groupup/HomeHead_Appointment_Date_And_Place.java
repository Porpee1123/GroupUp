package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeHead_Appointment_Date_And_Place extends AppCompatActivity {
    String id, eid, nameE, monS, monE, email, wait;
    Button btn_closeTime, btn_closePlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_appoint_date_and_place);
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        email = getIntent().getStringExtra("email");
        wait = getIntent().getStringExtra("wait");
        btn_closeTime = findViewById(R.id.btn_closeVoteTime);
        btn_closePlace = findViewById(R.id.btn_closeVotePlace);
        btn_closeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTime();
                btn_closeTime.setEnabled(false);
            }
        });
        btn_closePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePlace();
                btn_closePlace.setEnabled(false);
            }
        });
        Log.d("appoint", email + "/" + id + "/" + eid + "/" + nameE + "/" + monS + "/" + monE);
    }

    public void selectDateTime(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, Head_Date.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }

    public void selectPlace(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, Head_Place.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("wait", wait + "");
        startActivity(intent);
    }

    public void closePlace() {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closeVotePlace.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HomeHead_Appointment_Date_And_Place.this, "Close Place Vote Complete", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void closeTime() {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closevotetime.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HomeHead_Appointment_Date_And_Place.this, "Close Time Vote Complete", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
