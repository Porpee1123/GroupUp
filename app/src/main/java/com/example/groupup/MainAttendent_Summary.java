package com.example.groupup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainAttendent_Summary extends AppCompatActivity {
    String id = "", eId = "", eName = "", email;
    Button btn_review, btn_summary, btn_upslip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_summary);
        btn_review = findViewById(R.id.btn_review);
        btn_summary = findViewById(R.id.btn_summary);
        btn_upslip = findViewById(R.id.btn_upSlip);
        id = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        checkvisibleButton(id,eId,"3");
        btn_upslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAttendent_Summary.this, UploadSlip.class);
                intent.putExtra("id", id + "");
                intent.putExtra("eid", eId + "");
                intent.putExtra("nameEvent", eName + "");
                intent.putExtra("email", email + "");
                startActivity(intent);
            }
        });
        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumJob();
            }
        });
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAttendent_Summary.this, Reviews.class);
                intent.putExtra("id", id + "");
                intent.putExtra("eid", eId + "");
                intent.putExtra("nameEvent", eName + "");
                intent.putExtra("email", email + "");
                startActivity(intent);
//                final AlertDialog viewDetail = new AlertDialog.Builder(MainAttendent_Summary.this).create();
//                View mView = getLayoutInflater().inflate(R.layout.layout_showqrcode_dialog,null);
//                ImageView img_qr = mView.findViewById(R.id.qr_goEvent);
//                String dataQuery = id+":"+eId;
//                qrcodeReader(dataQuery,img_qr);
//                viewDetail.setView(mView);
//                viewDetail.show();
            }
        });
    }

    public void sumJob() {
        Intent intent = new Intent(MainAttendent_Summary.this, JobSummary.class);
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eId + "");
        intent.putExtra("nameEvent", eName + "");
        intent.putExtra("email", email + "");
        startActivity(intent);
    }

    public void qrcodeReader(String data, ImageView myqr) {
        Bitmap bmp;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            myqr.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void checkvisibleButton(String uid, String eid, String pid) {
        Log.d("checktrans", "id : " + uid + " eid : " + eid + " pid : " + pid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gettransid.php";
        url += "?uId=" + uid;
        url += "&eId=" + eid;
        url += "&pId=" + pid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("pri_id", c.getString("pri_id"));
                                MyArrList.add(map);
                            }
                            String state = MyArrList.get(0).get("states_id");
                            int stateId = Integer.parseInt(state);
                            Log.d("checktrans", "state " + state);
                            if (stateId == 11) {
                                btn_summary.setEnabled(true);
                                btn_upslip.setEnabled(false);
                                btn_review.setEnabled(false);
                                btn_summary.setAlpha(1);
                                btn_upslip.setAlpha((float) 0.5);
                                btn_review.setAlpha((float) 0.5);
                            } else if (stateId == 14) {
                                btn_upslip.setEnabled(true);
                                btn_summary.setEnabled(false);
                                btn_review.setEnabled(false);
                                btn_upslip.setAlpha(1);
                                btn_summary.setAlpha((float) 0.5);
                                btn_review.setAlpha((float) 0.5);
                            } else if (stateId == 17) {
                                btn_review.setEnabled(true);
                                btn_summary.setEnabled(false);
                                btn_upslip.setEnabled(false);
                                btn_review.setAlpha(1);
                                btn_summary.setAlpha((float) 0.5);
                                btn_upslip.setAlpha((float) 0.5);
                            } else {
                                btn_review.setEnabled(false);
                                btn_summary.setEnabled(false);
                                btn_upslip.setEnabled(false);
                                btn_review.setAlpha((float) 0.5);
                                btn_summary.setAlpha((float) 0.5);
                                btn_upslip.setAlpha((float) 0.5);
                            }
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
