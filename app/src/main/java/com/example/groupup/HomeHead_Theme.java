package com.example.groupup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xeoh.android.texthighlighter.TextHighlighter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeHead_Theme extends AppCompatActivity {
    HomeHead_Theme.ResponseStr responseStr = new HomeHead_Theme.ResponseStr();
    String id, eid, nameE, monS, monE, email, transId,wait;
    ArrayList<String> themeSelect = new ArrayList<>();
    Button b, btn_con;
    LinearLayout lShort, lCus;
    ScrollView scrollView;
    ImageView img_minimal, img_classic, img_buffet, img_river, img_karaoke, img_sky, img_kid;
    boolean checkVisible;
    ArrayList<String> nameType, idType;
    CheckBox cafe, coffee, river, karaoke, clubs, pub, wine, night, vegeterian, hotelBuf, rooftop, izakaya, dessert, alacarte, seafood, steak, iceCream, bakery,
            bbq, shabu, buffet, cleanFood, thaiBbq, pizza, sushi, burger, ramen, dimsum, vegan, veget, original, bar, outdoor, cozy, family, minimal, warm, child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_theme);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid = getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        wait = getIntent().getStringExtra("wait");
        lShort = findViewById(R.id.linear_shortcut);
        lCus = findViewById(R.id.linear_custom);
        b = findViewById(R.id.btn_customTheme);
        btn_con = findViewById(R.id.btn_confirmCustom);
        scrollView = findViewById(R.id.scroll_theme);
        lCus.setVisibility(View.GONE);
        btn_con.setVisibility(View.GONE);
        img_minimal = findViewById(R.id.theme_minimal);
        img_classic = findViewById(R.id.theme_classic);
        img_buffet = findViewById(R.id.theme_buffet);
        img_river = findViewById(R.id.theme_river);
        img_karaoke = findViewById(R.id.theme_karaoke);
        img_sky = findViewById(R.id.theme_sky);
        img_kid = findViewById(R.id.theme_kid);
        checkVisible = true;//close custom
        Log.d("wait",wait);
//        calwait(3);
        getTransIDByTrans(id, eid, "3");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleLinear();
                clearCheckBox();
            }
        });
        check();
        img_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(21 + "", R.string.minimal);
            }
        });
        img_classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(22 + "", R.string.classic);
            }
        });
        img_buffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(15 + "", R.string.buffet);
            }
        });
        img_river.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(36 + "", R.string.waterFront);
            }
        });
        img_karaoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(3 + "", R.string.karaoke);
            }
        });
        img_sky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(23 + "", R.string.sky);
            }
        });
        img_kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypeTheme(8 + "", R.string.forkids);
            }
        });
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < themeSelect.size(); i++) {
                    sentInviteTheme(themeSelect.get(i).toString(), eid);
                }
//                addDateevent();
                Log.d("themeSelect", "Remove : " + themeSelect.toString());
                backAppoint();
            }
        });

    }

    public void check() {
        cafe = findViewById(R.id.cafe);
        cafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cafe.isChecked()) {
                    cafe.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820591");
//                    Log.d("themeSelect",
//                            cafe.getText()+"--"+R.string.cafe+"");

                } else {
                    cafe.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820591");
                }

            }
        });


        coffee = findViewById(R.id.coffee_tea);
        coffee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (coffee.isChecked()) {
                    coffee.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820600");
                } else {
                    coffee.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820600");
                }

            }
        });

        river = findViewById(R.id.riverside);
        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (river.isChecked()) {
                    river.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820742");
                } else {
                    river.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820742");
                }

            }
        });

        karaoke = findViewById(R.id.karaoke);
        karaoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (karaoke.isChecked()) {
                    karaoke.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820666");
                } else {
                    karaoke.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820666");
                }

            }
        });

        clubs = findViewById(R.id.clubs);
        clubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (clubs.isChecked()) {
                    clubs.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820734");
                } else {
                    clubs.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820734");
                }

            }
        });

        pub = findViewById(R.id.pub);
        pub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (pub.isChecked()) {
                    pub.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820599");
                } else {
                    pub.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820599");
                }

            }
        });

        wine = findViewById(R.id.wine_bar);
        wine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (wine.isChecked()) {
                    wine.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820784");
                } else {
                    wine.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820784");
                }

            }
        });

        night = findViewById(R.id.late_night_rice);
        night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (night.isChecked()) {
                    night.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820668");
                } else {
                    night.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820668");
                }

            }
        });

        vegeterian = findViewById(R.id.vegeterian);
        vegeterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (vegeterian.isChecked()) {
                    vegeterian.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820778");
                } else {
                    vegeterian.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820778");
                }

            }
        });

        hotelBuf = findViewById(R.id.hotel_buffet);
        hotelBuf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (hotelBuf.isChecked()) {
                    hotelBuf.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820657");
                } else {
                    hotelBuf.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820657");
                }

            }
        });

        rooftop = findViewById(R.id.rooftop);
        rooftop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (rooftop.isChecked()) {
                    rooftop.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820743");
                } else {
                    rooftop.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820743");
                }

            }
        });

        izakaya = findViewById(R.id.izakaya);
        izakaya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (izakaya.isChecked()) {
                    izakaya.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820664");
                } else {
                    izakaya.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820664");
                }

            }
        });

        dessert = findViewById(R.id.dessert);
        dessert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dessert.isChecked()) {
                    dessert.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820630");
                } else {
                    dessert.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820630");
                }

            }
        });

        alacarte = findViewById(R.id.alacarte);
        alacarte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (alacarte.isChecked()) {
                    alacarte.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820576");
                } else {
                    alacarte.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820576");
                }

            }
        });

        seafood = findViewById(R.id.seafood);
        seafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (seafood.isChecked()) {
                    seafood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820745");
                } else {
                    seafood.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820745");
                }

            }
        });

        steak = findViewById(R.id.steak);
        steak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (steak.isChecked()) {
                    steak.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820764");
                } else {
                    steak.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820764");
                }

            }
        });

        iceCream = findViewById(R.id.iceCream);
        iceCream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (iceCream.isChecked()) {
                    iceCream.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820658");
                } else {
                    iceCream.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820658");
                }

            }
        });

        bakery = findViewById(R.id.bakery_cake);
        bakery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bakery.isChecked()) {
                    bakery.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820582");
                } else {
                    bakery.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820582");
                }

            }
        });

        bbq = findViewById(R.id.bbq);
        bbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bbq.isChecked()) {
                    bbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820585");
                } else {
                    bbq.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820585");
                }

            }
        });

        shabu = findViewById(R.id.shabu);
        shabu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (shabu.isChecked()) {
                    shabu.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820757");
                } else {
                    shabu.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820757");
                }

            }
        });

        buffet = findViewById(R.id.buffet);
        buffet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (buffet.isChecked()) {
                    buffet.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820589");
                } else {
                    buffet.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820589");
                }

            }
        });

        cleanFood = findViewById(R.id.cleanFood);
        cleanFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cleanFood.isChecked()) {
                    cleanFood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820597");
                } else {
                    cleanFood.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820597");
                }

            }
        });

        thaiBbq = findViewById(R.id.thai_bbq);
        thaiBbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (thaiBbq.isChecked()) {
                    thaiBbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820770");
                } else {
                    thaiBbq.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820770");
                }

            }
        });

        pizza = findViewById(R.id.pizza);
        pizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (pizza.isChecked()) {
                    pizza.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820730");
                } else {
                    pizza.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820730");
                }

            }
        });

        sushi = findViewById(R.id.sushi);
        sushi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sushi.isChecked()) {
                    sushi.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820767");
                } else {
                    sushi.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820767");
                }

            }
        });

        burger = findViewById(R.id.burger);
        burger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (burger.isChecked()) {
                    burger.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820590");
                } else {
                    burger.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820590");
                }

            }
        });

        ramen = findViewById(R.id.ramen);
        ramen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (ramen.isChecked()) {
                    ramen.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820737");
                } else {
                    ramen.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820737");
                }

            }
        });

        dimsum = findViewById(R.id.dimsum);
        dimsum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dimsum.isChecked()) {
                    dimsum.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820633");
                } else {
                    dimsum.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820633");
                }

            }
        });

        vegan = findViewById(R.id.vegan);
        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (vegan.isChecked()) {
                    vegan.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820777");
                } else {
                    vegan.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820777");
                }

            }
        });

        veget = findViewById(R.id.vegeterian2);
        veget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (veget.isChecked()) {
                    veget.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820778");
                } else {
                    veget.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820778");
                }

            }
        });

        original = findViewById(R.id.original);
        original.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (original.isChecked()) {
                    original.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820721");
                } else {
                    original.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820721");
                }

            }
        });

        bar = findViewById(R.id.bar);
        bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bar.isChecked()) {
                    bar.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820583");
                } else {
                    bar.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820583");
                }

            }
        });

        outdoor = findViewById(R.id.outdoor);
        outdoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (outdoor.isChecked()) {
                    outdoor.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820722");
                } else {
                    outdoor.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820722");
                }

            }
        });

        cozy = findViewById(R.id.cozy);
        cozy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cozy.isChecked()) {
                    cozy.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820620");
                } else {
                    cozy.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820620");
                }

            }
        });

        family = findViewById(R.id.family);
        family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (family.isChecked()) {
                    family.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820647");
                } else {
                    family.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820647");
                }

            }
        });

        minimal = findViewById(R.id.minimal);
        minimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (minimal.isChecked()) {
                    minimal.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820673");
                } else {
                    minimal.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820673");
                }

            }
        });

        warm = findViewById(R.id.warm);
        warm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (warm.isChecked()) {
                    warm.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820782");
//                    Log.d("box",warm+"");
                } else {
                    warm.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820782");
                }

            }
        });

        child = findViewById(R.id.child);
        child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (child.isChecked()) {
                    child.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add("2131820595");
//                    Log.d("box",child+"");
                } else {
                    child.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820595");
                }

            }
        });
        Log.d("themeSelect", themeSelect.toString());

    }

    public void removeTheme(String id) {
        String number = "";
        for (int i = 0; i < themeSelect.size(); i++) {
            if (id.equals(themeSelect.get(i))) {
                number = i + "";
            }
        }
        themeSelect.remove(Integer.parseInt(number));
//        Log.d("themeSelect","Remove : "+themeSelect.toString());
    }


    public void backAppoint(View v) {
//        Intent intent = new Intent(HomeHead_Theme.this, HomeHead_Appointment.class);
//        intent.putExtra("id", id + "");
//        intent.putExtra("email", email + "");
//        intent.putExtra("nEvent", nameE + "");
//        intent.putExtra("mStart", monS + "");
//        intent.putExtra("mEnd", monE + "");
//        intent.putExtra("eid", eid + "");
//        intent.putExtra("tab", 0 + "");
//        startActivity(intent);
        finish();
    }
    public void backAppoint() {
        Intent intent = new Intent(HomeHead_Theme.this, HomeHead_Appointment.class);
        intent.putExtra("id", id + "");
        intent.putExtra("email", email + "");
        intent.putExtra("nEvent", nameE + "");
        intent.putExtra("mStart", monS + "");
        intent.putExtra("mEnd", monE + "");
        intent.putExtra("eid", eid + "");
        intent.putExtra("tab", 0 + "");
        startActivity(intent);
    }

    public void visibleLinear() {
        if (checkVisible) {//show custom
            lCus.setVisibility(View.VISIBLE);
            lShort.setVisibility(View.GONE);
            btn_con.setVisibility(View.VISIBLE);
            b.setText(R.string.group_theme);
            scrollView.setScrollY(0);
            checkVisible = false;

        } else {//show group
            lCus.setVisibility(View.GONE);
            lShort.setVisibility(View.VISIBLE);
            btn_con.setVisibility(View.GONE);
            b.setText(R.string.custom);
            checkVisible = true;
        }
    }

    public void getTypeTheme(final String tyid, final int head) {
        responseStr = new HomeHead_Theme.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        nameType = new ArrayList<>();
        idType = new ArrayList<>();
        String url = "http://www.groupupdb.com/android/getthemebygroup.php";
        url += "?tyId=" + tyid;
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
                                map.put("theme_id", c.getString("theme_id"));
                                map.put("theme_name", c.getString("theme_name"));
                                MyArrList.add(map);
                            }
                            for (int i = 0; i < MyArrList.size(); i++) {
                                nameType.add(MyArrList.get(i).get("theme_name") + "\n");
                                idType.add(MyArrList.get(i).get("theme_id") + "\n");
                            }
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
        final AlertDialog viewDetail = new AlertDialog.Builder(HomeHead_Theme.this).create();
//        Log.d("themeSelect","myarr : "+MyArrList.toString());
        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                final AlertDialog viewDetail = new AlertDialog.Builder(HomeHead_Theme.this).create();
                View mView = getLayoutInflater().inflate(R.layout.layout_showtheme_dialog, null);
                final TextView headTheme = mView.findViewById(R.id.nameTheme);
                final TextView detailTheme = mView.findViewById(R.id.detailTheme);
                final TextView typeTheme = mView.findViewById(R.id.typeTheme);
                final ImageView imgTheme = mView.findViewById(R.id.imageTheme);
                final Button btn_cancle = mView.findViewById(R.id.btn_cancleTheme);
                final Button btn_custom = mView.findViewById(R.id.btn_customTheme);
                final Button btn_confirm = mView.findViewById(R.id.btn_confirmTheme);
                String s = "";
                for (int i = 0; i < nameType.size(); i++) {
                    s += " - "+nameType.get(i);
                }
                typeTheme.setText(s);
                headTheme.setText(head);
                detailTheme.setText(showDetail(tyid));
                imgTheme.setImageResource(imageDetail(tyid));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(350, 350,1);
                imgTheme.setLayoutParams(layoutParams);
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewDetail.dismiss();
                    }
                });
                btn_custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lShort.setVisibility(View.GONE);
                        lCus.setVisibility(View.VISIBLE);
                        btn_con.setVisibility(View.VISIBLE);
                        b.setText(R.string.group_theme);
                        checkVisible = false;
                        Log.d("idType", idType.toString());
                        checkCustomTheme(tyid);
                        viewDetail.dismiss();
                    }
                });
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < idType.size(); i++) {
                            sentInviteTheme(idType.get(i), eid);
                        }
//                        addDateevent();
                        backAppoint();
                    }
                });
                viewDetail.setView(mView);
                viewDetail.show();
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

    public void sentInviteTheme(String idTheme, String idEvent) {
        String url = "http://www.groupupdb.com/android/addeventtheme.php";
        url += "?tId=" + idTheme;
        url += "&eId=" + idEvent;
        Log.d("themeSelect", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
//                        Toast.makeText(HomeHead_Theme.this, response, Toast.LENGTH_SHORT).show();
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
        Log.d("themeSelect", "transid: " + transId);

        Extend_MyHelper.UpdateStateToDb(transId, 5 + "",this);
    }


    public void getTransIDByTrans(String uid, String eid, String pid) {
        responseStr = new HomeHead_Theme.ResponseStr();
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

    public void checkCustomTheme(String idType) {
        Log.d("Typeid", idType.toString());
        if (idType.equals("21")) {
            bakery.setChecked(true);
            coffee.setChecked(true);
            minimal.setChecked(true);
            cafe.setChecked(true);
            dessert.setChecked(true);
        } else if (idType.equals("22")) {
            vegan.setChecked(true);
            original.setChecked(true);
            night.setChecked(true);
            dimsum.setChecked(true);
            veget.setChecked(true);
            vegeterian.setChecked(true);
        } else if (idType.equals("15")) {
            river.setChecked(true);
            seafood.setChecked(true);
            family.setChecked(true);
            alacarte.setChecked(true);
            cozy.setChecked(true);
        } else if (idType.equals("36")) {
            warm.setChecked(true);
            sushi.setChecked(true);
            shabu.setChecked(true);
            buffet.setChecked(true);
            ramen.setChecked(true);
            bbq.setChecked(true);
            izakaya.setChecked(true);
            hotelBuf.setChecked(true);
        } else if (idType.equals("3")) {
            karaoke.setChecked(true);
            bar.setChecked(true);
            pub.setChecked(true);
            wine.setChecked(true);
            clubs.setChecked(true);
        } else if (idType.equals("23")) {
            rooftop.setChecked(true);
            cleanFood.setChecked(true);
            thaiBbq.setChecked(true);
            steak.setChecked(true);
            outdoor.setChecked(true);
        } else if (idType.equals("8")) {
            pizza.setChecked(true);
            child.setChecked(true);
            iceCream.setChecked(true);
            burger.setChecked(true);
        }
    }

    public int imageDetail(String idType) {
        Log.d("Typeid", idType.toString());
        if (idType.equals("21")) {
            return R.drawable.icon_theme_minimal;
        } else if (idType.equals("22")) {
            return R.drawable.icon_theme_classic;
        } else if (idType.equals("36")) {
            return R.drawable.icon_theme_water;
        } else if (idType.equals("15")) {
            return R.drawable.icon_theme_buffe;
        } else if (idType.equals("3")) {
            return R.drawable.icon_theme_karaoke;
        } else if (idType.equals("23")) {
            return R.drawable.icon_theme_rooftop;
        } else if (idType.equals("8")) {
            return R.drawable.icon_theme_kids;
        }
        return R.drawable.icon_theme_minimal;
    }
    public String showDetail (String idType) {
        Log.d("Typeid", idType.toString());
        if (idType.equals("21")) {
            String detail ="ร้านสไตล์มินิมอลเป็นร้านแนวคาเฟ่เบเกอร์รี่ที่มีบรรยากาศสบายๆมีมุมถ่ายรูปมากมายเน้นพบปะพูดคุยกันในหมู่เพื่อนๆ";
            return detail;
        } else if (idType.equals("22")) {
            String detail ="ร้านอาหารแนวคลาสสิคเป็นร้านอาหารที่เน้นอาหารในเชิงสุขภาพและมีบรรยากาศที่เป็นกันเองโดยสามารถพบปะพูดคุยทางการหรือเล่นๆก็ได้";
            return detail;
        } else if (idType.equals("36")) {
            String detail ="ร้านอาหารริมแม่น้ำเป็นร้านอาหารที่ให้บรรยากาศอบอุ่นและโรแมนติกในที่เดียวกันให้ความรู้สึกเป็นแบบครอบครัวเพื่อการพักผ่อนโดยเฉพาะ";
            return detail;
        } else if (idType.equals("15")) {
            String detail ="ร้านอาหารสไตล์บุฟเฟ่ห์เป็นร้านอาหารที่เน้นความคุ้มค่าและความอร่อยไปในที่เดียวกันโดยจะให้บรรยากาศเพลิดเพลินและได้รับประทานอาหารหลากหลายไปในที่เดียวกัน";
            return detail;
        } else if (idType.equals("3")) {
            String detail ="ร้านอาหารแนวคาราโอเกะเป็นร้านอาหารแนวสนุกสนานให้ความผ่อนคลายเพลิดเพลินและสนุกไปในที่เดียวกันโดยเน้นการพบปะมากกว่าการรับประทานอาหารแบบจริงจัง";
            return detail;
        } else if (idType.equals("23")) {
            String detail ="ร้านอาหารบนชั้นดาดฟ้าเป็นร้านอาหารที่ให้ความเป็นส่วนตัวและโรแมนติกเหมาะกับการออกเดทหรืองานเลี้ยงที่ต้องการความเป็นส่วนตัวและได้มองท้องฟ้าไปพร้อมๆกัน";
            return detail;
        } else if (idType.equals("8")) {
            String detail ="ร้านอาหารแนวสำหรับเด็กจะเป็นร้านอาหารที่เน้นทานง่ายมีของเล่นสำหรับเด็กเพื่อให้เด็กๆเล่นของเล่นโดยผู้ปกครองสามารถพูดคุยหรือทำงานได้ในที่เดียวกัน";
            return detail;
        }
        return "-";
    }

    public void clearCheckBox() {
        dessert.setChecked(false);
        bakery.setChecked(false);
        minimal.setChecked(false);
        coffee.setChecked(false);
        cafe.setChecked(false);
        vegan.setChecked(false);
        vegan.setChecked(false);
        original.setChecked(false);
        night.setChecked(false);
        dimsum.setChecked(false);
        veget.setChecked(false);
        vegeterian.setChecked(false);
        river.setChecked(false);
        seafood.setChecked(false);
        family.setChecked(false);
        alacarte.setChecked(false);
        cozy.setChecked(false);
        warm.setChecked(false);
        sushi.setChecked(false);
        shabu.setChecked(false);
        buffet.setChecked(false);
        ramen.setChecked(false);
        bbq.setChecked(false);
        izakaya.setChecked(false);
        hotelBuf.setChecked(false);
        karaoke.setChecked(false);
        bar.setChecked(false);
        pub.setChecked(false);
        wine.setChecked(false);
        clubs.setChecked(false);
        rooftop.setChecked(false);
        cleanFood.setChecked(false);
        thaiBbq.setChecked(false);
        steak.setChecked(false);
        outdoor.setChecked(false);
        pizza.setChecked(false);
        child.setChecked(false);
        iceCream.setChecked(false);
        burger.setChecked(false);
    }
//    private void addDateevent(){
//        String url = "http://www.groupupdb.com/android/caldateforheader.php";
//        url += "?eid=" + eid+"";
//        url += "&dLw=" + calwait(Integer.parseInt(wait))+"";
//        Log.d("testAPi",url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("testAPi","addDaevent "+response);
//                        Toast.makeText(HomeHead_Theme.this, "addDaevent Finish", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
////                            Toast.makeText(this,
////                                    error.getString(R.string.error_network_timeout),
////                                    Toast.LENGTH_LONG).show();
//                            Log.d("testAPi","TimeoutError"+error.getMessage());
////                            Log.d("testAPi",error.getMessage());
//                        } else if (error instanceof AuthFailureError) {
//                            //TODO
//                            Log.d("testAPi","AuthFailureError"+error.getMessage());
//                        } else if (error instanceof ServerError) {
//                            Log.d("testAPi","ServerError"+error.getMessage());
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            Log.d("testAPi","NetworkError"+error.getMessage());
//                            //TODO
//                        } else if (error instanceof ParseError) {
//                            //TODO
//                            Log.d("testAPi","ParseError"+error.getMessage());
//                        } else{
//                            Log.d("testAPi","else "+error.getMessage());
//                        }
//                    }
//                });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                6000000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue queue = Volley.newRequestQueue(HomeHead_Theme.this);
//        queue.add(stringRequest);
//    }

    public String calwait(int wait){
        Calendar cal = Calendar.getInstance();
        Date today1 = cal.getTime();
        cal.add(Calendar.DATE, wait); // to get previous year add -1
        Date nextYear1 = cal.getTime();
        DateFormat simpleNoHour = new SimpleDateFormat("yyyy-MM-dd");
        simpleNoHour.format(nextYear1);
        Log.d("wait",nextYear1+"");
        return simpleNoHour.format(nextYear1)+"";
    }

}
