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
    private RequestQueue requestQueue;
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
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/checkdatevotetime.php";
        url += "?eId=" + eid;
        url += "&pri=" + "2";
        url += "&stId=" + "8";
        url += "&title=" + "โหวตเวลาปิดเสร็จสิ้น";
        url += "&body=" + "ถึงเวลาเลือกสถานที่เพื่อการโหวต";
        url += "&intent=" + "OPEN_ACTIVITY_1";
//        Log.d("checkvote", "url " + url);
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
                            Log.d("checkvote", "UpdateAllState ");
//                            Extend_MyHelper.UpdateAllState(eid,"8","2",Home_Listview_Attendant.this);
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
        uploadData(stringRequest);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
    }

    public void checkVotePlace(final String eid) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

        String url = "http://www.groupupdb.com/android/checkdatevoteplace.php";
        url += "?eId=" + eid;
        url += "&priH=" + "2";
        url += "&stIdH=" + "10";
        url += "&priA=" + "3";
        url += "&stIdA=" + "11";
        url += "&title=" + "โหวตสถานที่ปิดเสร็จสิ้น";
        url += "&body=" + "ถึงเวลายืนยันการเข้าร่วมงาน";
        url += "&intent=" + "OPEN_ACTIVITY_1";
        Log.d("checkvote", "url " + url);
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
//                            Extend_MyHelper.UpdateAllState(eid,"11","3",Home_Listview_Attendant.this);
//                            Extend_MyHelper.UpdateAllState(eid,"10","2",Home_Listview_Attendant.this);
//                            int check = 0;
//                            for (int i = 0; i < place.size(); i++) {
//                                int c = Integer.parseInt(place.get(i).get("used"));
//                                if (c == 1) {
//                                    check = c;
//                                }
//                            }
//                            if (check != 1) {
//                                cVotePlace = Integer.parseInt(place.get(0).get("dd"));
//                                Log.d("checkvote", "abcVotePlace " + cVotePlace);
//                                if (cVotePlace > 0) {
//                                    closePlace(eid);
//                                }
//                            }

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
        uploadData(stringRequest);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
    }

    private void initItems2() {
        items2 = new ArrayList<Home_Listview_Head.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String eid = memberArray.get(i).get("events_id").toString();
            String eName = memberArray.get(i).get("events_name").toString();
            String sName = memberArray.get(i).get("states_name").toString();
            String eImage = memberArray.get(i).get("events_image").toString();
            checkVotePlace(eid);
            checkVoteTime(eid);
            checkdategotoevent(eid);

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
    public void checkdategotoevent(final String eid) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/checkdategotoevent.php";
        url += "?eId=" + eid;
        Log.d("checkdategotoevent","url "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
        uploadData(stringRequest);
    }
    public void uploadData(StringRequest s) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        } else {
            requestQueue.add(s);
        }
    }
}
