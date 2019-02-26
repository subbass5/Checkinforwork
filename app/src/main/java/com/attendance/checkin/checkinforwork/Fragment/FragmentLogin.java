package com.attendance.checkin.checkinforwork.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.attendance.checkin.checkinforwork.AdminActivity;
import com.attendance.checkin.checkinforwork.ApiService.NetworkConnection;
import com.attendance.checkin.checkinforwork.ApiService.OnCallbackLoginListener;
import com.attendance.checkin.checkinforwork.MainAppActivity;
import com.attendance.checkin.checkinforwork.Models.LoginModel;
import com.attendance.checkin.checkinforwork.R;
import com.attendance.checkin.checkinforwork.Util.MyFer;
import com.google.gson.Gson;

import okhttp3.ResponseBody;

public class FragmentLogin extends Fragment implements View.OnClickListener {
    private Button btn_login;
    private EditText et_usr,et_pwd;
    private Context context;
    private String TAG = "FragmentLogin";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {

        context = getContext();

        et_usr = v.findViewById(R.id.et_usr);
        et_pwd = v.findViewById(R.id.et_pwd);

         v.findViewById(R.id.btn_login).setOnClickListener(this);
         //init session
         sharedPreferences = getActivity().getSharedPreferences(MyFer.MY_FER,Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();
         et_usr.setText("user2");
         et_pwd.setText("user2");



    }

    private void showProgress(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("กำลังโหลดข้อมูล");
        progressDialog.show();
    }

    private void showLoginFail(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_info_black_24dp));
        builder.setTitle(getString(R.string.msgTitle));
        builder.setMessage(getString(R.string.msgLoginFail));
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                    login();
//                Toast.makeText(context, "login", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void login() {
        if(et_usr.getText().toString().isEmpty() && et_pwd.getText().toString().isEmpty()){

            et_usr.setError("กรุณากรอก Username");
            et_pwd.setError("กรุณากรอก Password");

        }else{
            String username = et_usr.getText().toString();
            String password = et_pwd.getText().toString();
            new NetworkConnection().callLogin(listener,username,password);

            showProgress();
        }

    }

    OnCallbackLoginListener listener = new OnCallbackLoginListener() {
        @Override
        public void onResponse(LoginModel loginModel) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Log.e(TAG,new Gson().toJson(loginModel));
            String result = new Gson().toJson(loginModel);

            if(result.equals("{}")){
                showLoginFail();
                et_usr.setText("");
                et_pwd.setText("");

            }else{

                //save data to session tmp
                editor.putString(MyFer.USER_ID,loginModel.getUserId());
                editor.putString(MyFer.FNAME,loginModel.getFname());
                editor.putString(MyFer.LNAME,loginModel.getLname());
                editor.putString(MyFer.EMAIL,loginModel.getEmail());
                editor.putString(MyFer.PHONE,loginModel.getPhone());
                editor.putString(MyFer.ADDRESS,loginModel.getAddress());
                editor.putString(MyFer.USERNAME,loginModel.getUsername());
                editor.putString(MyFer.PASSWORD,loginModel.getPassword());
                editor.commit();

                //if permission = 1 (admin)
                if(loginModel.getPerId().equals("1")){

                    Intent goAdmin = new Intent(context, AdminActivity.class);
                    startActivity(goAdmin);
                    getActivity().finish();

                }else{    //if permission = 2  ( user )

                    Intent goMain = new Intent(context,MainAppActivity.class);
                    startActivity(goMain);
                    getActivity().finish();

                }

            }

        }

        @Override
        public void onBodyError(ResponseBody responseBodyError) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,""+responseBodyError.source());
            showLoginFail();
            et_usr.setText("");
            et_pwd.setText("");
        }

        @Override
        public void onBodyErrorIsNull() {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Log.e(TAG,"null");
            showLoginFail();
            et_usr.setText("");
            et_pwd.setText("");
        }

        @Override
        public void onFailure(Throwable t) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            t.printStackTrace();
            showLoginFail();
            et_usr.setText("");
            et_pwd.setText("");
        }
    };


}
