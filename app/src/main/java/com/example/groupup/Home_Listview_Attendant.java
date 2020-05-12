package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class Home_Listview_Attendant extends AppCompatActivity {
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
        private ArrayList<Home_Listview_Attendant.Item2> arraylist2;
        private Context context;
        private List<Home_Listview_Attendant.Item2> list2;

        ItemsListAdapter2(Context c, List<Home_Listview_Attendant.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Home_Listview_Attendant.Item2>();
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
            Home_Listview_Attendant.ViewHolder2 viewHolder2 = new Home_Listview_Attendant.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_header_home, null);
                viewHolder2.icon = rowView.findViewById(R.id.col_img_event);
                viewHolder2.text = rowView.findViewById(R.id.col_name_header);
                viewHolder2.Status = rowView.findViewById(R.id.col_status_header);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Home_Listview_Attendant.ViewHolder2) rowView.getTag();
            }
            if (list2.get(position).ItemDrawable.equalsIgnoreCase("null")||list2.get(position).ItemDrawable == null){

            }else {
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
                for (Home_Listview_Attendant.Item2 wp : arraylist2) {
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
    Home_Listview_Attendant.ResponseStr responseStr = new Home_Listview_Attendant.ResponseStr();
    String name = "", id = "", email = "";
    static ListView listViewAttend;
    int cVoteTime, cVotePlace;
    ArrayList<HashMap<String, String>> place, time;
    static Home_Listview_Attendant.ItemsListAdapter2 myItemsListAdapter;
    List<Home_Listview_Attendant.Item2> items2 = new ArrayList<Home_Listview_Attendant.Item2>();
    ArrayList<HashMap<String, String>> memberArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attendant);
        listViewAttend = findViewById(R.id.listView_attend);
        place = new ArrayList<HashMap<String, String>>();
        time = new ArrayList<HashMap<String, String>>();
        id = getIntent().getStringExtra("id");
        memberArray = new ArrayList<>();
        email = getIntent().getStringExtra("email");
        Log.d("footer", "attend : id " + id);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                startActivity(getIntent());
                getEventAttend();
//                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        getEventAttend();
    }

    public void getEventAttend() {
        responseStr = new Home_Listview_Attendant.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        memberArray.clear();
        String url = "http://www.groupupdb.com/android/gethomeattend.php";
        url += "?sId=" + id;
        Log.d("footer", "id : id " + id);
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
                                map.put("events_image", c.getString("events_image"));
                                MyArrList.add(map);
                                memberArray.add(map);
                            }
                            initItems2();
                            Log.d("arry", "attend : " + Home.nameAttend.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("footer", "arraylist : id " + MyArrList.toString());
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
                // When timer is finished
//                listViewAttend.setVisibility(View.VISIBLE);
//                sAdapAttend = new SimpleAdapter(Home_Listview_Attendant.this, MyArrList, R.layout.activity_attend_home,
//                        new String[]{"events_name", "states_name"}, new int[]{R.id.col_name_attend, R.id.col_status_attend});
//                listViewAttend.setAdapter(sAdapAttend);
                Home.handlerHome.sendEmptyMessage(0);
                listViewAttend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        String eName = MyArrList.get(position).get("events_name");
                        String eId = MyArrList.get(position).get("events_id");
                        String eStatus = MyArrList.get(position).get("states_name");
                        checkVotePlace(eId);
                        checkVoteTime(eId);
                        Log.d("footer", "id " + eId + "/ name " + eName + "/ status " + eStatus);
                        Intent intent = new Intent(Home_Listview_Attendant.this, MainAttendent.class);
                        intent.putExtra("id", id + "");
                        intent.putExtra("eid", eId + "");
                        intent.putExtra("nameEvent", eName + "");
                        intent.putExtra("email", email);
                        intent.putExtra("tab", 0 + "");
                        startActivity(intent);
                    }
                });

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

    public void checkVoteTime(final String eid) {
        responseStr = new Home_Listview_Attendant.ResponseStr();

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
                                time.add(map);
                            }
                            int check = 0;
                            for (int i = 0; i < time.size(); i++) {
                                int c = Integer.parseInt(time.get(i).get("used"));
                                if (c == 1) {
                                    check = c;
                                }
                            }
                            if (check != 1) {
                                cVoteTime = Integer.parseInt(time.get(0).get("dd"));
                                Log.d("checkvote", "abcVoteTime " + cVoteTime);
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
        responseStr = new Home_Listview_Attendant.ResponseStr();

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
                                place.add(map);
                            }
                            int check = 0;
                            for (int i = 0; i < place.size(); i++) {
                                int c = Integer.parseInt(place.get(i).get("used"));
                                if (c == 1) {
                                    check = c;
                                }
                            }
                            if (check != 1) {
                                cVotePlace = Integer.parseInt(place.get(0).get("dd"));
                                Log.d("checkvote", "abcVotePlace " + cVotePlace);
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
                        Toast.makeText(Home_Listview_Attendant.this, "Close Place Vote Complete", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Home_Listview_Attendant.this, "Close Time Vote Complete", Toast.LENGTH_SHORT).show();
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
        items2 = new ArrayList<Home_Listview_Attendant.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String eid = memberArray.get(i).get("events_id").toString();
            String eName = memberArray.get(i).get("events_name").toString();
            String sName = memberArray.get(i).get("states_name").toString();
            String eImage = memberArray.get(i).get("events_image").toString();
            Home_Listview_Attendant.Item2 item2 = new Home_Listview_Attendant.Item2(eName,eid,eImage,sName);
            items2.add(item2);
        }
        myItemsListAdapter = new Home_Listview_Attendant.ItemsListAdapter2(this, items2);
        listViewAttend.setAdapter(myItemsListAdapter);
        Log.d("pathimage", items2.size() + "");
        listViewAttend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

            }
        });
    }
}
