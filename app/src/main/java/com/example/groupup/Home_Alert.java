package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home_Alert extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        String ItemDrawable;
        String ItemName;
        String ItemEventName;
        String ItemStatus;
        String ItemEStart;
        String ItemEEnd;
        String Id;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String name, String eventname, String status, String start, String end, String i, String drawable) {
            ItemDrawable = drawable;
            ItemName = name;
            ItemEventName = eventname;
            ItemStatus = status;
            ItemEStart = start;
            ItemEEnd = end;
            Id = i;
        }

    }

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

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView nEvent;
        TextView status;
        TextView start;
        TextView end;
    }

    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        ImageView Status;
    }

    public class ItemsListAdapter extends BaseAdapter {
        private ArrayList<Home_Alert.Item> arraylist;
        private Context context;
        private List<Home_Alert.Item> list;

        ItemsListAdapter(Context c, List<Home_Alert.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<Home_Alert.Item>();
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
            Home_Alert.ViewHolder viewHolder = new Home_Alert.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_invitation_home, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageUser);
                viewHolder.name = rowView.findViewById(R.id.rowName);
                viewHolder.status = rowView.findViewById(R.id.rowNameStatus);
                viewHolder.nEvent = rowView.findViewById(R.id.rowNameEvent);
                viewHolder.start = rowView.findViewById(R.id.rowStartDate);
                viewHolder.end = rowView.findViewById(R.id.rowEndDate);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Home_Alert.ViewHolder) rowView.getTag();
            }
            if (list.get(position).ItemDrawable.equalsIgnoreCase("null") || list.get(position).ItemDrawable == null) {

            } else {
                new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();
            }

            final String itemName = list.get(position).ItemName;
            final String itemEvent = list.get(position).ItemEventName;
            final String itemStatus = list.get(position).ItemStatus;
            final String itemStart = list.get(position).ItemEStart;
            final String itemEnd = list.get(position).ItemEEnd;
            viewHolder.name.setText(itemName);
            viewHolder.nEvent.setText(itemEvent);
            viewHolder.status.setText(itemStatus);
            viewHolder.start.setText(itemStart);
            viewHolder.end.setText(itemEnd);


            return rowView;
        }
    }

    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<Home_Alert.Item2> arraylist2;
        private Context context;
        private List<Home_Alert.Item2> list2;

        ItemsListAdapter2(Context c, List<Home_Alert.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Home_Alert.Item2>();
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
            Home_Alert.ViewHolder2 viewHolder2 = new Home_Alert.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_membernodelete, null);
                viewHolder2.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder2.text = rowView.findViewById(R.id.rowTextViewName);
                viewHolder2.Status = rowView.findViewById(R.id.rowimageStatus);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Home_Alert.ViewHolder2) rowView.getTag();
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
    String uid = "", email = "";
    ListView listViewInvite;
    ArrayList<HashMap<String, String>> alertArray, memberArray;
    Home_Alert.ResponseStr responseStr = new Home_Alert.ResponseStr();
    List<Home_Alert.Item> items = new ArrayList<Home_Alert.Item>();
    List<Home_Alert.Item2> items2 = new ArrayList<Home_Alert.Item2>();
    String[] some_array;

    static Home_Alert.ItemsListAdapter myItemsListAdapter;
    static Home_Alert.ItemsListAdapter2 myItemsListAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_alert);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        listViewInvite = findViewById(R.id.listView_slipCheck);
        alertArray = new ArrayList<>();
        memberArray = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.month);
        getEventInvitation();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventInvitation();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        in.putExtra("email", email + "");
        in.putExtra("tab", 0 + "");
        startActivity(in);
//        addNotification();
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void addEventFriend(String id, String eid) {
        String url = "http://www.groupupdb.com/android/adduserevent.php";
        url += "?sId=" + id;
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updateeventfrienddb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
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
        myItemsListAdapter = new Home_Alert.ItemsListAdapter(this, items);
        listViewInvite.setAdapter(myItemsListAdapter);
        listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_showalert_dialog, null);
                final TextView headTitle = mView.findViewById(R.id.head_eve);
                final ImageButton backAlert = mView.findViewById(R.id.back_home);
                final TextView title = mView.findViewById(R.id.alt_title_Invite);
                final TextView detail = mView.findViewById(R.id.alt_detail_Invite);
                final ImageView imgF = mView.findViewById(R.id.alt_image_invite);
                final TextView nameE = mView.findViewById(R.id.alt_name_invite);
                final TextView startdate = mView.findViewById(R.id.rowStartDate);
                final TextView enddate = mView.findViewById(R.id.rowEndDate);
                final TextView state = mView.findViewById(R.id.alt_state_invite);
                final Button btn_seeMember = mView.findViewById(R.id.btn_seeMember);
                final String tid = alertArray.get(position).get("trans_id").toString();
                final String eid = alertArray.get(position).get("events_id").toString();
                final String ename = alertArray.get(position).get("events_name").toString();
                String sSta = alertArray.get(position).get("events_month_start").toString();
                String sEnd = alertArray.get(position).get("events_month_end").toString();
                final String sPrId = alertArray.get(position).get("pri_id").toString();
                String priName = alertArray.get(position).get("pri_name").toString();
                String event_creater = alertArray.get(position).get("event_creater").toString();
                String user_photo = alertArray.get(position).get("user_photo").toString();
                String events_detail = alertArray.get(position).get("events_detail").toString();
                headTitle.setText(ename);
                title.setText(ename);
                if (events_detail==null||events_detail.equalsIgnoreCase("null")){
                    detail.setText("-");
                }else{
                    detail.setText(events_detail);
                }
                nameE.setText(event_creater);
                startdate.setText(some_array[Integer.parseInt(sSta)]);
                enddate.setText(some_array[Integer.parseInt(sEnd)]);
                state.setText(priName);
                Log.d("pathimage", alertArray.toString());
                new Extend_MyHelper.SendHttpRequestTask(user_photo, imgF, 250).execute();
                Button btn_join = mView.findViewById(R.id.btn_invite_join);
                Button btn_notJoin = mView.findViewById(R.id.btn_invite_notjoin);
                Button btn_later = mView.findViewById(R.id.btn_invite_later);
                backAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewDetail.dismiss();
                    }
                });
                btn_seeMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();
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
                btn_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected void onPostExecute(String string1) {
                                super.onPostExecute(string1);
                                Intent intent = new Intent(Home_Alert.this, Home.class);
                                intent.putExtra("id", id + "");
                                intent.putExtra("email", email);
                                startActivity(intent);
                                viewDetail.dismiss();
                                Toast.makeText(Home_Alert.this, string1, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                checkStateEvent(eid,tid,Integer.parseInt(sPrId));
                                return "join successful!!!";
                            }
                        }
                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                        AsyncTaskUploadClassOBJ.execute();
                    }
                });
                btn_notJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected void onPostExecute(String string1) {
                                super.onPostExecute(string1);
                                viewDetail.dismiss();
                                startActivity(getIntent());
                                Toast.makeText(Home_Alert.this, string1, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                deletetransID(tid);

                                return "deete successful!!!";
                            }
                        }
                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                        AsyncTaskUploadClassOBJ.execute();
                    }
                });
                btn_later.setOnClickListener(new View.OnClickListener() {
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
        items = new ArrayList<Home_Alert.Item>();
        Log.d("pathimage", "alertArray " + alertArray.toString());
        for (int i = 0; i < alertArray.size(); i++) {

            String tid = alertArray.get(i).get("trans_id").toString();
            String eid = alertArray.get(i).get("events_id").toString();
            String ename = alertArray.get(i).get("events_name").toString();
            String sSta = alertArray.get(i).get("events_month_start").toString();
            String sEnd = alertArray.get(i).get("events_month_end").toString();
            String sPrId = alertArray.get(i).get("pri_id").toString();
            String priName = alertArray.get(i).get("pri_name").toString();
            String event_creater = alertArray.get(i).get("event_creater").toString();
            String user_photo = alertArray.get(i).get("user_photo").toString();
            String events_detail = alertArray.get(i).get("events_detail").toString();
            String events_image = alertArray.get(i).get("events_image").toString();

//            String mystring = getResources().getString(R.string.mystring);
//            String s ="คุณ "+ event_creater + "\nได้เชิญคุณเข้าร่วม : " + ename + "\nสถานะ : " + priName + " \nช่วงเวลา : " + some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)];

            Home_Alert.Item item = new Home_Alert.Item(event_creater, ename, priName, some_array[Integer.parseInt(sSta)], some_array[Integer.parseInt(sEnd)], eid, events_image);
            items.add(item);
        }
        Log.d("pathimage", items.toString());
    }

    public void getEventInvitation() {
        responseStr = new Home_Alert.ResponseStr();
        alertArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
        url += "?sId=" + uid;
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
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("pri_id", c.getString("pri_id"));
                                map.put("pri_name", c.getString("pri_name"));
                                map.put("event_creater", c.getString("event_creater"));
                                map.put("user_photo", c.getString("user_photo"));
                                map.put("events_detail", c.getString("events_detail"));
                                map.put("events_image", c.getString("events_image"));

                                MyArrList.add(map);
                                alertArray.add(map);

                            }
                            Log.d("pathimage", "get alertArray " + alertArray.toString());
                            Log.d("pathimage", "get MyArrList " + MyArrList.toString());
                            showAllCheckboxClick();
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

    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
    }

    public void deletetransID(String tid) {
        responseStr = new Home_Alert.ResponseStr();
        String url = "http://www.groupupdb.com/android/deletetransid.php";
        url += "?tId=" + tid;
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

    private void addDateevent(String eid) {
        String url = "http://www.groupupdb.com/android/addDatetodateEvent.php";
        url += "?eid=" + eid + "";
        url += "&uid=" + uid + "";
        Log.d("testAPi", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("testAPi", "addDaevent " + response);
                        Toast.makeText(Home_Alert.this, "addDaevent Finish", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(this,
//                                    error.getString(R.string.error_network_timeout),
//                                    Toast.LENGTH_LONG).show();
                            Log.d("testAPi", "TimeoutError" + error.getMessage());
//                            Log.d("testAPi",error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.d("testAPi", "AuthFailureError" + error.getMessage());
                        } else if (error instanceof ServerError) {
                            Log.d("testAPi", "ServerError" + error.getMessage());
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.d("testAPi", "NetworkError" + error.getMessage());
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                            Log.d("testAPi", "ParseError" + error.getMessage());
                        } else {
                            Log.d("testAPi", "else " + error.getMessage());
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(Home_Alert.this);
        queue.add(stringRequest);
    }

    public void getMemberShow(final ListView list, String eid, final TextView tv) {
        responseStr = new Home_Alert.ResponseStr();
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
                            Log.d("pathimage", "get alertArray " + alertArray.toString());
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
        items2 = new ArrayList<Home_Alert.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String uid = memberArray.get(i).get("user_id").toString();
            String uName = memberArray.get(i).get("user_names").toString();
            String uEmail = memberArray.get(i).get("user_email").toString();
            String uPhoto = memberArray.get(i).get("user_photo").toString();
            String sId = memberArray.get(i).get("states_id").toString();
            Home_Alert.Item2 item2 = new Home_Alert.Item2(uName, uid, uPhoto, sId);
            items2.add(item2);
        }
        myItemsListAdapter2 = new Home_Alert.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
    public void checkStateEvent(final String eId , final String  tid,final int sPrId ){
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/checkStateEvent.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        url += "&pri=" + sPrId;
        Log.d("checkStatus","checkStatus url "+url);
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
                                map.put("states_id", c.getString("states_id"));
                                MyArrList.add(map);
                            }
                          String  state = MyArrList.get(0).get("states_id");
                            if (sPrId == 2){
                                Extend_MyHelper.UpdateStateToDb(tid, state + "", Home_Alert.this);
                                Log.d("checkStatus","sPrId "+sPrId);
                            }else{
                                Log.d("checkStatus","sPrId else "+sPrId);
                                addEventFriend(uid, eId);
                                addDateevent(eId);
                                Extend_MyHelper.UpdateStateToDb(tid, state + "", Home_Alert.this);
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
