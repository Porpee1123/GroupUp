package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

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

public class HomeHead_Theme extends AppCompatActivity {
    HomeHead_Theme.ResponseStr responseStr = new HomeHead_Theme.ResponseStr();
    String id,eid,nameE,monS,monE,email;
    ArrayList<Integer> themeSelect = new ArrayList<>();
    Button b,btn_con;
    LinearLayout lShort,lCus;
    ScrollView scrollView;
    ImageView img_minimal,img_classic,img_buffet,img_river,img_karaoke,img_sky,img_kid;
    boolean checkVisible;
    ArrayList<String>  name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        lShort =findViewById(R.id.linear_shortcut);
        lCus =findViewById(R.id.linear_custom);
        b = findViewById(R.id.btn_customTheme);
        btn_con = findViewById(R.id.btn_confirmCustom);
        scrollView = findViewById(R.id.scroll_theme);
        lCus.setVisibility(View.GONE);
        btn_con.setVisibility(View.GONE);
        img_minimal= findViewById(R.id.theme_minimal);
        img_classic= findViewById(R.id.theme_classic);
        img_buffet=findViewById(R.id.theme_buffet);
        img_river=findViewById(R.id.theme_river);
        img_karaoke=findViewById(R.id.theme_karaoke);
        img_sky=findViewById(R.id.theme_sky);
        img_kid=findViewById(R.id.theme_kid);
        checkVisible = true;//close custom
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleLinear();
            }
        });
        check();
        img_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(21+"",R.string.minimal);
            }
        });
        img_classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(22+"",R.string.original);
            }
        });
        img_buffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(15+"",R.string.family);
            }
        });
        img_river.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(36+"",R.string.warm);
            }
        });
        img_karaoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(3+"",R.string.bar);
            }
        });
        img_sky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(23+"",R.string.rooftop);
            }
        });
        img_kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getTypeTheme(8+"",R.string.kids);
            }
        });

    }

    public  void  check(){

        final CheckBox cafe = findViewById(R.id.cafe);
        cafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cafe.isChecked()){
                    cafe.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.cafe);
//                    Log.d("themeSelect",
//                            cafe.getText()+"--"+R.string.cafe+"");

                }else {
                    cafe.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.cafe);
                }

            }
        });

        final CheckBox coffee = findViewById(R.id.coffee_tea);
        coffee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(coffee.isChecked()){
                    coffee.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.coffee_tea);
                }else {
                    coffee.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.coffee_tea
                    );
                }

            }
        });

        final CheckBox river = findViewById(R.id.riverside);
        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(river.isChecked()){
                    river.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.riverside);
                }else {
                    river.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.riverside);
                }

            }
        });

        final CheckBox karaoke = findViewById(R.id.karaoke);
        karaoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(karaoke.isChecked()){
                    karaoke.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.karaoke);
                }else {
                    karaoke.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.karaoke);
                }

            }
        });

        final CheckBox clubs = findViewById(R.id.clubs);
        clubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(clubs.isChecked()){
                    clubs.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.clubs);
                }else {
                    clubs.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.clubs);
                }

            }
        });

        final CheckBox pub = findViewById(R.id.pub);
        pub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(pub.isChecked()){
                    pub.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.pub);
                }else {
                    pub.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.pub);
                }

            }
        });

        final CheckBox wine = findViewById(R.id.wine_bar);
        wine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(wine.isChecked()){
                    wine.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.wine_Bar);
                }else {
                    wine.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.wine_Bar);
                }

            }
        });

        final CheckBox night = findViewById(R.id.late_night_rice);
        night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(night.isChecked()){
                    night.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.late_night_rice);
                }else {
                    night.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.late_night_rice);
                }

            }
        });

        final CheckBox vegeterian = findViewById(R.id.vegeterian);
        vegeterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(vegeterian.isChecked()){
                    vegeterian.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.vegeterian);
                }else {
                    vegeterian.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.vegeterian);
                }

            }
        });

        final CheckBox hotelBuf = findViewById(R.id.hotel_buffet);
        hotelBuf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(hotelBuf.isChecked()){
                    hotelBuf.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.hotel_buffet);
                }else {
                    karaoke.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.hotel_buffet);
                }

            }
        });

        final CheckBox rooftop = findViewById(R.id.rooftop);
        rooftop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rooftop.isChecked()){
                    rooftop.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.rooftop);
                }else {
                    rooftop.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.rooftop);
                }

            }
        });

        final CheckBox izakaya = findViewById(R.id.izakaya);
        izakaya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(izakaya.isChecked()){
                    izakaya.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.izakaya);
                }else {
                    izakaya.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.izakaya);
                }

            }
        });

        final CheckBox dessert = findViewById(R.id.dessert);
        dessert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(dessert.isChecked()){
                    dessert.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.dessert);
                }else {
                    dessert.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.dessert);
                }

            }
        });

        final CheckBox alacarte = findViewById(R.id.alacarte);
        alacarte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(alacarte.isChecked()){
                    alacarte.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.alacarte);
                }else {
                    alacarte.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.alacarte);
                }

            }
        });

        final CheckBox seafood = findViewById(R.id.seafood);
        seafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(seafood.isChecked()){
                    seafood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.seafood);
                }else {
                    seafood.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.seafood);
                }

            }
        });

        final CheckBox steak = findViewById(R.id.steak);
        steak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(steak.isChecked()){
                    steak.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.steak);
                }else {
                    steak.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.steak);
                }

            }
        });

        final CheckBox iceCream = findViewById(R.id.iceCream);
        iceCream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(iceCream.isChecked()){
                    iceCream.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.iceCream);
                }else {
                    iceCream.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.iceCream);
                }

            }
        });

        final CheckBox bakery = findViewById(R.id.bakery_cake);
        bakery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bakery.isChecked()){
                    bakery.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.bakery_cake);
                }else {
                    bakery.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.bakery_cake);
                }

            }
        });

        final CheckBox bbq = findViewById(R.id.bbq);
        bbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bbq.isChecked()){
                    bbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.bbq);
                }else {
                    bbq.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.bbq);
                }

            }
        });

        final CheckBox shabu = findViewById(R.id.shabu);
        shabu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(shabu.isChecked()){
                    shabu.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.shabu);
                }else {
                    shabu.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.shabu);
                }

            }
        });

        final CheckBox buffet = findViewById(R.id.buffet);
        buffet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(buffet.isChecked()){
                    buffet.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.buffet);
                }else {
                    buffet.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.buffet);
                }

            }
        });

        final CheckBox cleanFood = findViewById(R.id.cleanFood);
        cleanFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cleanFood.isChecked()){
                    cleanFood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.cleanFood);
                }else {
                    cleanFood.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.cleanFood);
                }

            }
        });

        final CheckBox thaiBbq = findViewById(R.id.thai_bbq);
        thaiBbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(thaiBbq.isChecked()){
                    thaiBbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.thai_bbq);
                }else {
                    thaiBbq.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.thai_bbq);
                }

            }
        });

        final CheckBox pizza = findViewById(R.id.pizza);
        pizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(pizza.isChecked()){
                    pizza.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.pizza);
                }else {
                    pizza.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.pizza);
                }

            }
        });

        final CheckBox sushi = findViewById(R.id.sushi);
        sushi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(sushi.isChecked()){
                    sushi.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.sushi);
                }else {
                    sushi.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.sushi);
                }

            }
        });

        final CheckBox burger = findViewById(R.id.burger);
        burger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(burger.isChecked()){
                    burger.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.burger);
                }else {
                    burger.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.burger);
                }

            }
        });

        final CheckBox ramen = findViewById(R.id.ramen);
        ramen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(ramen.isChecked()){
                    ramen.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.ramen);
                }else {
                    ramen.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.ramen);
                }

            }
        });

        final CheckBox dimsum = findViewById(R.id.dimsum);
        dimsum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(dimsum.isChecked()){
                    dimsum.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.dimsum);
                }else {
                    dimsum.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.dimsum);
                }

            }
        });

        final CheckBox vegan = findViewById(R.id.vegan);
        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(vegan.isChecked()){
                    vegan.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.vegan);
                }else {
                    vegan.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.vegan);
                }

            }
        });

        final CheckBox veget = findViewById(R.id.vegeterian2);
        veget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(veget.isChecked()){
                    veget.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.vegeterian);
                }else {
                    veget.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.vegeterian);
                }

            }
        });

        final CheckBox original = findViewById(R.id.original);
        original.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(original.isChecked()){
                    original.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.original);
                }else {
                    original.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.original);
                }

            }
        });

        final CheckBox bar = findViewById(R.id.bar);
        bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bar.isChecked()){
                    bar.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.bar);
                }else {
                    bar.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.bar);
                }

            }
        });

        final CheckBox outdoor = findViewById(R.id.outdoor);
        outdoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(outdoor.isChecked()){
                    outdoor.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.outdoor);
                }else {
                    outdoor.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.outdoor);
                }

            }
        });

        final CheckBox cozy = findViewById(R.id.cozy);
        cozy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cozy.isChecked()){
                    cozy.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.cozy);
                }else {
                    cozy.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.cozy);
                }

            }
        });

        final CheckBox family = findViewById(R.id.family);
        family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(family.isChecked()){
                    family.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.family);
                }else {
                    family.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.family);
                }

            }
        });

        final CheckBox minimal = findViewById(R.id.minimal);
        minimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(minimal.isChecked()){
                    minimal.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.minimal);
                }else {
                    minimal.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.minimal);
                }

            }
        });

        final CheckBox warm = findViewById(R.id.warm);
        warm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(warm.isChecked()){
                    warm.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.warm);
//                    Log.d("box",warm+"");
                }else {
                    warm.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.warm);
                }

            }
        });

        final CheckBox child = findViewById(R.id.child);
        child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(child.isChecked()){
                    child.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(R.string.child);
//                    Log.d("box",child+"");
                }else {
                    child.setBackgroundResource(R.drawable.my_style);
                    removeTheme(R.string.child);
                }

            }
        });
        Log.d("themeSelect",themeSelect.toString());

    }
    public void removeTheme(int id){
        String number = "";
        for (int i =0; i< themeSelect.size(); i++){
            if (id == themeSelect.get(i)){
                number = i+"";
            }
        }
        themeSelect.remove(Integer.parseInt(number));
        Log.d("themeSelect","Remove : "+themeSelect.toString());
    }
    public void backAppoint(View v) {
        Intent intent = new Intent(HomeHead_Theme.this, HomeHead_Appointment.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        startActivity(intent);
    }
    public void visibleLinear(){
        if (checkVisible){//show custom
            lCus.setVisibility(View.VISIBLE);
            lShort.setVisibility(View.GONE);
            btn_con.setVisibility(View.VISIBLE);
            b.setText(R.string.group_theme);
            scrollView.setScrollY(0);
            checkVisible=false;

        }else {//show group
            lCus.setVisibility(View.GONE);
            lShort.setVisibility(View.VISIBLE);
            btn_con.setVisibility(View.GONE);
            b.setText(R.string.custom);
            checkVisible=true;
        }
    }
    public void getTypeTheme(String tyid, final int head) {
        responseStr = new HomeHead_Theme.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        name = new ArrayList<>();
        String url = "http://www.groupupdb.com/android/getthemebygroup.php";
        url += "?tyId=" + tyid;//รอเอาIdจากfirebase
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
                            for (int i=0;i<MyArrList.size();i++){
                                name.add(MyArrList.get(i).get("theme_name")+"\n");
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
        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(HomeHead_Theme.this);
//        Log.d("themeSelect","myarr : "+MyArrList.toString());
        new CountDownTimer(300,300){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                viewDetail.setTitle(head);
                Log.d("themeSelect","myarr : "+name.toString());
                String s="";
                for (int i=0;i<name.size();i++){
                    s+= name.get(i);
                }
                viewDetail.setMessage(s+"\n");
                viewDetail.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                viewDetail.setPositiveButton(R.string.btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
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

}
