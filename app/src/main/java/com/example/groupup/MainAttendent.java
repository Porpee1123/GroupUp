package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainAttendent extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//

    public class Item2 {
        String ItemDrawable;
        String ItemString;
        String Id;
        String ItemStatus;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String t, String i, String drawable, String Status) {
            ItemDrawable = drawable;
            ItemString = t;
            Id = i;
            ItemStatus = Status;
        }

    }

    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        ImageView Status;
    }

    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<MainAttendent.Item2> arraylist2;
        private Context context;
        private List<MainAttendent.Item2> list2;

        ItemsListAdapter2(Context c, List<MainAttendent.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<MainAttendent.Item2>();
            arraylist2.addAll(l);
        }

        @Override
        public int getCount() {
            return list2.size();
        }

        @Override
        public Object getItem(int position) {
            return list2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            MainAttendent.ViewHolder2 viewHolder2 = new MainAttendent.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_membernodelete, null);
                viewHolder2.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder2.text = rowView.findViewById(R.id.rowTextViewName);
                viewHolder2.Status = rowView.findViewById(R.id.rowimageStatus);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (MainAttendent.ViewHolder2) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();
            final String itemStr = list2.get(position).ItemString;
            viewHolder2.text.setText(itemStr);
            int state = Integer.parseInt(list2.get(position).ItemStatus);
            if (state != 2) {
                viewHolder2.Status.setImageResource(R.drawable.ic_tick);
            }
            return rowView;
        }
    }


    //***********************************************************************************************//
    String nEvent = "", id = "", eid = "", email = "", state = "";
    List<MainAttendent.Item2> items2 = new ArrayList<MainAttendent.Item2>();
    static MainAttendent.ItemsListAdapter2 myItemsListAdapter2;
    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    TextView nHead;
    ImageButton btn_note,btn_friend;
    String note;
    int tab = 0;
    TextView tv_maga;
    ImageView img_mega;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>>  memberArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        nHead = findViewById(R.id.inviteFriends);
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        tab = Integer.parseInt(getIntent().getStringExtra("tab"));
        nHead.setText(nEvent);
        memberArray = new ArrayList<>();
        tabHost = (TabHost) findViewById(R.id.tabhost);
        btn_note = findViewById(R.id.btn_noteAttend);
        btn_friend =findViewById(R.id.btn_seeFriend);
        tv_maga = findViewById(R.id.tv_Megaphone);
        img_mega = findViewById(R.id.img_Megaphone);
        tabHost.setup(mLocalActivityManager);
        checkvisibleButton(id, eid, "3");
        getEvent();
        Intent intentV = new Intent(this, MainAttendent_Vote.class);
        intentV.putExtra("id", id + "");
        intentV.putExtra("email", email + "");
        intentV.putExtra("nameEvent", nEvent + "");
        intentV.putExtra("eid", eid + "");
        Intent intentS = new Intent(this, MainAttendent_Summary.class);
        intentS.putExtra("id", id + "");
        intentS.putExtra("email", email + "");
        intentS.putExtra("nameEvent", nEvent + "");
        intentS.putExtra("eid", eid + "");
//        Intent intentR = new Intent(this,MainAttendent_Reviews.class);
//        intentR.putExtra("id", id+"");
//        intentR.putExtra("email", email+"");
//        intentR.putExtra("nameEvent", nEvent+"");
//        intentR.putExtra("eid", eid+"");

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("การโหวต")
                .setContent(intentV);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("สรุปงาน")
                .setContent(intentS);
//        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
//                .setIndicator("รีวิว")
//                .setContent(intentR);
        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);
//        tabHost.addTab(tabSpec3);
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
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(MainAttendent.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_showmember, null);
                ListView list = mView.findViewById(R.id.list_ShowMember);
                TextView numPeople = mView.findViewById(R.id.shownum_people);
                ImageButton btnClose = mView.findViewById(R.id.showbutton_btnClose);
                getMemberShow(list, eid, numPeople);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewDetail.dismiss();
                    }
                });
                viewDetail.setView(mView);
                viewDetail.show();
            }
        });
        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog viewNote = new AlertDialog.Builder(MainAttendent.this).create();
                View mViewnote = getLayoutInflater().inflate(R.layout.layout_addnote_dialog, null);
                final EditText mNameType = mViewnote.findViewById(R.id.note_edit);

                if (note.equals("") || note.equals("null")) {
                    mNameType.setText("");
                } else {
                    mNameType.setText(note);
                }
                Button btn_confirmType = mViewnote.findViewById(R.id.btn_ConfirmAddNote);
                btn_confirmType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String noteText = mNameType.getText().toString();
                        Toast.makeText(MainAttendent.this, noteText, Toast.LENGTH_SHORT).show();
                        if (!noteText.isEmpty()) {
                            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = ProgressDialog.show(MainAttendent.this, "กำลังอัปโหลดโน๊ต", "กรุณารอซักครู่", false, false);
                                }

                                @Override
                                protected void onPostExecute(String string1) {
                                    super.onPostExecute(string1);
                                    progressDialog.dismiss();
                                    startActivity(getIntent());
                                    Toast.makeText(MainAttendent.this, string1, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                protected String doInBackground(Void... params) {

                                    addNoteToDB(noteText);
                                    return "Add successful!!!";
                                }
                            }
                            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                            AsyncTaskUploadClassOBJ.execute();
                        } else {
                            Toast.makeText(MainAttendent.this, "Note is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewNote.setView(mViewnote);
                viewNote.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        id = getIntent().getStringExtra("id");
        nEvent = getIntent().getStringExtra("nameEvent");
        eid = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        nHead.setText(nEvent);
        tabHost.setCurrentTab(tab);
    }

    public void backHome(View v) {
        Intent intent = new Intent(MainAttendent.this, Home.class);
        intent.putExtra("email", email + "");
        startActivity(intent);
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

    public void addNoteToDB(String note) {
        Log.d("detail", note);
        String url = "http://www.groupupdb.com/android/addNoteEvent.php";
        url += "?eid=" + eid;
        url += "&nt=" + note;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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

    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + id;//ร  อเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
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
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("events_detail", c.getString("events_detail"));
                                map.put("events_note", c.getString("events_note"));
                                MyArrList.add(map);
                            }
                            note = MyArrList.get(0).get("events_note");
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

    public void checkStatususer(String eId, String uid, String pri) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getStatusAppoint.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        url += "&uId=" + uid;
        url += "&prId=" + pri;
        Log.d("checkStatus", "url " + url);
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
                                map.put("states_name", c.getString("states_name"));
                                MyArrList.add(map);
                            }
                            state = MyArrList.get(0).get("states_name");
                            if (state.equalsIgnoreCase("เลือกวันที่") || state.equalsIgnoreCase("เลือกสถานที่")) {
                                tabHost.setCurrentTab(1);
                            } else {
                                tabHost.setCurrentTab(0);
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

    public void checkvisibleButton(String uid, String eid, String pid) {
        Log.d("checktrans", "id : " + uid + " eid : " + eid + " pid : " + pid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gettransid.php";
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
                            String state = MyArrList.get(0).get("states_id");
                            int stateId = Integer.parseInt(state);
                            Log.d("checktrans", "state  Mega " + state);
                            if (stateId == 6 || stateId == 9 || stateId == 11 || stateId == 14 || stateId == 17 || stateId == 18) {
                                tv_maga.setVisibility(View.GONE);
                                img_mega.setVisibility(View.GONE);
                            } else {
                                tv_maga.setVisibility(View.VISIBLE);
                                img_mega.setVisibility(View.VISIBLE);
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
    public void getMemberShow(final ListView list, String eid, final TextView tv) {
        memberArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getmemberforalert.php";
        url += "?eId=" + eid;
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
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("user_email", c.getString("user_email"));
                                MyArrList.add(map);
                                memberArray.add(map);
                            }
                            initItems2(list);
                            tv.setText("จำนวนสมาชิกปัจจุบัน " + memberArray.size() + " คน");
                            Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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
    private void initItems2(ListView list) {
        items2 = new ArrayList<MainAttendent.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String uid = memberArray.get(i).get("user_id").toString();
            String uName = memberArray.get(i).get("user_names").toString();
            String uEmail = memberArray.get(i).get("user_email").toString();
            String uPhoto = memberArray.get(i).get("user_photo").toString();
            String sId = memberArray.get(i).get("states_id").toString();
            MainAttendent.Item2 item2 = new MainAttendent.Item2(uName, uid, uPhoto, sId);
            items2.add(item2);
        }
        myItemsListAdapter2 = new MainAttendent.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
}
