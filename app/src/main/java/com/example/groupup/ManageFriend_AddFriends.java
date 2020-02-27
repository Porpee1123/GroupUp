package com.example.groupup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ManageFriend_AddFriends extends AppCompatActivity {
    EditText searchFriend;
    TextView nameFriend, txtNoUser;
    ImageButton searchBtn, qrcodeBtn;
    LinearLayout showFriend, showType;
    Button btnAddFriend;
    Spinner spTypeFriend;
    ManageFriend_AddFriends.ResponseStr responseStr = new ManageFriend_AddFriends.ResponseStr();
    String TAG = "addfriend", uid = "",email="",emailScan="";
    String fid="",tfid="";
    SparseArray<String> type = new SparseArray<>();
    ArrayList<String> arSt;



    int MY_PERMISSIONS_REQUEST_CAMERA=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        searchFriend = findViewById(R.id.searchEmail);
        nameFriend = findViewById(R.id.nameFriend);
        searchBtn = findViewById(R.id.searchBtn);
        qrcodeBtn = findViewById(R.id.searchQrcodebtn);
        showFriend = findViewById(R.id.show_friend);
        txtNoUser = findViewById(R.id.txt_notfound);
        showType = findViewById(R.id.show_type);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        spTypeFriend = findViewById(R.id.typeFriend);
        arSt = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        emailScan = getIntent().getStringExtra("emailScan");
        Log.d(TAG,"emailScan : "+emailScan);
        searchFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if (actionId== EditorInfo.IME_ACTION_SEND){
                    closeKeyboard();
                    getUser();
                    getType();
                    handle=true;
                }
                return handle;
            }
        });
        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ManageFriend_AddFriends.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ManageFriend_AddFriends.this, "You have already permission access Camera", Toast.LENGTH_SHORT).show();
                    scanQr();
                } else {
                    if (requestImagePermission()){
//                        scanQr();
                    }

                }
            }
        });
        if (emailScan!=null){
            getUserByQrcode(emailScan);
            getType();
        }
    }

    public void backHome(View v) {
        Intent in = new Intent(this, ManageFriend.class);
        startActivity(in);
    }
    public void getUser() {
        responseStr = new ManageFriend_AddFriends.ResponseStr();
        final String[] user = {""};
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d(TAG, "email " + searchFriend.getText());
        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sEmail=" + searchFriend.getText();//รอเอาIdหรือ email จากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response);
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
                            Log.d(TAG, "data " + MyArrList.get(0).get("user_names"));
                            user[0] = MyArrList.get(0).get("user_name");
                            fid = MyArrList.get(0).get("user_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (user[0] != "") {
                            nameFriend.setText(MyArrList.get(0).get("user_names"));
                            showFriend.setVisibility(View.VISIBLE);
                            showType.setVisibility(View.VISIBLE);
                            txtNoUser.setVisibility(View.INVISIBLE);
                        } else {
                            txtNoUser.setVisibility(View.VISIBLE);
                            showFriend.setVisibility(View.INVISIBLE);
                            showType.setVisibility(View.INVISIBLE);
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
    public void getUserByQrcode(String s) {
        responseStr = new ManageFriend_AddFriends.ResponseStr();
        final String[] user = {""};
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d(TAG, "email " + s);
        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sEmail=" + s;//รอเอาIdหรือ email จากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response);
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
                            Log.d(TAG, "data " + MyArrList.get(0).get("user_names"));
                            user[0] = MyArrList.get(0).get("user_name");
                            fid = MyArrList.get(0).get("user_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (user[0] != "") {
                            nameFriend.setText(MyArrList.get(0).get("user_names"));
                            showFriend.setVisibility(View.VISIBLE);
                            showType.setVisibility(View.VISIBLE);
                            txtNoUser.setVisibility(View.INVISIBLE);
                        } else {
                            txtNoUser.setVisibility(View.VISIBLE);
                            showFriend.setVisibility(View.INVISIBLE);
                            showType.setVisibility(View.INVISIBLE);
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
    public void getType() {
        responseStr = new ManageFriend_AddFriends.ResponseStr();
        final String[] user = {""};
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d(TAG, "id " + uid);
        String url = "http://www.groupupdb.com/android/gettypefriend.php";
        url += "?sId=" + uid;//รอเอาIdหรือ email จากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("tfid", c.getString("tfid"));
                                map.put("type_name", c.getString("type_name"));
                                MyArrList.add(map);
                            }
                            //set Header menu name email
                            Log.d(TAG, "data " + MyArrList);
                            for (int i = 0; i < MyArrList.size(); i++) {
                                type.append(i,MyArrList.get(i).get("type_name"));
                            }
                            final Extend_SpinnerAdapter exSpin = new Extend_SpinnerAdapter(ManageFriend_AddFriends.this, type, "Plese select");
                            spTypeFriend.setAdapter(exSpin);

//                            spTypeFriend.set
                            btnAddFriend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "checkbox " + exSpin.getCheckedValues());
                                    arSt = cutString(exSpin.getCheckedValues());
                                    Log.d(TAG,arSt.toString());
                                    if (addFriendToDb()){
                                        exSpin.getCheckedValues();
                                        arSt = cutString(exSpin.getCheckedValues());
                                        for (int i=0;i<arSt.size();i++){
                                            addAllFriendToDb(arSt.get(i));
                                        }
                                        Intent in = new Intent(ManageFriend_AddFriends.this, Home.class);
                                        startActivity(in);
                                    }
                                }
                            });

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
    public void searchFriend(View v){
        closeKeyboard();
        getUser();
        getType();
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void closeKeyboard(){
        View v = this.getCurrentFocus();
        if (v!= null){
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
    public void scanQr(){
        Intent intent = new Intent(this, AddFriend_QRCode.class);
        intent.putExtra("email", email+"");
        startActivity(intent);
    }
    public boolean requestImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the camera")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ManageFriend_AddFriends.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }).create().show();
            return false;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return true;
        }
    }
    public boolean addFriendToDb() {
        final boolean[] re = {true};
        Log.d(TAG,"addFriendToDb : true");
        String url = "http://www.groupupdb.com/android/addfriend.php";
        url += "?sEmail=" + searchFriend.getText();
        url += "&sId=" +uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
//                                Toast.makeText(ManageFriend_AddFriends.this, "Cannot Add Friend to DB", Toast.LENGTH_SHORT).show();
                                re[0] =false;
                            } else {
//                                dialog.setTitle(R.string.submit_title);
//                                dialog.setMessage(R.string.submit_result);
//                                dialog.show();
//                                Toast.makeText(ManageFriend_AddFriends.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
                                searchFriend.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ManageFriend_AddFriends.this, "Submission Error!", Toast.LENGTH_LONG).show();
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
        return re[0];
    }
    public boolean addAllFriendToDb(String tfid) {
        final EditText txtEmail = findViewById(R.id.title);
        Log.d(TAG,"sid : "+uid +" ifs : "+fid+" tifs : "+tfid);
        String url = "http://www.groupupdb.com/android/addallfriend.php";
        url += "?sId=" + uid;
        url += "&fId=" +fid;
        url += "&tfId=" +tfid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                Toast.makeText(ManageFriend_AddFriends.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
//                                dialog.setTitle(R.string.submit_title);
//                                dialog.setMessage(R.string.submit_result);
//                                dialog.show();
                                searchFriend.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ManageFriend_AddFriends.this, "Submission Error!", Toast.LENGTH_LONG).show();
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
    public ArrayList cutString(String s){
        StringTokenizer st = new StringTokenizer(s,",");
        ArrayList<String> as = new ArrayList<>();
        while (st.hasMoreTokens()){
            as.add(st.nextToken());
        }
        return as;

    }

}

