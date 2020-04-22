package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class JobSummary extends AppCompatActivity {
    String id="",eId="",eName="";
    Button bJoin,bNotJoin,btn;
    boolean checkVisible, isChecked;
    RadioButton payment,upSlip;
    RadioGroup rGroup;
    LinearLayout uploadSlip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_job_summary);
        id = getIntent().getStringExtra("id");
        eId = getIntent().getStringExtra("eid");
        eName = getIntent().getStringExtra("nameEvent");
        checkVisible = true;
        bJoin = findViewById(R.id.btn_join);
        bNotJoin = findViewById(R.id.btn_notJoin);
        btn = findViewById(R.id.btn_confirm);
        payment = findViewById(R.id.payment);
        upSlip = findViewById(R.id.upSlip);
        uploadSlip = findViewById(R.id.upload);
        rGroup = findViewById(R.id.radioGroup);
        payment.setVisibility(View.GONE);
        upSlip.setVisibility(View.GONE);
        uploadSlip.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleLinear();
            }
        });
        bNotJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.setVisibility(View.GONE);
                upSlip.setVisibility(View.GONE);
                uploadSlip.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                checkVisible = true;
            }
        });

    }
    public void backSum(View v) {
        Intent intent = new Intent(JobSummary.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("nameEvent",eName+"");
        startActivity(intent);
    }

    public void visibleLinear() {
        if (checkVisible) {//join
            payment.setVisibility(View.VISIBLE);
            upSlip.setVisibility(View.VISIBLE);
            uploadSlip.setVisibility(View.GONE);
            if (payment.isChecked()||upSlip.isChecked()){
                btn.setVisibility(View.VISIBLE);
                if(upSlip.isChecked()){
                    uploadSlip.setVisibility(View.VISIBLE);
                }            }else {
                btn.setVisibility(View.GONE);
            }

            checkVisible = false;
            rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                   if(payment.isChecked()){
                       btn.setVisibility(View.VISIBLE);
                       uploadSlip.setVisibility(View.GONE);
                   }else {
                       uploadSlip.setVisibility(View.VISIBLE);
                       btn.setVisibility(View.VISIBLE);
                   }
                }
            });

        }
    }

}
