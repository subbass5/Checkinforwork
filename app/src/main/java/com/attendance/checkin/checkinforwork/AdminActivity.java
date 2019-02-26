package com.attendance.checkin.checkinforwork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackCheckinListenner;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackSaveProfileListerner;
import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;


    private ProgressDialog progressDialog;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String  user_id ="";
    private static final String TAG = "AdminActivity";
    private LinearLayout mainLayout;
    private ImageView image_profile;
    private TextView tv_name;
    private Dialog dialog;
    private String uid ;
    private String fname ;
    private String lname;
    private String email;
    private String phone ;
    private String address ;
    private String password;
    private String stateCheckin ="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        init();

    }

    private void init() {
        //hide title bar
        getSupportActionBar().hide();

        context = AdminActivity.this;
        sharedPreferences = getSharedPreferences(MyFer.MY_FER,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id  = sharedPreferences.getString(MyFer.USER_ID,"");

        mainLayout = findViewById(R.id.mainlayout);
        image_profile = findViewById(R.id.profile_image);

        fname = sharedPreferences.getString(MyFer.FNAME,"");
        lname = sharedPreferences.getString(MyFer.LNAME,"");

        tv_name = findViewById(R.id.tv_name);

        //set name
        tv_name.setText(" "+fname+" "+lname);

        //bind widget
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_checkin).setOnClickListener(this);
        findViewById(R.id.btn_checkout).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }


    private void checkin_checkout(final String task) {

        stateCheckin = task;
//        Toast.makeText(context, ""+task, Toast.LENGTH_SHORT).show();
        mainLayout.setVisibility(View.GONE);
        image_profile.setVisibility(View.GONE);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.cs_datetime,null);
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_time = view.findViewById(R.id.tv_time);
        Button btn_save = view.findViewById(R.id.btn_save);
        ImageView img_close = view.findViewById(R.id.image_close);

        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();

        tv_date.setText(dateFormat.format(date));
        tv_time.setText(timeFormat.format(time));

        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        builder.setView(view);
        final AlertDialog di  = builder.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setVisibility(View.VISIBLE);
                image_profile.setVisibility(View.VISIBLE);
                di.dismiss();

                IntentIntegrator integrator = new IntentIntegrator((AppCompatActivity) context);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("กรุณานำกล้องแสกนเพื่อ "+task);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();


            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setVisibility(View.VISIBLE);
                image_profile.setVisibility(View.VISIBLE);
                di.dismiss();
            }
        });

    }


    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("คำเตือน");
        builder.setMessage("คุณต้องการที่จะออกจากระบบหรือไม่");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                editor.clear();
                editor.commit();

                Intent goLogin = new Intent(AdminActivity.this,AuthenActivity.class);
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
    private void edit() {

        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.layout_user_data,null);
        final EditText et_fname,et_lname,et_email,et_phone,et_address,et_password;

        et_fname = v.findViewById(R.id.et_fname);
        et_lname = v.findViewById(R.id.et_lname);
        et_email = v.findViewById(R.id.et_email);
        et_phone = v.findViewById(R.id.et_phone);
        et_address = v.findViewById(R.id.et_address);
        et_password = v.findViewById(R.id.et_password);

        et_fname.setText(sharedPreferences.getString(MyFer.FNAME,""));
        et_lname.setText(sharedPreferences.getString(MyFer.LNAME,""));
        et_email.setText(sharedPreferences.getString(MyFer.EMAIL,""));
        et_phone.setText(sharedPreferences.getString(MyFer.PHONE,""));
        et_address.setText(sharedPreferences.getString(MyFer.ADDRESS,""));
        et_password.setText(sharedPreferences.getString(MyFer.PASSWORD,""));

        Button btn_submit,btn_cancel;
        btn_submit = v.findViewById(R.id.btn_submit);
        btn_cancel = v.findViewById(R.id.btn_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        dialog = builder.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                uid = sharedPreferences.getString(MyFer.USER_ID,"");
                fname = et_fname.getText().toString();
                lname = et_lname.getText().toString();
                email = et_email.getText().toString();
                phone = et_phone.getText().toString();
                address = et_address.getText().toString();
                password = et_password.getText().toString();

                showProgress();
                new NetworkConnection().callSaveProfile(listenner,uid,fname,lname,email,phone,address,password);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void showProgress(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.msgLoading));
        progressDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        try{

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            String [] tmp   = result.getContents().split("-");
            Log.e(TAG,""+result.getContents());

            new NetworkConnection().callcheck(listennerCheckin,tmp[0],tmp[1],tmp[2],stateCheckin);
            showProgress();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "ยกเลิกการแสกน QR Code", Toast.LENGTH_SHORT).show();
        }
    }


    OnCallbackCheckinListenner listennerCheckin = new OnCallbackCheckinListenner() {
        @Override
        public void onResponse(CheckinModel checkinModel) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("สถานะ");
            builder.setMessage(checkinModel.getState());
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

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


    OnCallbackSaveProfileListerner listenner = new OnCallbackSaveProfileListerner() {
        @Override
        public void onResponse(SaveProfileModel saveProfileModel) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,new Gson().toJson(saveProfileModel));
            Toast.makeText(context, ""+saveProfileModel.getState(), Toast.LENGTH_SHORT).show();
            tv_name.setText(fname+" "+lname );
            if(saveProfileModel.getState().equals("บันทึกข้อมูลเสร็จสิ้น")){
                editor.putString(MyFer.USER_ID,uid);
                editor.putString(MyFer.FNAME,fname);
                editor.putString(MyFer.LNAME,lname);
                editor.putString(MyFer.EMAIL,email);
                editor.putString(MyFer.PHONE,phone);
                editor.putString(MyFer.ADDRESS,address);
                editor.putString(MyFer.PASSWORD,password);
                editor.commit();
            }
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
            Log.e(TAG,"data is null.");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                edit();
                break;
            case R.id.btn_checkin:
                checkin_checkout("checkin");
                break;
            case R.id.btn_checkout:
                checkin_checkout("checkout");
                break;
            case R.id.btn_logout:
                logout();
                break;
        }


    }

}
