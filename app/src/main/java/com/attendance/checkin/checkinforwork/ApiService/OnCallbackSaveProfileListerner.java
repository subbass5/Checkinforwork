package com.attendance.checkin.checkinforwork.ApiService;

import com.attendance.checkin.checkinforwork.Models.GetCheckinModel;
import com.attendance.checkin.checkinforwork.Models.SaveProfileModel;

import java.util.List;

import okhttp3.ResponseBody;

public interface OnCallbackSaveProfileListerner {

    public void onResponse(SaveProfileModel saveProfileModel);

    public void onBodyError(ResponseBody responseBodyError);

    public void onBodyErrorIsNull();

    public void onFailure(Throwable t);

}
