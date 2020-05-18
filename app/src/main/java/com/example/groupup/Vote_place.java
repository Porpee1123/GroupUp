package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
            RatingString = Rating + "";

        }

    }
    static class ViewHolder {
        ImageView icon;
        TextView text;
        TextView description;
        TextView facility;
        RatingBar rating;
        Button seedetail;
        Button voteSelect;
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
                rowView = inflater.inflate(R.layout.activity_place, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageViewVotePlace);
                viewHolder.text = rowView.findViewById(R.id.rowPlaceNameVotePlace);
                viewHolder.description = rowView.findViewById(R.id.rowDescriptionVotePlace);
                viewHolder.facility = rowView.findViewById(R.id.rowFacilityVotePlace);
                viewHolder.rating = rowView.findViewById(R.id.rowRatingVotePlace);
                viewHolder.seedetail = rowView.findViewById(R.id.rowButtonVoteSeeDetail);
                viewHolder.voteSelect = rowView.findViewById(R.id.rowButtonVoteSelect);
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
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.voteSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Vote_place.this).create();
                    viewDetail.setTitle("ยืนยันการเลือกสถานที่ : " + finalViewHolder.text.getText());
                    viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VotePlace(ItemId);
                            Extend_MyHelper.UpdateStateToDb(transId, "10", Vote_place.this);
                            Extend_MyHelper.sentInviteFCMPerson(id,eid,"3","การโหวตสถานที่เสร็จสิ้น","กรุณารอแม่งานทำการปิดโหวต","OPEN_ACTIVITY_1",Vote_place.this);
                            Intent in = new Intent(Vote_place.this, Home.class);
                            in.putExtra("email", email + "");
                            in.putExtra("id", id + "");
                            startActivity(in);
//                            finish();
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
                    final Button btn_review = mView.findViewById(R.id.btn_seeReview);
                    placeImage.clear();
                    final ImageButton btn_close = mView.findViewById(R.id.showplace_btnClose);
                    final RatingBar rt = mView.findViewById(R.id.showplace_ratingBar);
                    final String sId = placeArray.get(position).get("place_id").toString();
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
                    rt.setNumStars (5);
                    sliderView = mView.findViewById(R.id.imageSlider);
                    String[] some_arrayPrice = getResources().getStringArray(R.array.sppriceRange);
                    String[] some_arrayPeople = getResources().getStringArray(R.array.spnumberOfSeats);
                    getPlacePhotoPid(sId);
                    title.setText(sTitle);
                    detail.setText(sDetail);
                    ArrayList<String> s = spiltGetDate(sDay);
                    time.setText(showStringDay(s) + " \n" + sSTime + " - " + sETime);
                    tel.setText(sTel);
                    price.setText(some_arrayPrice[Integer.parseInt(sPrice)]);
                    people.setText(some_arrayPeople[Integer.parseInt(sPeople)]);
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
                    btn_review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog viewDetail = new AlertDialog.Builder(Vote_place.this).create();
                            View mView = getLayoutInflater().inflate(R.layout.layout_showreview_dialog, null);
                            ImageButton btn_close = mView.findViewById(R.id.showbutton_btnClose);
                            ListView list = mView.findViewById(R.id.list_ShowReview);
                            getReview(sId,list);
                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewDetail.dismiss();
                                }
                            });
                            viewDetail.setView(mView);
                            viewDetail.show();
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
                    } else if (wp.ItemFaciString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    } else if (wp.RatingString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }
    public class Item2 {
        //        String ItemDrawable;
        String ItemName;
        String ItemReview;
        String ItemScore;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String name, String review, String score) {
//            ItemDrawable = drawable;
            ItemName = name;
            ItemReview = review;
            ItemScore = score;
        }

    }
    static class ViewHolder2 {
        //        ImageView icon;
        TextView tName;
        TextView tReview;
        RatingBar rtScore;
    }
    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<Vote_place.Item2> arraylist2;
        private Context context;
        private List<Vote_place.Item2> list2;

        ItemsListAdapter2(Context c, List<Vote_place.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Vote_place.Item2>();
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
            
            Vote_place.ViewHolder2 viewHolder2 = new Vote_place.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_review, null);
                viewHolder2.tName = rowView.findViewById(R.id.rowNameReview);
                viewHolder2.tReview = rowView.findViewById(R.id.rowRatingDetail);
                viewHolder2.rtScore = rowView.findViewById(R.id.rowRatingReview);
                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Vote_place.ViewHolder2) rowView.getTag();
            }
//            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();
            final String itemStr = list2.get(position).ItemName;
            final String itemRev = list2.get(position).ItemReview;
            float score = Float.parseFloat(list2.get(position).ItemScore);
            viewHolder2.tName.setText(itemStr);
            viewHolder2.tReview.setText(itemRev);
            viewHolder2.rtScore.setRating(score);
            return rowView;
        }
    }

    //***********************************************************************************************//
    String id, eid, nameE, email,transId;
    Vote_place.ResponseStr responseStr = new Vote_place.ResponseStr();
    String[] some_array;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> placeArray, placeImage;
    Vote_place.ItemsListAdapter myItemsListAdapter;
    List<Vote_place.Item> items = new ArrayList<Vote_place.Item>();
    List<Vote_place.Item2> items2 = new ArrayList<Vote_place.Item2>();
    ArrayList<HashMap<String, String>> placeReview;
    Vote_place.ItemsListAdapter2 myItemsListAdapter2;
    ListView placeList;
    SliderView sliderView;
    private Vote_place.SliderAdapterExample adapter;
    Button btn_con;
    ListView listViewReview;
    SimpleAdapter sAdapReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_vote_place);
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        btn_con = findViewById(R.id.btn_VotePlace);
        placeList = findViewById(R.id.listView_vote_place);
        placeReview = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        placeArray = new ArrayList<>();
        placeImage = new ArrayList<>();
        progressDialog = new ProgressDialog(Vote_place.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                startActivity(getIntent());
                getplace();
//                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        getplace();
        getTransIDByTrans(id,eid,"3");
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
        intent.putExtra("id", id + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("email", email + "");
        intent.putExtra("tab", 0 + "");

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
        placeArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getvoteplace.php";
        url += "?sId=" + eid;
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
                            showAllCheckboxClick();
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
                                adapter = new Vote_place.SliderAdapterExample(Vote_place.this);
                                sliderView.setSliderAdapter(adapter);
                                renewItems(sliderView, placeImage);
                                sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                sliderView.setIndicatorSelectedColor(Color.WHITE);
                                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                                sliderView.setScrollTimeInSec(3);
                                sliderView.setAutoCycle(true);
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
    }

    public void setItemsListView() {
        myItemsListAdapter = new Vote_place.ItemsListAdapter(this, items);
        placeList.setAdapter(myItemsListAdapter);
        progressDialog.dismiss();
        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("listSelect", "listSelect" + myItemsListAdapter.getItem(position).toString());
            }
        });
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

    public ArrayList spiltGetDate(String s) {
        ArrayList<String> arrayList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s, ":");
        while (st.hasMoreTokens()) {
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

    public void renewItems(View view, ArrayList<HashMap<String, String>> image) {
        List<Vote_place.SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            Vote_place.SliderItem sliderItem = new Vote_place.SliderItem();
            sliderItem.setImageUrl(image.get(i).get("photoplace_path"));
            Log.d("photoplace_path", image.get(i).get("photoplace_path"));
            int count = i+1;
            sliderItem.setDescription(count+"");
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void VotePlace(String pid) {
        Log.d("votedate", eid + " : " + pid);
        String url = "http://www.groupupdb.com/android/addpointvoteplace.php";
        url += "?pId=" + pid;
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(HomeHead_Appointment.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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
    public void getReview(String pid, final ListView listView) {
        placeReview.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getreviewdetail.php";
        url += "?pId=" + pid;
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
                                map.put("user_id", c.getString("user_id"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("review_detail", c.getString("review_detail"));
                                map.put("review_score", c.getString("review_score"));
                                MyArrList.add(map);
                                placeReview.add(map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initItems2(listView);
                        Log.d("pathimage", "get alertArray " + placeReview.toString());
                        Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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
        items2 = new ArrayList<Vote_place.Item2>();
        Log.d("pathimage", "memberArray " + placeReview.toString());
        for (int i = 0; i < placeReview.size(); i++) {
            String uid = placeReview.get(i).get("user_id").toString();
            String name = placeReview.get(i).get("user_names").toString();
            String detail = placeReview.get(i).get("review_detail").toString();
            String score = placeReview.get(i).get("review_score").toString();
            String pId = placeReview.get(i).get("place_id").toString();
            Vote_place.Item2 item2 = new Vote_place.Item2(name,detail,score);
            items2.add(item2);
        }
        myItemsListAdapter2 = new Vote_place.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
    public void getTransIDByTrans(String uid, String eid, String pid) {
        Log.d("themeSelect", "id : " + uid + " eid : " + eid + " pid : " + pid);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/gettransid.php";
        url += "?uId=" + uid;
        url += "&eId=" + eid;
        url += "&pId=" + pid;
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
                                map.put("user_id", c.getString("user_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("pri_id", c.getString("pri_id"));
                                MyArrList.add(map);
                            }
                            transId = MyArrList.get(0).get("trans_id");
//                            Log.d("themeSelect","myarr : "+MyArrList.toString());
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
}
