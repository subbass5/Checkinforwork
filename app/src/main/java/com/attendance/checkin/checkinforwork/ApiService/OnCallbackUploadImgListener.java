package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.LoginModel;
import com.attendance.checkin.checkinforwork.Models.UploadImgModel;

import okhttp3.ResponseBody;

public interface OnCallbackUploadImgListener {

    public void onResponse(UploadImgModel uploadImgModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
