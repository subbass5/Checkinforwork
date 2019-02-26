package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.LeaveModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackGetLeaveListenner {

    public void onResponse(List<GetLeaveModel> getLeaveModelList);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
