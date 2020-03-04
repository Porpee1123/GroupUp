package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class InviteFriend_Attendant extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        boolean checked;
        ImageView ItemDrawable;
        String ItemString;
        //        Item(ImageView drawable, String t, boolean b){
        Item( String t, boolean b){
//            ItemDrawable = drawable;
            ItemString = t;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<InviteFriend_Attendant.Item> list;

        ItemsListAdapter(Context c, List<InviteFriend_Attendant.Item> l) {
            context = c;
            list = l;
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
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (InviteFriend_Attendant.ViewHolder) rowView.getTag();
            }

//        viewHolder.icon.setImageBitmap(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            /*
            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).checked = b;

                    Toast.makeText(getApplicationContext(),
                            itemStr + "onCheckedChanged\nchecked: " + b,
                            Toast.LENGTH_LONG).show();
                }
            });
            */

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
    }
    //***********************************************************************************************//
    Button btnLookup,btn_friendAll;
    View gab;
    String uid = "",email="";
    int countType=0;
    ListView listViewFriend;
    List<InviteFriend_Attendant.Item> items;
    ArrayList<HashMap<String, String>> frientArray;
    ArrayList<String> typefriend;
    InviteFriend_Attendant.ResponseStr responseStr = new InviteFriend_Attendant.ResponseStr();
    InviteFriend_Attendant.ItemsListAdapter myItemsListAdapter;
    LinearLayout lShortcut ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_attendant);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        linearLayout.setBackgroundColor(Color.parseColor("#BCD0ED"));
        lShortcut = findViewById(R.id.layout_shortcut);
        typefriend = new ArrayList<>();
        btnLookup = findViewById(R.id.slide);
        btn_friendAll = findViewById(R.id.btn_friendAll);
        gab =findViewById(R.id.view_gab);
        listViewFriend = findViewById(R.id.listview_friend);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        frientArray = new ArrayList<>();
        getType();
        getFriend();
        new CountDownTimer(300, 300) {
            public void onFinish() {
                initItems();
                setItemsListView();
                shortCutAddFriend();
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmFriend();
            }
        });
    }
    private void initItems(){
        items = new ArrayList<InviteFriend_Attendant.Item>();
        for (int i=0;i<frientArray.size();i++){
            String s =frientArray.get(i).get("friend_name");
            boolean b= false;
            InviteFriend_Attendant.Item item = new InviteFriend_Attendant.Item(s, b);;
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
        url += "?sId=" + uid;//รอเอาIdจากfirebase
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
                                MyArrList.add(map);
                                frientArray.add(map);
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
    public void setItemsListView(){
        myItemsListAdapter = new InviteFriend_Attendant.ItemsListAdapter(this, items);
        listViewFriend.setAdapter(myItemsListAdapter);
        Log.d("friend","size"+myItemsListAdapter.getCount()+"");
    }
    public void confirmFriend(){
                String str = "Check items:\n";
                for (int i=0; i<items.size(); i++){
                    if (items.get(i).isChecked()){
                        str += i + "\n";
                    }
                }
                Log.d("friend",str);
//                Toast.makeText(InviteFriend_Attendant.this, str, Toast.LENGTH_LONG).show();
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
                            countType = MyArrList.size();
                            for (int i=0;i<MyArrList.size();i++){
                                typefriend.add(MyArrList.get(i).get("type_name"));
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
    public void shortCutAddFriend(){
        Log.d("friend","countType : "+countType+"");
        for (int i=0;i<countType;i++){
            Log.d("friend","i : "+i+"");
            Button b = new Button(this);
            ImageView v = new ImageView(this);
            b.setBackgroundResource(R.drawable.circle_button);
            b.setText(typefriend.get(i));
            b.setHeight(lShortcut.getHeight());
            v.setImageResource(R.drawable.viewgab);
            lShortcut.addView(b);
            lShortcut.addView(v);
//            lShortcut.addView(v);
        }
    }
}
