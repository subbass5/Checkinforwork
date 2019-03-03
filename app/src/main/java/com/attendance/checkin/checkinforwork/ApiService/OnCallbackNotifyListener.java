package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.NotifyModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackNotifyListener {

    public void onResponse(NotifyModel notifyModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
