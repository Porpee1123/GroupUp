package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import java.util.StringTokenizer;

public class MainAttendent_Reviews extends AppCompatActivity {
    String uid = "", eId = "", eName = "", email = "", placeId = "", placeName = "";
    TextView tvName;
    RatingBar rtBar;
    EditText edt_datail;
    Button btn_con;
    String textReview = "", nameReview = "";
    float scoreReview = 0;
    float scoreEDb = 0, scorePeople = 0;
    int sumpeople = 0, peopleEDb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_reviews);
        tvName = findViewById(R.id.review_name);
        rtBar = findViewById(R.id.review_ratingBar);
        edt_datail = findViewById(R.id.review_edtdetail);
        btn_con = findViewById(R.id.review_btnConfirm);
        uid = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        getJob();
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });
    }

    public void backSum(View v) {
        finish();
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
                            placeId = MyArrList.get(0).get("place_id");
                            placeName = MyArrList.get(0).get("place_name");
                            tvName.setText(placeName);
                            getScorePeople();
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

    public void getScorePeople() {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/getallscoreandpeoplereview.php";
        url += "?pId=" + placeId;//ร  อเอาIdหรือ email จากfirebase
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
                                map.put("sumReview", c.getString("sumReview"));
                                map.put("peopleReview", c.getString("peopleReview"));
                                MyArrList.add(map);
                            }
                            sumpeople = Integer.parseInt(MyArrList.get(0).get("peopleReview"));
                            scorePeople = Float.parseFloat(MyArrList.get(0).get("sumReview"));
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

    public void addJob() {
        scoreReview = rtBar.getRating();
        textReview = edt_datail.getText().toString();
        scoreEDb = scorePeople + scoreReview;
        peopleEDb = sumpeople + 1;
        Log.d("checkscoreReview", "EDB score " + scoreEDb + " people " + peopleEDb);
        Log.d("checkscoreReview", "score " + scorePeople + " people " + sumpeople + " rtScore " + scoreReview);

//        String url = "http://www.groupupdb.com/android/addreviewtodb.php";
//        url += "?pid=" + placeId + "";
//        url += "&uid=" + uid + "";
//        url += "&rts=" + scoreReview + "";
//        url += "&rvt=" + textReview;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(MainAttendent_Reviews.this, "Add Review Complete", Toast.LENGTH_SHORT).show();
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

    }

}
