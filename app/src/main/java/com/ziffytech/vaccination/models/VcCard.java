
package com.ziffytech.vaccination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VcCard {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_vaccination_ids")
    @Expose
    private String userVaccinationIds;
    @SerializedName("baby_user_id")
    @Expose
    private String babyUserId;
    @SerializedName("given_vaccination")
    @Expose
    private String givenVaccination;
    @SerializedName("next_vaccination")
    @Expose
    private String nextVaccination;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserVaccinationIds() {
        return userVaccinationIds;
    }

    public void setUserVaccinationIds(String userVaccinationIds) {
        this.userVaccinationIds = userVaccinationIds;
    }

    public String getBabyUserId() {
        return babyUserId;
    }

    public void setBabyUserId(String babyUserId) {
        this.babyUserId = babyUserId;
    }

    public String getGivenVaccination() {
        return givenVaccination;
    }

    public void setGivenVaccination(String givenVaccination) {
        this.givenVaccination = givenVaccination;
    }

    public String getNextVaccination() {
        return nextVaccination;
    }

    public void setNextVaccination(String nextVaccination) {
        this.nextVaccination = nextVaccination;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
