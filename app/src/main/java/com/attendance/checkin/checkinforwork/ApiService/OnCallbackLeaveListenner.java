package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.CheckinModel;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;

import okhttp3.ResponseBody;

public interface OnCallbackLeaveListenner {

    public void onResponse(LeaveModel leaveModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
