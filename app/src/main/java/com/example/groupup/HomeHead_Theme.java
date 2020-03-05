package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class HomeHead_Theme extends AppCompatActivity {

    ArrayList<Integer> themeSelect = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        check();

    }

    public  void  check(){
        final CheckBox cafe = findViewById(R.id.cafe);
        cafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cafe.isChecked()){
                    cafe.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(cafe.getId());
                }else {
                    cafe.setBackgroundResource(R.drawable.my_style);
                    removeTheme(cafe.getId());
                }

            }
        });

        final CheckBox coffee = findViewById(R.id.coffee_tea);
        coffee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(coffee.isChecked()){
                    coffee.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(coffee.getId());
                }else {
                    coffee.setBackgroundResource(R.drawable.my_style);
                    removeTheme(coffee.getId());
                }

            }
        });

        final CheckBox river = findViewById(R.id.riverside);
        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(river.isChecked()){
                    river.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(river.getId());
                }else {
                    river.setBackgroundResource(R.drawable.my_style);
                    removeTheme(river.getId());
                }

            }
        });

        final CheckBox karaoke = findViewById(R.id.karaoke);
        karaoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(karaoke.isChecked()){
                    karaoke.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(karaoke.getId());
                }else {
                    karaoke.setBackgroundResource(R.drawable.my_style);
                    removeTheme(karaoke.getId());
                }

            }
        });

        final CheckBox clubs = findViewById(R.id.clubs);
        clubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(clubs.isChecked()){
                    clubs.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(clubs.getId());
                }else {
                    clubs.setBackgroundResource(R.drawable.my_style);
                    removeTheme(clubs.getId());
                }

            }
        });

        final CheckBox pub = findViewById(R.id.pub);
        pub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(pub.isChecked()){
                    pub.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(pub.getId());
                }else {
                    pub.setBackgroundResource(R.drawable.my_style);
                    removeTheme(pub.getId());
                }

            }
        });

        final CheckBox wine = findViewById(R.id.wine_bar);
        wine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(wine.isChecked()){
                    wine.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(wine.getId());
                }else {
                    wine.setBackgroundResource(R.drawable.my_style);
                    removeTheme(wine.getId());
                }

            }
        });

        final CheckBox night = findViewById(R.id.late_night_rice);
        night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(night.isChecked()){
                    night.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(night.getId());
                }else {
                    night.setBackgroundResource(R.drawable.my_style);
                    removeTheme(night.getId());
                }

            }
        });

        final CheckBox vegeterian = findViewById(R.id.vegeterian);
        vegeterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(vegeterian.isChecked()){
                    vegeterian.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(vegeterian.getId());
                }else {
                    vegeterian.setBackgroundResource(R.drawable.my_style);
                    removeTheme(vegeterian.getId());
                }

            }
        });

        final CheckBox hotelBuf = findViewById(R.id.hotel_buffet);
        hotelBuf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(hotelBuf.isChecked()){
                    hotelBuf.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(hotelBuf.getId());
                }else {
                    karaoke.setBackgroundResource(R.drawable.my_style);
                    removeTheme(hotelBuf.getId());
                }

            }
        });

        final CheckBox rooftop = findViewById(R.id.rooftop);
        rooftop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rooftop.isChecked()){
                    rooftop.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(rooftop.getId());
                }else {
                    rooftop.setBackgroundResource(R.drawable.my_style);
                    removeTheme(rooftop.getId());
                }

            }
        });

        final CheckBox izakaya = findViewById(R.id.izakaya);
        izakaya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(izakaya.isChecked()){
                    izakaya.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(izakaya.getId());
                }else {
                    izakaya.setBackgroundResource(R.drawable.my_style);
                    removeTheme(izakaya.getId());
                }

            }
        });

        final CheckBox dessert = findViewById(R.id.dessert);
        dessert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(dessert.isChecked()){
                    dessert.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(dessert.getId());
                }else {
                    dessert.setBackgroundResource(R.drawable.my_style);
                    removeTheme(dessert.getId());
                }

            }
        });

        final CheckBox alacarte = findViewById(R.id.alacarte);
        alacarte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(alacarte.isChecked()){
                    alacarte.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(alacarte.getId());
                }else {
                    alacarte.setBackgroundResource(R.drawable.my_style);
                    removeTheme(alacarte.getId());
                }

            }
        });

        final CheckBox seafood = findViewById(R.id.seafood);
        seafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(seafood.isChecked()){
                    seafood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(seafood.getId());
                }else {
                    seafood.setBackgroundResource(R.drawable.my_style);
                    removeTheme(seafood.getId());
                }

            }
        });

        final CheckBox steak = findViewById(R.id.steak);
        steak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(steak.isChecked()){
                    steak.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(steak.getId());
                }else {
                    steak.setBackgroundResource(R.drawable.my_style);
                    removeTheme(steak.getId());
                }

            }
        });

        final CheckBox iceCream = findViewById(R.id.iceCream);
        iceCream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(iceCream.isChecked()){
                    iceCream.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(iceCream.getId());
                }else {
                    iceCream.setBackgroundResource(R.drawable.my_style);
                    removeTheme(iceCream.getId());
                }

            }
        });

        final CheckBox bakery = findViewById(R.id.bakery_cake);
        bakery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bakery.isChecked()){
                    bakery.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(bakery.getId());
                }else {
                    bakery.setBackgroundResource(R.drawable.my_style);
                    removeTheme(bakery.getId());
                }

            }
        });

        final CheckBox bbq = findViewById(R.id.bbq);
        bbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bbq.isChecked()){
                    bbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(bbq.getId());
                }else {
                    bbq.setBackgroundResource(R.drawable.my_style);
                    removeTheme(bbq.getId());
                }

            }
        });

        final CheckBox shabu = findViewById(R.id.shabu);
        shabu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(shabu.isChecked()){
                    shabu.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(shabu.getId());
                }else {
                    shabu.setBackgroundResource(R.drawable.my_style);
                    removeTheme(shabu.getId());
                }

            }
        });

        final CheckBox buffet = findViewById(R.id.buffet);
        buffet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(buffet.isChecked()){
                    buffet.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(buffet.getId());
                }else {
                    buffet.setBackgroundResource(R.drawable.my_style);
                    removeTheme(buffet.getId());
                }

            }
        });

        final CheckBox cleanFood = findViewById(R.id.cleanFood);
        cleanFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cleanFood.isChecked()){
                    cleanFood.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(cleanFood.getId());
                }else {
                    cleanFood.setBackgroundResource(R.drawable.my_style);
                    removeTheme(cleanFood.getId());
                }

            }
        });

        final CheckBox thaiBbq = findViewById(R.id.thai_bbq);
        thaiBbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(thaiBbq.isChecked()){
                    thaiBbq.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(thaiBbq.getId());
                }else {
                    thaiBbq.setBackgroundResource(R.drawable.my_style);
                    removeTheme(thaiBbq.getId());
                }

            }
        });

        final CheckBox pizza = findViewById(R.id.pizza);
        pizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(pizza.isChecked()){
                    pizza.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(pizza.getId());
                }else {
                    pizza.setBackgroundResource(R.drawable.my_style);
                    removeTheme(pizza.getId());
                }

            }
        });

        final CheckBox sushi = findViewById(R.id.sushi);
        sushi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(sushi.isChecked()){
                    sushi.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(sushi.getId());
                }else {
                    sushi.setBackgroundResource(R.drawable.my_style);
                    removeTheme(sushi.getId());
                }

            }
        });

        final CheckBox burger = findViewById(R.id.burger);
        burger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(burger.isChecked()){
                    burger.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(burger.getId());
                }else {
                    burger.setBackgroundResource(R.drawable.my_style);
                    removeTheme(burger.getId());
                }

            }
        });

        final CheckBox ramen = findViewById(R.id.ramen);
        ramen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(ramen.isChecked()){
                    ramen.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(ramen.getId());
                }else {
                    ramen.setBackgroundResource(R.drawable.my_style);
                    removeTheme(ramen.getId());
                }

            }
        });

        final CheckBox dimsum = findViewById(R.id.dimsum);
        dimsum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(dimsum.isChecked()){
                    dimsum.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(dimsum.getId());
                }else {
                    dimsum.setBackgroundResource(R.drawable.my_style);
                    removeTheme(dimsum.getId());
                }

            }
        });

        final CheckBox vegan = findViewById(R.id.vegan);
        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(vegan.isChecked()){
                    vegan.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(vegan.getId());
                }else {
                    vegan.setBackgroundResource(R.drawable.my_style);
                    removeTheme(vegan.getId());
                }

            }
        });

        final CheckBox veget = findViewById(R.id.vegeterian2);
        veget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(veget.isChecked()){
                    veget.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(veget.getId());
                }else {
                    veget.setBackgroundResource(R.drawable.my_style);
                    removeTheme(veget.getId());
                }

            }
        });

        final CheckBox original = findViewById(R.id.original);
        original.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(original.isChecked()){
                    original.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(original.getId());
                }else {
                    original.setBackgroundResource(R.drawable.my_style);
                    removeTheme(original.getId());
                }

            }
        });

        final CheckBox bar = findViewById(R.id.bar);
        bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(bar.isChecked()){
                    bar.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(bar.getId());
                }else {
                    bar.setBackgroundResource(R.drawable.my_style);
                    removeTheme(bar.getId());
                }

            }
        });

        final CheckBox outdoor = findViewById(R.id.outdoor);
        outdoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(outdoor.isChecked()){
                    outdoor.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(outdoor.getId());
                }else {
                    outdoor.setBackgroundResource(R.drawable.my_style);
                    removeTheme(outdoor.getId());
                }

            }
        });

        final CheckBox cozy = findViewById(R.id.cozy);
        cozy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cozy.isChecked()){
                    cozy.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(cozy.getId());
                }else {
                    cozy.setBackgroundResource(R.drawable.my_style);
                    removeTheme(cozy.getId());
                }

            }
        });

        final CheckBox family = findViewById(R.id.family);
        family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(family.isChecked()){
                    family.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(family.getId());
                }else {
                    family.setBackgroundResource(R.drawable.my_style);
                    removeTheme(family.getId());
                }

            }
        });

        final CheckBox minimal = findViewById(R.id.minimal);
        minimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(minimal.isChecked()){
                    minimal.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(minimal.getId());
                }else {
                    minimal.setBackgroundResource(R.drawable.my_style);
                    removeTheme(minimal.getId());
                }

            }
        });

        final CheckBox warm = findViewById(R.id.warm);
        warm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(warm.isChecked()){
                    warm.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(warm.getId());
//                    Log.d("box",warm.getId()+"");
                }else {
                    warm.setBackgroundResource(R.drawable.my_style);
                    removeTheme(warm.getId());
                }

            }
        });

        final CheckBox child = findViewById(R.id.child);
        child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(child.isChecked()){
                    child.setBackgroundResource(R.color.blueWhite);
                    themeSelect.add(child.getId());
//                    Log.d("box",child.getId()+"");
                }else {
                    child.setBackgroundResource(R.drawable.my_style);
                    removeTheme(child.getId());
                }

            }
        });

    }

    public void removeTheme(int id){
        String number = "";
        for (int i =0; i< themeSelect.size(); i++){
            if (id == themeSelect.get(i)){
                number = i+"";
            }
        }
        themeSelect.remove(Integer.parseInt(number));
    }

    public void backAppoint(View v) {
        Intent intent = new Intent(HomeHead_Theme.this, HomeHead_Appointment.class);
        startActivity(intent);
    }

}
