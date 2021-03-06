package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Head_Place extends AppCompatActivity {
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
            SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void renewItems(List<SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SliderItem sliderItem = mSliderItems.get(position);

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
        CheckBox checkbox;
        Button seedetail;
        Button selectPlace;
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
                viewHolder.seedetail = rowView.findViewById(R.id.rowButtonSeeDetail);
                viewHolder.selectPlace = rowView.findViewById(R.id.rowButtonPlaceSelect);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Head_Place.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final int maxLimit = 4;
//            Log.d("checkCB", "list " + list.toString());
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
            viewHolder.selectPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("checkCB", "cbCount " + cbCount);
                    Log.d("checkCB", "placeDelPosition " + placeDelPosition);
                    if (checkAlreadyClick(ItemId)) {
                        Toast.makeText(getApplicationContext(),
                                "คุณไม่สามารถเลือกสถานที่ซ้ำได้", Toast.LENGTH_SHORT).show();
                    } else {
                        if (placeDelPosition.size() == 0) {
                            scrollSelect.setVisibility(View.VISIBLE);
                            lineOnSelect.setVisibility(View.VISIBLE);
                            if (cbCount <= 4) {
                                placeSelect.add(ItemId);
                                showCb(cbCount, list.get(position).ItemDrawable, ItemName, ItemId);
                                cbCount++;
                                Log.d("checkCB", placeSelect.toString());
                                Log.d("checkCB", placeDelPosition.toString());
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "สามารถเลือกได้สูงสุด 4 ตัวเลือก", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            scrollSelect.setVisibility(View.VISIBLE);
                            lineOnSelect.setVisibility(View.VISIBLE);
                            placeSelect.add(ItemId);
                            showCb(placeDelPosition.get(0), list.get(position).ItemDrawable, ItemName, ItemId);
                            cbCount++;
                            placeDelPosition.remove(0);
                            Log.d("checkCB", "else " + placeSelect.toString());
                            Log.d("checkCB", "else " + placeDelPosition.toString());
                        }
                    }
                }
            });
            viewHolder.seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Head_Place.this).create();
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
                    sliderView = mView.findViewById(R.id.imageSlider);
                    String[] some_arrayPrice = getResources().getStringArray(R.array.sppriceRange);
                    String[] some_arrayPeople = getResources().getStringArray(R.array.spnumberOfSeats);
                    getPlacePhotoPid(sId);
                    rt.setNumStars(5);
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
                            final AlertDialog viewDetail = new AlertDialog.Builder(Head_Place.this).create();
                            View mView = getLayoutInflater().inflate(R.layout.layout_showreview_dialog, null);
                            ImageButton btn_close = mView.findViewById(R.id.showbutton_btnClose);
                            ListView list = mView.findViewById(R.id.list_ShowReview);
                            getReview(sId, list);
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

//            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (count[0] == maxLimit - 1) {
//                        if (count[0] == maxLimit && isChecked) {
//                            buttonView.setChecked(false);
//                            Toast.makeText(getApplicationContext(),
//                                    "สามารถเลือกได้สูงสุด 4 ตัวเลือก", Toast.LENGTH_SHORT).show();
//                        } else if (isChecked) {
//                            showCb(cbCount);
//                            count[0]++;
//                            cbCount++;
//                            placeSelect.add(ItemId);
//                        } else if (!isChecked) {
//                            hideCb(cbCount);
//                            removePlace(ItemId);
//                            count[0]--;
//                            cbCount--;
//                        }
//                    } else {
//                        if (count[0] == maxLimit - 1 && isChecked) {
//                            buttonView.setChecked(false);
//                            Toast.makeText(getApplicationContext(),
//                                    "สามารถเลือกได้สูงสุด 4 ตัวเลือก", Toast.LENGTH_SHORT).show();
//                        } else if (isChecked) {
//                            showCb(cbCount);
//                            count[0]++;
//                            cbCount++;
//                            placeSelect.add(ItemId);
//                        } else if (!isChecked) {
//                            hideCb(cbCount);
//                            removePlace(ItemId);
//                            count[0]--;
//                            cbCount--;
//                        }
//                    }
//                    Log.d("checkCB", placeSelect.toString());
//                }
//            });
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
        private ArrayList<Head_Place.Item2> arraylist2;
        private Context context;
        private List<Head_Place.Item2> list2;

        ItemsListAdapter2(Context c, List<Head_Place.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Head_Place.Item2>();
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

            Head_Place.ViewHolder2 viewHolder2 = new Head_Place.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_review, null);
                viewHolder2.tName = rowView.findViewById(R.id.rowNameReview);
                viewHolder2.tReview = rowView.findViewById(R.id.rowRatingDetail);
                viewHolder2.rtScore = rowView.findViewById(R.id.rowRatingReview);
                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Head_Place.ViewHolder2) rowView.getTag();
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
    ArrayList<HashMap<String, String>> placeArray, placeImage;
    ProgressDialog progressDialog;
    ResponseStr responseStr = new ResponseStr();
    String uid, eid, nameE, monS, monE, email, wait;
    String[] some_array;
    Head_Place.ItemsListAdapter myItemsListAdapter;
    List<Head_Place.Item> items = new ArrayList<Head_Place.Item>();
    ListView placeList;
    HorizontalScrollView scrollSelect;
    SliderView sliderView;
    List<Head_Place.Item2> items2 = new ArrayList<Head_Place.Item2>();
    ArrayList<HashMap<String, String>> placeReview;
    Head_Place.ItemsListAdapter2 myItemsListAdapter2;
    ArrayAdapter<String> adpScore, adpFaci, adpPrice, adpPeople, adpTheme;
    ArrayList<String> placeSelect;
    ArrayList<Integer> placeDelPosition;
    Spinner sp_score, sp_faci, sp_price, sp_people, sp_theme;
    private SliderAdapterExample adapter;
    final int[] count = {0};
    int cbCount = 1;
    Button btn_con;
    EditText searchText;
    ImageView img_selPlace1, img_selPlace2, img_selPlace3, img_selPlace4;
    ImageButton imb_selPlace1, imb_selPlace2, imb_selPlace3, imb_selPlace4;
    TextView tv_selPlace1, tv_selPlace2, tv_selPlace3, tv_selPlace4;
    TextView tv_pid1, tv_pid2, tv_pid3, tv_pid4;
    View lineSelect1, lineSelect2, lineSelect3, lineSelect4,lineOnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_head__place);
        sp_faci = findViewById(R.id.spinner_facility);
        sp_people = findViewById(R.id.spinner_seat);
        sp_price = findViewById(R.id.spinner_price);
        sp_score = findViewById(R.id.spinner_score);
        sp_theme = findViewById(R.id.spinner_theme);
        searchText = findViewById(R.id.searchTextPlace);
        imb_selPlace1 = findViewById(R.id.row_img_del1);
        imb_selPlace2 = findViewById(R.id.row_img_del2);
        imb_selPlace3 = findViewById(R.id.row_img_del3);
        imb_selPlace4 = findViewById(R.id.row_img_del4);
        img_selPlace1 = findViewById(R.id.rowImageView1);
        img_selPlace2 = findViewById(R.id.rowImageView2);
        img_selPlace3 = findViewById(R.id.rowImageView3);
        img_selPlace4 = findViewById(R.id.rowImageView4);
        tv_selPlace1 = findViewById(R.id.rowTextViewName1);
        tv_selPlace2 = findViewById(R.id.rowTextViewName2);
        tv_selPlace3 = findViewById(R.id.rowTextViewName3);
        tv_selPlace4 = findViewById(R.id.rowTextViewName4);
        tv_pid1 = findViewById(R.id.tv_ItemId1);
        tv_pid2 = findViewById(R.id.tv_ItemId2);
        tv_pid3 = findViewById(R.id.tv_ItemId3);
        tv_pid4 = findViewById(R.id.tv_ItemId4);
        lineSelect1 = findViewById(R.id.lineSelect1);
        lineSelect2 = findViewById(R.id.lineSelect2);
        lineSelect3 = findViewById(R.id.lineSelect3);
        lineSelect4 = findViewById(R.id.lineSelect4);
        lineOnSelect= findViewById(R.id.lineOnSelect);
        scrollSelect = findViewById(R.id.scrollSelect);
        img_selPlace1.setVisibility(View.INVISIBLE);
        imb_selPlace1.setVisibility(View.INVISIBLE);
        lineSelect1.setVisibility(View.INVISIBLE);
        tv_selPlace1.setVisibility(View.INVISIBLE);
        img_selPlace2.setVisibility(View.INVISIBLE);
        imb_selPlace2.setVisibility(View.INVISIBLE);
        tv_selPlace2.setVisibility(View.INVISIBLE);
        lineSelect2.setVisibility(View.INVISIBLE);
        img_selPlace3.setVisibility(View.INVISIBLE);
        imb_selPlace3.setVisibility(View.INVISIBLE);
        tv_selPlace3.setVisibility(View.INVISIBLE);
        lineSelect3.setVisibility(View.INVISIBLE);
        img_selPlace4.setVisibility(View.INVISIBLE);
        imb_selPlace4.setVisibility(View.INVISIBLE);
        tv_selPlace4.setVisibility(View.INVISIBLE);
        lineSelect4.setVisibility(View.INVISIBLE);

        scrollSelect.setVisibility(View.GONE);
        lineOnSelect.setVisibility(View.GONE);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        wait = "";
        placeList = findViewById(R.id.listView_showPlace);
        btn_con = findViewById(R.id.btn_conPlace);
        placeArray = new ArrayList<>();
        placeImage = new ArrayList<>();
        placeSelect = new ArrayList<>();
        placeReview = new ArrayList<>();
        placeDelPosition = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        progressDialog = new ProgressDialog(Head_Place.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        adpFaci = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spfacility));
        adpFaci.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpPeople = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spnumberOfSeats));
        adpPeople.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpPrice = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sppriceRange));
        adpPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpScore = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spscore));
        adpScore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpTheme = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sptheme));
        adpTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_faci.setAdapter(adpFaci);
        sp_faci.setSelection(0);
        sp_score.setAdapter(adpScore);
        sp_score.setSelection(0);
        sp_price.setAdapter(adpPrice);
        sp_price.setSelection(0);
        sp_people.setAdapter(adpPeople);
        sp_people.setSelection(0);
        sp_theme.setAdapter(adpTheme);
        sp_theme.setSelection(0);

        sp_faci.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String typeChange = selectedItemText;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        progressDialog.show();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                startActivity(getIntent());
                placeSelect.clear();
                getplace();
                count[0] = 0;
//                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        getplace();
        getEvent();
        search();
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeSelect.size() == 4) {
                    for (int i = 0; i < placeSelect.size(); i++) {
                        saveToVotePlace(placeSelect.get(i));
                    }
                    Extend_MyHelper.sentInviteFCMPerson("0", eid, "3", "ถึงเวลาโหวตสถานที่", "กรุณาโหวตสถานที่ที่ต้องการนัดหมาย", "OPEN_ACTIVITY_1", Head_Place.this);
                    Extend_MyHelper.UpdateAllState(eid, "9", "3", Head_Place.this);
                    Extend_MyHelper.UpdateAllState(eid, "9", "2", Head_Place.this);
                    Intent in = new Intent(Head_Place.this, Home.class);
                    in.putExtra("email", email + "");
                    in.putExtra("id", uid + "");
                    startActivity(in);
//                    saveToVotePlaceRandom();
//                    finish();
                } else {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Head_Place.this).create();

                    viewDetail.setTitle("กรุณาเลือก 4 ตัวเลือก");
                    viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewDetail.dismiss();
                        }
                    });
                    viewDetail.show();
                    Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    btnPositive.setLayoutParams(layoutParams);
                }

            }
        });
        imb_selPlace1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCb(1);
                removeArray(placeSelect, tv_pid1.getText().toString());
//                placeSelect.remove(0);
                placeDelPosition.add(1);
                cbCount--;
            }
        });
        imb_selPlace2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCb(2);
                removeArray(placeSelect, tv_pid2.getText().toString());
//                placeSelect.remove(1);
                placeDelPosition.add(2);
                cbCount--;
            }
        });
        imb_selPlace3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCb(3);
                removeArray(placeSelect, tv_pid3.getText().toString());
//                placeSelect.remove(2);
                placeDelPosition.add(3);
                cbCount--;
            }
        });
        imb_selPlace4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCb(4);
                removeArray(placeSelect, tv_pid4.getText().toString());
//                placeSelect.remove(3);
                placeDelPosition.add(4);
                cbCount--;
            }
        });
    }

    public void backAppoint(View v) {
        Intent intent = new Intent(Head_Place.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid + "");
        intent.putExtra("email", email + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("nameEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("tab", 1 + "");
        startActivity(intent);
//        finish();
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
        placeArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplaceforHeader.php";
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
                                showAllCheckboxClick();
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
    }

    public void getPlacePhotoPid(String pId) {
        responseStr = new Head_Place.ResponseStr();
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
                                adapter = new SliderAdapterExample(Head_Place.this);
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
        progressDialog.dismiss();
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
                day += "เสาร์ - อาทิตย์ ";
            } else if (Integer.parseInt(date.get(i)) == 9) {
                day += "จันทร์ - ศุกร์ ";
            } else if (Integer.parseInt(date.get(i)) == 10) {
                day += "ทุกวัน ";
            }
        }
        return day;
    }

    public void renewItems(View view, ArrayList<HashMap<String, String>> image) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setImageUrl(image.get(i).get("photoplace_path"));
            Log.d("photoplace_path", image.get(i).get("photoplace_path"));
            int count = i + 1;
            sliderItem.setDescription(count + "");
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removePlace(String id) {
        String number = "";
        for (int i = 0; i < placeSelect.size(); i++) {
            if (id.equals(placeSelect.get(i))) {
                number = i + "";
            }
        }
        if (number != "") {
            placeSelect.remove(Integer.parseInt(number));
        }
        Log.d("dateselect", placeSelect.toString());
    }

    public void saveToVotePlace(String pid) {
        Log.d("saveToVotePlace", pid);
        String url = "http://www.groupupdb.com/android/addplaceforvote.php";
        url += "?pid=" + pid;
        url += "&dLw=" + calwait(Integer.parseInt(wait)) + "";
        url += "&eid=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Head_Place.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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

    public void saveToVotePlaceRandom() {
        String url = "http://www.groupupdb.com/android/addplaceforvote.php";
        url += "?pid=" + "random";
        url += "&dLw=" + calwait(Integer.parseInt(wait)) + "";
        url += "&eid=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Head_Place.this, "Add Friend Complete", Toast.LENGTH_SHORT).show();
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

    public String calwait(int wait) {
        Calendar cal = Calendar.getInstance();
        Date today1 = cal.getTime();
        cal.add(Calendar.DATE, wait); // to get previous year add -1
        Date nextYear1 = cal.getTime();
        DateFormat simpleNoHour = new SimpleDateFormat("yyyy-MM-dd");
        simpleNoHour.format(nextYear1);
        Log.d("wait", nextYear1 + "");
        return simpleNoHour.format(nextYear1) + "";
    }

    public void getEvent() {

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
//        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/geteventHeader.php";
        url += "?sId=" + uid;//ร  อเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("states_name", c.getString("states_name"));
                                map.put("events_month_start", c.getString("events_month_start"));
                                map.put("events_month_end", c.getString("events_month_end"));
                                map.put("events_detail", c.getString("events_detail"));
                                map.put("events_note", c.getString("events_note"));
                                map.put("events_wait", c.getString("events_wait"));

                                MyArrList.add(map);
                            }
                            wait = MyArrList.get(0).get("events_wait");
                            Log.d("tab", "wait : " + wait);
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

    public void search() {
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

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
        items2 = new ArrayList<Head_Place.Item2>();
        Log.d("pathimage", "memberArray " + placeReview.toString());
        for (int i = 0; i < placeReview.size(); i++) {
            String uid = placeReview.get(i).get("user_id").toString();
            String name = placeReview.get(i).get("user_names").toString();
            String detail = placeReview.get(i).get("review_detail").toString();
            String score = placeReview.get(i).get("review_score").toString();
            String pId = placeReview.get(i).get("place_id").toString();
            Head_Place.Item2 item2 = new Head_Place.Item2(name, detail, score);
            items2.add(item2);
        }
        myItemsListAdapter2 = new Head_Place.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }

    public void hideCb(int count) {
        if (count == 1) {
            img_selPlace1.setVisibility(View.GONE);
            imb_selPlace1.setVisibility(View.GONE);
            tv_selPlace1.setVisibility(View.GONE);
            lineSelect1.setVisibility(View.GONE);

        } else if (count == 2) {
            img_selPlace2.setVisibility(View.GONE);
            imb_selPlace2.setVisibility(View.GONE);
            tv_selPlace2.setVisibility(View.GONE);
            lineSelect2.setVisibility(View.GONE);

        } else if (count == 3) {
            img_selPlace3.setVisibility(View.GONE);
            imb_selPlace3.setVisibility(View.GONE);
            tv_selPlace3.setVisibility(View.GONE);
            lineSelect3.setVisibility(View.GONE);
        } else if (count == 4) {
            img_selPlace4.setVisibility(View.GONE);
            imb_selPlace4.setVisibility(View.GONE);
            tv_selPlace4.setVisibility(View.GONE);
            lineSelect4.setVisibility(View.GONE);
        }
    }

    public void showCb(int count, String path, String name, String itemId) {
        if (count == 1) {
            img_selPlace1.setVisibility(View.VISIBLE);
            imb_selPlace1.setVisibility(View.VISIBLE);
            tv_selPlace1.setVisibility(View.VISIBLE);
            lineSelect1.setVisibility(View.VISIBLE);
            new Extend_MyHelper.SendHttpRequestTask(path, img_selPlace1, 250).execute();
            tv_selPlace1.setText(name);
            tv_pid1.setText(itemId);
        } else if (count == 2) {
            img_selPlace2.setVisibility(View.VISIBLE);
            imb_selPlace2.setVisibility(View.VISIBLE);
            tv_selPlace2.setVisibility(View.VISIBLE);
            lineSelect2.setVisibility(View.VISIBLE);
            new Extend_MyHelper.SendHttpRequestTask(path, img_selPlace2, 250).execute();
            tv_selPlace2.setText(name);
            tv_pid2.setText(itemId);
        } else if (count == 3) {
            img_selPlace3.setVisibility(View.VISIBLE);
            imb_selPlace3.setVisibility(View.VISIBLE);
            tv_selPlace3.setVisibility(View.VISIBLE);
            lineSelect3.setVisibility(View.VISIBLE);
            new Extend_MyHelper.SendHttpRequestTask(path, img_selPlace3, 250).execute();
            tv_selPlace3.setText(name);
            tv_pid3.setText(itemId);
        } else if (count == 4) {
            img_selPlace4.setVisibility(View.VISIBLE);
            imb_selPlace4.setVisibility(View.VISIBLE);
            tv_selPlace4.setVisibility(View.VISIBLE);
            lineSelect4.setVisibility(View.VISIBLE);
            new Extend_MyHelper.SendHttpRequestTask(path, img_selPlace4, 250).execute();
            tv_selPlace4.setText(name);
            tv_pid4.setText(itemId);
        }
    }

    public boolean checkAlreadyClick(String id) {
        Log.d("placeselect", "id : " + id);
        Log.d("placeselect", "friendInDb : " + placeSelect.toString());
        for (int j = 0; j < placeSelect.size(); j++) {
            String fdb = placeSelect.get(j);
            if (fdb.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public void removeArray(ArrayList<String> arr, String id) {
        String number = "";
        for (int i = 0; i < arr.size(); i++) {
            if (id.equals(arr.get(i))) {
                number = i + "";
            }
        }
        arr.remove(Integer.parseInt(number));
        if (arr.size() == 0) {
            scrollSelect.setVisibility(View.GONE);
            lineOnSelect.setVisibility(View.GONE);
        }
    }
//    public void ckeckCB(ArrayList<String> p){
//        Head_Place.ViewHolder viewHolder = new Head_Place.ViewHolder();
//        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
//        rowView = inflater.inflate(R.layout.layout_head_selectplace, null);
//        viewHolder.icon = rowView.findViewById(R.id.rowImageViewHeadPlace);
//        viewHolder.text = rowView.findViewById(R.id.rowPlaceNameHeadPlace);
//        viewHolder.description = rowView.findViewById(R.id.rowDescriptionHeadPlace);
//        viewHolder.facility = rowView.findViewById(R.id.rowFacilityHeadPlace);
//        viewHolder.rating = rowView.findViewById(R.id.rowRatingHeadPlace);
//        viewHolder.checkbox = rowView.findViewById(R.id.rowCheckboxHeadPlace);
//        for (int i =0;i<placeArray.size();i++){
//            for (int j=0;j<p.size();j++){
//                if (placeArray.get(i).equals(p.get(j))){
//                    viewHolder.checkbox.setChecked(true);
//                }else {
//                    viewHolder.checkbox.setChecked(false);
//                }
//
//            }
//        }
//
//    }

}
