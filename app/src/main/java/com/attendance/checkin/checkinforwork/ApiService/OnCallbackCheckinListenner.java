package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.CheckinModel;

import okhttp3.ResponseBody;

public interface OnCallbackCheckinListenner {

    public void onResponse(CheckinModel checkinModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
