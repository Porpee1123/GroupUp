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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class JobSummary extends AppCompatActivity {
    String id = "", eId = "", eName = "", email = "", transId = "";
    Button bJoin, bNotJoin, btn,btn_uploadSlip;
    boolean checkVisible, isChecked;
    RadioButton payment, upSlip;
    RadioGroup rGroup;
    LinearLayout uploadSlip;
    boolean check = true;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    ImageView SelectImageGallery;
    String ServerUploadPath = "http://www.groupupdb.com/android/addBillEventTransfer.php";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    ImageView iconBank;
    TextView tvShowBank, tvNameE, tvDate, tvTime, tvPlace, tvPeople;
    int cAccept, cCancle, cCash, cTransfer;

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
        cAccept = 0;
        cCancle = 0;
        cCash = 0;
        cTransfer = 0;
        bJoin = findViewById(R.id.btn_join);
        SelectImageGallery = findViewById(R.id.img_slip);
        bNotJoin = findViewById(R.id.btn_notJoin);
        btn = findViewById(R.id.btn_confirm);
        payment = findViewById(R.id.payment);
        upSlip = findViewById(R.id.upSlip);
        uploadSlip = findViewById(R.id.upload);
        rGroup = findViewById(R.id.radioGroup);
        tvShowBank = findViewById(R.id.tv_showBank);
        btn_uploadSlip = findViewById(R.id.btn_uploadSlip);
        tvNameE = findViewById(R.id.sumnaneEvent);
        tvDate = findViewById(R.id.sumdateApp);
        tvTime = findViewById(R.id.sumtimeApp);
        tvPlace = findViewById(R.id.sumplaceApp);
        tvPeople = findViewById(R.id.sumpeople);
        iconBank = findViewById(R.id.sumicon_bank);
        SelectImageGallery.setVisibility(View.GONE);
        iconBank.setVisibility(View.GONE);
        payment.setVisibility(View.GONE);
        upSlip.setVisibility(View.GONE);
        uploadSlip.setVisibility(View.GONE);
        tvShowBank.setVisibility(View.GONE);
        btn_uploadSlip.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        getTransIDByTrans(id, eId, "2");
        Extend_MyHelper.checkStatusTrans(eId, "10", JobSummary.this, tvPeople);
        getJob();
        Log.d("checktrans", "tran " + transId);
        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rGroup.clearCheck();visibleLinear();
            }
        });
        bNotJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setEnabled(true);
                btn.setAlpha(1);
                payment.setVisibility(View.GONE);
                upSlip.setVisibility(View.GONE);
                uploadSlip.setVisibility(View.GONE);
                tvShowBank.setVisibility(View.GONE);
                btn_uploadSlip.setVisibility(View.GONE);
                iconBank.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                checkVisible = true;
                cAccept = 0;
                cCancle = 1;
                cCash = 0;
                cTransfer = 0;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cCancle==1){
                    Log.d("checkCon", "cancle");
                    Extend_MyHelper.UpdateStateToDb(transId, 15 + "",JobSummary.this);
                    finish();
                }else if (cAccept==1&&cCash==1){
                    Log.d("checkCon","Cash");
                    Extend_MyHelper.UpdateStateToDb(transId, 13 + "",JobSummary.this);
                    addBill(id,eId,"3");
                    finish();
                }else if (cAccept==1&&cTransfer==1){
                    Log.d("checkCon","Transfer");
                    Extend_MyHelper.UpdateStateToDb(transId, 12 + "",JobSummary.this);
                    ImageUploadToServerFunction();

                }
            }
        });
        btn_uploadSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageGallery.setVisibility(View.VISIBLE);
                btn.setAlpha(1);
                btn.setEnabled(true);
                if (ContextCompat.checkSelfPermission(JobSummary.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(JobSummary.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("email", email + "");
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
                } else {
                    requestImagePermission();
                }
            }
        });

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

    public void visibleLinear() {
        if (checkVisible) {//join
            cAccept = 1;
            cCancle = 0;
            cCash = 0;
            cTransfer = 0;
            payment.setVisibility(View.VISIBLE);
            upSlip.setVisibility(View.VISIBLE);
            uploadSlip.setVisibility(View.GONE);
            tvShowBank.setVisibility(View.GONE);
            btn_uploadSlip.setVisibility(View.GONE);
            iconBank.setVisibility(View.GONE);
            if (payment.isChecked() || upSlip.isChecked()) {
                btn.setVisibility(View.VISIBLE);
                if (upSlip.isChecked()) {
                    uploadSlip.setVisibility(View.VISIBLE);
                    tvShowBank.setVisibility(View.VISIBLE);
                    btn_uploadSlip.setVisibility(View.VISIBLE);
                    iconBank.setVisibility(View.VISIBLE);
                }
            } else {//payment
                btn.setVisibility(View.GONE);
            }

            checkVisible = false;
            rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (payment.isChecked()) {
                        cAccept = 1;
                        cCancle = 0;
                        cCash = 1;
                        cTransfer = 0;
                        btn.setVisibility(View.VISIBLE);
                        uploadSlip.setVisibility(View.GONE);
                        tvShowBank.setVisibility(View.GONE);
                        btn_uploadSlip.setVisibility(View.GONE);
                        iconBank.setVisibility(View.GONE);
                        btn.setAlpha(1);
                        btn.setEnabled(true);
                        SelectImageGallery.setVisibility(View.GONE);
                    } else {
                        cAccept = 1;
                        cCancle = 0;
                        cCash = 0;
                        cTransfer = 1;
                        btn.setEnabled(false);
                        btn.setAlpha((float) 0.5);
                        uploadSlip.setVisibility(View.VISIBLE);
                        tvShowBank.setVisibility(View.VISIBLE);
                        btn_uploadSlip.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        iconBank.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    public void getJob() {
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
                            String bankName = some_array[Integer.parseInt(MyArrList.get(0).get("events_bankname"))];
                            tvShowBank.setText(bankName + " : " + MyArrList.get(0).get("events_bankid") + "\nชื่อ : " + MyArrList.get(0).get("events_bankaccount"));

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

    public void getTransIDByTrans(String uid, String eid, String pid) {
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
                            transId = MyArrList.get(0).get("trans_id");
//                            Log.d("themeSelect","myarr : "+MyArrList.toString());
                            Log.d("checktrans", "tran " + transId);
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
                            ActivityCompat.requestPermissions(JobSummary.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(JobSummary.this, "Image is Uploading", "Please Wait", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);
                    finish();
                    // Dismiss the progress dialog after done uploading.
                    progressDialog.dismiss();

                    // Printing uploading success message coming from server on android app.
                    Toast.makeText(JobSummary.this, string1, Toast.LENGTH_LONG).show();

                    // Setting image as transparent after done uploading.
                    SelectImageGallery.setImageResource(android.R.color.transparent);

                }

                @Override
                protected String doInBackground(Void... params) {

                    JobSummary.ImageProcessClass imageProcessClass = new JobSummary.ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
                    HashMapParams.put("uId", id);
                    HashMapParams.put("eId", eId);
                    HashMapParams.put("photo", ConvertImage);
                    Log.d("hashmap", HashMapParams.toString());
                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();


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

    public void addBill(String uid, String eid, String sbid) {
        Log.d("checktrans", "id : " + uid + " eid : " + eid + " sbid : " + sbid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/addSliptobill.php";
        url += "?uId=" + uid;
        url += "&eId=" + eid;
        url += "&sbId=" + sbid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Log", "response " + response);
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


//    public void cancleEvent(String tid){
//        Log.d("votedate", eid + " : " + pid);
//        String url = "http://www.groupupdb.com/android/addpointvoteplace.php";
//        url += "?tid=" + tid;
//        url += "&eId=" + eid;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
//                    }
//                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//    }
}
