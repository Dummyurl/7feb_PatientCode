
package com.ziffytech.vaccination.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("vc_details")
    @Expose
    private VcDetails vcDetails;
    @SerializedName("vc_card")
    @Expose
    private VcCard vcCard;
    @SerializedName("birth_data")
    @Expose
    private BirthData birthData;
    @SerializedName("result")
    @Expose
    private List<VaccinationData> vaccinationDataList = null;

    public VcDetails getVcDetails() {
        return vcDetails;
    }

    public void setVcDetails(VcDetails vcDetails) {
        this.vcDetails = vcDetails;
    }

    public VcCard getVcCard() {
        return vcCard;
    }

    public void setVcCard(VcCard vcCard) {
        this.vcCard = vcCard;
    }

    public BirthData getBirthData() {
        return birthData;
    }

    public void setBirthData(BirthData birthData) {
        this.birthData = birthData;
    }

    public List<VaccinationData> getResult() {
        return vaccinationDataList;
    }

    public void setResult(List<VaccinationData> result) {
        this.vaccinationDataList = result;
    }

}
