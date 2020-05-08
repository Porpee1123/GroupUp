package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.core.app.NotificationCompat;

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
        String ItemString;
        String Id;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String t, String i, String drawable) {
            ItemDrawable = drawable;
            ItemString = t;
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
        TextView text;
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
                viewHolder.icon = rowView.findViewById(R.id.row_img_Invite);
                viewHolder.text = rowView.findViewById(R.id.row_name_Invite);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Home_Alert.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

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
                rowView = inflater.inflate(R.layout.layout_member, null);
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
            if (state != 2){
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

//    public void getEventInvitation() {
//        responseStr = new Home_Alert.ResponseStr();
//
//        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "id" + id);
//        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
//        url += "?sId=" + id;//รอเอาIdจากfirebase
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //textView.setText("Response is: "+ response.toString());
//
//                        try {
//                            HashMap<String, String> map;
//                            JSONArray data = new JSONArray(response.toString());
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject c = data.getJSONObject(i);
//                                map = new HashMap<String, String>();
//                                map.put("trans_id", c.getString("trans_id"));
//                                map.put("events_id", c.getString("events_id"));
//                                map.put("events_name", c.getString("events_name"));
//                                map.put("events_month_start", c.getString("events_month_start"));
//                                map.put("events_month_end", c.getString("events_month_end"));
//                                map.put("pri_id", c.getString("pri_id"));
//                                map.put("pri_name", c.getString("pri_name"));
//                                map.put("event_creater", c.getString("event_creater"));
//                                map.put("user_photo", c.getString("user_photo"));
//                                map.put("events_detail", c.getString("events_detail"));
//
//                                //map.put("events_wait", c.getString("events_wait"));
//                                MyArrList.add(map);
//                            }
//                            Log.d("query", MyArrList.size() + "");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
//                    }
//                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//        new CountDownTimer(500, 500) {
//            public void onFinish() {
//                // When timer is finished
//                // Execute your code here
//                listViewInvite.setVisibility(View.VISIBLE);
//                SimpleAdapter sAdap;
//                sAdap = new SimpleAdapter(Home_Alert.this, MyArrList, R.layout.activity_invitation_home,
//                        new String[]{"event_creater", "events_name", "events_month_start", "events_month_end", "pri_name"}, new int[]{R.id.col_head, R.id.col_name_header, R.id.col_time, R.id.col_time_end, R.id.col_pri});
//                listViewInvite.setAdapter(sAdap);
//                final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();
//
//                listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
//                        String sCreater = MyArrList.get(position).get("event_creater").toString();
//                        final String sName = MyArrList.get(position).get("events_name").toString();
//                        final String eid = MyArrList.get(position).get("events_id").toString();
//                        String sSta = MyArrList.get(position).get("events_month_start").toString();
//                        String sEnd = MyArrList.get(position).get("events_month_end").toString();
//                        String sPri = MyArrList.get(position).get("pri_name").toString();
//                        String sTim = sSta + " - " + sEnd;
//                        final String tranId = MyArrList.get(position).get("trans_id").toString();
//                        viewDetail.setIcon(android.R.drawable.btn_star_big_on);
//                        viewDetail.setTitle("รายละเอียด" + tranId);
//                        viewDetail.setMessage("ผู้เชิญ : " + sCreater + "\n"
//                                + "ชื่อการนัดหมาย : " + sName + "\n" + "ช่วงเวลา : " + sTim + "\n"
//                                + "สถานะ : " + sPri + "\n");
//
//
//                        viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "เอาไว้ก่อน", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//                        viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "เข้าร่วม", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Extend_MyHelper.UpdateStateToDb(tranId, 3 + "",Home_Alert.this);
//
//                                addEventFriend(id,eid,sName);
//
//                            }
//                        });
//                        viewDetail.show();
//                        Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
//                        Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);
//
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
//                        layoutParams.weight = 10;
//                        btnPositive.setLayoutParams(layoutParams);
//                        btnNegative.setLayoutParams(layoutParams);
//                    }
//                });
//
//            }
//
//            public void onTick(long millisUntilFinished) {
//                // millisUntilFinished    The amount of time until finished.
//            }
//        }.start();
//
//    }

    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setContentTitle("การเชิญเพื่อน")
                .setContentText("คุณนิวได้เชิญคุณเข้าร่วมนัดหมาย");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, Home_Alert.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
                final TextView title = mView.findViewById(R.id.alt_title_Invite);
                final TextView detail = mView.findViewById(R.id.alt_detail_Invite);
                final ImageView imgF = mView.findViewById(R.id.alt_image_invite);
                final TextView nameE = mView.findViewById(R.id.alt_name_invite);
                final TextView startEnd = mView.findViewById(R.id.alt_start_end);
                final TextView state = mView.findViewById(R.id.alt_state_invite);
                final Button btn_seeMember = mView.findViewById(R.id.btn_seeMember);
                final String tid = alertArray.get(position).get("trans_id").toString();
                final String eid = alertArray.get(position).get("events_id").toString();
                final String ename = alertArray.get(position).get("events_name").toString();
                String sSta = alertArray.get(position).get("events_month_start").toString();
                String sEnd = alertArray.get(position).get("events_month_end").toString();
                String sPrId = alertArray.get(position).get("pri_id").toString();
                String priName = alertArray.get(position).get("pri_name").toString();
                String event_creater = alertArray.get(position).get("event_creater").toString();
                String user_photo = alertArray.get(position).get("user_photo").toString();
                String events_detail = alertArray.get(position).get("events_detail").toString();
                title.setText(ename);
                detail.setText(events_detail);
                nameE.setText(event_creater);
                startEnd.setText(some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)]);
                state.setText(priName);
                Log.d("pathimage", alertArray.toString());
                new Extend_MyHelper.SendHttpRequestTask(user_photo, imgF, 250).execute();
                Button btn_join = mView.findViewById(R.id.btn_invite_join);
                Button btn_notJoin = mView.findViewById(R.id.btn_invite_notjoin);
                Button btn_later = mView.findViewById(R.id.btn_invite_later);
                btn_seeMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();
                        View mView = getLayoutInflater().inflate(R.layout.layout_showmember, null);
                        ListView list = mView.findViewById(R.id.list_ShowMember);
                        TextView numPeople = mView.findViewById(R.id.shownum_people);
                        ImageButton btnClose = mView.findViewById(R.id.showbutton_btnClose);
                        getMemberShow(list,eid,numPeople);
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
                                Intent intent = new Intent(Home_Alert.this, MainAttendent.class);
                                intent.putExtra("id", id + "");
                                intent.putExtra("eid", eid + "");
                                intent.putExtra("nameEvent", ename + "");
                                intent.putExtra("email", email);
                                intent.putExtra("tab", 0 + "");
                                startActivity(intent);
                                viewDetail.dismiss();
                                Toast.makeText(Home_Alert.this, string1, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                addEventFriend(uid, eid);
                                addDateevent(eid);
                                Extend_MyHelper.UpdateStateToDb(tid, 3 + "", Home_Alert.this);
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
//            String mystring = getResources().getString(R.string.mystring);
            String s ="คุณ "+ event_creater + "\nได้เชิญคุณเข้าร่วม : " + ename + "\nสถานะ : " + priName + " \nช่วงเวลา : " + some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)];

            Home_Alert.Item item = new Home_Alert.Item(s, eid, user_photo);
            items.add(item);
        }
        Log.d("pathimage", items.toString());
    }

    public void getEventInvitation() {
        responseStr = new Home_Alert.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gethomeinvite.php";
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

                                MyArrList.add(map);
                                alertArray.add(map);

                            }
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
        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showAllCheckboxClick();
            }
        }.start();


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
        url += "?eId=" + eid;//รอเอาIdจากfirebase
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
                            tv.setText("จำนวนสมาชิกปัจจุบัน "+memberArray.size()+" คน");
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
            Home_Alert.Item2 item2 = new Home_Alert.Item2(uName,uid,uPhoto,sId);
            items2.add(item2);
        }
        myItemsListAdapter2 = new Home_Alert.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
}
