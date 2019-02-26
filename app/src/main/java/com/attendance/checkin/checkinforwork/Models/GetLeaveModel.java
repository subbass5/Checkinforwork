package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLeaveModel {

    @SerializedName("leave_id")
    @Expose
    private String leaveId;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("save_at")
    @Expose
    private String saveAt;
    @SerializedName("date_start")
    @Expose
    private String dateStart;
    @SerializedName("date_end")
    @Expose
    private String dateEnd;
    @SerializedName("approve")
    @Expose
    private String approve;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("leave_type")
    @Expose
    private String leaveType;
    @SerializedName("img_id")
    @Expose
    private Object imgId;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getSaveAt() {
        return saveAt;
    }

    public void setSaveAt(String saveAt) {
        this.saveAt = saveAt;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Object getImgId() {
        return imgId;
    }

    public void setImgId(Object imgId) {
        this.imgId = imgId;
    }

}
