package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NumLeaveModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<DataLeaveModel> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataLeaveModel> getData() {
        return data;
    }

    public void setData(List<DataLeaveModel> data) {
        this.data = data;
    }

}
