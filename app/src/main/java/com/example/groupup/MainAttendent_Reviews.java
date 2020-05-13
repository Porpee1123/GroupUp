package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainAttendent_Reviews extends AppCompatActivity {
    String id = "", eId = "", eName = "", email;
    TextView tvName;
    RatingBar rtBar;
    EditText edt_datail ;
    Button btn_con;
    String textReview="",nameReview="";
    float scoreReview=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_reviews);
        tvName =findViewById(R.id.review_name);
        rtBar = findViewById(R.id.review_ratingBar);
        edt_datail = findViewById(R.id.review_edtdetail);
        btn_con =findViewById(R.id.review_btnConfirm);
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
