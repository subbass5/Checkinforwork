package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleModel {
    @SerializedName("leave_id")
    @Expose
    private String leaveId;
    @SerializedName("save_at")
    @Expose
    private String saveAt;
    @SerializedName("schedule_date")
    @Expose
    private String scheduleDate;
    @SerializedName("work_shift")
    @Expose
    private String workShift;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getSaveAt() {
        return saveAt;
    }

    public void setSaveAt(String saveAt) {
        this.saveAt = saveAt;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getWorkShift() {
        return workShift;
    }

    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
