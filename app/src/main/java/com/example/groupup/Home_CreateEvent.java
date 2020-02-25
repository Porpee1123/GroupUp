package com.example.groupup;

import android.app.AlertDialog;
import android.content.Intent;
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

public class Home_CreateEvent extends AppCompatActivity {
    String name="",id="";
    String nEvent,mStart,mEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //Spinner
        Spinner sp = findViewById(R.id.spin_wait);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        //Spinner month start
        Spinner spms = findViewById(R.id.spin_start);
        Spinner spme = findViewById(R.id.spin_end);
        ArrayAdapter<CharSequence> adm = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spms.setAdapter(adm);
        spme.setAdapter(adm);
        //start 3 end 2 คือ 3 2020 - 2 2021
        Button btn_create = findViewById(R.id.newGroup_confirm);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveData()) {
                    nextNewGroup();
                }
            }
        });
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        Log.d("footer",name);
        Log.d("footer",id);

    }

    public boolean saveData() {
        final EditText txtName = (EditText) findViewById(R.id.title);
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
        String url = "http://www.groupupdb.com/android/creategroup.php";
        url += "?sName=" + txtName.getText().toString();
        url += "&sStart=" + spst.getSelectedItem().toString();
        url += "&sEnd=" + sped.getSelectedItem().toString();
        url += "&sWait=" + spwa.getSelectedItem().toString();
        url += "&sProvi=" +name;
        url += "&sProid=" +id;
        nEvent =txtName.getText().toString();
        mStart = spst.getSelectedItem().toString();
        mEnd =sped.getSelectedItem().toString();
        Log.d("footer",url);
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
                                txtName.setText("");
                                spst.setSelection(0);
                                sped.setSelection(0);
                                spwa.setSelection(0);
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
        return true;
    }

    public void backHome(View v) {
        Intent intent = new Intent(Home_CreateEvent.this, Home.class);
        startActivity(intent);
    }

    public void nextNewGroup() {
        Intent intent = new Intent(Home_CreateEvent.this, HomeHead_Appointment.class);
        intent.putExtra("nameEvent",nEvent);
        intent.putExtra("mStart",mStart);
        intent.putExtra("mEnd",mEnd);
        intent.putExtra("id",id);
        startActivity(intent);
    }

}
