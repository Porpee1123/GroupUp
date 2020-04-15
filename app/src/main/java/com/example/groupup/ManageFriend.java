package com.example.groupup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ManageFriend extends AppCompatActivity {

    ManageFriend.ResponseStr responseStr = new ManageFriend.ResponseStr();
    String uid = "", email = "";
    static ListView listViewFriend;
    ArrayList<HashMap<String, String>> frientArray;
    Spinner spinnerType;
    ArrayList<String> typefriend;
    ArrayAdapter<String> dataAdapter;
    ImageButton addTypeFriend;
    ProgressDialog progressDialog;
    static SimpleAdapter sAdapFriendType;
    LinearLayout lf ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_manage_friend);
        listViewFriend = findViewById(R.id.listview_friend);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        frientArray = new ArrayList<>();
        spinnerType = findViewById(R.id.spinner_type);
        typefriend = new ArrayList<>();
        addTypeFriend = findViewById(R.id.btn_addTypeFriend);
        Log.d("listA", "idA " + uid);
        getType();
        getFriendType("ALL");

        addTypeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_addtype_dialog,null);
                final EditText mNameType = mView.findViewById(R.id.addNameType);
                final String mName = mNameType.getText().toString();
                Button btn_confirmType = mView.findViewById(R.id.btn_ConfirmAddType);
                btn_confirmType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mName.isEmpty()){
                            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = ProgressDialog.show(ManageFriend.this, "Type name is Uploading", "Please Wait", false, false);
                                }

                                @Override
                                protected void onPostExecute(String string1) {
                                    super.onPostExecute(string1);
                                    progressDialog.dismiss();
                                    startActivity(getIntent());
                                    Toast.makeText(ManageFriend.this, string1, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                protected String doInBackground(Void... params) {

                                    AddTypeFriend(mName);
                                    return "Add successful!!!";
                                }
                            }
                            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                            AsyncTaskUploadClassOBJ.execute();
                        }else {
                            Toast.makeText(ManageFriend.this,R.string.err_nametype,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewDetail.setView(mView);
                viewDetail.show();

            }
        });
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typefriend);
        new CountDownTimer(500, 500) {
            public void onFinish() {
//                initItems();

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
                spinnerType.setSelection(0);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        if (selectedItemText.equals("ALL")) {
                            getFriendType(selectedItemText);
                        } else {

                            getFriendType(selectedItemText);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        writeFile(uid, email);
    }
    public void AddTypeFriend(String name){
        String url = "http://www.groupupdb.com/android/addtypefriend.php";
        url += "?sId=" + uid;
        url += "&nTy=" + name;
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void addFriend(View v) {
        Intent in = new Intent(this, ManageFriend_AddFriends.class);
        in.putExtra("id", uid + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void getType() {
        responseStr = new ManageFriend.ResponseStr();
        final String[] user = {""};
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
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
                            //set Header menu name email;
                            typefriend.add("ALL");

                            for (int i = 0; i < MyArrList.size(); i++) {
                                typefriend.add(MyArrList.get(i).get("type_name"));
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

    public void writeFile(String id, String email) {
        String filename = "userFriend.txt";
        String sid = id + ":";
        String semail = email + "\n";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(sid.getBytes());
            outputStream.write(semail.getBytes());
            outputStream.close();
            Log.d("AddFriend", "write file : id " + sid + " / status " + semail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFriendType(final String typeName) {
        Log.d("position", "uid " + uid + " typeName " + typeName);
        responseStr = new ManageFriend.ResponseStr();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> positionId = new ArrayList<>();
        final ArrayList<String> positionEmail = new ArrayList<>();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "";
        if (typeName.equals("ALL")) {
            url = "http://www.groupupdb.com/android/getFriend.php";
            url += "?sId=" + uid;//รอเอาIdจากfirebase
        }else {
            url = "http://www.groupupdb.com/android/getfriendIntype.php";
            url += "?sId=" + uid;
            url += "&tname=" + typeName;
        }
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
                                map.put("afid", c.getString("afid"));
                                map.put("fid", c.getString("fid"));
                                map.put("friend_name", c.getString("friend_name"));
                                map.put("friend_email", c.getString("friend_email"));

                                MyArrList.add(map);
                                frientArray.add(map);
                            }
                            Log.d("position", "MyArrList " + MyArrList.size() + "");
                            for (int i = 0; i < MyArrList.size(); i++) {
                                position.add(MyArrList.get(i).get("friend_name"));
                                positionId.add(MyArrList.get(i).get("fid"));
                                positionEmail.add(MyArrList.get(i).get("friend_email"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("position", "response " + response + "");
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
        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                sAdapFriendType = new SimpleAdapter(ManageFriend.this, MyArrList, R.layout.activity_showfriendnocheckbox,
                        new String[]{"friend_name"}, new int[]{R.id.rowTextViewName});
                listViewFriend.setAdapter(sAdapFriendType);
                listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        final AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                        View mView = getLayoutInflater().inflate(R.layout.layout_deletefriend_dialog,null);
                        final TextView mName = mView.findViewById(R.id.nameHeadDel);
                        final TextView mEmail = mView.findViewById(R.id.emailFriend);
                        final String fid =MyArrList.get(position).get("fid");
                        final String fname =MyArrList.get(position).get("friend_name");
                        final String femail =MyArrList.get(position).get("friend_email");
                        mName.setText(fname);
                        mEmail.setText(femail);
                        Button btn_confirmDel = mView.findViewById(R.id.btn_delFriend);
                        Button btn_confirmDelAll = mView.findViewById(R.id.btn_delAllFriend);
                        Button btn_cancle = mView.findViewById(R.id.btn_cancleDel);

                        btn_confirmDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            progressDialog = ProgressDialog.show(ManageFriend.this, "Deleting friend", "Please Wait", false, false);
                                        }

                                        @Override
                                        protected void onPostExecute(String string1) {
                                            super.onPostExecute(string1);
                                            startActivity(getIntent());
                                            progressDialog.dismiss();
                                            viewDetail.dismiss();
                                            Toast.makeText(ManageFriend.this, string1, Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        protected String doInBackground(Void... params) {
                                            if (typeName.equals("ALL")){
                                                deleteFriendAll(fid);
                                            }else{
                                                deleteFriendInType(fid,typeName);
                                            }

//                                            AddTypeFriend(mNameString);
                                            return "delete successful!!!";
                                        }
                                    }
                                    AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                                    AsyncTaskUploadClassOBJ.execute();
                            }
                        });
                        btn_confirmDelAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        progressDialog = ProgressDialog.show(ManageFriend.this, "Deleting All Friend", "Please Wait", false, false);
                                    }

                                    @Override
                                    protected void onPostExecute(String string1) {
                                        super.onPostExecute(string1);
                                        startActivity(getIntent());
                                        progressDialog.dismiss();
                                        viewDetail.dismiss();
                                        Toast.makeText(ManageFriend.this, string1, Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        deleteFriendAll(fid);
                                        return "delete successful!!!";
                                    }
                                }
                                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                                AsyncTaskUploadClassOBJ.execute();
                            }
                        });
                        btn_cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDetail.dismiss();
                            }
                        });

                        viewDetail.setView(mView);
                        viewDetail.show();

                    }});

            }
        }.start();

    }

    public void deleteFriendAll(String fid){

        responseStr = new ManageFriend.ResponseStr();
        String url = "http://www.groupupdb.com/android/delFriendAllType.php";
        url += "?sId=" + uid;
        url += "&fId=" + fid;
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
    public void deleteFriendInType(String fid,String type){
        responseStr = new ManageFriend.ResponseStr();
        String url = "http://www.groupupdb.com/android/delFriendInType.php";
        url += "?sId=" + uid;
        url += "&fId=" + fid;
        url += "&type=" + type;
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
}
