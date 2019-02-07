package com.ziffytech.vaccination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VaccinationDetails {

@SerializedName("id")
@Expose
private String id;
@SerializedName("vaccination_id")
@Expose
private String vaccinationId;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("bus_id")
@Expose
private String busId;
@SerializedName("app_id")
@Expose
private String appId;
@SerializedName("vacc_baby_id")
@Expose
private String vaccBabyId;
@SerializedName("due_on")
@Expose
private String dueOn;
@SerializedName("given_on")
@Expose
private String givenOn;
@SerializedName("batch")
@Expose
private String batch;
@SerializedName("weight")
@Expose
private String weight;
@SerializedName("user_relation_with")
@Expose
private String userRelationWith;
@SerializedName("details")
@Expose
private String details;
@SerializedName("is_display")
@Expose
private String isDisplay;
@SerializedName("created_date")
@Expose
private String createdDate;
@SerializedName("updated_date")
@Expose
private String updatedDate;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getVaccinationId() {
return vaccinationId;
}

public void setVaccinationId(String vaccinationId) {
this.vaccinationId = vaccinationId;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getBusId() {
return busId;
}

public void setBusId(String busId) {
this.busId = busId;
}

public String getAppId() {
return appId;
}

public void setAppId(String appId) {
this.appId = appId;
}

public String getVaccBabyId() {
return vaccBabyId;
}

public void setVaccBabyId(String vaccBabyId) {
this.vaccBabyId = vaccBabyId;
}

public String getDueOn() {
return dueOn;
}

public void setDueOn(String dueOn) {
this.dueOn = dueOn;
}

public String getGivenOn() {
return givenOn;
}

public void setGivenOn(String givenOn) {
this.givenOn = givenOn;
}

public String getBatch() {
return batch;
}

public void setBatch(String batch) {
this.batch = batch;
}

public String getWeight() {
return weight;
}

public void setWeight(String weight) {
this.weight = weight;
}

public String getUserRelationWith() {
return userRelationWith;
}

public void setUserRelationWith(String userRelationWith) {
this.userRelationWith = userRelationWith;
}

public String getDetails() {
return details;
}

public void setDetails(String details) {
this.details = details;
}

public String getIsDisplay() {
return isDisplay;
}

public void setIsDisplay(String isDisplay) {
this.isDisplay = isDisplay;
}

public String getCreatedDate() {
return createdDate;
}

public void setCreatedDate(String createdDate) {
this.createdDate = createdDate;
}

public String getUpdatedDate() {
return updatedDate;
}

public void setUpdatedDate(String updatedDate) {
this.updatedDate = updatedDate;
}

}