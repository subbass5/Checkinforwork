package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.LoginModel;

import okhttp3.ResponseBody;

public interface OnCallbackLoginListener {

    public void onResponse(LoginModel loginModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
