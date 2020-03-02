package com.example.groupup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Extend_Fragment_ScanQRCode extends Fragment {
    String emailScan;
    private ZXingScannerView zXingScannerView;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //QR Controller
                zXingScannerView = new ZXingScannerView(getActivity());
                getActivity().setContentView(zXingScannerView);
                zXingScannerView.startCamera();
                zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {
                        getActivity().setContentView(R.layout.activity_scan_qr);
                        String resultString = result.getText().toString();
                        emailScan =resultString;
                        StringTokenizer st = new StringTokenizer(emailScan,",");
                        ArrayList<String> qr = new ArrayList<>();
                        while (st.hasMoreTokens()){
                            qr.add(st.nextToken());
                        }
//                        Toast.makeText(getActivity(), "QR code = " + resultString, Toast.LENGTH_LONG).show();
                        Log.d("scanQR", "QR code ==> " + resultString);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment, new Extend_Fragment_ScanQRCode()).commit();
                        Intent intent = new Intent(getActivity(), ManageFriend_AddFriends.class);
                        intent.putExtra("emailScan", qr.get(0)+"");
                        intent.putExtra("id", qr.get(1)+"");
                        startActivity(intent);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_qr, container, false);
        return view;
    }

    public static String scanQRImage(Bitmap bMap) {
        String contents = null;

        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        }
        catch (Exception e) {
            Log.e("QrTest", "Error decoding barcode", e);
        }
        return contents;
    }

}
