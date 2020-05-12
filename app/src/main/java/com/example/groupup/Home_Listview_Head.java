package com.example.groupup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class Home_Listview_Head extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item2 {
        String ItemDrawable;
        String ItemString;
        String Id;
        String ItemStatus;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String name, String id, String drawable, String Status) {
            ItemDrawable = drawable;
            ItemString = name;
            Id = id;
            ItemStatus = Status;
            Log.d("dataShow", "ItemDrawable " + ItemDrawable.toString());
        }

    }

    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        TextView Status;
    }

    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<Home_Listview_Head.Item2> arraylist2;
        private Context context;
        private List<Home_Listview_Head.Item2> list2;

        ItemsListAdapter2(Context c, List<Home_Listview_Head.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Home_Listview_Head.Item2>();
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
            Home_Listview_Head.ViewHolder2 viewHolder2 = new Home_Listview_Head.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_attend_home, null);
                viewHolder2.icon = rowView.findViewById(R.id.col_img_event);
                viewHolder2.text = rowView.findViewById(R.id.col_name_attend);
                viewHolder2.Status = rowView.findViewById(R.id.col_status_attend);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Home_Listview_Head.ViewHolder2) rowView.getTag();
            }
            if (list2.get(position).ItemDrawable.equalsIgnoreCase("null") || list2.get(position).ItemDrawable == null) {

            } else {
                new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();
            }

            final String itemStr = list2.get(position).ItemString;
            final String itemtag = list2.get(position).ItemStatus;
            viewHolder2.text.setText(itemStr);
            viewHolder2.Status.setText(itemtag);
            return rowView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list2.clear();
            if (charText.length() == 0) {
                list2.addAll(arraylist2);
            } else {
                for (Home_Listview_Head.Item2 wp : arraylist2) {
                    if (wp.ItemString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list2.add(wp);
                    }
                    if (wp.ItemStatus.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list2.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    //*******************************TextView with checkbox******************************************//
    Home_Listview_Head.ResponseStr responseStr = new Home_Listview_Head.ResponseStr();
    String email = "", id = "",uid = "";
    int cVoteTime, cVotePlace;
    static ListView listViewHeader;
    static Home_Listview_Head.ItemsListAdapter2 myItemsListAdapter;
    List<Home_Listview_Head.Item2> items2 = new ArrayList<Home_Listview_Head.Item2>();
    ArrayList<HashMap<String, String>> memberArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_header);
        listViewHeader = findViewById(R.id.listView_Header);
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        memberArray = new ArrayList<>();
        Log.d("listA", "idA " + id);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventHeader();
                pullToRefresh.setRefreshing(false);
            }
        });
        getEventHeader();


    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void getEventHeader() {
        responseStr = new Home_Listview_Head.ResponseStr();
        memberArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/gethomehead.php";
        url += "?sId=" + id;
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
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_wait", c.getString("events_wait"));
                                map.put("events_image", c.getString("events_image"));
                                MyArrList.add(map);
                                memberArray.add(map);
                            }
                            initItems2();
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

    public void checkVoteTime(final String eid) {
        responseStr = new Home_Listview_Head.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/checkdatevotetime.php";
        url += "?eId=" + eid;
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
                                map.put("dd", c.getString("dd"));
                                map.put("datelastwait", c.getString("datelastwait"));
                                map.put("used", c.getString("used"));
                                MyArrList.add(map);
                            }
                            int check = 0;
                            for (int i = 0; i < MyArrList.size(); i++) {
                                int c = Integer.parseInt(MyArrList.get(i).get("used"));
                                if (c == 1) {
                                    check = c;
                                }
                            }
                            if (check != 1) {
                                cVoteTime = Integer.parseInt(MyArrList.get(0).get("dd"));
                                Log.d("checkvote", "hbcVoteTime " + cVotePlace);
                                if (cVoteTime > 0) {
                                    closeTime(eid);
                                }
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

    public void checkVotePlace(final String eid) {
        responseStr = new Home_Listview_Head.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/checkdatevoteplace.php";
        url += "?eId=" + eid;
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
                                map.put("dd", c.getString("dd"));
                                map.put("datelastwait", c.getString("datelastwait"));
                                map.put("used", c.getString("used"));
                                MyArrList.add(map);
                            }
                            int check = 0;
                            for (int i = 0; i < MyArrList.size(); i++) {
                                int c = Integer.parseInt(MyArrList.get(i).get("used"));
                                if (c == 1) {
                                    check = c;
                                }
                            }
//                            Log.d("checkvote","check "+check);
                            if (check != 1) {
                                cVotePlace = Integer.parseInt(MyArrList.get(0).get("dd"));
                                Log.d("checkvote", "hbcVotePlace " + cVotePlace);
                                if (cVotePlace > 0) {
                                    closePlace(eid);
                                }
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

    public void closePlace(String eid) {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closeVotePlace.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Home_Listview_Head.this, "Close Place Vote Complete", Toast.LENGTH_SHORT).show();
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

    public void closeTime(String eid) {
        Log.d("votedate", eid);
        String url = "http://www.groupupdb.com/android/closevotetime.php";
        url += "?eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Home_Listview_Head.this, "Close Time Vote Complete", Toast.LENGTH_SHORT).show();
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

    private void initItems2() {
        items2 = new ArrayList<Home_Listview_Head.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String eid = memberArray.get(i).get("events_id").toString();
            String eName = memberArray.get(i).get("events_name").toString();
            String sName = memberArray.get(i).get("states_name").toString();
            String eImage = memberArray.get(i).get("events_image").toString();
            Home_Listview_Head.Item2 item2 = new Home_Listview_Head.Item2(eName, eid, eImage, sName);
            items2.add(item2);
        }
        myItemsListAdapter = new Home_Listview_Head.ItemsListAdapter2(this, items2);
        listViewHeader.setAdapter(myItemsListAdapter);
        Log.d("pathimage", items2.size() + "");
        listViewHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                String eName = memberArray.get(position).get("events_name");
                String eId = memberArray.get(position).get("events_id");
                String eStatus = memberArray.get(position).get("states_name");
                String ewait = memberArray.get(position).get("events_wait");
                Log.d("footer", "id " + eId + "/ name " + eName + "/ status " + eStatus);
                checkVotePlace(eId);
                checkVoteTime(eId);

//                        checkCloseVote(eId);
                Intent intent = new Intent(Home_Listview_Head.this, HomeHead_Appointment.class);
                intent.putExtra("id", uid + "");
                intent.putExtra("eid", eId + "");
                intent.putExtra("nameEvent", eName + "");
                intent.putExtra("email", email + "");
                intent.putExtra("tab", 0 + "");
                intent.putExtra("wait", ewait + "");
                Log.d("checkIntent",uid+" "+email+" "+eId+" ");
                startActivity(intent);
            }
        });
    }
//    public void getbillcheck() {
//        memberArray.clear();
//        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        String url = "http://www.groupupdb.com/android/getbillcheck.php";
//        url += "?eId=" + eid;
//        url += "&stId=" + "1";
//        Log.d("position", "stringRequest  " + url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            HashMap<String, String> map;
//                            JSONArray data = new JSONArray(response.toString());
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject c = data.getJSONObject(i);
//                                map = new HashMap<String, String>();
//                                map.put("bill_id", c.getString("bill_id"));
//                                map.put("user_id", c.getString("user_id"));
//                                map.put("statusbill_id", c.getString("statusbill_id"));
//                                map.put("bill_image", c.getString("bill_image"));
//                                map.put("statusname", c.getString("statusname"));
//                                map.put("user_names", c.getString("user_names"));
//                                map.put("user_email", c.getString("user_email"));
//                                map.put("user_photo", c.getString("user_photo"));
//                                MyArrList.add(map);
//                                memberArray.add(map);
//                            }
//                            initItems2();
//                            Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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
//    }
//
//    private void initItems2() {
//        items2 = new ArrayList<Home_Listview_Head.Item2>();
//        Log.d("pathimage", "memberArray " + memberArray.toString());
//        for (int i = 0; i < memberArray.size(); i++) {
//            String uid = memberArray.get(i).get("user_id").toString();
//            String uName = memberArray.get(i).get("user_names").toString();
//            String uEmail = memberArray.get(i).get("user_email").toString();
//            String uPhoto = memberArray.get(i).get("user_photo").toString();
//            String bId = memberArray.get(i).get("bill_id").toString();
//            String sbId = memberArray.get(i).get("statusbill_id").toString();
//            String bImage = memberArray.get(i).get("bill_image").toString();
//            String sbName = memberArray.get(i).get("statusname").toString();
//            Home_Listview_Head.Item2 item2 = new Home_Listview_Head.Item2(uName, uid, uPhoto, sbId, sbName);
//            items2.add(item2);
//        }
//        myItemsListAdapter = new Home_Listview_Head.ItemsListAdapter2(this, items2);
//        slipCheck.setAdapter(myItemsListAdapter);
//        Log.d("pathimage", items2.size() + "");
//        slipCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
//                final AlertDialog viewDetail = new AlertDialog.Builder(Home_Listview_Head.this).create();
//                View mView = getLayoutInflater().inflate(R.layout.layout_showbill_dialog, null);
//                final TextView headTheme = mView.findViewById(R.id.userhead);
//                final ImageView imgTheme = mView.findViewById(R.id.img_bill);
//                final Button btn_cancle = mView.findViewById(R.id.btn_cancle);
//                final Button btn_later = mView.findViewById(R.id.btn_later);
//                final Button btn_confirm = mView.findViewById(R.id.btn_cofirm);
//                headTheme.setText(memberArray.get(position).get("user_names").toString());
//                Log.d("getTransIDByTrans", "uid " + memberArray.get(position).get("user_id"));
//                Log.d("getTransIDByTrans", "eid " + eid);
//                final String userId = memberArray.get(position).get("user_id");
//                new Extend_MyHelper.SendHttpRequestTask(memberArray.get(position).get("bill_image").toString(), imgTheme, 350).execute();
//                getTransIDByTrans(memberArray.get(position).get("user_id"), eid, "2");
//                btn_later.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewDetail.dismiss();
//                    }
//                });
//                btn_confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("getTransIDByTrans", transId);
//                        updatestatusbill(userId, eid, "2");
//                        viewDetail.dismiss();
//
//                    }
//                });
//                btn_cancle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Extend_MyHelper.UpdateStateToDb(transId, "11", Home_Listview_Head.this);
//                        getbillcheck();
//                        viewDetail.dismiss();
//                        //แจ้งเตือนให้ผู้ใช้
//                    }
//                });
//                viewDetail.setView(mView);
//                viewDetail.show();
//            }
//        });
//    }
}
