package com.example.groupup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Extend_MyHelper {
    public static void checkInternetLost(Context context) {
        //////////////////////check status internet///////////////////////
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
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

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        String urlImg;
        ImageView imb;
        float imgSize;

        public SendHttpRequestTask(String url, ImageView image, float size) {
            urlImg = url;
            imb = image;
            imgSize = size;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(urlImg);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bitmap lbp = scaleDown(myBitmap, imgSize, false);
                Log.d("http123", connection.toString());
                return lbp;

            } catch (Exception e) {
                Log.d("http123", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imb.setImageBitmap(result);
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static void UpdateStateToDb(String transId, String statusId, Context context) {
        String url = "http://www.groupupdb.com/android/acceptEvent.php";
        url += "?tId=" + transId;
        url += "&stId=" + statusId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updatedb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public static String checkStatusTrans(String eId, String stateId, Context context, final TextView edt) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/checkpeopleinstate.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        url += "&stId=" + stateId;
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
                                map.put("num", c.getString("num"));
                                MyArrList.add(map);
                            }
                            state[0] = MyArrList.get(0).get("num");
                            edt.setText(state[0]);
                            Log.d("checkStatus", state[0]);
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
        return state[0];
    }

    public static String getDayFromDateString(String stringDate, String dateTimeFormat) {
        String[] daysArray = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
        String day = "";

        int dayOfWeek = 0;
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return day;
    }

    public static void UpdateAllState(String eId, String statusId, String priId, Context context) {
        String url = "http://www.groupupdb.com/android/updateallstatusinevent.php";
        url += "?eid=" + eId;
        url += "&pri=" + priId;
        url += "&stId=" + statusId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updatedb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public static String checkStatususer(String eId, String uid, String pri, Context context, final TextView edt) {
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        final String[] state = {""};
        String url = "http://www.groupupdb.com/android/getStatusAppoint.php";
        url += "?eId=" + eId;//ร  อเอาIdหรือ email จากfirebase
        url += "&uId=" + uid;
        url += "&prId=" + pri;
        Log.d("checkStatus", "url " + url);
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
                                map.put("states_name", c.getString("states_name"));
                                MyArrList.add(map);
                            }
                            state[0] = MyArrList.get(0).get("states_name");
                            edt.setText("สถานะ: " + state[0]);
                            Log.d("checkStatus", "status appoint " + state[0]);
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
        return state[0];
    }

    public static void sentInviteFCMPerson(String uId,String eId, String priId, String title, String body, String intent, Context context) {
        String url = "http://www.groupupdb.com/android/fcm_request.php";
        url += "?eid=" + eId;
        url += "&uid=" + uId;
        url += "&pri=" + priId;
        url += "&title=" + title;
        url += "&body=" + body;
        url += "&intent=" + intent;
        Log.d("Extend Myhelper","url : "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updatedb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    public static void sentInviteFCMPlace(String pId ,String title, String body, String intent, Context context) {
        String url = "http://www.groupupdb.com/android/fcm_requestPlace.php";
        url += "?pid=" + pId;
        url += "&title=" + title;
        url += "&body=" + body;
        url += "&intent=" + intent;
        Log.d("Extend Myhelper","url : "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updatedb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }


}
