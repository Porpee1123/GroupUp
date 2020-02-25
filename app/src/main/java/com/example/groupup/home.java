package com.example.groupup;

import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private GoogleSignInClient mGoogleSignInClient;
    ResponseStr responseStr = new ResponseStr();
    JSONArray data;
    ListView listViewInvite, listViewHeader, listViewAttend;
    TextView hName;
    TextView hEmail;
    String name = "", id = "", email = "";
    boolean invite = false, head = false, attend = false;//ปิด list view
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        listViewInvite = new ListView(this);
        listViewHeader = findViewById(R.id.listView_Header);
        listViewAttend = findViewById(R.id.listView_attend);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        navigationView.setNavigationItemSelectedListener(home.this);
        navigationView.bringToFront();
        createTab();
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        email = getIntent().getStringExtra("email");
        getUser();
        new CountDownTimer(500, 500) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
//                getEventInvitation();
//                getEventHeader();
//                getEventAttend();

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        //firebase signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
    public void createTab(){

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("ผู้เข้าร่วมงาน")
                .setContent(new Intent(this, attendant.class));

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("แม่งาน")
                .setContent(new Intent(this, header.class));
        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
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

    @Override
    protected void onResume() {
        super.onResume();
//        getUser();
        readFile();
//        getEventInvitation();
//        getEventHeader();
//        getEventAttend();


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
                signout();
                Log.d(TAG, "onNavigationItemSelected signout: " + item.getTitle());
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    public void goToManageAccount() {
        Intent intent = new Intent(home.this, register.class);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void goToCalendar() {
        Intent intent = new Intent(home.this, Manage_calendar.class);
        startActivity(intent);
    }

    public void goToManageFriend() {
        Intent intent = new Intent(home.this, addFriends.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        startActivity(intent);
    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.revokeAccess();
        Intent intent = new Intent(home.this, MainActivity.class);
        startActivity(intent);
    }

    public void getUser() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sEmail=" + email;//รอเอาIdหรือ email จากfirebase
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
//                            Log.d("footer", MyArrList.get(0).get("user_id"));
//                            Log.d("footer", MyArrList.get(0).get("user_names"));
//                            Log.d("footer", MyArrList.get(0).get("user_email"));
                            hName.setText(MyArrList.get(0).get("user_names"));
                            name = MyArrList.get(0).get("user_names");
                            id = MyArrList.get(0).get("user_id");
                            hEmail.setText(MyArrList.get(0).get("user_email"));
                            writeFile(id,name,email);
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
        Log.d("footer", "id" + id);
        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
        url += "?sId=" + id;//รอเอาIdจากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText("Response is: "+ response.toString());

                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("events_id", c.getString("events_id"));
                                map.put("event_creater", c.getString("event_creater"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                //map.put("events_wait", c.getString("events_wait"));
                                MyArrList.add(map);
                            }
                            Log.d("query", MyArrList.size() + "");
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
//        LayoutInflater inflater = getLayoutInflater();
//        final LinearLayout listInviteView = (LinearLayout) inflater.inflate(R.layout.activity_btnfooter_collapse, listViewInvite,false);


        ImageButton btnget = findViewById(R.id.btn_notification);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invite) {
                    listViewInvite.setVisibility(View.GONE);//ปิด
                    listViewInvite.removeAllViewsInLayout();
//                    listViewInvite.removeFooterView(listInviteView);
                    invite = false;
                } else {
                    invite = true;
                    listViewInvite.setVisibility(View.VISIBLE);
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_invitation_home,
                            new String[]{"event_creater", "events_name", "events_month_start", "events_month_end"}, new int[]{R.id.col_head, R.id.col_name_header, R.id.col_time, R.id.col_time_end});
                    listViewInvite.setAdapter(sAdap);
//                    listViewInvite.addFooterView(listInviteView);
//                    listInviteView.findViewById(R.id.btnCollapse).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.d("footer","btnhideInvite");
//                            listViewInvite.setVisibility(View.GONE);
//                            listViewInvite.removeFooterView(listInviteView);
//                            invite=false;
//                        }
//                    });
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
        url += "?sId=" + id;//รอเอาIdจากfirebase
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
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_name", c.getString("states_name"));
                                MyArrList.add(map);
                            }
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
                    listViewHeader.setVisibility(View.GONE);//ปิด
                    listViewHeader.removeAllViewsInLayout();

                    head = false;
                } else {
                    head = true;
                    listViewHeader.setVisibility(View.VISIBLE);//เปิด
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(home.this, MyArrList, R.layout.activity_header_home,
                            new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_header, R.id.col_status_header});
                    listViewHeader.setAdapter(sAdap);
                    final AlertDialog.Builder viewDetail = new AlertDialog.Builder(home.this);
                    listViewHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                            // เข้าสู่ event
                            String eName= MyArrList.get(position).get("events_name");
                            String eId= MyArrList.get(position).get("events_id");
                            String eStatus= MyArrList.get(position).get("states_name");
                            Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
                            Intent intent = new Intent(home.this,appointment.class);
                            intent.putExtra("id",id);
                            intent.putExtra("eid",eId);
                            intent.putExtra("nameEvent",eName);

                            startActivity(intent);
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
        url += "?sId=" + id;//รอเอาIdจากfirebase
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
                                map.put("events_id", c.getString("events_id"));
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
                    listViewAttend.removeAllViewsInLayout();
                    attend = false;
                } else {
//                    listViewAttend.removeFooterView(listAttendView);
                    Log.d("footer", "showAttend");
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
//                            Intent intent = new Intent(home.this,appointment.class);
//                            Object list =  myAdapter.getItemIdAtPosition(position);

                           String eName= MyArrList.get(position).get("events_name");
                            String eId= MyArrList.get(position).get("events_id");
                            String eStatus= MyArrList.get(position).get("states_name");
                            Log.d("footer","id "+eId +"/ name "+eName+"/ status "+ eStatus);
//                            intent.putExtra("nameEvent",li);
//                            intent.putExtra("mStart",mStart);
//                            intent.putExtra("mEnd",mEnd);
//                            startActivity(intent);
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

//    public void btncloseFooter(final ListView listView){
//        //add in if
////        listViewHeader.removeFooterView(listHeaderView);
//        //call after set adapter
//        final LayoutInflater inflater = getLayoutInflater();
//        final LinearLayout listHeaderView = (LinearLayout) inflater.inflate(R.layout.activity_btnfooter_collapse, listView,false);
//        listView.addFooterView(listHeaderView);
//        listHeaderView.findViewById(R.id.btnCollapse).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("footer","btnhideHead");
//                listView.setVisibility(View.GONE);
//                listView.removeFooterView(listHeaderView);
//                head=false;
//            }
//        });
//    }

    public void writeFile(String id,String name,String email) {
        String filename = "user.txt";
        String sid = id+ ":";
        String sname = name + ":";
        String semail = email + "\n";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(sid.getBytes());
            outputStream.write(sname.getBytes());
            outputStream.write(semail.getBytes());
            outputStream.close();
            Log.d("footer","write file : id "+sid +"/ name "+sname+"/ status "+ semail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readFile(){
        String filename = "user.txt";
        try {
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(openFileInput(filename)));

            ArrayList<String> his = new ArrayList<>();
            String line = "";
            while ((line = inputReader.readLine()) != null) {
                his.add(line);
            }
            for (int i = 0; i < his.size(); i++) {
                StringTokenizer st = new StringTokenizer(his.get(i), ":");
                id = st.nextToken();
                name = st.nextToken();
                email = st.nextToken();
            }
            Log.d("footer","read file : id "+id +"/ name "+name+"/ status "+ email);
            hName.setText(name);
            hEmail.setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
