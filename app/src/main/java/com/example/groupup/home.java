package com.example.groupup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import java.util.zip.Inflater;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ResponseStr responseStr = new ResponseStr();
    JSONArray data;
    ListView listViewInvite, listViewHeader, listViewAttend;
    TextView hName;
    TextView hEmail;
    String name = "", id = "";
    boolean invite = false, head = false, attend = false;//ปิด list view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listViewInvite = findViewById(R.id.listView_invite);
        listViewHeader = findViewById(R.id.listView_Header);
        listViewAttend = findViewById(R.id.listView_attend);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        navigationView.setNavigationItemSelectedListener(home.this);
        navigationView.bringToFront();
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        getUser();
        getEventInvitation();
        getEventHeader();
        getEventAttend();
        final Button btn = (Button) findViewById(R.id.btngotohead);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoManageHeader();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        getEventInvitation();
        getEventHeader();
        getEventAttend();
    }

    public void setHumburgerButton() {

    }

    public void createGroup(View v) {
        Intent intent = new Intent(home.this, createGroup.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void search(View v) {

    }

    public void menuHamberger(View v) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Hello item");
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.menu_home:
                Log.d(TAG, "onNavigationItemSelected home: " + item.getTitle());
                break;
            case R.id.menu_account:
                goToManageAccount();
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

    public void goToManageAccount() {
        Intent intent = new Intent(home.this,register.class);
        startActivity(intent);
    }

    public void goToCalendar() {
        Intent intent = new Intent(home.this, Manage_calendar.class);
        startActivity(intent);
    }

    public void goToManageFriend() {
        Intent intent = new Intent(home.this, addFriends.class);
        startActivity(intent);
    }

    public void signout() {
    }

    public void getUser() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sId=" + "1";//รอเอาIdหรือ email จากfirebase
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
                                map.put("user_id", c.getString("user_id"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_email", c.getString("user_email"));
                                map.put("user_photo", c.getString("user_photo"));
                                MyArrList.add(map);
                            }
                            //set Header menu name email
//                            Log.d("@query", MyArrList.get(0).get("name"));
//                            Log.d("@query", MyArrList.get(0).get("email"));
                            hName.setText(MyArrList.get(0).get("user_names"));
                            name = MyArrList.get(0).get("user_names");
                            id = MyArrList.get(0).get("user_id");
                            hEmail.setText(MyArrList.get(0).get("user_email"));
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

    public void getEventInvitation() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
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
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("event_creater", c.getString("event_creater"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                //map.put("events_wait", c.getString("events_wait"));

//                                map.put("note", c.getString("note"));
                                MyArrList.add(map);
                            }
                            //textView.setText(" :: "+ MyArrList.size());
                            Log.d("query",MyArrList.size()+"");
                            //textView.setText(" :: "+ responseStr.str);
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

        Button btnget = findViewById(R.id.btnInvi);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invite) {
                    listViewInvite.setVisibility(View.GONE);
                    invite = false;
                } else {
                    invite = true;
                    listViewInvite.setVisibility(View.VISIBLE);
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_invitation_home,
                            new String[]{"event_creater", "events_name", "events_month_start", "events_month_end"}, new int[]{R.id.col_head, R.id.col_name_header, R.id.col_time, R.id.col_time_end});
                    listViewInvite.setAdapter(sAdap);
                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout listHeaderView = (LinearLayout) inflater.inflate(R.layout.activity_buttom_footer, listViewInvite,false);
                    listViewInvite.addFooterView(listHeaderView);
                    final AlertDialog.Builder viewDetail = new AlertDialog.Builder(home.this);

                    listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                            String sCreater = MyArrList.get(position).get("event_creater").toString();
                            String sName = MyArrList.get(position).get("events_name").toString();
                            String sSta = MyArrList.get(position).get("events_month_start").toString();
                            String sEnd = MyArrList.get(position).get("events_month_end").toString();
                            String sTim = sSta + " - " + sEnd;
                            viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                            viewDetail.setTitle("รายละเอียด");
                            viewDetail.setMessage("ผู้เชิญ : " + sCreater + "\n"
                                    + "ชื่อการนัดหมาย : " + sName + "\n" + "ช่วงเวลา : " + sTim + "\n");
                            viewDetail.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            viewDetail.setNegativeButton("ไม่เข้าร่วม", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            viewDetail.setPositiveButton("เข้าร่วม", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            viewDetail.show();

                        }
                    });
                }

            }
        });


    }

    public void getEventHeader() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomehead.php";
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
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_name", c.getString("states_name"));
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
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        Button btnget = findViewById(R.id.btnHead);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (head) {
                    listViewHeader.setVisibility(View.GONE);
                    head = false;
                } else {
                    head = true;
                    listViewHeader.setVisibility(View.VISIBLE);
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_header_home,
                            new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_header, R.id.col_status_header});
                    listViewHeader.setAdapter(sAdap);

                    listViewHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                            // เข้าสู่ event
                        }
                    });
                }

            }
        });


    }

    public void getEventAttend() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomeattend.php";
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
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_name", c.getString("states_name"));
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
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        Button btnget = findViewById(R.id.btnAttend);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attend) {
                    listViewAttend.setVisibility(View.GONE);
                    attend = false;
                } else {
                    attend = true;
                    listViewAttend.setVisibility(View.VISIBLE);
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_attend_home,
                            new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_attend, R.id.col_status_attend});
                    listViewAttend.setAdapter(sAdap);

                    listViewAttend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
//                        String sCreater = MyArrList.get(position).get("events_name").toString();
//                        String sName = MyArrList.get(position).get("states_name").toString();
                            // เข้าสู่ event
                        }
                    });
                }

            }
        });


    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void gotoManageHeader() {
        Intent intent = new Intent(home.this, appointment.class);
        startActivity(intent);
    }
}
