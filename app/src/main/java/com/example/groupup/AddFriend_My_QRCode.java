package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AddFriend_My_QRCode extends AppCompatActivity {
    ImageView myqr ;
    Bitmap bmp;
    String email,id,sent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_my_qr);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        sent = email+","+id;
        myqr = findViewById(R.id.myQr);
        qrcodeReader(sent);
        Button bsave =  findViewById(R.id.btn_saveImage);
        bsave.setVisibility(View.INVISIBLE);
//        bsave.setText("SAVE QR-CODE");
//        bsave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                saveToInternalStorage(bmp);
//                saveImage(bmp);
//            }
//        });
    }
    public void qrcodeReader(String data){
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
//    private String saveToInternalStorage(Bitmap bitmapImage){
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,"qrcode.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return directory.getAbsolutePath();
//    }
//    private void saveImage(Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String fname = "qrcode"+ timeStamp +".jpg";
//
//        File file = new File(root, fname);
//        if (file.exists()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            Toast.makeText(AddFriend_My_QRCode.this, fname+" save complete", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(AddFriend_My_QRCode.this, fname+" save fail", Toast.LENGTH_LONG).show();
//        }
//    }

//    private void loadImageFromStorage(String path) {
//
//        try {
//            File f=new File(path, "profile.jpg");
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            ImageView img=(ImageView)findViewById(R.id.imgPicker);
//            img.setImageBitmap(b);
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//
//    }
}
