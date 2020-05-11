package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainAttendent extends AppCompatActivity {
    String nEvent="",id="",eid="",email="";
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    TextView nHead;
    ImageButton btn_note;
    String note ;
    int tab = 0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        nHead = findViewById(R.id.inviteFriends);
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        tab = Integer.parseInt( getIntent().getStringExtra("tab"));
        nHead.setText(nEvent);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        btn_note = findViewById(R.id.btn_noteAttend);
        tabHost.setup(mLocalActivityManager);
        getEvent();
        Intent intentV = new Intent(this,MainAttendent_Vote.class);
        intentV.putExtra("id", id+"");
        intentV.putExtra("email", email+"");
        intentV.putExtra("nameEvent", nEvent+"");
        intentV.putExtra("eid", eid+"");
        Intent intentS = new Intent(this,MainAttendent_Summary.class);
        intentS.putExtra("id", id+"");
        intentS.putExtra("email", email+"");
        intentS.putExtra("nameEvent", nEvent+"");
        intentS.putExtra("eid", eid+"");
//        Intent intentR = new Intent(this,MainAttendent_Reviews.class);
//        intentR.putExtra("id", id+"");
//        intentR.putExtra("email", email+"");
//        intentR.putExtra("nameEvent", nEvent+"");
//        intentR.putExtra("eid", eid+"");

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("การโหวต")
                .setContent(intentV);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("สรุปงาน")
                .setContent(intentS);
//        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
//                .setIndicator("รีวิว")
//                .setContent(intentR);
        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
//        tabHost.addTab(tabSpec3);
        tabHost.getTabWidget()
                .getChildAt(0)
                .setBackgroundResource(
                        R.drawable.shape_tab);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                updateTabs();
            }
        });
        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog viewNote = new AlertDialog.Builder(MainAttendent.this).create();
                View mViewnote = getLayoutInflater().inflate(R.layout.layout_addnote_dialog,null);
                final EditText mNameType = mViewnote.findViewById(R.id.note_edit);

                if (note.equals("") || note.equals("null")) {
                    mNameType.setText("");
                } else {
                    mNameType.setText(note);
                }
                Button btn_confirmType = mViewnote.findViewById(R.id.btn_ConfirmAddNote);
                btn_confirmType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String noteText = mNameType.getText().toString();
                        Toast.makeText(MainAttendent.this, noteText, Toast.LENGTH_SHORT).show();
                        if (!noteText.isEmpty()){
                            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = ProgressDialog.show(MainAttendent.this, "Note is Uploading", "Please Wait", false, false);
                                }

                                @Override
                                protected void onPostExecute(String string1) {
                                    super.onPostExecute(string1);
                                    progressDialog.dismiss();
                                    startActivity(getIntent());
                                    Toast.makeText(MainAttendent.this, string1, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                protected String doInBackground(Void... params) {

                                    addNoteToDB(noteText);
                                    return "Add successful!!!";
                                }
                            }
                            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                            AsyncTaskUploadClassOBJ.execute();
                        }else {
                            Toast.makeText(MainAttendent.this,"Note is empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewNote.setView(mViewnote);
                viewNote.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        nHead.setText(nEvent);
        tabHost.setCurrentTab(tab);
    }

    public void backHome(View v) {
        Intent intent = new Intent(MainAttendent.this, Home.class);
        intent.putExtra("email", email+"");
        startActivity(intent);
    }
    protected void updateTabs() {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.shape_tab);
            }
            else {

                tabHost.getTabWidget()
                        .getChildAt(i)
                        .setBackgroundResource(
                                R.drawable.visible);

            }
        }

    }
    public void addNoteToDB(String note) {
        Log.d("detail", note);
        String url = "http://www.groupupdb.com/android/addNoteEvent.php";
        url += "?eid=" + eid;
        url += "&nt=" + note;
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
    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + id;//ร  อเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
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
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("events_detail", c.getString("events_detail"));
                                map.put("events_note", c.getString("events_note"));
                                MyArrList.add(map);
                            }
                            note = MyArrList.get(0).get("events_note");
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
