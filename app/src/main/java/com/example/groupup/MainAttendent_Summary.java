package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainAttendent_Summary extends AppCompatActivity {
    String id="",eId="",eName="",email;
    Button btn_review, btn_summary,btn_upslip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_summary);
        btn_review =findViewById(R.id.btn_review);
        btn_summary = findViewById(R.id.btn_summary);
        btn_upslip= findViewById(R.id.btn_upSlip);
        id = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");

        btn_upslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAttendent_Summary.this, UploadSlip.class);
                intent.putExtra("id",id+"");
                intent.putExtra("eid",eId+"");
                intent.putExtra("nameEvent",eName+"");
                intent.putExtra("email", email+"");
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
                Intent intent = new Intent(MainAttendent_Summary.this, MainAttendent_Reviews.class);
                intent.putExtra("id",id+"");
                intent.putExtra("eid",eId+"");
                intent.putExtra("nameEvent",eName+"");
                intent.putExtra("email", email+"");
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
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eId+"");
        intent.putExtra("nameEvent",eName+"");
        intent.putExtra("email", email+"");
        startActivity(intent);
    }

    public void qrcodeReader(String data,ImageView myqr) {
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
}
