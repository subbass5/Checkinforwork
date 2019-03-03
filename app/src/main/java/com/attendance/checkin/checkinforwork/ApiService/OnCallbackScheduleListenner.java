package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.ScheduleModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackScheduleListenner {

    public void onResponse(List<ScheduleModel> scheduleModelList);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);
}
