package com.example.groupup;

import android.Manifest;
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

public class ManageFriend_AddFriends extends AppCompatActivity {
    EditText searchFriend;
    TextView nameFriend, txtNoUser;
    ImageButton searchBtn, qrcodeBtn;
    LinearLayout showFriend, showType;
    Spinner spTypeFriend;
    ManageFriend_AddFriends.ResponseStr responseStr = new ManageFriend_AddFriends.ResponseStr();
    String TAG = "addfriend", uid = "",email="",emailScan="";
    SparseArray<String> type = new SparseArray<>();
    int MY_PERMISSIONS_REQUEST_CAMERA=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ManageFriend_AddFriends);
        searchFriend = findViewById(R.id.searchEmail);
        nameFriend = findViewById(R.id.nameFriend);
        searchBtn = findViewById(R.id.searchBtn);
        qrcodeBtn = findViewById(R.id.searchQrcodebtn);
        showFriend = findViewById(R.id.show_friend);
        txtNoUser = findViewById(R.id.txt_notfound);
        showType = findViewById(R.id.show_type);
        spTypeFriend = findViewById(R.id.typeFriend);
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
        Intent in = new Intent(this, Home.class);
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
                            spTypeFriend.setAdapter(new Extend_SpinnerAdapter(ManageFriend_AddFriends.this, type, "Plese select"));
//                            Log.d(TAG, "checkbox " + spTypeFriend.getId());
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
    public void addFriend(View v){
//        Log.d(TAG,"spGet"+ spTypeFriend.getSelectedItem());
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

}
