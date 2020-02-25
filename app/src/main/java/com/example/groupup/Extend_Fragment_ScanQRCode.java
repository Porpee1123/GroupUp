package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;

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
//                        Toast.makeText(getActivity(), "QR code = " + resultString, Toast.LENGTH_LONG).show();
                        Log.d("scanQR", "QR code ==> " + resultString);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment, new Extend_Fragment_ScanQRCode()).commit();
                        Intent intent = new Intent(getActivity(), ManageFriend_AddFriends.class);
                        intent.putExtra("emailScan", emailScan+"");
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

}
