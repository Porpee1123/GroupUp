package com.example.groupup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Extend_MyHelper {
    public static void checkInternetLost(Context context){
        //////////////////////check status internet///////////////////////
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //เมื่อมีการเชื่อมต่ออินเตอร์เน็ต
//            Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            //ไม่มีการเชื่อมต่อ
        }
        //////////////////////check status internet///////////////////////
    }
}
