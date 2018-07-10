package org.pdxfinder.transdatamodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Drug",
        "Manufacturer",
        "Dose",
        "Duration",
        "Frequency",
        "Arm Size",
        "Response",
        "Passage Range",
        "Starting Date"
})




public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String drug;
    private String manufacturer;
    private String dose;
    private String duration;
    private String frequency;
    private String armSize;
    private String response;
    private String passageRange;
    private String startingDate;

    @ManyToOne
    @JoinColumn(name="pdxinfo_id")
    private PdmrPdxInfo pdmrPdxInfo;


    public Treatment(String drug, String manufacturer,
                     String dose, String duration,
                     String frequency, String armSize,
                     String response, String passageRange, String startingDate) {
        this.drug = drug;
        this.manufacturer = manufacturer;
        this.dose = dose;
        this.duration = duration;
        this.frequency = frequency;
        this.armSize = armSize;
        this.response = response;
        this.passageRange = passageRange;
        this.startingDate = startingDate;
    }

    public Treatment() {
    }


    @JsonProperty("Drug")
    public String getDrug() {
        return drug;
    }

    @JsonProperty("Drug")
    public void setDrug(String drug) {
        this.drug = drug;
    }

    @JsonProperty("Manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @JsonProperty("Dose")
    public void setDose(String dose) {
        this.dose = dose;
    }

    @JsonProperty("Duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("Duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("Frequency")
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @JsonProperty("Arm Size")
    public void setArmSize(String armSize) {
        this.armSize = armSize;
    }

    @JsonProperty("Response")
    public String getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(String response) {
        this.response = response;
    }

    @JsonProperty("Passage Range")
    public void setPassageRange(String passageRange) {
        this.passageRange = passageRange;
    }

    @JsonProperty("Starting Date")
    public String getStartingDate() {
        return startingDate;
    }

    @JsonProperty("Starting Date")
    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public void setPdmrPdxInfo(PdmrPdxInfo pdmrPdxInfo) {
        this.pdmrPdxInfo = pdmrPdxInfo;
    }
}
