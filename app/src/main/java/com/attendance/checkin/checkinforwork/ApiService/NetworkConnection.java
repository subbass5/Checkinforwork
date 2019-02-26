package com.attendance.checkin.checkinforwork.ApiService;

import android.util.Log;

import com.attendance.checkin.checkinforwork.Models.AuthPinModel;
import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;
import com.attendance.checkin.checkinforwork.Models.LoginModel;
import com.attendance.checkin.checkinforwork.Models.NumLeaveModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;
import com.attendance.checkin.checkinforwork.Models.UploadImgModel;
import com.attendance.checkin.checkinforwork.Util.API_UTIL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnection {

    public NetworkConnection(){

    }

    public void callLogin(final OnCallbackLoginListener listener, String usr,String pwd){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.logInFrg(usr,pwd);

        call.enqueue(new Callback<LoginModel>() {

            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                try {

                   LoginModel loginRes =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(loginRes);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }

    public void callcheck(final OnCallbackCheckinListenner listener, String user_id,String username,String password,String state){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);

        //call  server
        Call call = server.checkIn(user_id,username,password,state);

        call.enqueue(new Callback<CheckinModel>() {

            @Override
            public void onResponse(Call<CheckinModel> call, Response<CheckinModel> response) {
                try {

                    CheckinModel checkinModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(checkinModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CheckinModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }

    public void callLeave(final OnCallbackLeaveListenner listener, String user_id,String datetime_start,String datetime_end,String leave_state){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.leave(user_id,datetime_start,datetime_end,leave_state);

        call.enqueue(new Callback<LeaveModel>() {

            @Override
            public void onResponse(Call<LeaveModel> call, Response<LeaveModel> response) {
                try {

                    LeaveModel checkinModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(checkinModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<LeaveModel> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t);

            }
        });

    }

    public void callGetLeave(final OnCallbackGetLeaveListenner listener, String user_id,String month){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.getLeave(user_id,month);

        call.enqueue(new Callback<List<GetLeaveModel>>() {

            @Override
            public void onResponse(Call<List<GetLeaveModel>> call, Response<List<GetLeaveModel>> response) {
                try {

                    List<GetLeaveModel> checkinModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(checkinModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<GetLeaveModel>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t);

            }
        });

    }

    public void callGetCheckin(final OnCallbackGetCheckinListener listener, String user_id,String month){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.getHistory(user_id,month);

        call.enqueue(new Callback<List<GetCheckinModel>>() {

            @Override
            public void onResponse(Call<List<GetCheckinModel>> call, Response<List<GetCheckinModel>> response) {
                try {

                    List<GetCheckinModel> checkinModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(checkinModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<GetCheckinModel>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t);

            }
        });

    }

    //OnCallbackSaveProfileListerner
    public void callSaveProfile(final OnCallbackSaveProfileListerner listener, String uid,String fname,String lname,String email,String phone,String address,String password){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.setProfile(uid,fname,lname,email,phone,address,password);

        call.enqueue(new Callback<SaveProfileModel>() {

            @Override
            public void onResponse(Call<SaveProfileModel> call, Response<SaveProfileModel> response) {
                try {

                    SaveProfileModel saveProfileModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(saveProfileModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<SaveProfileModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }

    public void callGetNumLeave(final OnCallbackGetNumLeaveListener listener, String user_id){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.getNumLeave(user_id);

        call.enqueue(new Callback<NumLeaveModel>() {

            @Override
            public void onResponse(Call<NumLeaveModel> call, Response<NumLeaveModel> response) {
                try {

                    NumLeaveModel loginRes =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(loginRes);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<NumLeaveModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }

    public void callUploadImg(final OnCallbackUploadImgListener listener,String leave_id, MultipartBody.Part img){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.uploadImgLeave(leave_id,img);

        call.enqueue(new Callback<UploadImgModel>() {

            @Override
            public void onResponse(Call<UploadImgModel> call, Response<UploadImgModel> response) {
                try {

                    UploadImgModel uploadImgModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(uploadImgModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<UploadImgModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }
    public void callAuthPin(final OnCallbackauthPinListenner listener,String pin){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_UTIL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ConnectApi server = retrofit.create(ConnectApi.class);
        //call  server
        Call call = server.getCheckPin(pin);

        call.enqueue(new Callback<AuthPinModel>() {

            @Override
            public void onResponse(Call<AuthPinModel> call, Response<AuthPinModel> response) {
                try {

                    AuthPinModel authPinModel =  response.body();

                    if (response.code() != 200) {

                        ResponseBody responseBody = response.errorBody();

                        if (responseBody != null) {
                            listener.onBodyError(responseBody);
                        } else if (responseBody == null) {
                            listener.onBodyErrorIsNull();
                        }

                    } else {
                        listener.onResponse(authPinModel);
                    }


                }catch (Exception e){
                    listener.onFailure(e);
                    Log.d("try",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<AuthPinModel> call, Throwable t) {
                Log.d("Network",t.getMessage());
                listener.onFailure(t);

            }
        });

    }
}
