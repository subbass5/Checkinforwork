package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImgModel {

    @SerializedName("state")
    @Expose
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
