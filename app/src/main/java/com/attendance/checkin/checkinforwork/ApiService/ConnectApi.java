package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;
import com.attendance.checkin.checkinforwork.Models.LoginModel;
import com.attendance.checkin.checkinforwork.Models.NumLeaveModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;
import com.attendance.checkin.checkinforwork.Models.UploadImgModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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



}
