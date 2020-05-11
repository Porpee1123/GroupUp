package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class SlipCheck_finish extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item2 {
        String ItemDrawable;
        String ItemString;
        String Id;
        String ItemStatus;
        String ItemTag;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String t, String i, String drawable, String Status,String tag) {
            ItemDrawable = drawable;
            ItemString = t;
            Id = i;
            ItemStatus = Status;
            ItemTag = tag;
            Log.d("dataShow", "ItemDrawable " + ItemDrawable.toString());
        }

    }
    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        TextView tag;
        ImageView Status;
    }
    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<SlipCheck_finish.Item2> arraylist2;
        private Context context;
        private List<SlipCheck_finish.Item2> list2;

        ItemsListAdapter2(Context c, List<SlipCheck_finish.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<SlipCheck_finish.Item2>();
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
            SlipCheck_finish.ViewHolder2 viewHolder2 = new SlipCheck_finish.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_slip_finish, null);
                viewHolder2.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder2.text = rowView.findViewById(R.id.rowTextViewName);
                viewHolder2.tag = rowView.findViewById(R.id.rowTextViewTag);
                viewHolder2.Status = rowView.findViewById(R.id.rowimageButton6);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (SlipCheck_finish.ViewHolder2) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();

            final String itemStr = list2.get(position).ItemString;
            final String itemtag = list2.get(position).ItemTag;
            viewHolder2.text.setText(itemStr);
            viewHolder2.Status.setImageResource(R.drawable.ic_tick);
            viewHolder2.tag.setText(itemtag);
            return rowView;
        }
        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list2.clear();
            if (charText.length() == 0) {
                list2.addAll(arraylist2);
            } else {
                for (SlipCheck_finish.Item2 wp : arraylist2) {
                    if (wp.ItemString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list2.add(wp);
                    }
                    if (wp.ItemTag.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list2.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
    //*******************************TextView with checkbox******************************************//
    String id,eid,nameE,monS,monE,email,transId="";
    ListView slipFinish ;
    static SlipCheck_finish.ItemsListAdapter2 myItemsListAdapter;
    List<SlipCheck_finish.Item2> items2 = new ArrayList<SlipCheck_finish.Item2>();
    ArrayList<HashMap<String, String>>  memberArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_slip_check_finish);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        slipFinish = findViewById(R.id.listView_slipFinish);
        memberArray = new ArrayList<>();
//        getSlipFinish();
        getSlipFinish();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSlipFinish();
                pullToRefresh.setRefreshing(false);
            }
        });
    }
    public void getSlipFinish() {
        memberArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getbillfinish.php";
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
                                map.put("bill_id", c.getString("bill_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("statusbill_id", c.getString("statusbill_id"));
                                map.put("bill_image", c.getString("bill_image"));
                                map.put("statusname", c.getString("statusname"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_email", c.getString("user_email"));
                                map.put("user_photo", c.getString("user_photo"));
                                MyArrList.add(map);
                                memberArray.add(map);
                            }
                            initItems2();
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


    private void initItems2() {
        items2 = new ArrayList<SlipCheck_finish.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String uid = memberArray.get(i).get("user_id").toString();
            String uName = memberArray.get(i).get("user_names").toString();
            String uEmail = memberArray.get(i).get("user_email").toString();
            String uPhoto = memberArray.get(i).get("user_photo").toString();
            String bId = memberArray.get(i).get("bill_id").toString();
            String sbId = memberArray.get(i).get("statusbill_id").toString();
            String bImage = memberArray.get(i).get("bill_image").toString();
            String sbName = memberArray.get(i).get("statusname").toString();
            SlipCheck_finish.Item2 item2 = new SlipCheck_finish.Item2(uName,uid,uPhoto,sbId,sbName);
            items2.add(item2);
        }

        myItemsListAdapter = new SlipCheck_finish.ItemsListAdapter2(this, items2);
        slipFinish.setAdapter(myItemsListAdapter);
        Log.d("pathimage", items2.size()+"");

    }

}
