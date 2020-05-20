package com.example.groupup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.StringTokenizer;

public class Reviews extends AppCompatActivity {
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
            SliderViewAdapter<Reviews.SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<Reviews.SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void renewItems(List<Reviews.SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(Reviews.SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public Reviews.SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new Reviews.SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(Reviews.SliderAdapterExample.SliderAdapterVH viewHolder, final int position) {

            Reviews.SliderItem sliderItem = mSliderItems.get(position);

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
        private ArrayList<Reviews.Item2> arraylist2;
        private Context context;
        private List<Reviews.Item2> list2;

        ItemsListAdapter2(Context c, List<Reviews.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<Reviews.Item2>();
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

            Reviews.ViewHolder2 viewHolder2 = new Reviews.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_review, null);
                viewHolder2.tName = rowView.findViewById(R.id.rowNameReview);
                viewHolder2.tReview = rowView.findViewById(R.id.rowRatingDetail);
                viewHolder2.rtScore = rowView.findViewById(R.id.rowRatingReview);
                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (Reviews.ViewHolder2) rowView.getTag();
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

    //************************************** Slide View *********************************************//
    String uid = "", eId = "", eName = "", email = "", placeId = "", placeName = "", transId = "";
    TextView tvName;
    RatingBar rtBar;
    EditText edt_datail;
    Button btn_con, btn_showPlace;
    String textReview = "", nameReview = "";
    ArrayList<HashMap<String, String>> placeArray, placeImage;
    ArrayList<HashMap<String, String>> placeReview;
    List<Reviews.Item2> items2 = new ArrayList<Reviews.Item2>();
    Reviews.ItemsListAdapter2 myItemsListAdapter2;
    SliderView sliderView;
    String[] some_array;
    private Reviews.SliderAdapterExample adapter;
    float scoreReview = 0;
    float scoreEDb = 0, scorePeople = 0;
    int sumpeople = 0, peopleEDb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_reviews);
        tvName = findViewById(R.id.review_name);
        rtBar = findViewById(R.id.review_ratingBar);
        edt_datail = findViewById(R.id.review_edtdetail);
        btn_con = findViewById(R.id.review_btnConfirm);
        btn_showPlace = findViewById(R.id.ButtonSeeDetail);
        placeArray = new ArrayList<>();
        placeImage = new ArrayList<>();
        placeReview = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        uid = getIntent().getStringExtra("id");
        eName = getIntent().getStringExtra("nameEvent");
        eId = getIntent().getStringExtra("eid");
        email = getIntent().getStringExtra("email");
        getJob();
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });
        btn_showPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(Reviews.this).create();
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
                final Button btn_review = mView.findViewById(R.id.btn_seeReview);
                final RatingBar rt = mView.findViewById(R.id.showplace_ratingBar);
                final String sId = placeArray.get(0).get("place_id").toString();
                String sTitle = placeArray.get(0).get("place_name").toString();
                String sDetail = placeArray.get(0).get("place_detail").toString();
                String sTel = placeArray.get(0).get("place_phone").toString();
                String sPrice = placeArray.get(0).get("place_price").toString();
                String sPeople = placeArray.get(0).get("place_numofseat").toString();
                String sFacility = placeArray.get(0).get("place_facility").toString();
                String sImage = placeArray.get(0).get("place_photoShow").toString();
                String sRating = placeArray.get(0).get("place_score").toString();
                String sDay = placeArray.get(0).get("place_date").toString();
                String sSTime = placeArray.get(0).get("place_stime").toString();
                String sETime = placeArray.get(0).get("place_etime").toString();
                String sVisit = placeArray.get(0).get("place_visit").toString();
                sliderView = mView.findViewById(R.id.imageSlider);
                String[] some_arrayPrice = getResources().getStringArray(R.array.sppriceRange);
                String[] some_arrayPeople = getResources().getStringArray(R.array.spnumberOfSeats);
                getPlacePhotoPid(sId);
                title.setText(sTitle);
                rt.setNumStars(5);
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
                        final AlertDialog viewDetail = new AlertDialog.Builder(Reviews.this).create();
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
    }

    public void backSum(View v) {
        finish();
    }

    public void getJob() {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/getjobsummary.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
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
                                map.put("events_name", c.getString("events_name"));
                                map.put("time", c.getString("time"));
                                map.put("timerange", c.getString("timerange"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_name", c.getString("place_name"));
                                map.put("events_bankid", c.getString("events_bankid"));
                                map.put("events_bankname", c.getString("events_bankname"));
                                map.put("events_bankaccount", c.getString("events_bankaccount"));

                                MyArrList.add(map);
                            }
                            placeId = MyArrList.get(0).get("place_id");
                            placeName = MyArrList.get(0).get("place_name");
                            tvName.setText(placeName);
                            getplace();
                            getScorePeople();
                            getTransIDByTrans(uid, eId, "3");
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

    public void getScorePeople() {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/getallscoreandpeoplereview.php";
        url += "?pId=" + placeId;//ร  อเอาIdหรือ email จากfirebase
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
                                map.put("sumReview", c.getString("sumReview"));
                                map.put("peopleReview", c.getString("peopleReview"));
                                MyArrList.add(map);
                            }
                            String pReview = MyArrList.get(0).get("peopleReview");
                            String sReview = MyArrList.get(0).get("sumReview");
                            if (pReview.equalsIgnoreCase("null") || pReview == null) {
                                sumpeople = 0;
                            } else {
                                sumpeople = Integer.parseInt(MyArrList.get(0).get("peopleReview"));
                            }
                            if (sReview.equalsIgnoreCase("null") || sReview == null) {
                                scorePeople = 0;
                            } else {
                                scorePeople = Float.parseFloat(MyArrList.get(0).get("sumReview"));
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

    public void addJob() {
        scoreReview = rtBar.getRating();
        textReview = edt_datail.getText().toString();
        scoreEDb = scorePeople + scoreReview;
        peopleEDb = sumpeople + 1;
        float scorevisitplace = scoreEDb / peopleEDb;
        Log.d("checkscoreReview", "EDB score " + scoreEDb + " people " + peopleEDb);
        Log.d("checkscoreReview", "score " + scorePeople + " people " + sumpeople + " rtScore " + scoreReview + " scVisit " + scorevisitplace);

        String url = "http://www.groupupdb.com/android/addreviewtodb.php";
        url += "?pid=" + placeId + "";
        url += "&uid=" + uid + "";
        url += "&rts=" + scoreReview + "";
        url += "&rvt=" + textReview + "";
        url += "&pvp=" + peopleEDb + "";
        url += "&svp=" + scorevisitplace + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Extend_MyHelper.UpdateStateToDb(transId, 18 + "", Reviews.this);
                        Toast.makeText(Reviews.this, "Add Review Complete", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(Reviews.this, Home.class);
                        in.putExtra("email", email + "");
                        in.putExtra("id", uid + "");
                        startActivity(in);
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

    public void getTransIDByTrans(String uid, String eid, String pid) {
        Log.d("checktrans", "id : " + uid + " eid : " + eid + " pid : " + pid);
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
                            Log.d("checktrans", "tran " + transId);
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
        placeArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplacebyplaceid.php";
        url += "?pId=" + placeId + "";
        Log.d("placeHome", "stringRequest  " + url);
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
//                showAllCheckboxClick();
            }
        }.start();
    }

    public void getPlacePhotoPid(String pId) {
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

                adapter = new Reviews.SliderAdapterExample(Reviews.this);
                sliderView.setSliderAdapter(adapter);
                renewItems(sliderView, placeImage);
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
        List<Reviews.SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            Reviews.SliderItem sliderItem = new Reviews.SliderItem();
            sliderItem.setImageUrl(image.get(i).get("photoplace_path"));
            Log.d("photoplace_path", image.get(i).get("photoplace_path"));
            int count = i + 1;
            sliderItem.setDescription(count + "");
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void getReview(String pid, final ListView listView) {
        placeReview.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getreviewdetail.php";
        url += "?pId=" + pid;//รอเอาIdจากfirebase
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
        items2 = new ArrayList<Reviews.Item2>();
        Log.d("pathimage", "memberArray " + placeReview.toString());
        for (int i = 0; i < placeReview.size(); i++) {
            String uid = placeReview.get(i).get("user_id").toString();
            String name = placeReview.get(i).get("user_names").toString();
            String detail = placeReview.get(i).get("review_detail").toString();
            String score = placeReview.get(i).get("review_score").toString();
            String pId = placeReview.get(i).get("place_id").toString();
            Reviews.Item2 item2 = new Reviews.Item2(name, detail, score);
            items2.add(item2);
        }
        myItemsListAdapter2 = new Reviews.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }

}
