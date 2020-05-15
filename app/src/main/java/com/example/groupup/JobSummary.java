package com.example.groupup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

public class JobSummary extends AppCompatActivity {
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
            SliderViewAdapter<JobSummary.SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<JobSummary.SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void renewItems(List<JobSummary.SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(JobSummary.SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public JobSummary.SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new JobSummary.SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(JobSummary.SliderAdapterExample.SliderAdapterVH viewHolder, final int position) {

            JobSummary.SliderItem sliderItem = mSliderItems.get(position);

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
        private ArrayList<JobSummary.Item2> arraylist2;
        private Context context;
        private List<JobSummary.Item2> list2;

        ItemsListAdapter2(Context c, List<JobSummary.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<JobSummary.Item2>();
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

            JobSummary.ViewHolder2 viewHolder2 = new JobSummary.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_review, null);
                viewHolder2.tName = rowView.findViewById(R.id.rowNameReview);
                viewHolder2.tReview = rowView.findViewById(R.id.rowRatingDetail);
                viewHolder2.rtScore = rowView.findViewById(R.id.rowRatingReview);
                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (JobSummary.ViewHolder2) rowView.getTag();
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
    String id = "", eId = "", eName = "", email = "", transId = "", placeId = "";
    Button bJoin, bNotJoin,  btn_showPlace;
    boolean checkVisible;
    TextView tvNameE, tvDate, tvTime, tvPlace, tvPeople;
    int cAccept, cCancle, cCash, cTransfer;
    ArrayList<HashMap<String, String>> placeArray, placeImage;
    List<JobSummary.Item2> items2 = new ArrayList<JobSummary.Item2>();
    ArrayList<HashMap<String, String>> placeReview;
    JobSummary.ItemsListAdapter2 myItemsListAdapter2;
    SliderView sliderView;
    String[] some_array;
    private JobSummary.SliderAdapterExample adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_job_summary);
        id = getIntent().getStringExtra("id");
        eId = getIntent().getStringExtra("eid");
        eName = getIntent().getStringExtra("nameEvent");
        email = getIntent().getStringExtra("email");
        checkVisible = true;
        cAccept = 0;
        cCancle = 0;
        cCash = 0;
        cTransfer = 0;
        bJoin = findViewById(R.id.btn_join);
        placeArray = new ArrayList<>();
        placeImage = new ArrayList<>();
        placeReview = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        bNotJoin = findViewById(R.id.btn_notJoin);
        tvNameE = findViewById(R.id.sumnaneEvent);
        tvDate = findViewById(R.id.sumdateApp);
        tvTime = findViewById(R.id.sumtimeApp);
        tvPlace = findViewById(R.id.sumplaceApp);
        tvPeople = findViewById(R.id.sumpeople);
        btn_showPlace = findViewById(R.id.btn_showPlace);
        getTransIDByTrans(id, eId, "3");//ผู้เข้าร่วมงาน
        Extend_MyHelper.checkStatusTrans(eId, "12", JobSummary.this, tvPeople);
        getJob();
        Log.d("checktrans", "tran " + transId);
        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(JobSummary.this).create();
                viewDetail.setTitle("ยืนยันการเลือก");
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Extend_MyHelper.UpdateStateToDb(transId, 12 + "", JobSummary.this);
                        Extend_MyHelper.UpdateAllState(eId,"13","2",JobSummary.this);//ถ้ามีเช็คคนครบค่อยมาเปลี่ยนเอาอันนี้ออก
//                        finish();
                        Intent in = new Intent(JobSummary.this, Home.class);
                        in.putExtra("email", email + "");
                        in.putExtra("id", id + "");
                        startActivity(in);
                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });
        bNotJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(JobSummary.this).create();
                viewDetail.setTitle("ยืนยันการเลือก");
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Extend_MyHelper.UpdateStateToDb(transId, 18 + "", JobSummary.this);
                        finish();
                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnNegative.setTextColor(getResources().getColor(R.color.red));
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

            }
        });
        btn_showPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog viewDetail = new AlertDialog.Builder(JobSummary.this).create();
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
                rt.setNumStars (5);
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
                        final AlertDialog viewDetail = new AlertDialog.Builder(JobSummary.this).create();
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
//        Intent intent = new Intent(JobSummary.this, MainAttendent.class);
//        intent.putExtra("id",id+"");
//        intent.putExtra("eid",eId+"");
//        intent.putExtra("email",email+"");
//        intent.putExtra("nameEvent",eName+"");
//        intent.putExtra("tab",1+"");
//        startActivity(intent);
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
                            String[] some_array = getResources().getStringArray(R.array.bank);

                            String date = MyArrList.get(0).get("time");
                            String dayString = Extend_MyHelper.getDayFromDateString(date, "dd/MM/yyyy");
                            tvNameE.setText(MyArrList.get(0).get("events_name"));
//                            tvDate.setText(MyArrList.get(0).get("time"));
                            tvTime.setText(MyArrList.get(0).get("timerange"));
                            tvPlace.setText(MyArrList.get(0).get("place_name"));
                            placeId = MyArrList.get(0).get("place_id");
                            String fullDate="";
                            String d = "", m="", y="";
                            String[] some_arraymonth = getResources().getStringArray(R.array.month);
                            StringTokenizer st = new StringTokenizer(MyArrList.get(0).get("time"), "/");
                            while (st.hasMoreTokens()) {
                                d = st.nextToken();
                                m = st.nextToken();
                                y = st.nextToken();
                            }
                            fullDate = "วัน" + dayString + "ที่ " + d + " " + some_arraymonth[Integer.parseInt(m)] + " " + y;
                            tvDate.setText(fullDate);
                            getplace();
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

                adapter = new JobSummary.SliderAdapterExample(JobSummary.this);
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
        List<JobSummary.SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            JobSummary.SliderItem sliderItem = new JobSummary.SliderItem();
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
        items2 = new ArrayList<JobSummary.Item2>();
        Log.d("pathimage", "memberArray " + placeReview.toString());
        for (int i = 0; i < placeReview.size(); i++) {
            String uid = placeReview.get(i).get("user_id").toString();
            String name = placeReview.get(i).get("user_names").toString();
            String detail = placeReview.get(i).get("review_detail").toString();
            String score = placeReview.get(i).get("review_score").toString();
            String pId = placeReview.get(i).get("place_id").toString();
            JobSummary.Item2 item2 = new JobSummary.Item2(name, detail, score);
            items2.add(item2);
        }
        myItemsListAdapter2 = new JobSummary.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
}
