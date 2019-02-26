package com.attendance.checkin.checkinforwork.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class FragmentShowQR extends Fragment implements View.OnClickListener {

    private ImageView img_qrcode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_qr,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        fragmentManager  =  getActivity().getSupportFragmentManager();
        v.findViewById(R.id.btn_back).setOnClickListener(this);
        img_qrcode = v.findViewById(R.id.img_qrcode);
        sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String uid = sharedPreferences.getString(MyFer.USER_ID,"");
        String name = sharedPreferences.getString(MyFer.USERNAME,"");
        String password = sharedPreferences.getString(MyFer.PASSWORD,"");

        //generate qr code
        String text= uid+"-"+name+"-"+password;// Whatever you need to encode in the QR code

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                fragmentManager.popBackStack();
                break;
        }
    }
}
