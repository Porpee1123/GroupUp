package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class Show_invitation_attendant extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item2 {
        String ItemDrawable;
        String ItemString;
        String Id;
        String ItemStatus;
        String ItemTrans;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String t, String i, String drawable, String Status,String transId) {
            ItemDrawable = drawable;
            ItemString = t;
            Id = i;
            ItemStatus = Status;
            ItemTrans =transId;
            Log.d("dataShow", "ItemDrawable " + ItemDrawable.toString());
        }

    }
    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        ImageView Status;
        ImageButton img_del;
    }
    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<Show_invitation_attendant.Item2> arraylist2;
        private Context context;
        private List<Show_invitation_attendant.Item2> list2;

        ItemsListAdapter2(Context c, List<Show_invitation_attendant.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Show_invitation_attendant.Item2>();
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
            Show_invitation_attendant.ViewHolder2 viewHolder2 = new Show_invitation_attendant.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_member, null);
                viewHolder2.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder2.text = rowView.findViewById(R.id.rowTextViewName);
                viewHolder2.Status = rowView.findViewById(R.id.rowimageStatus);
                viewHolder2.img_del = rowView.findViewById(R.id.img_del);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Show_invitation_attendant.ViewHolder2) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();

            final String itemStr = list2.get(position).ItemString;
            final String itemTriD = list2.get(position).ItemTrans;

            viewHolder2.text.setText(itemStr);
            Log.d("dataShow", "ItemDrawable " + itemStr.toString());
            int state = Integer.parseInt(list2.get(position).ItemStatus);
            if (state != 2){
                viewHolder2.Status.setImageResource(R.drawable.ic_tick);
            }
            viewHolder2.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Show_invitation_attendant.this).create();
                    viewDetail.setTitle("ยืนยันการลบสมาชิก");
                    viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletetransID(itemTriD);
                        }
                    });
                    viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewDetail.dismiss();
                        }
                    });
                    viewDetail.show();
                    Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();
                    layoutParams.weight = 10;
                    btnNegative.setTextColor(getResources().getColor(R.color.red));
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
                }
            });
            return rowView;
        }
        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list2.clear();
            if (charText.length() == 0) {
                list2.addAll(arraylist2);
            } else {
                for (Show_invitation_attendant.Item2 wp : arraylist2) {
                    if (wp.ItemString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list2.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
    //*******************************TextView with checkbox******************************************//
    String uid, eid, nameE, monS, monE, email;
    ListView list_Attend;
    static ItemsListAdapter2 myItemsListAdapterAttend;
    List<Show_invitation_attendant.Item2> items2 = new ArrayList<Show_invitation_attendant.Item2>();
    ArrayList<HashMap<String, String>>  memberArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_show_invitation_attendant);
        list_Attend = findViewById(R.id.listview_show_invite_Attendant);
        memberArray = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        getMemberShow();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberShow();
                pullToRefresh.setRefreshing(false);
            }
        });

    }
    public void getMemberShow() {
        memberArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getmemberinvite.php";
        url += "?eId=" + eid;
        url += "&prId=" + "3";
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
                                map.put("trans_id", c.getString("trans_id"));

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
        items2 = new ArrayList<Show_invitation_attendant.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String uid = memberArray.get(i).get("user_id").toString();
            String uName = memberArray.get(i).get("user_names").toString();
            String uEmail = memberArray.get(i).get("user_email").toString();
            String uPhoto = memberArray.get(i).get("user_photo").toString();
            String sId = memberArray.get(i).get("states_id").toString();
            String trId = memberArray.get(i).get("trans_id").toString();
            Show_invitation_attendant.Item2 item2 = new Show_invitation_attendant.Item2(uName,uid,uPhoto,sId,trId);
            items2.add(item2);
        }
        myItemsListAdapterAttend = new Show_invitation_attendant.ItemsListAdapter2(this, items2);
        list_Attend.setAdapter(myItemsListAdapterAttend);
        Log.d("pathimage", items2.size()+"");
    }
    public void deletetransID(String tid) {
        String url = "http://www.groupupdb.com/android/deletetransid.php";
        url += "?tId=" + tid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getMemberShow();
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
}
