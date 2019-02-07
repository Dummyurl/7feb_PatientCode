
package com.ziffytech.vaccination.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgeVaccinationData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vaccination_name")
    @Expose
    private String vaccinationName;
    @SerializedName("is_number")
    @Expose
    private String isNumber;
    @SerializedName("age_vaccination")
    @Expose
    private String ageVaccination;
    @SerializedName("show_date")
    @Expose
    private String showDate;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    @SerializedName("vaccinaction")
    @Expose
    private VaccinationDetails vaccinactionDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public String getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(String isNumber) {
        this.isNumber = isNumber;
    }

    public String getAgeVaccination() {
        return ageVaccination;
    }

    public void setAgeVaccination(String ageVaccination) {
        this.ageVaccination = ageVaccination;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public VaccinationDetails getVaccinactionDetails() {
        return vaccinactionDetails;
    }

    public void setVaccinactionDetails(VaccinationDetails vaccinactionDetails) {
        this.vaccinactionDetails = vaccinactionDetails;
    }
}
