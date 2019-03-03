package com.attendance.checkin.checkinforwork.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackGetNotifyListener;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackSaveProfileListerner;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackauthPinListenner;
import com.attendance.checkin.checkinforwork.AuthenActivity;
import com.attendance.checkin.checkinforwork.Models.AuthPinModel;
import com.attendance.checkin.checkinforwork.Models.GetNotifyModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.minibugdev.drawablebadge.BadgePosition;
import com.minibugdev.drawablebadge.DrawableBadge;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

public class FragmentMainApp extends Fragment implements View.OnClickListener{

    private Context context;
    private LinearLayout mainLayout;
    private ImageView image_profile;
    private TextView tv_name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private String TAG = "FragmentMainApp";
    private String uid ;
    private String fname ;
    private String lname;
    private String email;
    private String phone ;
    private String address ;
    private String password;
    private String pin = "";
    private String tmp = "";
    private Dialog dialogPin;
    private ImageView imgNotify;
    private TextView tv_notify;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mainapp_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        context = getContext();
        v.findViewById(R.id.btn_schedule_exchange).setOnClickListener(this);
        v.findViewById(R.id.btn_myqrcode).setOnClickListener(this);
        v.findViewById(R.id.btn_leave).setOnClickListener(this);
        v.findViewById(R.id.btn_history).setOnClickListener(this);
        v.findViewById(R.id.btn_check_leave).setOnClickListener(this);
        v.findViewById(R.id.btn_logout).setOnClickListener(this);
        v.findViewById(R.id.btn_edit).setOnClickListener(this);


        mainLayout = v.findViewById(R.id.mainlayout);
        image_profile = v.findViewById(R.id.profile_image);
        tv_notify = v.findViewById(R.id.tv_notify);

        sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        fname = sharedPreferences.getString(MyFer.FNAME,"");
        lname = sharedPreferences.getString(MyFer.LNAME,"");

        tv_name = v.findViewById(R.id.tv_name);
        tv_name.setText(" "+fname+" "+lname);
        FirebaseMessaging.getInstance().subscribeToTopic("promotion");
        String user_id = sharedPreferences.getString(MyFer.USER_ID,"");

        showProgress();
        new NetworkConnection().callGetNotify(notifyListener,user_id);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit:
                edit();
                break;
//            case R.id.btn_checkin:
//                checkin_checkout("ลงชื่อเข้างาน");
//                break;
//            case R.id.btn_checkout:
//                checkin_checkout("ลงชื่อออกงาน");
//                break;
            case R.id.btn_myqrcode:
                showMyQR();
                break;
            case R.id.btn_leave:
                leave();

                break;
            case R.id.btn_schedule_exchange:
                schedule_ex();
                break;
            case R.id.btn_history:
                history();
                break;
            case R.id.btn_check_leave:
                check_leave();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }

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

    private void showProgress(){
        pin = "";
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("กำลังโหลดข้อมูล");
        progressDialog.show();
    }

    private void input_pin(){


        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.layout_pin,null);
        final TextView tv_show = v.findViewById(R.id.tv_show);

        ImageButton img1,img2,img3,img4,img5,img6,img7,img8,img9,img0,imgdel;
        Button btn_ok = v.findViewById(R.id.btn_ok);
        Button btn_cancel = v.findViewById(R.id.btn_close);

        img0 = v.findViewById(R.id.img0);
        img1 = v.findViewById(R.id.img1);
        img2 = v.findViewById(R.id.img2);
        img3 = v.findViewById(R.id.img3);
        img4 = v.findViewById(R.id.img4);
        img5 = v.findViewById(R.id.img5);
        img6 = v.findViewById(R.id.img6);
        img7 = v.findViewById(R.id.img7);
        img8 = v.findViewById(R.id.img8);
        img9 = v.findViewById(R.id.img9);
        imgdel = v.findViewById(R.id.imgdel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        builder.setView(v);
         dialogPin =  builder.show();

        img0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pin += ""+0;

                tv_show.setText(pin);

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+1;

                tv_show.setText(pin);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+2;

                tv_show.setText(pin);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+3;

                tv_show.setText(pin);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+4;

                tv_show.setText(pin);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+5;

                tv_show.setText(pin);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+6;

                tv_show.setText(pin);
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+7;

                tv_show.setText(pin);
            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+8;

                tv_show.setText(pin);
            }
        });
        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin += ""+9;

                tv_show.setText(pin);
            }
        });

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pin = pin.substring(0, pin.length()-1);
                    tv_show.setText(pin);
                }catch (Exception e){
                    Toast.makeText(context, "กรุณากรอกข้อมูลก่อนลบ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

                new NetworkConnection().callAuthPin(onCallbackauthPinListenner,pin);
                showProgress();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPin.dismiss();
            }
        });

    }


    private void checkin_checkout(final String task) {

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

    private void showMyQR() {

//ไปยังหน้า  บันทึกการลางาน
        fragmentTran(new FragmentShowQR());
    }


    private void leave() {
        //ไปยังหน้า  บันทึกการลางาน
        fragmentTran(new FragmentLeave());

    }

    private void schedule_ex() {
        input_pin();
//        Toast.makeText(context, "กำลังแก้ไขส่วนนี้", Toast.LENGTH_SHORT).show();
    }

    private void history() {
        fragmentTran(new FragmentHistory());
    }



    private void check_leave() {
        //เช็คประวัติการลา
        fragmentTran(new FragmentHistoryLeave());
    }

    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("คำเตือน");
        builder.setMessage("คุณต้องการที่จะออกจากระบบหรือไม่");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                editor.clear();
                editor.commit();

                Intent goLogin = new Intent(getActivity(),AuthenActivity.class);
                startActivity(goLogin);
                getActivity().finish();
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

    public void fragmentTran(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction frgTran = fragmentManager.beginTransaction();

        frgTran.replace(R.id.content, fragment).addToBackStack(null).commit();

    }

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


    OnCallbackauthPinListenner onCallbackauthPinListenner = new OnCallbackauthPinListenner() {
        @Override
        public void onResponse(AuthPinModel authPinModel) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
//            Toast.makeText(context, ""+authPinModel.getState(), Toast.LENGTH_SHORT).show();

            if (authPinModel.getState().equals("success")){

                Toast.makeText(context, "ยืนยันตัวตนสำเร็จ", Toast.LENGTH_SHORT).show();
                fragmentTran(new FragmentSchedule());
                dialogPin.dismiss();
                pin = "";

            }else {
                pin = "";
                Toast.makeText(context, "กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
//                dialogPin.dismiss();
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
            Log.e(TAG,"response is null");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };

    OnCallbackGetNotifyListener notifyListener  = new OnCallbackGetNotifyListener() {
        @Override
        public void onResponse(List<GetNotifyModel> getNotifyModelList) {
            Log.e(TAG,new Gson().toJson(getNotifyModelList));
            int tmp = 0;
            for (int i =0;i<getNotifyModelList.size();i++){
                if(getNotifyModelList.get(i).getStateRead().equals("0")){
                    tmp++;
                }
            }

            tv_notify.setText(""+tmp);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
//            Toast.makeText(context, "ยังไม่ได้อ่าน = "+tmp, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"response error ="+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"response is null");
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
