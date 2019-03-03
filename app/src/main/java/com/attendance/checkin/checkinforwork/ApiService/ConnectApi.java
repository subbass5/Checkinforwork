package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.AuthPinModel;
import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.GetNotifyModel;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;
import com.attendance.checkin.checkinforwork.Models.LoginModel;
import com.attendance.checkin.checkinforwork.Models.NotifyModel;
import com.attendance.checkin.checkinforwork.Models.NumLeaveModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;
import com.attendance.checkin.checkinforwork.Models.ScheduleModel;
import com.attendance.checkin.checkinforwork.Models.UploadImgModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ConnectApi {

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> logInFrg(@Field("username") String usr , @Field("password") String pwd);

    @FormUrlEncoded
    @POST("checkin")
    Call<CheckinModel> checkIn(@Field("user_id") String user_id , @Field("username") String username, @Field("password") String password,@Field("status") String status);

    @FormUrlEncoded
    @POST("leave")
    Call<LeaveModel> leave(@Field("user_id") String user_id ,
                           @Field("date_start") String date_start,
                           @Field("date_end") String date_end,
                           @Field("leave_type") String leave_type);

    @FormUrlEncoded
    @POST("getLeave")
    Call<List<GetLeaveModel>> getLeave(@Field("user_id") String user_id,@Field("month") String month);


    @FormUrlEncoded
    @POST("getCheckin")
    Call<List<GetCheckinModel>> getHistory(@Field("user_id") String user_id,@Field("month") String month);

    @FormUrlEncoded
    @POST("changeName")
    Call<SaveProfileModel> setProfile(@Field("user_id") String user_id,
                                      @Field("fname") String fname,
                                      @Field("lname") String lname,
                                      @Field("email") String email,
                                      @Field("phone") String phone,
                                      @Field("address") String address,
                                      @Field("password") String password
                                          );



    @FormUrlEncoded
    @POST("getNumLeave")
    Call<NumLeaveModel> getNumLeave(@Field("user_id") String user_id);


    @Multipart
    @POST("leaveUpload")
    Call<UploadImgModel> uploadImgLeave(@Part("leave_id") String leave_id,
                                        @Part MultipartBody.Part image);


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("authPin")
    Call<AuthPinModel> getCheckPin(@Field("pin") String pin);


    @GET("getSchedule")
    Call<List<ScheduleModel>> getSchedule();

    @FormUrlEncoded
    @POST("push_notify")
    Call<NotifyModel> getNotify(@Field("user_id_save") String user_id_save,@Field("user_id_for") String user_id_for,@Field("leave_id") String leave_id);


    @FormUrlEncoded
    @POST("get_notify")
    Call<List<GetNotifyModel>> getNotifyList(@Field("user_id") String user_id);


}
