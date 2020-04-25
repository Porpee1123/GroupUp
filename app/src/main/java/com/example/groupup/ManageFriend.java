package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;
import java.util.Locale;


public class ManageFriend extends AppCompatActivity {

    //*******************************TextView with checkbox******************************************//
    public class Item {
        String ItemDrawable;
        String ItemString;
        String Itememail;
        String Id;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String t, String i, String drawable,String email) {
            ItemDrawable = drawable;
            ItemString = t;
            Itememail = email;
            Id = i;
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {
        private ArrayList<Item> arraylist;
        private Context context;
        private List<ManageFriend.Item> list;

        ItemsListAdapter(Context c, List<ManageFriend.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<Item>();
            arraylist.addAll(l);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ManageFriend.ViewHolder viewHolder = new ManageFriend.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_showfriendnocheckbox, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder.text = rowView.findViewById(R.id.rowTextViewName);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ManageFriend.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            return rowView;
        }
        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(arraylist);
            } else {
                for (Item wp : arraylist) {
                    if (wp.ItemString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }



    //***********************************************************************************************//

    ManageFriend.ResponseStr responseStr = new ManageFriend.ResponseStr();
    String uid = "", email = "",typeChange ;
    static ListView listViewFriend;
    ArrayList<HashMap<String, String>> frientArray;
    Spinner spinnerType;
    ArrayList<String> typefriend;
    ArrayList<String> typefriendId;
    ArrayAdapter<String> dataAdapter;
    ImageButton addTypeFriend;
    ProgressDialog progressDialog;
    List<ManageFriend.Item> items;
    EditText searchText;

    static ManageFriend.ItemsListAdapter myItemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_manage_friend);
        listViewFriend = findViewById(R.id.listview_friend);
        searchText = findViewById(R.id.searchText);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        frientArray = new ArrayList<>();
        spinnerType = findViewById(R.id.spinner_type);
        typefriend = new ArrayList<>();
        typefriendId = new ArrayList<>();
        addTypeFriend = findViewById(R.id.btn_addTypeFriend);
        Log.d("listA", "idA " + uid);
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ManageFriend.this, "Friend is Dowloading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                new CountDownTimer(300, 300) {
                    public void onFinish() {

                        progressDialog.dismiss();
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }

            @Override
            protected String doInBackground(Void... params) {
                getType();
                getFriend();
                initItems();
                return "Finish";
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
        addTypeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_addtype_dialog, null);
                final EditText mNameType = mView.findViewById(R.id.addNameType);
                Button btn_confirmType = mView.findViewById(R.id.btn_ConfirmAddType);

                btn_confirmType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String mName = mNameType.getText().toString();
                        if (!mName.isEmpty()) {
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
                        } else {
                            Toast.makeText(ManageFriend.this, R.string.err_nametype, Toast.LENGTH_SHORT).show();
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
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
                spinnerType.setSelection(0);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        typeChange = selectedItemText;
                        Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        if (selectedItemText.equals("ALL")) {
                            showAllCheckboxClick();
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
        search();
    }

    public void AddTypeFriend(String name) {
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
        String filename = "MangeFriend" + id + "userFriend.txt";
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

    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
    }

    public void checkBoxClick(ArrayList position, ArrayList positionId, ArrayList positionImage,ArrayList positionEmail) {
        Log.d("position", "position size: " + position.size() + "");
        items = new ArrayList<ManageFriend.Item>();
        for (int i = 0; i < position.size(); i++) {
            String s = position.get(i).toString();
            String id = positionId.get(i).toString();
            String path = positionImage.get(i).toString();
            String email = positionEmail.get(i).toString();
            ManageFriend.Item item = new ManageFriend.Item(s, id, path,email);
            items.add(item);
        }
        myItemsListAdapter = new ManageFriend.ItemsListAdapter(this, items);

        listViewFriend.setAdapter(myItemsListAdapter);
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_deletefriend_dialog, null);
                final TextView mName = mView.findViewById(R.id.nameHeadDel);
                final TextView mEmail = mView.findViewById(R.id.emailFriend);
                final ImageView imgF = mView.findViewById(R.id.imageViewDelFriend);
                final String fid = items.get(position).Id;
                final String fname = items.get(position).ItemString;
                final String femail = items.get(position).Itememail;
                final String path = items.get(position).ItemDrawable;
                Log.d("pathimage","path "+path);
                new Extend_MyHelper.SendHttpRequestTask(path,imgF,250).execute();
                mName.setText(fname);
                mEmail.setText(femail);
                Button btn_confirmDel = mView.findViewById(R.id.btn_delFriend);
                Button btn_confirmDelAll = mView.findViewById(R.id.btn_delAllFriend);
                Button btn_cancle = mView.findViewById(R.id.btn_cancleDel);
                Log.d("delete",fid +" : "+ fname+" : "+fname);
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
                                if (typeChange.equals("ALL")) {
                                    deleteFriendAll(fid);
                                    Log.d("delete",fid );
                                } else {
                                    deleteFriendInType(fid, typeChange);
                                    Log.d("delete",fid +" : "+ typeChange);
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
            }
        });

    }

    public void setItemsListView() {
        myItemsListAdapter = new ManageFriend.ItemsListAdapter(this, items);
        listViewFriend.setAdapter(myItemsListAdapter);
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_deletefriend_dialog, null);
                final TextView mName = mView.findViewById(R.id.nameHeadDel);
                final TextView mEmail = mView.findViewById(R.id.emailFriend);
                final ImageView imgF = mView.findViewById(R.id.imageViewDelFriend);
                final String fid = frientArray.get(position).get("fid");
                final String fname = frientArray.get(position).get("friend_name");
                final String femail = frientArray.get(position).get("friend_email");
                final String path = frientArray.get(position).get("user_photo");
                Log.d("pathimage","path "+path);
                new Extend_MyHelper.SendHttpRequestTask(path,imgF,250).execute();
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
                                Log.d("typeChange",typeChange);
                                if (typeChange.equals("ALL")) {
                                    deleteFriendAll(fid);
                                } else {
                                    deleteFriendInType(fid, typeChange);
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
            }
        });

        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
    }

    private void initItems() {
        items = new ArrayList<ManageFriend.Item>();
        for (int i = 0; i < frientArray.size(); i++) {
            String s = frientArray.get(i).get("friend_name");
            String id = frientArray.get(i).get("fid");
            String path = frientArray.get(i).get("user_photo");
            String email = frientArray.get(i).get("friend_email");


            ManageFriend.Item item = new ManageFriend.Item(s, id, path,email);
            items.add(item);
        }
    }

    public void deleteFriendAll(String fid) {

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

    public void deleteFriendInType(String fid, String type) {
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

    public void search() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                myItemsListAdapter.filter(text);
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

    public void getFriend() {
        responseStr = new ManageFriend.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> positionId = new ArrayList<>();
        final ArrayList<String> positionImage = new ArrayList<>();
        String url = "http://www.groupupdb.com/android/getFriend.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
        Log.d("position", "stringRequest  " + url);
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
                                map.put("afid", c.getString("afid"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("friend_name", c.getString("friend_name"));
                                map.put("friend_email", c.getString("friend_email"));
                                map.put("type_name", c.getString("type_name"));
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("fid", c.getString("fid"));

                                MyArrList.add(map);
                                frientArray.add(map);
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
        showAllCheckboxClick();

    }

    public void getFriendType(String typeName) {
        responseStr = new ManageFriend.ResponseStr();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> positionId = new ArrayList<>();
        final ArrayList<String> positionImage = new ArrayList<>();
        final ArrayList<String> positionEmail = new ArrayList<>();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getfriendIntype.php";
        url += "?sId=" + uid;
        url += "&tname=" + typeName;
        Log.d("position", "stringRequest  " + url);
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
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("friend_email", c.getString("friend_email"));
                                MyArrList.add(map);
                            }
                            Log.d("position", MyArrList.size() + "");
                            for (int i = 0; i < MyArrList.size(); i++) {
                                position.add(MyArrList.get(i).get("friend_name"));
                                positionId.add(MyArrList.get(i).get("fid"));
                                positionImage.add(MyArrList.get(i).get("user_photo"));
                                positionEmail.add(MyArrList.get(i).get("friend_email"));
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
        new CountDownTimer(300, 300) {
            public void onFinish() {
                checkBoxClick(position, positionId, positionImage,positionEmail);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

}
