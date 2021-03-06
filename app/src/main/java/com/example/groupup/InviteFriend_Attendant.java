package com.example.groupup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.List;
import java.util.Locale;

public class InviteFriend_Attendant extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        boolean checked;
        String ItemDrawable;
        String ItemString;
        String Id;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String t, boolean b, String i, String drawable) {
            ItemDrawable = drawable;
            ItemString = t;
            checked = b;
            Id = i;
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
        private ArrayList<InviteFriend_Attendant.Item> arraylist;
        private Context context;
        private List<InviteFriend_Attendant.Item> list;

        ItemsListAdapter(Context c, List<InviteFriend_Attendant.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<InviteFriend_Attendant.Item>();
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

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            InviteFriend_Attendant.ViewHolder viewHolder = new InviteFriend_Attendant.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_showfriend, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextViewName);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (InviteFriend_Attendant.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();
//            viewHolder.icon.setImageBitmap(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
//                    Toast.makeText(context.getApplicationContext(),
//                            itemStr + "setOnClickListener\nchecked: " + newState,
//                            Toast.LENGTH_LONG).show();
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(arraylist);
            } else {
                for (InviteFriend_Attendant.Item wp : arraylist) {
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
    Button btnConfirmAttendant;
    View gab;
    String uid, eid, nameE, monS, monE, email;
    int countType = 0;
    static ListView listViewFriend;
    List<InviteFriend_Attendant.Item> items;
    ArrayList<HashMap<String, String>> frientArray;
    ArrayList<String> typefriend;
    ArrayList<String> typefriendId;
    ArrayList<String> friendInDb;
    InviteFriend_Attendant.ResponseStr responseStr = new InviteFriend_Attendant.ResponseStr();
    static InviteFriend_Attendant.ItemsListAdapter myItemsListAdapter;
    LinearLayout lShortcut;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_invite_attendant);
        // LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        // linearLayout.setBackgroundColor(Color.parseColor("#BCD0ED"));
        lShortcut = findViewById(R.id.layout_shortcut);
        typefriend = new ArrayList<>();
        typefriendId = new ArrayList<>();
        friendInDb = new ArrayList<>();
        btnConfirmAttendant = findViewById(R.id.slide);
        gab = findViewById(R.id.view_gab);
        listViewFriend = findViewById(R.id.listview_friend);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        frientArray = new ArrayList<>();
        Log.d("checkIntent",uid+" "+email+" "+eid+" ");
        getType();
        btnConfirmAttendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmFriend();
            }
        });
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showAllCheckboxClick();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void initItems() {
        items = new ArrayList<InviteFriend_Attendant.Item>();
        for (int i = 0; i < frientArray.size(); i++) {
            String s = frientArray.get(i).get("friend_name");
            String id = frientArray.get(i).get("fid");
            String path = frientArray.get(i).get("user_photo");
//            boolean b = checkFriendAlreadysent(id);
            boolean b = false;
            InviteFriend_Attendant.Item item = new InviteFriend_Attendant.Item(s, b, id, path);
            items.add(item);
        }
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void getFriend() {
        responseStr = new InviteFriend_Attendant.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/getFriend.php";
        url += "?sId=" + uid;
        Log.d("pathinvite",url);
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
                                initItems();
                                setItemsListView();
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

    public void setItemsListView() {
        myItemsListAdapter = new InviteFriend_Attendant.ItemsListAdapter(this, items);
        listViewFriend.setAdapter(myItemsListAdapter);
        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
    }

    public void confirmFriend() {
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(InviteFriend_Attendant.this, "กำลังอัปโหลดเพื่อน", "กรุณารอซักครู่", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                Extend_MyHelper.UpdateAllState(eid,"4","2",InviteFriend_Attendant.this);
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();
                InviteFriend.tabHost.setCurrentTab(1);
                // Printing uploading success message coming from server on android app.
                Toast.makeText(InviteFriend_Attendant.this, string1, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                String str = "Check items:\n";
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isChecked()) {
                        String fid = items.get(i).Id;
                        str += i + " " + items.get(i).ItemString + "-" + fid + "\n";
                        Log.d("friend", "item : " + items.get(i).ItemString + "");
                        sentInviteToFriend(fid, eid);
                    }
                }
                Log.d("friend", str);
                return "Finish";
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public void getType() {
        responseStr = new InviteFriend_Attendant.ResponseStr();
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
                            typefriend.add("ทั้งหมด");
                            typefriendId.add("0");
                            countType = MyArrList.size();
                            for (int i = 0; i < MyArrList.size(); i++) {
                                typefriend.add(MyArrList.get(i).get("type_name"));
                                typefriendId.add(MyArrList.get(i).get("tfid"));
                            }
                            shortCutAddFriend();
                            getFriend();
                            getFriendIDByTrans(uid, eid, 3 + "");
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

    public void shortCutAddFriend() {
        Log.d("friend", "countType : " + countType + "");
        for (int i = 0; i < typefriendId.size(); i++) {
            Log.d("friend", "i : " + i + "");
            final Button b = new Button(this);
            ImageView v = new ImageView(this);
            if (typefriendId.get(i).equals("0")) {
                b.setBackgroundResource(R.drawable.all_button);
            } else if (typefriendId.get(i).equals("1")) {
                b.setBackgroundResource(R.drawable.b1_button);
            } else if (typefriendId.get(i).equals("2")) {
                b.setBackgroundResource(R.drawable.b2_button);
            } else if (typefriendId.get(i).equals("3")) {
                b.setBackgroundResource(R.drawable.b3_button);
            } else if (typefriendId.get(i).equals("4")) {
                b.setBackgroundResource(R.drawable.b4_button);
            } else if (typefriendId.get(i).equals("5")) {
                b.setBackgroundResource(R.drawable.b5_button);
            } else {
                b.setBackgroundResource(R.drawable.b6_button);
            }
            b.setText(typefriend.get(i));
            b.setHeight(lShortcut.getHeight());
            v.setImageResource(R.drawable.viewgab);
            lShortcut.addView(b);
            lShortcut.addView(v);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("friend", "btn : " + b.getText() + "");
                    if (b.getText().equals("ทั้งหมด")) {
                        b.setAlpha((float) 0.5);
                        new CountDownTimer(200, 200) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                b.setAlpha(1);
                                lShortcut.removeAllViews();
                                showAllCheckboxClick();
                            }
                        }.start();
                    } else {
                        b.setAlpha((float) 0.5);
                        getFriendType(b.getText().toString());
                        new CountDownTimer(200, 200) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                b.setAlpha(1);
                            }
                        }.start();
                    }

                }
            });
        }
    }

    public void getFriendType(String typeName) {
        responseStr = new InviteFriend_Attendant.ResponseStr();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> positionId = new ArrayList<>();
        final ArrayList<String> positionImage = new ArrayList<>();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getfriendIntype.php";
        url += "?sId=" + uid;
        url += "&tname=" + typeName;
        Log.d("pathinvite",url);
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
                                MyArrList.add(map);
                            }
                            Log.d("position", MyArrList.size() + "");
                            for (int i = 0; i < MyArrList.size(); i++) {
                                position.add(MyArrList.get(i).get("friend_name"));
                                positionId.add(MyArrList.get(i).get("fid"));
                                positionImage.add(MyArrList.get(i).get("user_photo"));
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
                checkBoxClick(position, positionId,positionImage);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    public void checkBoxClick(ArrayList position, ArrayList positionId, ArrayList positionImage) {
        Log.d("position", "position size: " + position.size() + "");
        items = new ArrayList<InviteFriend_Attendant.Item>();
        for (int i = 0; i < position.size(); i++) {
            String s = position.get(i).toString();
            String id = positionId.get(i).toString();
            String path = positionImage.get(i).toString();
            boolean b = true;
            InviteFriend_Attendant.Item item = new InviteFriend_Attendant.Item(s, b, id, path);
            items.add(item);
        }
        myItemsListAdapter = new InviteFriend_Attendant.ItemsListAdapter(this, items);
        listViewFriend.setAdapter(myItemsListAdapter);
    }

    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
        shortCutAddFriend();
    }

    public void sentInviteToFriend(final String idInvite, final String idEvent) {
        String url = "http://www.groupupdb.com/android/addFriendInvitationAttend.php";
        url += "?sId=" + idInvite;
        url += "&sEid=" + idEvent;
        Log.d("footer", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Extend_MyHelper.sentInviteFCMPerson(idInvite,idEvent,"3","คำเชิญผู้เข้าร่วมงาน","คุณได้รับคำเชิญเป็นผู้เข้าร่วมงาน","OPEN_ACTIVITY_1",InviteFriend_Attendant.this);
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
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

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(InviteFriend_Attendant.this, "Submission Error!", Toast.LENGTH_LONG).show();
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

    public void getFriendIDByTrans(String uid, String eid, String pid) {
        responseStr = new InviteFriend_Attendant.ResponseStr();
        Log.d("friendselect", "id : " + uid + " eid : " + eid + " pid : " + pid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getfriendalreadyInvite.php";
        url += "?uId=" + uid;
        url += "&eId=" + eid;
        url += "&pId=" + pid;
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
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("pri_id", c.getString("pri_id"));
                                MyArrList.add(map);
                            }
                            for (int i = 0; i < MyArrList.size(); i++) {
                                friendInDb.add(MyArrList.get(i).get("user_id"));
                            }
                            Log.d("friendselect", "myarrAttend : " + friendInDb.toString());
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

//    public boolean checkFriendAlreadysent(String id) {
//        Log.d("friendselect", "id : " + id);
//        Log.d("friendselect", "friendInDb : " + friendInDb.toString());
//        for (int j = 0; j < friendInDb.size(); j++) {
//            String fdb = friendInDb.get(j);
//            if (fdb.equals(id)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    public void checkBoxClick(ArrayList position){    //show all
//        Log.d("position","position size: "+position.size()+"");
//        ArrayList<Boolean> statusAll  = new ArrayList<>();
//        ArrayList<String> nameClick  = new ArrayList<>();
//        items = new ArrayList<InviteFriend_Attendant.Item>();
//        for (int i=0;i<frientArray.size();i++){
//            String s =frientArray.get(i).get("friend_name");
//            nameClick.add(s);
//            boolean b= false;
//            statusAll.add(b);
//        }
//        for (int i =0 ;i<nameClick.size();i++){
//            for (int j=0;j<position.size();j++){
//                if (nameClick.get(i).equals(position.get(j))) {
//                    statusAll.set(i,true);
//                    Log.d("status","status : "+statusAll.get(i)+"");
//                }
//            }
//        }
//        for (int i=0;i<nameClick.size();i++){
//            InviteFriend_Attendant.Item item = new InviteFriend_Attendant.Item(nameClick.get(i), statusAll.get(i));
//            items.add(item);
//        }
//        myItemsListAdapter = new InviteFriend_Attendant.ItemsListAdapter(this, items);
//        listViewFriend.setAdapter(myItemsListAdapter);
//    }
}
