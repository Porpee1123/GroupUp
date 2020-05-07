package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JobSummary extends AppCompatActivity {
    String id="",eId="",eName="",email;
    Button bJoin,bNotJoin,btn;
    boolean checkVisible, isChecked;
    RadioButton payment,upSlip;
    RadioGroup rGroup;
    LinearLayout uploadSlip;
    ImageView iconBank;
    TextView tvShowBank,tvNameE,tvDate,tvTime,tvPlace,tvPeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_job_summary);
        id = getIntent().getStringExtra("id");
        eId = getIntent().getStringExtra("eid");
        eName = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        checkVisible = true;
        bJoin = findViewById(R.id.btn_join);
        bNotJoin = findViewById(R.id.btn_notJoin);
        btn = findViewById(R.id.btn_confirm);
        payment = findViewById(R.id.payment);
        upSlip = findViewById(R.id.upSlip);
        uploadSlip = findViewById(R.id.upload);
        rGroup = findViewById(R.id.radioGroup);
        tvShowBank = findViewById(R.id.tv_showBank);
        tvNameE =findViewById(R.id.sumnaneEvent);
        tvDate =findViewById(R.id.sumdateApp);
        tvTime =findViewById(R.id.sumtimeApp);
        tvPlace =findViewById(R.id.sumplaceApp);
        tvPeople =findViewById(R.id.sumpeople);
        iconBank =findViewById(R.id.sumicon_bank);
        iconBank.setVisibility(View.GONE);
        payment.setVisibility(View.GONE);
        upSlip.setVisibility(View.GONE);
        uploadSlip.setVisibility(View.GONE);
        tvShowBank.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        Extend_MyHelper.checkStatusTrans(eId,"10",JobSummary.this,tvPeople);
        getJob();
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
                tvShowBank.setVisibility(View.GONE);
                iconBank.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                checkVisible = true;

            }
        });

    }
    public void backSum(View v) {
        Intent intent = new Intent(JobSummary.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("email",email+"");
        intent.putExtra("nameEvent",eName+"");
        intent.putExtra("tab",1+"");
        startActivity(intent);
    }

    public void visibleLinear() {
        if (checkVisible) {//join
            payment.setVisibility(View.VISIBLE);
            upSlip.setVisibility(View.VISIBLE);
            uploadSlip.setVisibility(View.GONE);
            tvShowBank.setVisibility(View.GONE);
            iconBank.setVisibility(View.GONE);
            if (payment.isChecked()||upSlip.isChecked()){
                btn.setVisibility(View.VISIBLE);
                if(upSlip.isChecked()){
                    uploadSlip.setVisibility(View.VISIBLE);
                    tvShowBank.setVisibility(View.VISIBLE);
                    iconBank.setVisibility(View.VISIBLE);
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
                       tvShowBank.setVisibility(View.GONE);
                       iconBank.setVisibility(View.GONE);
                   }else {
                       uploadSlip.setVisibility(View.VISIBLE);
                       tvShowBank.setVisibility(View.VISIBLE);
                       btn.setVisibility(View.VISIBLE);
                       iconBank.setVisibility(View.VISIBLE);
                   }
                }
            });

        }
    }

    public void getJob(){
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/getjobsummary.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("events_name", c.getString("events_name"));
                                map.put("time", c.getString("time"));
                                map.put("timerange", c.getString("timerange"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_name", c.getString("place_name"));
                                map.put("events_bankid", c.getString("events_bankid"));
                                map.put("events_bankname", c.getString("events_bankname"));
                                map.put("events_bankaccount", c.getString("events_bankaccount"));

                                MyArrList.add(map);
                            }
                            String[] some_array = getResources().getStringArray(R.array.bank);
                            tvNameE.setText(MyArrList.get(0).get("events_name"));
                            tvDate.setText(MyArrList.get(0).get("time"));
                            tvTime.setText(MyArrList.get(0).get("timerange"));
                            tvPlace.setText(MyArrList.get(0).get("place_name"));
                            String bankName=some_array[Integer.parseInt(MyArrList.get(0).get("events_bankname"))];
                            tvShowBank.setText(bankName+" : "+MyArrList.get(0).get("events_bankid")+"\nชื่อ : "+MyArrList.get(0).get("events_bankaccount"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
