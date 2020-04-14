package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class ManageFriend extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        boolean checked;
        ImageView ItemDrawable;
        String ItemString;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String t, boolean b) {
//            ItemDrawable = drawable;
            ItemString = t;
            checked = b;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
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

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_showfriend, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextViewName);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

//        viewHolder.icon.setImageBitmap(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            /*
            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).checked = b;

                    Toast.makeText(getApplicationContext(),
                            itemStr + "onCheckedChanged\nchecked: " + b,
                            Toast.LENGTH_LONG).show();
                }
            });
            */

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                    Toast.makeText(context.getApplicationContext(),
                            itemStr + "setOnClickListener\nchecked: " + newState,
                            Toast.LENGTH_LONG).show();
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }
//***********************************************************************************************//

    ManageFriend.ResponseStr responseStr = new ManageFriend.ResponseStr();
    String uid = "", email = "";
    ListView listViewFriend;
    List<Item> items;
    ArrayList<HashMap<String, String>> frientArray;
    ItemsListAdapter myItemsListAdapter;
    Spinner spinnerType;
    ArrayList<String> typefriend;
    ArrayAdapter<String> dataAdapter;
    ImageButton addTypeFriend;
    ProgressDialog progressDialog;

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
        getFriend();
        getType();

        addTypeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog viewDetail = new AlertDialog.Builder(ManageFriend.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_addtype_dialog,null);
                final EditText mNameType = mView.findViewById(R.id.addNameType);
                Button btn_confirmType = mView.findViewById(R.id.btn_ConfirmAddType);
                btn_confirmType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mNameType.getText().toString().isEmpty()){
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

                                    AddTypeFriend(mNameType.getText().toString());
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
                initItems();
                click();

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(dataAdapter);
                spinnerType.setSelection(0);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        getFriendType(selectedItemText);//get ได้แล้ว เหลือหาวิธีให้แสดง

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

    public void getFriend() {
        responseStr = new ManageFriend.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getFriend.php";
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
        new CountDownTimer(500, 500) {
            public void onFinish() {
                // When timer is finished
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
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

    private void initItems() {
        items = new ArrayList<Item>();
        for (int i = 0; i < frientArray.size(); i++) {
            String s = frientArray.get(i).get("friend_name");
            boolean b = false;
            Item item = new Item(s, b);
            items.add(item);
        }
    }

    public void click() {
        myItemsListAdapter = new ItemsListAdapter(this, items);
        listViewFriend.setAdapter(myItemsListAdapter);
        Log.d("listA", items.toString());
        Log.d("listA", "size" + myItemsListAdapter.getCount() + "");
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ManageFriend.this,
                        ((Item) (parent.getItemAtPosition(position))).ItemString,
                        Toast.LENGTH_LONG).show();
            }
        });
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

    public void getFriendType(String typeName) {
        Log.d("position", "uid " + uid + " typeName " + typeName);
        responseStr = new ManageFriend.ResponseStr();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> positionId = new ArrayList<>();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getfriendIntype.php";
        url += "?sId=" + uid;
        url += "&tname=" + typeName;
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
                                MyArrList.add(map);
                                frientArray.add(map);
                            }
                            Log.d("position", "MyArrList " + MyArrList.size() + "");
                            for (int i = 0; i < MyArrList.size(); i++) {
                                position.add(MyArrList.get(i).get("friend_name"));
                                positionId.add(MyArrList.get(i).get("fid"));
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
//                checkBoxClick(position,positionId);
            }
        }.start();

    }
}
