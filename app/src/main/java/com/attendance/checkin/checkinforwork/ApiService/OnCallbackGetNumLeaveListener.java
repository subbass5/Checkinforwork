package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.GetLeaveModel;
import com.attendance.checkin.checkinforwork.Models.NumLeaveModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackGetNumLeaveListener {

    public void onResponse(NumLeaveModel numLeaveModels);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
