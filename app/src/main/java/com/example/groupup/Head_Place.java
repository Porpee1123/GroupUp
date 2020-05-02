package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.util.StringTokenizer;

public class Head_Place extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        String ItemId;
        String ItemDrawable;
        String ItemName;
        String ItemDest;
        String ItemFaci;
        float Rating;
        String ItemUserId;
        String ItemPrice;
        String ItemPhone;
        String ItemSeat;
        String ItemDeposite;
        String ItemDay;
        String ItemStartTime;
        String ItemEndTime;
        String ItemFaciString;

        //        upid,pPrice,pPhone,pSeat,pDepo,pDate,pStartTime,pEndTime
        //        Item(ImageView drawable, String t, boolean b){
        Item(String id, String name, String desc, String faci, float rating, String drawable, String uid, String price, String phone, String seat, String deposit, String day, String sTime, String eTime) {
            ItemId = id;
            ItemDrawable = drawable;
            ItemName = name;
            ItemDest = desc;
            ItemFaci = faci;
            Rating = rating;
            ItemUserId = uid;
            ItemPrice = price;
            ItemPhone = phone;
            ItemSeat = seat;
            ItemDeposite = deposit;
            ItemDay = day;
            ItemStartTime = sTime;
            ItemEndTime = eTime;
            ItemFaciString = showFacility(ItemFaci);
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
        TextView description;
        TextView facility;
        RatingBar rating;
        Button checkbox;
    }
    public class ItemsListAdapter extends BaseAdapter {
        private ArrayList<Head_Place.Item> arraylist;
        private Context context;
        private List<Head_Place.Item> list;

        ItemsListAdapter(Context c, List<Head_Place.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<Head_Place.Item>();
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
            Head_Place.ViewHolder viewHolder = new Head_Place.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_head_selectplace, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageViewHeadPlace);
                viewHolder.text = rowView.findViewById(R.id.rowPlaceNameHeadPlace);
                viewHolder.description = rowView.findViewById(R.id.rowDescriptionHeadPlace);
                viewHolder.facility = rowView.findViewById(R.id.rowFacilityHeadPlace);
                viewHolder.rating = rowView.findViewById(R.id.rowRatingHeadPlace);
                viewHolder.checkbox = rowView.findViewById(R.id.rowCheckboxHeadPlace);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Head_Place.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final String ItemName = list.get(position).ItemName;
            final String ItemDest = list.get(position).ItemDest;
            final String ItemFaci = list.get(position).ItemFaci;
            final float ItemRating = list.get(position).Rating;
            final String ItemId = list.get(position).ItemId;
            final String ItemUserId = list.get(position).ItemUserId;
            final String ItemPrice = list.get(position).ItemPrice;
            final String ItemPhone = list.get(position).ItemPhone;
            final String ItemSeat = list.get(position).ItemSeat;
            final String ItemDeposite = list.get(position).ItemDeposite;
            final String ItemDay = list.get(position).ItemDay;
            final String ItemStartTime = list.get(position).ItemStartTime;
            final String ItemEndTime = list.get(position).ItemEndTime;
            viewHolder.text.setText(ItemName);
            viewHolder.rating.setRating(ItemRating);
            viewHolder.description.setText(ItemDest);

            viewHolder.facility.setText(showFacility(ItemFaci));
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ItemId,ItemDrawable,ItemName,ItemDest,ItemFaci,Rating,ItemUserId,ItemPrice,ItemPhone,ItemSeat,ItemDeposite,ItemDay,ItemStartTime,ItemEndTime,
//                    Intent in = new Intent(Head_Place.this, Edit_place.class);
//                    in.putExtra("ItemId", ItemId + "");
//                    in.putExtra("ItemName", ItemName + "");
//                    in.putExtra("ItemDest", ItemDest + "");
//                    in.putExtra("ItemFaci", ItemFaci + "");
//                    in.putExtra("Rating", ItemRating + "");
//                    in.putExtra("ItemUserId", ItemUserId + "");
//                    in.putExtra("ItemPrice", ItemPrice + "");
//                    in.putExtra("ItemPhone", ItemPhone + "");
//                    in.putExtra("ItemSeat", ItemSeat + "");
//                    in.putExtra("ItemDeposite", ItemDeposite + "");
//                    in.putExtra("ItemDay", ItemDay + "");
//                    in.putExtra("ItemStartTime", ItemStartTime + "");
//                    in.putExtra("ItemEndTime", ItemEndTime + "");
//                    in.putExtra("ItemUserEmail", email + "");
//                    startActivity(in);
//
//                    Log.d("listclick", position + "  " + ItemId);
                }
            });
            return rowView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(arraylist);
            } else {
                for (Item wp : arraylist) {
                    if (wp.ItemName.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }
                    else if (wp.ItemFaciString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }
    //***********************************************************************************************//
    ArrayList<HashMap<String, String>> placeArray;
    ProgressDialog progressDialog;
    ResponseStr responseStr = new ResponseStr();
    String uid,eid,nameE,monS,monE,email;
    String[] some_array;
    static Head_Place.ItemsListAdapter myItemsListAdapter;
    List<Head_Place.Item> items = new ArrayList<Head_Place.Item>();
    ListView placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_head__place);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        placeList = findViewById(R.id.listView_showPlace);
        placeArray = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        progressDialog = new ProgressDialog(Head_Place.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
//        progressDialog.show();
        getplace();
    }
    public void backAppoint(View v) {
//        Intent intent = new Intent(Head_Place.this, HomeHead_Appointment.class);
//        intent.putExtra("id", uid+"");
//        intent.putExtra("email", email+"");
//        intent.putExtra("eid",eid+"");
//        intent.putExtra("nameEvent",nameE+"");
//        intent.putExtra("mStart",monS+"");
//        intent.putExtra("mEnd",monE+"");
//        intent.putExtra("tab",1+"");
//        startActivity(intent);
        finish();
    }
    public String showFacility(String d) {
        StringTokenizer st = new StringTokenizer(d, ":");
        String s = "";
        ArrayList<Integer> array = new ArrayList<>();
        while (st.hasMoreTokens()) {
            array.add(Integer.parseInt(st.nextToken()) - 1);
        }
        for (int i = 0; i < array.size(); i++) {
            if (i != array.size() - 1) {
                s += "- " + some_array[array.get(i)] + "\n";
            } else {
                s += "- " + some_array[array.get(i)];
            }
        }
        return s;
    }
    public void getplace() {
        responseStr = new Head_Place.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplaceforHeader.php";
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
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_upid", c.getString("place_upid"));
                                map.put("place_name", c.getString("place_name"));
                                map.put("place_detail", c.getString("place_detail"));
                                map.put("place_price", c.getString("place_price"));
                                map.put("place_phone", c.getString("place_phone"));
                                map.put("place_numofseat", c.getString("place_numofseat"));
                                map.put("place_deposit", c.getString("place_deposit"));
                                map.put("place_date", c.getString("place_date"));
                                map.put("place_stime", c.getString("place_stime"));
                                map.put("place_etime", c.getString("place_etime"));
                                map.put("place_facility", c.getString("place_facility"));
                                map.put("place_score", c.getString("place_score"));
                                map.put("place_visit", c.getString("place_visit"));
                                map.put("place_photoShow", c.getString("place_photoShow"));
                                MyArrList.add(map);
                                placeArray.add(map);

                            }
                            Log.d("placeHome", "get placeArray " + placeArray.toString());
//                            Log.d("place", "get MyArrList " + MyArrList.toString());
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
        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showAllCheckboxClick();
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
    public void setItemsListView() {
        myItemsListAdapter = new Head_Place.ItemsListAdapter(this, items);
        placeList.setAdapter(myItemsListAdapter);
        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
    }
    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
    }
    private void initItems() {
        items = new ArrayList<Head_Place.Item>();
        Log.d("pathimage", "placeArray " + placeArray.toString());
        for (int i = 0; i < placeArray.size(); i++) {
            String pid = placeArray.get(i).get("place_id").toString();
            String upid = placeArray.get(i).get("place_upid").toString();
            String pName = placeArray.get(i).get("place_name").toString();
            String pDetail = placeArray.get(i).get("place_detail").toString();
            String pPrice = placeArray.get(i).get("place_price").toString();
            String pPhone = placeArray.get(i).get("place_phone").toString();
            String pSeat = placeArray.get(i).get("place_numofseat").toString();
            String pDepo = placeArray.get(i).get("place_deposit").toString();
            String pDate = placeArray.get(i).get("place_date").toString();
            String pStartTime = placeArray.get(i).get("place_stime").toString();
            String pEndTime = placeArray.get(i).get("place_etime").toString();
            String pFacility = placeArray.get(i).get("place_facility").toString();
            String pScore = placeArray.get(i).get("place_score").toString();
            String pVisit = placeArray.get(i).get("place_visit").toString();
            String pImage = placeArray.get(i).get("place_photoShow").toString();

//            String mystring = getResources().getString(R.string.mystring);
//            String s = event_creater + "ได้เชิญคุณเข้าร่วม " + ename + " เป็น " + priName + " โดยมีช่วงเวลาระหว่างเดือน " + some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)];
            Log.d("pathimage", "item Home : " + pName + " / " + pDetail + " / " + pFacility + " / " + pScore + " / " + pImage);
            Head_Place.Item item = new Head_Place.Item(pid, pName, pDetail, pFacility, Float.parseFloat(pScore), pImage, upid, pPrice, pPhone, pSeat, pDepo, pDate, pStartTime, pEndTime);
            items.add(item);
            Log.d("pathimage", "item : " + items.toString());
        }

    }
}
