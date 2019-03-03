package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNotifyModel {

    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_from")
    @Expose
    private String userFrom;
    @SerializedName("user_id_save")
    @Expose
    private String userIdSave;
    @SerializedName("user_id_for")
    @Expose
    private String userIdFor;
    @SerializedName("leave_id")
    @Expose
    private String leaveId;
    @SerializedName("state_read")
    @Expose
    private String stateRead;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserIdSave() {
        return userIdSave;
    }

    public void setUserIdSave(String userIdSave) {
        this.userIdSave = userIdSave;
    }

    public String getUserIdFor() {
        return userIdFor;
    }

    public void setUserIdFor(String userIdFor) {
        this.userIdFor = userIdFor;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getStateRead() {
        return stateRead;
    }

    public void setStateRead(String stateRead) {
        this.stateRead = stateRead;
    }
}
