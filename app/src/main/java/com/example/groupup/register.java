package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.FileOutputStream;

public class register extends AppCompatActivity {
    final int PIC_CROP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final ImageButton btn3 = (ImageButton) findViewById(R.id.addPicture);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                startActivityForResult(intent, 2);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,

                        filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmpPic = BitmapFactory.decodeFile(imgDecodableString);
                Bitmap dstBmp;
                if (bmpPic.getWidth() >= bmpPic.getHeight()){

                    dstBmp = Bitmap.createBitmap(
                            bmpPic,
                            bmpPic.getWidth()/2 - bmpPic.getHeight()/2,
                            0,
                            bmpPic.getHeight(),
                            bmpPic.getHeight()
                    );

                }else{

                    dstBmp = Bitmap.createBitmap(
                            bmpPic,
                            0,
                            bmpPic.getHeight()/2 - bmpPic.getWidth()/2,
                            bmpPic.getWidth(),
                            bmpPic.getWidth()
                    );
                }
                Bitmap lbp =scaleDown(dstBmp,300,false);
                ImageButton img = findViewById(R.id.addPicture);
                img.setImageBitmap(lbp);

            } catch (Exception e) {
                Log.e("Log", "Error from Gallery Activity");
            }

        }
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public boolean SaveData(){
        final EditText txtName = (EditText)findViewById(R.id.name);
        final EditText txtEmail = (EditText)findViewById(R.id.email);
        final EditText txtId = (EditText)findViewById(R.id.id);
        final EditText txtPhoto = (EditText)findViewById(R.id.id);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle(R.string.err_title);
//        dialog.setIcon(android.R.drawable.btn_star_big_on);
//        dialog.setPositiveButton("Close", null);
//        if(txtName.getText().length() == 0){
//            dialog.setMessage(R.string.input_name);
//            dialog.show();
//            txtName.requestFocus();
//            return false;
//        }
//        if(txtMsg.getText().length() == 0){
//            dialog.setMessage(R.string.input_msg);
//            dialog.show();
//            txtMsg.requestFocus();
//            return false;
//        }
//        if(txtAmt.getText().length() == 0) {
//            dialog.setMessage(R.string.input_amt);
//            dialog.show();
//            txtAmt.requestFocus();
//            return false;
//        }
        String url = "http://www.groupupdb.com/android/register.php";
        url += "?sName=" + txtName.getText().toString();
        url += "&sMsg=" + txtEmail.getText().toString();
        url += "&sAmt=" + txtId.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //str = new String(response, StandardCharsets.UTF_8);
                        //String reader = new String(response, StandardCharsets.UTF_8);
                        try {
                            String strStatusID = "0";
                            String strError = "Unknow Status!";
                            JSONObject c;
                            JSONArray data = new JSONArray("["+response.toString()+"]");
                            for(int i = 0; i < data.length(); i++){
                                c = data.getJSONObject(i);
                                strStatusID = c.getString("StatusID");
                                strError = c.getString("Error");
                            }
                            if(strStatusID.equals("0")){
                                dialog.setMessage(strError);
                                dialog.show();
                            } else {
//                                dialog.setTitle(R.string.submit_title);
//                                dialog.setMessage(R.string.submit_result);
                                dialog.show();
                                txtName.setText("");
                                txtEmail.setText("");
                                txtId.setText("");
//                                txtNote.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(register.this,"Submission Error!" ,Toast.LENGTH_LONG).show();                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():"+error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        return true;
    }
}
