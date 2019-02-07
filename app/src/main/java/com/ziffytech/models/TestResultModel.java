package com.ziffytech.models;

public class TestResultModel {

    String value_type;
    String Observation_value;
    String Sequence_No;
    String Effective_date_of_last_normal_observation;
    String Observation_Coding_System;
    String Observation_Test_id;
    String Observation_Text;
    String Observation_result_status;
    String Abnormal_flags;
    String token;
    String result_unit_reference_range;


    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public String getObservation_value() {
        return Observation_value;
    }

    public void setObservation_value(String observation_value) {
        Observation_value = observation_value;
    }

    public String getSequence_No() {
        return Sequence_No;
    }

    public void setSequence_No(String sequence_No) {
        Sequence_No = sequence_No;
    }

    public String getEffective_date_of_last_normal_observation() {
        return Effective_date_of_last_normal_observation;
    }

    public void setEffective_date_of_last_normal_observation(String effective_date_of_last_normal_observation) {
        Effective_date_of_last_normal_observation = effective_date_of_last_normal_observation;
    }

    public String getObservation_Coding_System() {
        return Observation_Coding_System;
    }

    public void setObservation_Coding_System(String observation_Coding_System) {
        Observation_Coding_System = observation_Coding_System;
    }

    public String getObservation_Test_id() {
        return Observation_Test_id;
    }

    public void setObservation_Test_id(String observation_Test_id) {
        Observation_Test_id = observation_Test_id;
    }

    public String getObservation_Text() {
        return Observation_Text;
    }

    public void setObservation_Text(String observation_Text) {
        Observation_Text = observation_Text;
    }

    public String getObservation_result_status() {
        return Observation_result_status;
    }

    public void setObservation_result_status(String observation_result_status) {
        Observation_result_status = observation_result_status;
    }

    public String getAbnormal_flags() {
        return Abnormal_flags;
    }

    public void setAbnormal_flags(String abnormal_flags) {
        Abnormal_flags = abnormal_flags;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResult_unit_reference_range() {
        return result_unit_reference_range;
    }

    public void setResult_unit_reference_range(String result_unit_reference_range) {
        this.result_unit_reference_range = result_unit_reference_range;
    }
}
