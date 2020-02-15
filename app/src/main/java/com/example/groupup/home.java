package com.example.groupup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ResponseStr responseStr = new ResponseStr();
    JSONArray data;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView)findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        navigationView.setNavigationItemSelectedListener(home.this);
        navigationView.bringToFront();
//        getUser();
        getEvent();

    }
    public void setHumburgerButton(){

    }
    public void createGroup(View v){
        Intent intent = new Intent(home.this,createGroup.class);
        startActivity(intent);
    }
    public void search(View v){

    }
    public void menuHamberger(View v){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Hello item");
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menu_home:
                Log.d(TAG, "onNavigationItemSelected home: " + item.getTitle());
            break;
            case R.id.menu_account:
                Log.d(TAG, "onNavigationItemSelected account: " + item.getTitle());
                break;
            case R.id.menu_calendar:
                goToCalendar();
                Log.d(TAG, "onNavigationItemSelected calendar: " + item.getTitle());
                break;
            case R.id.menu_friend:
                goToManageFriend();
                Log.d(TAG, "onNavigationItemSelected friend: " + item.getTitle());
                break;
            case R.id.menu_signout:
                Log.d(TAG, "onNavigationItemSelected signout: " + item.getTitle());
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }
    public void goToManageAccount(){}
    public void goToCalendar(){
        Intent intent = new Intent(home.this,Manage_calendar.class);
        startActivity(intent);
    }
    public void goToManageFriend(){
        Intent intent = new Intent(home.this,addFriends.class);
        startActivity(intent);
    }
    public void signout(){}
    public void getUser(){
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sId=" + "1";//รอเอาIdจากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText("Response is: "+ response.toString());

                        try {
                            //responseStr = new ResponseStr(response.toString());
                            //responseStr.setValue(new JSONArray(response.toString()));


                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for(int i = 0; i < data.length(); i++){
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("id", c.getString("id"));
                                map.put("name", c.getString("name"));
                                map.put("email", c.getString("email"));
                                map.put("photo", c.getString("photo"));
//                                map.put("note", c.getString("note"));
                                MyArrList.add(map);
                            }
                            //textView.setText(" :: "+ MyArrList.size());

                            //textView.setText(" :: "+ responseStr.str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():"+error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        final Button btn = (Button) findViewById(R.id.btngetuser);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_column,
                        new String[] {"id", "name", "email", "photo"}, new int[] {R.id.col_trans_id, R.id.col_name, R.id.col_msg, R.id.col_amt});
                listView.setAdapter(sAdap);
                final AlertDialog.Builder viewDetail = new AlertDialog.Builder(home.this);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String sTransID = MyArrList.get(position).get("id").toString();
                        String sName = MyArrList.get(position).get("name").toString();
                        String sMsg = MyArrList.get(position).get("email").toString();
                        String sAmt = MyArrList.get(position).get("photo").toString();
                        viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                        viewDetail.setTitle("รายละเอียด");
                        viewDetail.setMessage("เลขที่รายการ : " + sTransID + "\n"
                                + "ชื่อ : " + sName + "\n" + "รายการ : " + sMsg + "\n"
                                + "จำนวนเงิน : " + Double.parseDouble(sAmt)+ "\n");
                        viewDetail.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        viewDetail.show();

                    }
                });
            }
        });


    }
    public void getEvent(){
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getevent.php";
        url += "?sId=" + "1";//รอเอาIdจากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText("Response is: "+ response.toString());

                        try {
                            //responseStr = new ResponseStr(response.toString());
                            //responseStr.setValue(new JSONArray(response.toString()));


                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for(int i = 0; i < data.length(); i++){
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("eid", c.getString("eid"));
                                map.put("name", c.getString("name"));
                                map.put("month_start", c.getString("month_start"));
                                map.put("month_end", c.getString("month_end"));
                                map.put("wait", c.getString("wait"));

//                                map.put("note", c.getString("note"));
                                MyArrList.add(map);
                            }
                            //textView.setText(" :: "+ MyArrList.size());

                            //textView.setText(" :: "+ responseStr.str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():"+error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        final Button btn = (Button) findViewById(R.id.btngetuser);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_column,
                        new String[] {"eid", "name", "month_start", "month_end"}, new int[] {R.id.col_trans_id, R.id.col_name, R.id.col_msg, R.id.col_amt});
                listView.setAdapter(sAdap);
                final AlertDialog.Builder viewDetail = new AlertDialog.Builder(home.this);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String sTransID = MyArrList.get(position).get("eid").toString();
                        String sName = MyArrList.get(position).get("name").toString();
                        String sMsg = MyArrList.get(position).get("month_start").toString();
                        String sAmt = MyArrList.get(position).get("month_end").toString();
                        viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                        viewDetail.setTitle("รายละเอียด");
                        viewDetail.setMessage("เลขที่รายการ : " + sTransID + "\n"
                                + "ชื่อ : " + sName + "\n" + "รายการ : " + sMsg + "\n"
                                + "จำนวนเงิน : " + Double.parseDouble(sAmt)+ "\n");
                        viewDetail.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        viewDetail.show();

                    }
                });
            }
        });


    }
    public class ResponseStr{
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr){
            this.jsonArray = jsonArr;
        }

    }
}
