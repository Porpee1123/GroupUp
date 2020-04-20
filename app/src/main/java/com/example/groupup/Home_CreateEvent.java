package com.example.groupup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Home_CreateEvent extends AppCompatActivity {
    String name = "", id = "", email = "";
    String nEvent, mStart, mEnd,eid;
    int startId, endId;
    Spinner spms, spme;
    ArrayList<String> dateLange;
    ProgressDialog progressDialog ;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_create_group);
        dateLange = new ArrayList<>();
        //Spinner
        Spinner sp = findViewById(R.id.spin_wait);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        sp.setSelection(1);
        //Spinner month start
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
        spms.setAdapter(adm);
        spme.setAdapter(adm);
        spms.setSelection(sdate);
        spme.setSelection(edate);
        Button btn_create = findViewById(R.id.newGroup_confirm);


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int finalEdate = spme.getSelectedItemPosition();
                int finalSdate = spms.getSelectedItemPosition();

                if (finalSdate <= finalEdate) {
                    int rangemonth = (finalEdate - finalSdate) + 1;
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
                    int rangemonth = (finalEdate - finalSdate) + 13;
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
    }

    public boolean saveData() {
        final EditText txtName = findViewById(R.id.title);
        final Spinner spst = findViewById(R.id.spin_start);
        final Spinner sped = findViewById(R.id.spin_end);
        final Spinner spwa = findViewById(R.id.spin_wait);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.err_title);
        dialog.setIcon(android.R.drawable.btn_star_big_on);
        dialog.setPositiveButton("Close", null);
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
        if (spst.getSelectedItemId() == sped.getSelectedItemId()) {
            dialog.setMessage("เดือนเริ่มต้นและสิ้นสุดไม่สามารถเป็นเดือนเดียวกันได้");
            dialog.show();
            sped.requestFocus();
            return false;
        }
        String url = "http://www.groupupdb.com/android/creategroup.php";
        url += "?sName=" + txtName.getText().toString();
        url += "&sStart=" + spst.getSelectedItemPosition() + "";
        url += "&sEnd=" + sped.getSelectedItemPosition() + "";
        url += "&sWait=" + spwa.getSelectedItem().toString();
        url += "&sProvi=" + name;
        url += "&sProid=" + id;
        String[] some_array = getResources().getStringArray(R.array.month);
        nEvent = txtName.getText().toString();
        startId = spst.getSelectedItemPosition();
        endId = sped.getSelectedItemPosition();
        mStart = some_array[startId];
        mEnd = some_array[endId];
        Log.d("footer", url);
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

                                        progressDialog = ProgressDialog.show(Home_CreateEvent.this, " กำลังสร้างกลุ่ม", "กรุณารอสักครู่", false, false);
                                    }

                                    @Override
                                    protected void onPostExecute(String string1) {

                                        super.onPostExecute(string1);
                                        txtName.setText("");
                                        spst.setSelection(0);
                                        sped.setSelection(0);
                                        spwa.setSelection(0);
                                        nextNewGroup();
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        addDateAvaliableTodb();
                                        return "Finish";
                                    }
                                }
                                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                                AsyncTaskUploadClassOBJ.execute();

                                Log.d("rangemonth", "eid : " + eid.toString());
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
        Intent intent = new Intent(Home_CreateEvent.this, HomeHead_Appointment.class);
        final Spinner spst = findViewById(R.id.spin_start);
        final Spinner sped = findViewById(R.id.spin_end);
        startId = spst.getSelectedItemPosition();
        endId = sped.getSelectedItemPosition();
        intent.putExtra("nameEvent", nEvent + "");
        intent.putExtra("mStart", startId + "");
        intent.putExtra("mEnd", endId + "");
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("tab", 0 + "");
        intent.putExtra("create", "create");
        Log.d("inten12", nEvent + ":" + startId + ":" + endId + ":" + id + ":" + email);
        startActivity(intent);
    }

    public void addDateAvaliableTodb() {
        Log.d("url", "dateLange: " +dateLange.size());
        for (int i = 0; i < dateLange.size(); i++) {
            sentDateForCalToDB(dateLange.get(i), "0", "0", "0", "0");
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
    public void sentDateForCalToDB(String date, String morning, String late, String afternoon, String evening) {
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

}
