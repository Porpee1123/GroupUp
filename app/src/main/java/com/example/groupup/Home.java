package com.example.groupup;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.xeoh.android.texthighlighter.TextHighlighter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private GoogleSignInClient mGoogleSignInClient;
    ResponseStr responseStr = new ResponseStr();
    TextView hName;
    TextView hEmail,numNoti;
    ImageView img ;
    EditText searchText;
    static ArrayList<String> nameHead =new ArrayList<>();
    static ArrayList<String> nameAttend =new ArrayList<>();
    String name = "", id = "", email = "",image ="";
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    static ProgressDialog progressDialog;
//    TextHighlighter textHighlighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        Extend_MyHelper.deleteCache(this);
        setContentView(R.layout.activity_home);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
//        listViewInvite = new ListView(this);
//        listViewHeader = findViewById(R.id.listView_Header);
//        listViewAttend = findViewById(R.id.listView_attend);
        searchText = findViewById(R.id.searchText);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        numNoti =findViewById(R.id.badge_notification_2);
        navigationView.setNavigationItemSelectedListener(Home.this);
        navigationView.bringToFront();
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        img = v.findViewById(R.id.image_user);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("id");
        new Extend_MyHelper.SendHttpRequestTask(image,img,250).execute();
        progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        numNoti.setVisibility(View.INVISIBLE);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        deleteDateOldDay();
        getUser();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    getUser();
//
//                }
//            }).start();
//            new CountDownTimer(1000, 1000) {
//                public void onFinish() {
////                    createTab();
//                    search();
//                }
//                public void onTick(long millisUntilFinished) {
//                    // millisUntilFinished    The amount of time until finished.
//                }
//            }.start();
            //firebase signin
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            ImageButton btnNoti = findViewById(R.id.btn_notification);
            btnNoti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(Home.this, Home_Alert.class);
                    in.putExtra("id", id + "");
                    in.putExtra("email", email + "");
                    startActivity(in);
                }
            });

    }

    static Handler handlerHome = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
        }
    };

    public void createTab() {
        searchAttend();
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        Intent inA = new Intent(this, Home_Listview_Attendant.class);
        inA.putExtra("id", id + "");
        inA.putExtra("email", email + "");
        Intent inH = new Intent(this, Home_Listview_Head.class);
        inH.putExtra("id", id + "");
        inH.putExtra("email", email + "");
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("ผู้เข้าร่วมงาน")
                .setContent(inA);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("แม่งาน")
                .setContent(inH);
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
                searchHead();
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
            } else {

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
        email = getIntent().getStringExtra("email");
        Log.d("footer", "resume: " + email);
    }

    public void createGroup(View v) {
        Intent intent = new Intent(Home.this, Home_CreateEvent.class);
        intent.putExtra("id", id + "");
        intent.putExtra("name", name + "");
        intent.putExtra("email", email + "");
        startActivity(intent);
    }
    public void searchAttend() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                Home_Listview_Attendant.myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void searchHead() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                Home_Listview_Head.myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

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
                startActivity(getIntent());
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
        Intent intent = new Intent(Home.this, Manage_Account.class);
        intent.putExtra("email", email + "");
        intent.putExtra("name", name + "");
        intent.putExtra("photo", image + "");
        startActivity(intent);
    }

    public void goToCalendar() {
        Intent intent = new Intent(Home.this, Manage_calendar.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        startActivity(intent);
    }

    public void goToManageFriend() {
        Intent intent = new Intent(Home.this, ManageFriend.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        startActivity(intent);
    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.revokeAccess();
        Intent intent = new Intent(Home.this, Login.class);
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
                            Log.d("footer", MyArrList.get(0).get("user_id"));
                            Log.d("footer", MyArrList.get(0).get("user_names"));
                            Log.d("footer", MyArrList.get(0).get("user_email"));
                            Log.d("footer", MyArrList.get(0).get("user_photo"));
                            hName.setText(MyArrList.get(0).get("user_names"));
                            name = MyArrList.get(0).get("user_names");
                            id = MyArrList.get(0).get("user_id");
                            image = MyArrList.get(0).get("user_photo");
                            hEmail.setText(MyArrList.get(0).get("user_email"));
                            Log.d("imageview",image);
                            new Extend_MyHelper.SendHttpRequestTask(image,img,250).execute();
                            createTab();
                            getnumNotification();
//                            writeFile(id,name,email);
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
//        uploadData(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void writeFile(String id, String name, String email) {
        String filename = "user.txt";
        String sid = id + ":";
        String sname = name + ":";
        String semail = email + "\n";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(sid.getBytes());
            outputStream.write(sname.getBytes());
            outputStream.write(semail.getBytes());
            outputStream.close();
            Log.d("footer", "write file : id " + sid + "/ name " + sname + "/ status " + semail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
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
            Log.d("footer", "read file : id " + id + "/ name " + name + "/ status " + email);
            hName.setText(name);
            hEmail.setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void deleteDateOldDay() {
        responseStr = new ResponseStr();
        String url = "http://www.groupupdb.com/android/deleteDate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
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
    public void getnumNotification() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "id getnumNotification" + id);
        String url = "http://www.groupupdb.com/android/getnumnotification.php";
        url += "?uId=" + id;
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
                                map.put("num", c.getString("num"));
                               MyArrList.add(map);
                            }
                            //set Header menu name email
                            Log.d("footer", MyArrList.get(0).get("num"));
                            String numSn =MyArrList.get(0).get("num");
                            int numIn = Integer.parseInt(numSn);
                            if (numIn>0){
                                numNoti.setVisibility(View.VISIBLE);
                                numNoti.setText(numSn);
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
    }

}
