
package com.ziffytech.vaccination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VcDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("salutations")
    @Expose
    private String salutations;
    @SerializedName("m_name")
    @Expose
    private String mName;
    @SerializedName("relation")
    @Expose
    private String relation;
    @SerializedName("m_gender")
    @Expose
    private String mGender;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("alternate_number")
    @Expose
    private String alternateNumber;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vc_status")
    @Expose
    private String vcStatus;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modeified")
    @Expose
    private String modeified;

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

    public String getSalutations() {
        return salutations;
    }

    public void setSalutations(String salutations) {
        this.salutations = salutations;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMGender() {
        return mGender;
    }

    public void setMGender(String mGender) {
        this.mGender = mGender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVcStatus() {
        return vcStatus;
    }

    public void setVcStatus(String vcStatus) {
        this.vcStatus = vcStatus;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModeified() {
        return modeified;
    }

    public void setModeified(String modeified) {
        this.modeified = modeified;
    }

}
