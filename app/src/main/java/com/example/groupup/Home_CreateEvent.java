package com.example.groupup;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Home_CreateEvent extends AppCompatActivity {
    String name = "", id = "", email = "";
    String nEvent, mStart, mEnd,eid;
    EditText edt_bank;
    int startId, endId;
    Spinner spms, spme,spb;
    ArrayList<String> dateLange;
    ProgressDialog progressDialog ;
    private RequestQueue requestQueue;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    int img_click=0;
    ImageButton SelectImageGallery;
    String ServerUploadPath = "http://www.groupupdb.com/android/addimagecreategroup.php";
    Bitmap bitmap;
    boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_create_group);
        dateLange = new ArrayList<>();
        SelectImageGallery = findViewById(R.id.img_slip);
        //Spinner
        Spinner sp = findViewById(R.id.spin_wait);
        edt_bank = findViewById(R.id.edt_bankAccount);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        sp.setSelection(1);
        //Spinner month start
        spb = findViewById(R.id.spin_bank);
        spms = findViewById(R.id.spin_start);
        spme = findViewById(R.id.spin_end);
        Date date = new Date();
        int sdate, edate;
        sdate = date.getMonth() + 1;
        edate = date.getMonth() + 4;
        if (sdate > 11) {
            sdate -= 11;
        }
        if (edate > 11) {
            edate -= 11;
        }
        ArrayAdapter<CharSequence> adm = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adb = ArrayAdapter.createFromResource(this, R.array.bank, android.R.layout.simple_spinner_item);
        adb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spms.setAdapter(adm);
        spme.setAdapter(adm);
        spb.setAdapter(adb);
        spms.setSelection(sdate);
        spme.setSelection(edate);
        spb.setSelection(0);
        Button btn_create = findViewById(R.id.newGroup_confirm);


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int finalEdate = spme.getSelectedItemPosition();
                int finalSdate = spms.getSelectedItemPosition();

                if (finalSdate <= finalEdate) {
                    int rangemonth = (finalEdate - finalSdate);//+1
                    Log.d("rangemonth", " start : " + finalSdate + " end : " + finalEdate);
                    Log.d("rangemonth", "range : " + rangemonth);
                    Calendar cal = Calendar.getInstance();
                    Date today1 = cal.getTime();
                    cal.add(Calendar.MONTH, rangemonth);
                    Date nextYear1 = cal.getTime();
                    Log.d("rangemonth", " today1 : " + today1 + " nextYear1 : " + nextYear1);
                    getDatesBetweenInYear(today1, nextYear1);
                    Log.d("rangemonth", "dateLange : " + dateLange.size() + " " + dateLange.toString());

                } else {
                    int rangemonth = (finalEdate - finalSdate)+12 ; //+13
                    Log.d("rangemonth", " start : " + finalSdate + " end : " + finalEdate);
                    Log.d("rangemonth", "range : " + rangemonth);
                    Calendar cal = Calendar.getInstance();
                    Date today1 = cal.getTime();
                    cal.add(Calendar.MONTH, rangemonth);
                    Date nextYear1 = cal.getTime();
                    Log.d("rangemonth", " today1 : " + today1 + " nextYear1 : " + nextYear1);
                    getDatesBetweenInYear(today1, nextYear1);
                    Log.d("rangemonth", "dateLange : " + dateLange.size() + " " + dateLange.toString());

                }
                if (saveData()) {
                }
            }
        });
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageGallery.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(Home_CreateEvent.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Home_CreateEvent.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
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

    public boolean saveData() {
        final EditText txtName = findViewById(R.id.title);
        final Spinner spst = findViewById(R.id.spin_start);
        final Spinner sped = findViewById(R.id.spin_end);
        final Spinner spwa = findViewById(R.id.spin_wait);
        final EditText edt_nameAccount = findViewById(R.id.edt_accountName);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.err_title);
        dialog.setIcon(android.R.drawable.btn_star_big_on);
        dialog.setPositiveButton("Close", null);
        if (img_click == 0) {
            dialog.setMessage("กรุณาใส่รูปภาพ");
            dialog.show();
            SelectImageGallery.requestFocus();
            return false;
        }
        if (txtName.getText().length() == 0) {
            dialog.setMessage(R.string.err_input_name);
            dialog.show();
            txtName.requestFocus();
            return false;
        }
        if (spst.getSelectedItemId() == 0) {
            dialog.setMessage("กรุณาเลือกเดือนเริ่มต้น");
            dialog.show();
            spst.requestFocus();
            return false;
        }
        if (sped.getSelectedItemId() == 0) {
            dialog.setMessage("กรุณาเลือกเดือนสิ้นสุด");
            dialog.show();
            sped.requestFocus();
            return false;
        }
        if (spb.getSelectedItemId() == 0) {
            dialog.setMessage("กรุณาเลือกธนาคาร");
            dialog.show();
            spb.requestFocus();
            return false;
        }
        if (spst.getSelectedItemId() == sped.getSelectedItemId()) {
            dialog.setMessage("เดือนเริ่มต้นและสิ้นสุดไม่สามารถเป็นเดือนเดียวกันได้");
            dialog.show();
            sped.requestFocus();
            return false;
        }
        if (edt_bank.getText().length() == 0) {
            dialog.setMessage("กรุณากรอกหมายเลขบัญชีธนาคาร");
            dialog.show();
            edt_bank.requestFocus();
            return false;
        }
        if (edt_nameAccount.getText().length() == 0) {
            dialog.setMessage("กรุณากรอกชื่อบัญชีธนาคาร");
            dialog.show();
            edt_nameAccount.requestFocus();
            return false;
        }

        String url = "http://www.groupupdb.com/android/creategroup.php";
        url += "?sName=" + txtName.getText().toString();
        url += "&sStart=" + spst.getSelectedItemPosition() + "";
        url += "&sEnd=" + sped.getSelectedItemPosition() + "";
        url += "&sWait=" + spwa.getSelectedItem().toString();
        url += "&sBank=" + spb.getSelectedItemPosition()+"";
        url += "&sBankNo=" + edt_bank.getText().toString();
        url += "&sBankAcc=" + edt_nameAccount.getText().toString();
        url += "&sProvi=" + name;
        url += "&sProid=" + id;
        String[] some_array = getResources().getStringArray(R.array.month);
        nEvent = txtName.getText().toString();
        startId = spst.getSelectedItemPosition();
        endId = sped.getSelectedItemPosition();
        mStart = some_array[startId];
        mEnd = some_array[endId];
        Log.d("footer", url);
        progressDialog = ProgressDialog.show(Home_CreateEvent.this, " กำลังสร้างกลุ่ม", "กรุณารอสักครู่", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
                        try {
                            String strStatusID = "0";
                            String strError = "Unknow Status!";
                            JSONObject c;
                            JSONArray data = new JSONArray("[" + response.toString() + "]");
                            for (int i = 0; i < data.length(); i++) {
                                c = data.getJSONObject(i);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            }
                            if (strStatusID.equals("0")) {

//                                dialog.setMessage(strError);
//                                dialog.show();
                            } else {
//                                dialog.setTitle(R.string.submit_title);
//                                dialog.setMessage(R.string.submit_result);
//                                dialog.show();
                                eid = strError;
                                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                    @Override
                                    protected void onPreExecute() {

                                        super.onPreExecute();


                                    }

                                    @Override
                                    protected void onPostExecute(String string1) {

                                        super.onPostExecute(string1);
                                        nextNewGroup();
                                        txtName.setText("");
                                        spst.setSelection(0);
                                        sped.setSelection(0);
                                        spwa.setSelection(0);

//                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        addDateAvaliableTodb();
                                        ImageUploadToServerFunction(eid);
                                        return "Finish";
                                    }
                                }
                                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                                AsyncTaskUploadClassOBJ.execute();

                                Log.d("", "eid : " + eid.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Home_CreateEvent.this, "Submission Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                240000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        Log.d("rangemonth", "queue : " + queue.getCache());
        Log.d("rangemonth", "stringRequest : " + stringRequest.getErrorListener());
        return true;
    }

    public void backHome(View v) {
        Intent intent = new Intent(Home_CreateEvent.this, Home.class);
        intent.putExtra("email", email + "");
        startActivity(intent);
    }

    public void nextNewGroup() {
        Intent intent = new Intent(Home_CreateEvent.this, Home.class);
        intent.putExtra("email", email + "");
        intent.putExtra("id", id + "");
        startActivity(intent);
//        Intent intent = new Intent(Home_CreateEvent.this, HomeHead_Appointment.class);
//        final Spinner spst = findViewById(R.id.spin_start);
//        final Spinner sped = findViewById(R.id.spin_end);
//        startId = spst.getSelectedItemPosition();
//        endId = sped.getSelectedItemPosition();
//        intent.putExtra("nameEvent", nEvent + "");
//        intent.putExtra("mStart", startId + "");
//        intent.putExtra("mEnd", endId + "");
//        intent.putExtra("id", id + "");
//        intent.putExtra("email", email + "");
//        intent.putExtra("tab", 0 + "");
//        intent.putExtra("create", "create");
//        Log.d("inten12", nEvent + ":" + startId + ":" + endId + ":" + id + ":" + email);
//        startActivity(intent);
    }

    public void addDateAvaliableTodb() {
        Log.d("url", "dateLange: " +dateLange.size());
        for (int i = 0; i < dateLange.size(); i++) {
            int num = i+1;
            sentDateForCalToDB(dateLange.get(i), "0", "0", "0", "0",num+"");
        }
    }

    public List<Date> getDatesBetweenInYear(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            dateLange.add(result.toString());
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }
    public void sentDateForCalToDB(String date, String morning, String late, String afternoon, String evening,String num) {
        Log.d("checkDB ", "dateString123 : " + date);
        DateFormat simpleHour = new SimpleDateFormat("dd/MM/yyyy");
        long dt = Date.parse(date);
        Date d = new Date(dt);
        String dateCheck = simpleHour.format(d);
        Log.d("checkDB ", "eid : " + eid);
        String url = "http://www.groupupdb.com/android/adddateevent.php";
        url += "?eId=" + eid+"";
        url += "&sdc=" + dateCheck + "";
        url += "&mor=" + morning + "";
        url += "&lat=" + late + "";
        url += "&aft=" + afternoon + "";
        url += "&eve=" + evening + "";
        url += "&num=" + num + "";
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Manage_calendar.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
        uploadData(stringRequest);
    }
    public void uploadData(StringRequest s) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        } else {
            requestQueue.add(s);
        }
    }
    private void addDateevent(String eid) {
        String url = "http://www.groupupdb.com/android/addDatetodateEvent.php";
        url += "?eid=" + eid + "";
        url += "&uid=" + id + "";
        Log.d("testAPi", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("testAPi", "addDaevent " + response);
                        Toast.makeText(Home_CreateEvent.this, "addDaevent Finish", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(this,
//                                    error.getString(R.string.error_network_timeout),
//                                    Toast.LENGTH_LONG).show();
                            Log.d("testAPi", "TimeoutError" + error.getMessage());
//                            Log.d("testAPi",error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.d("testAPi", "AuthFailureError" + error.getMessage());
                        } else if (error instanceof ServerError) {
                            Log.d("testAPi", "ServerError" + error.getMessage());
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.d("testAPi", "NetworkError" + error.getMessage());
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                            Log.d("testAPi", "ParseError" + error.getMessage());
                        } else {
                            Log.d("testAPi", "else " + error.getMessage());
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(Home_CreateEvent.this);
        queue.add(stringRequest);
    }//add date user create to event
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img_click = 1;
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
                Bitmap lbp = scaleDown(dstBmp, 250, false);

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
                            ActivityCompat.requestPermissions(Home_CreateEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void ImageUploadToServerFunction(final String eid) {

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
//                progressDialog = ProgressDialog.show(Home_CreateEvent.this, "กำลังอัปโหลดข้อมูล", "กรุณารอซักครู่", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                finish();
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Home_CreateEvent.this, string1, Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                SelectImageGallery.setImageResource(android.R.color.transparent);

            }

            @Override
            protected String doInBackground(Void... params) {

                Home_CreateEvent.ImageProcessClass imageProcessClass = new Home_CreateEvent.ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("eId", eid);
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

                httpURLConnectionObject.setReadTimeout(24000);

                httpURLConnectionObject.setConnectTimeout(24000);

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
}
