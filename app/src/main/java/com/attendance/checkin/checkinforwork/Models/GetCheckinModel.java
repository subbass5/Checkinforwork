package com.attendance.checkin.checkinforwork.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCheckinModel {

    @SerializedName("checkin_id")
    @Expose
    private String checkinId;
    @SerializedName("checkin_at")
    @Expose
    private String checkinAt;
    @SerializedName("checkout_at")
    @Expose
    private String checkoutAt;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getCheckinId() {
        return checkinId;
    }

    public void setCheckinId(String checkinId) {
        this.checkinId = checkinId;
    }

    public String getCheckinAt() {
        return checkinAt;
    }

    public void setCheckinAt(String checkinAt) {
        this.checkinAt = checkinAt;
    }

    public String getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(String checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
