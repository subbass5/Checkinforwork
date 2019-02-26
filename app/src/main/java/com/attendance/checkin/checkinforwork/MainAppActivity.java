package com.attendance.checkin.checkinforwork;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackCheckinListenner;
import com.attendance.checkin.checkinforwork.Fragment.FragmentMainApp;
import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;

public class MainAppActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    private ProgressDialog progressDialog;
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String  user_id ="";
    private static final String TAG = "MAINAPPACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayPrimaryColorMain, true);
        setContentView(R.layout.activity_mainapp);
        getSupportActionBar().hide();
        fragmentManager = getSupportFragmentManager();

        fragmentTran(new FragmentMainApp());
        context = MainAppActivity.this;

        sharedPreferences = getSharedPreferences(MyFer.MY_FER,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id  = sharedPreferences.getString(MyFer.USER_ID,"");

        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
//                Toast.makeText(context, "Do", Toast.LENGTH_SHORT).show();
                Log.e("RUN","do at "+getCurrentTimeStamp());
                if(getCurrentTimeStamp().equals("08:00")){
                    Notification notification =
                            new NotificationCompat.Builder(context) // this is context
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("คำเตือน")
                                    .setContentText("ใกล้ถึงเวลาเข้างานแล้ว")
                                    .setAutoCancel(true)
                                    .build();
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1000, notification);
                }else if(getCurrentTimeStamp().equals("16:30")){


                        Notification notification =
                                new NotificationCompat.Builder(context) // this is context
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("คำเตือน")
                                        .setContentText("ใกล้ถึงเวลาออกงานแล้ว")
                                        .setAutoCancel(true)
                                        .build();
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(1000, notification);
                    }


            }
        },0,30000);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }


    public void showProgress(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }

    public void fragmentTran(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();
        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        int countFragment = fragmentManager.getBackStackEntryCount();

        if(countFragment > 1){
            fragmentManager.popBackStack();
        }else{
            getLogout();
        }
    }

    private void getLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAppActivity.this);
        builder.setTitle("คำเตือน");
        builder.setMessage("คุณต้องการที่จะออกจากระบบหรือไม่");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent goLogin = new Intent(MainAppActivity.this,AuthenActivity.class);
                startActivity(goLogin);
                finish();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        try{
//
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//
//            String [] tmp   = result.getContents().split(" ");
//            Log.e(TAG,""+result.getContents());
//
////            new NetworkConnection().callcheck(listenner,user_id,result.getContents(),tmp[1]);
////            showProgress();
//
//        }catch (Exception e){
//            e.printStackTrace();
//            Toast.makeText(this, "ยกเลิกการแสกน QR Code", Toast.LENGTH_SHORT).show();
//        }
//    }


    OnCallbackCheckinListenner listenner = new OnCallbackCheckinListenner() {
        @Override
        public void onResponse(CheckinModel checkinModel) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(context, ""+checkinModel.getState(), Toast.LENGTH_SHORT).show();
            Log.e(TAG,new Gson().toJson(checkinModel));
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();

        }
    };

}
