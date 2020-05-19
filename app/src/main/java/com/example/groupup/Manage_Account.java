package com.example.groupup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Manage_Account extends AppCompatActivity {
    final int PIC_CROP = 1;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    ImageButton SelectImageGallery;
    Bitmap bitmap;
    boolean check = true;
    ProgressDialog progressDialog;
    String ServerUploadPath = "http://www.groupupdb.com/android/manageaccount.php";
    String name, email, id, photo;
    EditText txtName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_register);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        photo = getIntent().getStringExtra("photo");
        txtEmail = findViewById(R.id.email);
        txtName = findViewById(R.id.name);
        SelectImageGallery = findViewById(R.id.addPicture);
        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Manage_Account.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Manage_Account.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("email", email + "");
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
                } else {
                    requestImagePermission();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
                }
            }
        });
        new Extend_MyHelper.SendHttpRequestTask(photo,SelectImageGallery,250).execute();

        EditText edt_name = findViewById(R.id.name);
        EditText edt_email = findViewById(R.id.email);
        edt_name.setText(name);
        edt_email.setText(email);
        edt_email.setFocusable(false);
        edt_email.setEnabled(false);


        final Button bcon = (Button) findViewById(R.id.account_confirm);
        bcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUploadToServerFunction();
                name = txtName.getText().toString();
                Intent in = new Intent(Manage_Account.this, Home.class);
                in.putExtra("id", id + "");
                in.putExtra("name", name + "");
                in.putExtra("email", email + "");
                in.putExtra("photo", "http://www.groupupdb.com/android/images/"+id);
                startActivity(in);
            }
        });

    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap.getWidth() >= bitmap.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                            0,
                            bitmap.getHeight(),
                            bitmap.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            0,
                            bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                            bitmap.getWidth(),
                            bitmap.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 375, false);

                SelectImageGallery.setImageBitmap(lbp);
//                SelectImageGallery.setAlpha(0.5f);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public void requestImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the gallery")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Manage_Account.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        if (bitmap == null) {
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(Manage_Account.this, "กำลังอัปโหลดข้อมูล", "กรุณารอซักครู่", false, false);
                    name = txtName.getText().toString();
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);

                    // Dismiss the progress dialog after done uploading.
                    progressDialog.dismiss();


                    // Printing uploading success message coming from server on android app.
                    Toast.makeText(Manage_Account.this, string1, Toast.LENGTH_LONG).show();

                    // Setting image as transparent after done uploading.
                    SelectImageGallery.setImageResource(android.R.color.transparent);

                }

                @Override
                protected String doInBackground(Void... params) {
                    updateNoimage(name);
                    return "finish update name";
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();

        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    name = txtName.getText().toString();
                    progressDialog = ProgressDialog.show(Manage_Account.this, "กำลังอัปโหลดข้อมูล", "กรุณารอซักครู่", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);

                    // Dismiss the progress dialog after done uploading.
                    progressDialog.dismiss();

                    // Printing uploading success message coming from server on android app.
                    Toast.makeText(Manage_Account.this, string1, Toast.LENGTH_LONG).show();

                    // Setting image as transparent after done uploading.
                    SelectImageGallery.setImageResource(android.R.color.transparent);

                }

                @Override
                protected String doInBackground(Void... params) {

                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
                    HashMapParams.put("name", name);
                    HashMapParams.put("email", email);
                    HashMapParams.put("photo", ConvertImage);
                    Log.d("hashmap", HashMapParams.toString());
                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();
        }

    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        in.putExtra("id", id + "");
        startActivity(in);
    }

    public void updateNoimage(String name) {
        String url = "http://www.groupupdb.com/android/managefriendnoimage.php";
        url += "?name=" + name;
        url += "&email=" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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
