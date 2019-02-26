package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.AuthPinModel;
import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackauthPinListenner {

    public void onResponse(AuthPinModel authPinModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
