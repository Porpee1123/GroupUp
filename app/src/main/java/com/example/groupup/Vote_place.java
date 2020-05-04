package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Vote_place extends AppCompatActivity {
    //************************************** Slide View *********************************************//
    public class SliderItem {

        private String description;
        private String imageUrl;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
    public class SliderAdapterExample extends
            SliderViewAdapter<Vote_place.SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<Vote_place.SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void renewItems(List<Vote_place.SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(Vote_place.SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public Vote_place.SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new Vote_place.SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(Vote_place.SliderAdapterExample.SliderAdapterVH viewHolder, final int position) {

            Vote_place.SliderItem sliderItem = mSliderItems.get(position);

            viewHolder.textViewDescription.setText(sliderItem.getDescription());
            viewHolder.textViewDescription.setTextSize(16);
            viewHolder.textViewDescription.setTextColor(Color.WHITE);
            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageUrl())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            ImageView imageGifContainer;
            TextView textViewDescription;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
        }

    }
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
        String RatingString;

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
            RatingString = Rating+"";

        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
        TextView description;
        TextView facility;
        RatingBar rating;
        CheckBox checkbox;
        Button seedetail;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private ArrayList<Vote_place.Item> arraylist;
        private Context context;
        private List<Vote_place.Item> list;

        ItemsListAdapter(Context c, List<Vote_place.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<Vote_place.Item>();
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
            Vote_place.ViewHolder viewHolder = new Vote_place.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_head_selectplace, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageViewHeadPlace);
                viewHolder.text = rowView.findViewById(R.id.rowPlaceNameHeadPlace);
                viewHolder.description = rowView.findViewById(R.id.rowDescriptionHeadPlace);
                viewHolder.facility = rowView.findViewById(R.id.rowFacilityHeadPlace);
                viewHolder.rating = rowView.findViewById(R.id.rowRatingHeadPlace);
                viewHolder.checkbox = rowView.findViewById(R.id.rowCheckboxHeadPlace);
                viewHolder.seedetail = rowView.findViewById(R.id.rowButtonSeeDetail);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Vote_place.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final int maxLimit = 3;

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
            viewHolder.seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Vote_place.this).create();
                    View mView = getLayoutInflater().inflate(R.layout.layout_showplace_dialog, null);
                    final TextView title = mView.findViewById(R.id.showplace_Title);
                    final TextView detail = mView.findViewById(R.id.showplace_detail);
                    final TextView time = mView.findViewById(R.id.showplace_time);
                    final TextView tel = mView.findViewById(R.id.showplace_tel);
                    final TextView price = mView.findViewById(R.id.showplace_price);
                    final TextView people = mView.findViewById(R.id.showplace_people);
                    final TextView facility = mView.findViewById(R.id.showplace_facility);
                    final TextView visit = mView.findViewById(R.id.showplace_PeopleVisit);
                    placeImage.clear();
                    final ImageButton btn_close = mView.findViewById(R.id.showplace_btnClose);
                    final RatingBar rt = mView.findViewById(R.id.showplace_ratingBar);
                    String sId = placeArray.get(position).get("place_id").toString();
                    String sTitle = placeArray.get(position).get("place_name").toString();
                    String sDetail = placeArray.get(position).get("place_detail").toString();
                    String sTel = placeArray.get(position).get("place_phone").toString();
                    String sPrice = placeArray.get(position).get("place_price").toString();
                    String sPeople = placeArray.get(position).get("place_numofseat").toString();
                    String sFacility = placeArray.get(position).get("place_facility").toString();
                    String sImage = placeArray.get(position).get("place_photoShow").toString();
                    String sRating = placeArray.get(position).get("place_score").toString();
                    String sDay = placeArray.get(position).get("place_date").toString();
                    String sSTime = placeArray.get(position).get("place_stime").toString();
                    String sETime = placeArray.get(position).get("place_etime").toString();
                    String sVisit = placeArray.get(position).get("place_visit").toString();
                    sliderView = mView.findViewById(R.id.imageSlider);
                    getPlacePhotoPid(sId);
                    title.setText(sTitle);
                    detail.setText(sDetail);
                    ArrayList<String> s =spiltGetDate(sDay);
                    time.setText(showStringDay(s)+ " \n" + sSTime + " - " + sETime);
                    tel.setText(sTel);
                    price.setText(sPrice);
                    people.setText(sPeople);
                    facility.setText(showFacility(sFacility));
                    rt.setRating(Float.parseFloat(sRating));
                    visit.setText("( " + sVisit + " คน)");
                    viewDetail.setView(mView);
                    viewDetail.show();
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewDetail.dismiss();
                        }
                    });
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
                for (Vote_place.Item wp : arraylist) {
                    if (wp.ItemName.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }
                    else if (wp.ItemFaciString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }else if (wp.RatingString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }

    //***********************************************************************************************//
    String id,eid,nameE,email;
    Vote_place.ResponseStr responseStr = new Vote_place.ResponseStr();
    String[] some_array;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> placeArray, placeImage;
    Vote_place.ItemsListAdapter myItemsListAdapter;
    List<Vote_place.Item> items = new ArrayList<Vote_place.Item>();
    ListView placeList;
    SliderView sliderView;
    private Vote_place.SliderAdapterExample adapter;
    Button btn_con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_vote_place);
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        btn_con=findViewById(R.id.btn_VotePlace);
        placeList = findViewById(R.id.listView_vote_place);
        placeArray = new ArrayList<>();
        placeImage = new ArrayList<>();
        progressDialog = new ProgressDialog(Vote_place.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        getplace();
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void backVote(View v) {
        Intent intent = new Intent(Vote_place.this, MainAttendent.class);
        intent.putExtra("id",id+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("email", email+"");
        intent.putExtra("tab",0+"");

        startActivity(intent);
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
        responseStr = new Vote_place.ResponseStr();

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

    public void getPlacePhotoPid(String pId) {
        responseStr = new Vote_place.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplacephotobypid.php";
        url += "?sId=" + pId;
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
                                map.put("photoplace_id", c.getString("photoplace_id"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("photoplace_path", c.getString("photoplace_path"));
                                map.put("place_upid", c.getString("place_upid"));
                                MyArrList.add(map);
                                placeImage.add(map);

                            }
                            Log.d("editplce", placeImage.toString());
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

                adapter = new Vote_place.SliderAdapterExample(Vote_place.this);
                sliderView.setSliderAdapter(adapter);
                renewItems(sliderView,placeImage);
                sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
//                if (placeImage.size() == 0) {
//
//                } else {
//                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(0).get("photoplace_path"), img1, 450).execute();
//                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(1).get("photoplace_path"), img2, 450).execute();
//                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(2).get("photoplace_path"), img3, 450).execute();
//                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(3).get("photoplace_path"), img4, 450).execute();
//                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(4).get("photoplace_path"), img5, 450).execute();
//                }
            }
        }.start();
    }
    
    public void setItemsListView() {
        myItemsListAdapter = new Vote_place.ItemsListAdapter(this, items);
        placeList.setAdapter(myItemsListAdapter);
        progressDialog.dismiss();
        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
    }

    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
    }

    private void initItems() {
        items = new ArrayList<Vote_place.Item>();
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
            Vote_place.Item item = new Vote_place.Item(pid, pName, pDetail, pFacility, Float.parseFloat(pScore), pImage, upid, pPrice, pPhone, pSeat, pDepo, pDate, pStartTime, pEndTime);
            items.add(item);
            Log.d("pathimage", "item : " + items.toString());
        }

    }
    public ArrayList spiltGetDate(String s){
        ArrayList<String> arrayList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s,":");
        while (st.hasMoreTokens()){
            arrayList.add(st.nextToken());
        }
        return arrayList;
    }
    public String showStringDay(ArrayList<String> date) {
        String day = "";
        for (int i = 0; i < date.size(); i++) {
            if (Integer.parseInt(date.get(i)) == 1) {
                day += "จันทร์ ";

            } else if (Integer.parseInt(date.get(i)) == 2) {
                day += "อังคาร ";
            } else if (Integer.parseInt(date.get(i)) == 3) {
                day += "พุธ ";
            } else if (Integer.parseInt(date.get(i)) == 4) {
                day += "พฤหัสบดี ";
            } else if (Integer.parseInt(date.get(i)) == 5) {
                day += "ศุกร์ ";
            } else if (Integer.parseInt(date.get(i)) == 6) {
                day += "เสาร์ ";
            } else if (Integer.parseInt(date.get(i)) == 7) {
                day += "อาทิตย์ ";
            } else if (Integer.parseInt(date.get(i)) == 8) {
                day += "วันเสาร์อาทิตย์ ";
            } else if (Integer.parseInt(date.get(i)) == 9) {
                day += "จันทร์ - ศุกร์ ";
            } else if (Integer.parseInt(date.get(i)) == 10) {
                day += "ทุกวัน ";
            }
        }
        return day;
    }
    public void renewItems(View view,ArrayList<HashMap<String, String>> image) {
        List<Vote_place.SliderItem> sliderItemList = new ArrayList<>();
        for (int i =0;i<image.size();i++){
            Vote_place.SliderItem sliderItem = new Vote_place.SliderItem();
            sliderItem.setImageUrl(image.get(i).get("photoplace_path"));
            Log.d("photoplace_path",image.get(i).get("photoplace_path"));
            sliderItem.setDescription("Slider Item " + i);
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

}
