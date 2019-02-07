
package com.ziffytech.vaccination.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VaccinationData {

    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("data")
    @Expose
    private ArrayList<AgeVaccinationData> ageVaccinationDataList = null;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ArrayList<AgeVaccinationData> getAgeVaccinationDataList() {
        return ageVaccinationDataList;
    }

    public void setAgeVaccinationDataList(ArrayList<AgeVaccinationData> ageVaccinationDataList) {
        this.ageVaccinationDataList = ageVaccinationDataList;
    }
}
