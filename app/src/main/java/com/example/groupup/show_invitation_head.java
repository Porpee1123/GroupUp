package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class show_invitation_head extends AppCompatActivity {
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
            Log.d("dataShow", "ItemDrawable " + ItemDrawable.toString());
        }

    }
    static class ViewHolder2 {
        ImageView icon;
        TextView text;
        ImageView Status;
    }
    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<show_invitation_head.Item2> arraylist2;
        private Context context;
        private List<show_invitation_head.Item2> list2;

        ItemsListAdapter2(Context c, List<show_invitation_head.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<show_invitation_head.Item2>();
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
            show_invitation_head.ViewHolder2 viewHolder2 = new show_invitation_head.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_member, null);
                viewHolder2.icon = rowView.findViewById(R.id.rowImageView);
                viewHolder2.text = rowView.findViewById(R.id.rowTextViewName);
                viewHolder2.Status = rowView.findViewById(R.id.rowimageStatus);

                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (show_invitation_head.ViewHolder2) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();

            final String itemStr = list2.get(position).ItemString;
            viewHolder2.text.setText(itemStr);
            Log.d("dataShow", "ItemDrawable " + itemStr.toString());
            int state = Integer.parseInt(list2.get(position).ItemStatus);
            if (state != 2){
                viewHolder2.Status.setImageResource(R.drawable.ic_tick);
            }
            return rowView;
        }
    }
    //*******************************TextView with checkbox******************************************//
    String uid, eid, nameE, monS, monE, email;
    ListView list_Attend;
    show_invitation_head.ItemsListAdapter2 myItemsListAdapterHead;
    List<show_invitation_head.Item2> items2 = new ArrayList<show_invitation_head.Item2>();
    ArrayList<HashMap<String, String>>  memberArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_show_invitation_head);
        list_Attend = findViewById(R.id.listview_show_invite_head);
        memberArray = new ArrayList<>();
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        getMemberShow();
    }
    public void getMemberShow() {
        memberArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getmemberinvite.php";
        url += "?eId=" + eid;//รอเอาIdจากfirebase
        url += "&prId=" + "2";
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
        items2 = new ArrayList<show_invitation_head.Item2>();
        Log.d("pathimage", "memberArray " + memberArray.toString());
        for (int i = 0; i < memberArray.size(); i++) {
            String uid = memberArray.get(i).get("user_id").toString();
            String uName = memberArray.get(i).get("user_names").toString();
            String uEmail = memberArray.get(i).get("user_email").toString();
            String uPhoto = memberArray.get(i).get("user_photo").toString();
            String sId = memberArray.get(i).get("states_id").toString();
            show_invitation_head.Item2 item2 = new show_invitation_head.Item2(uName,uid,uPhoto,sId);
            items2.add(item2);
        }
        myItemsListAdapterHead = new show_invitation_head.ItemsListAdapter2(this, items2);
        list_Attend.setAdapter(myItemsListAdapterHead);
        Log.d("pathimage", items2.size()+"");
    }
}
