package com.example.groupup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class register extends AppCompatActivity {
    final int PIC_CROP = 1;
    final int READ_EXTERNAL_PERMISSION_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final ImageButton btn3 = (ImageButton) findViewById(R.id.addPicture);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(register.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(register.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else {
                    requestImagePermission();
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
                }


            }
        });
        final Button bcon = (Button)findViewById(R.id.account_confirm);
        bcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(SaveData()) {
                    Intent intent = new Intent(register.this, home.class);
                    startActivity(intent);
//                }
            }
        });
//        final ImageButton btn = (ImageButton) findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(SaveData()){
//                    if (v != null) {
//                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                        final EditText txtMsg = (EditText)findViewById(R.id.msg);
//                        txtMsg.requestFocus();
//                    }
//                }
//            }
//        });

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
    public boolean SaveData(){
        final EditText txtName = (EditText)findViewById(R.id.name);
        final EditText txtEmail = (EditText)findViewById(R.id.email);
        final ImageButton txtImage = (ImageButton)findViewById(R.id.addPicture);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.err_title);
        dialog.setIcon(android.R.drawable.btn_star_big_on);
        dialog.setPositiveButton("Close", null);
        if(txtName.getText().length() == 0){
            dialog.setMessage(R.string.err_input_name);
            dialog.show();
            txtName.requestFocus();
            return false;
        }
        if(txtEmail.getText().length() == 0){
            dialog.setMessage(R.string.err_input_email);
            dialog.show();
            txtEmail.requestFocus();
            return false;
        }
        String url = "http://www.groupupdb.com/android/register.php";
        url += "?sName=" + txtName.getText().toString();
        url += "&sEmail=" + txtEmail.getText().toString();
        url += "&sImage=" + txtImage.getBackground().toString();
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
                                dialog.setTitle(R.string.submit_title);
                                dialog.setMessage(R.string.submit_result);
                                dialog.show();
                                txtName.setText("");
                                txtEmail.setText("");
                                txtImage.setBackground(null);
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
    public void requestImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the gallery")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    })
//                    .setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            })
                    .create().show();
//            Toast.makeText(register.this, "This permission is needed for access the gallery", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }
}
