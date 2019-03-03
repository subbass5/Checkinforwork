package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.GetNotifyModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackGetNotifyListener {

    public void onResponse(List<GetNotifyModel> getNotifyModelList);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
