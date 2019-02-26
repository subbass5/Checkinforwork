package com.attendance.checkin.checkinforwork.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackGetNumLeaveListener;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackLeaveListenner;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackUploadImgListener;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;
import com.attendance.checkin.checkinforwork.Models.NumLeaveModel;
import com.attendance.checkin.checkinforwork.Models.UploadImgModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

public class FragmentLeave extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    FragmentManager fragmentManager;
    final Calendar myCalendar_start = Calendar.getInstance();
    final Calendar myCalendar_end = Calendar.getInstance();
    Context context;
    EditText et_date_start,et_date_end;
    TextView tv_date_sum,tv_numLeave;

    RadioButton rad1,rad2;
    String user_id,numLeave;
    ImageView image_leave ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImagePicker imagePicker;
    ProgressDialog progressDialog;
    private final String TAG = "FragmentLeave";
    boolean state = false;

    String nameImg = "";
    MultipartBody.Part body;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.leave_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        fragmentManager = getActivity().getSupportFragmentManager();
        context = getContext();
        image_leave = v.findViewById(R.id.img_leave);

        v.findViewById(R.id.btn_submit).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
        v.findViewById(R.id.btn_select_img).setOnClickListener(this);
        v.findViewById(R.id.btn_take_photo).setOnClickListener(this);

        et_date_start = v. findViewById(R.id.et_date_start);
        et_date_end = v. findViewById(R.id.et_date_end);
        tv_date_sum = v.findViewById(R.id.tv_date_sum);
        tv_numLeave = v.findViewById(R.id.tv_numLeave);

        // Check whether this app has write external storage permission or not.
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
// If do not grant write external storage permission.
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);

        user_id = sharedPreferences.getString(MyFer.USER_ID,"");

        // เลือกรูปภาพใน gallery
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                               @Override

                                               public void onImagesChosen(List<ChosenImage> list) {
                                                   // Do somethig
                                                   // get path and create file.
                                                   String path = list.get(0).getOriginalPath();
                                                   File file = new File(path);

                                                   // convert file to bitmap and set to imageView.
//                                                   if(file.exists()){
                                                       Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                       image_leave.setImageBitmap(myBitmap);
                                                       RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                                                       body = MultipartBody.Part.createFormData("leave", file.getName(), reqFile);
                                                       nameImg = file.getName();
                                                       state = true;
//                                                       Toast.makeText(context, "Set", Toast.LENGTH_SHORT).show();
//                                                   }

                                               }

                                               @Override
                                               public void onError(String message) {
                                                   // Do error handling
                                                   Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );


        showProgress();
        new NetworkConnection().callGetNumLeave(getNumLeaveListener,user_id);

        rad1 = v.findViewById(R.id.rad_ch1);
        rad2 = v.findViewById(R.id.rad_ch2);
        tv_date_sum.setText("กรุณาเลือกวันที่");
        rad1.setChecked(true);

        final DatePickerDialog.OnDateSetListener date_start = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_start.set(Calendar.YEAR, year);
                myCalendar_start.set(Calendar.MONTH, monthOfYear);

                myCalendar_start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };



        et_date_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
//                        apointDate.setText(dayOfMonth + "/" + month + "/" + year);
//                        updateLabel_start();
                       et_date_start.setText(year+"-"+month+"-"+dayOfMonth);
                    }
                }, myCalendar_start.get(Calendar.YEAR), myCalendar_start.get(Calendar.MONTH), myCalendar_start.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date_start, myCalendar_start
//                        .get(Calendar.YEAR), myCalendar_start.get(Calendar.MONTH),
//                        myCalendar_start.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar_end.set(Calendar.YEAR, year);
                myCalendar_end.set(Calendar.MONTH, monthOfYear);
                myCalendar_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };

        et_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        et_date_end.setText(year+"-"+month+"-"+dayOfMonth);
                        if(!et_date_start.getText().toString().isEmpty()){
                            String diff = diff_date(et_date_start.getText().toString().trim(),et_date_end.getText().toString().trim());
                            tv_date_sum.setText(diff+ " วัน");
                        }
//                        apointDate.setText(dayOfMonth + "/" + month + "/" + year);
//                        updateLabel_end();

                    }
                }, myCalendar_end.get(Calendar.YEAR), myCalendar_end.get(Calendar.MONTH), myCalendar_end.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Picker.PICK_IMAGE_DEVICE) {
            Toast.makeText(context, "Do", Toast.LENGTH_SHORT).show();
            imagePicker.submit(data);
        }
    }

    private void updateLabel_start(String date) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,new  Locale("TH"));

        et_date_start.setText(sdf.format(new Date(date)));
    }





    private void updateLabel_end(String date ) {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,new  Locale("TH"));

        et_date_end.setText(sdf.format(date));

        if(!et_date_start.getText().toString().isEmpty()){
            String diff = diff_date(et_date_start.getText().toString().trim(),et_date_end.getText().toString().trim());
//            Toast.makeText(context, ""+diff, Toast.LENGTH_SHORT).show();
            tv_date_sum.setText(diff+ " วัน");
        }
    }

    private String diff_date(String date_start, String date_end) {

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(date_start);
            d2 = format.parse(date_end);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();


            long diffDays = diff / (24 * 60 * 60 * 1000);

            return "" + (diffDays+1);

        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

        @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_select_img:
                state = false;
                imagePicker.pickImage();
                break;
            case R.id.btn_take_photo:

                break;
            case R.id.btn_submit:
                //ส่งคำร้อง
                submit();
                break;
            case R.id.btn_cancel:
                fragmentManager.popBackStack();
                break;
        }

    }


    private void submit() {

        String datetime_start =  et_date_start.getText().toString();
        String datetime_end = et_date_end.getText().toString();
        String user_id = sharedPreferences.getString(MyFer.USER_ID,"");


        Log.e(TAG,"Date start = "+datetime_start+" Date end = "+datetime_end );
        String leave_state = "";
        if(rad1.isChecked()){
            leave_state = rad1.getText().toString();
        }
        if(rad2.isChecked()){
            leave_state = rad2.getText().toString();
        }

        if(!user_id.isEmpty() && !datetime_start.isEmpty() && !datetime_end.isEmpty() && !leave_state.isEmpty()){

            showProgress();
            new NetworkConnection().callLeave(listenner,user_id,datetime_start,datetime_end,leave_state);
        }


    }
    private void showProgress(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("กำลังโหลดข้อมูล");
        progressDialog.show();
    }

    OnCallbackLeaveListenner listenner = new OnCallbackLeaveListenner() {
        @Override
        public void onResponse(LeaveModel leaveModel) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(leaveModel.getLeaveId() != "0"){

                if(state == true){
                    showProgress();
                    new NetworkConnection().callUploadImg(uploadImgListener,leaveModel.getLeaveId(),body);
//                    Toast.makeText(context, ""+body.body(), Toast.LENGTH_SHORT).show();
                }else{

                    LayoutInflater layoutInflater = getLayoutInflater();
                    View v = layoutInflater.inflate(R.layout.dialog_leave,null);
                    ImageView imgClose = v.findViewById(R.id.image_close);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(v);
                    final Dialog dialog = builder.show();

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            fragmentManager.popBackStack();
                        }
                    });

                }

            }else{

            }


            Log.e(TAG,new Gson().toJson(leaveModel));
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
            Log.e(TAG," DATA is null");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };

    OnCallbackGetNumLeaveListener getNumLeaveListener = new OnCallbackGetNumLeaveListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(NumLeaveModel numLeaveModels) {
            numLeave = diff_date(numLeaveModels.getData().get(0).getDateStart(),numLeaveModels.getData().get(0).getDateEnd());

            tv_numLeave.setText(numLeave+"  วัน");
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
           if( progressDialog.isShowing()){
               progressDialog.dismiss();
           }
        }

        @Override
        public void onBodyErrorIsNull() {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    };

    OnCallbackUploadImgListener uploadImgListener = new OnCallbackUploadImgListener() {
        @Override
        public void onResponse(UploadImgModel uploadImgModel) {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(uploadImgModel.getState().equals("success")){
                LayoutInflater layoutInflater = getLayoutInflater();
                View v = layoutInflater.inflate(R.layout.dialog_leave,null);
                ImageView imgClose = v.findViewById(R.id.image_close);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(v);
                final Dialog dialog = builder.show();

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        fragmentManager.popBackStack();
                    }
                });
            }

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+responseBodyError.source());
        }

        @Override
        public void onBodyErrorIsNull() {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"response is null ");
        }

        @Override
        public void onFailure(Throwable t) {
            if( progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
        }
    };



}
