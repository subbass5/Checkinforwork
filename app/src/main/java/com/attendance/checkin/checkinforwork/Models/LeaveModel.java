package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveModel {

    @SerializedName("leave_id")
    @Expose
    private String leaveId;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

}
