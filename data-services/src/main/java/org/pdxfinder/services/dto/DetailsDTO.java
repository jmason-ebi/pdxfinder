package org.pdxfinder.services.dto;

import org.pdxfinder.dao.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by csaba on 22/05/2017.
 */
public class DetailsDTO {

    private String modelId;
    private String externalId;
    private String dataSource;
    private String sourceDescription;
    private String patientId;
    private String gender;
    private String ageAtCollection;
    private String race;
    private String ethnicity;

    private String diagnosis;
    private String tumorType;
    private String originTissue;
    private String sampleSite;

    @Deprecated
    private String classification;

    private String stage;
    private String grade;
    private String stageClassification;
    private String gradeClassification;

    private String sampleType; //Engraftment Type
    private String strain;
    private String mouseSex;
    private String engraftmentSite; // Engraftment Site
    private Set<EngraftmentDataDTO> pdxModelList;

    private String externalUrl;
    private String externalUrlText;
    private String mappedOntology;

    private String contacts;
    private String externalDataSourceDesc;
    private String drugProtocolUrl;
    private int totalPages;
    private int presentPage;
    private int variationDataCount;
    private int drugSummaryRowNumber;
    private PatientDTO patient;


    // Quality control information
    private List<QualityAssurance> qualityAssurances;

    private List<String> cancerGenomics;
    private List<Specimen> specimens;
    private Set<Platform> platforms;
    private Set<MolecularCharacterization>  molecularCharacterizations;
    private Set< List<MarkerAssociation> > markerAssociations;

    private Map<String, String> patientTech;
    private Map<String, Set<String>> modelTechAndPassages;
    private List<String> relatedModels;
    private List<DrugSummaryDTO> drugSummary;
    private List<VariationDataDTO> variationDataDTOList;
    private Map<String, String> techNPassToSampleId;
    private Set<String> autoSuggestList;
    private Map<String, String> platformsAndUrls;
    private List<Map> dataSummary;




    public DetailsDTO() {
        this.modelId = "";
        this.externalId = "";
        this.dataSource = "";
        this.sourceDescription = "";
        this.patientId = "";
        this.gender = "";
        this.ageAtCollection = "";
        this.race = "";
        this.ethnicity = "";

        this.diagnosis = "";
        this.tumorType = "";
        this.classification = "";

        this.stage = "";
        this.grade = "";
        this.stageClassification = "";
        this.gradeClassification = "";

        this.originTissue = "";
        this.sampleSite = "";

        this.sampleType = "";
        this.strain = "";
        this.mouseSex = "";
        this.engraftmentSite = "";
        this.externalUrl = "";
        this.externalUrlText = "";
        this.mappedOntology = "";

        this.totalPages = 0;
        this.contacts = "";
        this.externalDataSourceDesc = "";
    }



    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeAtCollection() {
        return ageAtCollection;
    }

    public void setAgeAtCollection(String ageAtCollection) {
        this.ageAtCollection = ageAtCollection;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTumorType() {
        return tumorType;
    }

    public void setTumorType(String tumorType) {
        this.tumorType = tumorType;
    }

    public String getOriginTissue() {
        return originTissue;
    }

    public void setOriginTissue(String originTissue) {
        this.originTissue = originTissue;
    }

    public String getSampleSite() {
        return sampleSite;
    }

    public void setSampleSite(String sampleSite) {
        this.sampleSite = sampleSite;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public List<String> getCancerGenomics() {
        return cancerGenomics;
    }

    public void setCancerGenomics(List<String> cancerGenomics) {
        this.cancerGenomics = cancerGenomics;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getMouseSex() {
        return mouseSex;
    }

    public Set<EngraftmentDataDTO> getPdxModelList() {
        return pdxModelList;
    }

    public void setPdxModelList(Set<EngraftmentDataDTO> pdxModelList) {
        this.pdxModelList = pdxModelList;
    }

    public void setMouseSex(String mouseSex) {
        this.mouseSex = mouseSex;
    }

    public String getEngraftmentSite() {
        return engraftmentSite;
    }

    public void setEngraftmentSite(String engraftmentSite) {
        this.engraftmentSite = engraftmentSite;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalUrlText() {
        return externalUrlText;
    }

    public void setExternalUrlText(String externalUrlText) {
        this.externalUrlText = externalUrlText;
    }

    public List<Specimen> getSpecimens() {
        return specimens;
    }

    public void setMappedOntology(String mappedOntology) {
        this.mappedOntology = mappedOntology;
    }

    public String getMappedOntology() {
        return mappedOntology;
    }

    public void setSpecimens(List<Specimen> specimens) {
        this.specimens = specimens;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public Set< List<MarkerAssociation> > getMarkerAssociations() {
        return markerAssociations;
    }

    public void setMarkerAssociations(Set< List<MarkerAssociation> > markerAssociations) {
        this.markerAssociations = markerAssociations;
    }

    public Set<MolecularCharacterization>  getMolecularCharacterizations() {

        return molecularCharacterizations;
    }

    public void setMolecularCharacterizations(Set<MolecularCharacterization> molecularCharacterizations) {
        this.molecularCharacterizations = molecularCharacterizations;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPresentPage() {
        return presentPage;
    }

    public void setPresentPage(int presentPage) {
        this.presentPage = presentPage;
    }

    public int getVariationDataCount() {
        return variationDataCount;
    }

    public void setVariationDataCount(int variationDataCount) {
        this.variationDataCount = variationDataCount;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getExternalDataSourceDesc() {
        return externalDataSourceDesc;
    }

    public void setExternalDataSourceDesc(String externalDataSourceDesc) {
        this.externalDataSourceDesc = externalDataSourceDesc;
    }

    public List<QualityAssurance> getQualityAssurances() {
        return qualityAssurances;
    }

    public void setQualityAssurances(List<QualityAssurance> qualityAssurances) {
        this.qualityAssurances = qualityAssurances;
    }

    public String getDrugProtocolUrl() {
        return drugProtocolUrl;
    }

    public void setDrugProtocolUrl(String drugProtocolUrl) {
        this.drugProtocolUrl = drugProtocolUrl;
    }

    public Map<String, String> getPatientTech() {
        return patientTech;
    }

    public void setPatientTech(Map<String, String> patientTech) {
        this.patientTech = patientTech;
    }

    public Map<String, Set<String>> getModelTechAndPassages() {
        return modelTechAndPassages;
    }

    public void setModelTechAndPassages(Map<String, Set<String>> modelTechAndPassages) {
        this.modelTechAndPassages = modelTechAndPassages;
    }

    public List<String> getRelatedModels() {
        return relatedModels;
    }

    public void setRelatedModels(List<String> relatedModels) {
        this.relatedModels = relatedModels;
    }

    public List<DrugSummaryDTO> getDrugSummary() {
        return drugSummary;
    }

    public void setDrugSummary(List<DrugSummaryDTO> drugSummary) {
        this.drugSummary = drugSummary;
    }

    public int getDrugSummaryRowNumber() {
        return drugSummaryRowNumber;
    }

    public void setDrugSummaryRowNumber(int drugSummaryRowNumber) {
        this.drugSummaryRowNumber = drugSummaryRowNumber;
    }

    public List<VariationDataDTO> getVariationDataDTOList() {
        return variationDataDTOList;
    }

    public void setVariationDataDTOList(List<VariationDataDTO> variationDataDTOList) {
        this.variationDataDTOList = variationDataDTOList;
    }

    public Map<String, String> getTechNPassToSampleId() {
        return techNPassToSampleId;
    }

    public void setTechNPassToSampleId(Map<String, String> techNPassToSampleId) {
        this.techNPassToSampleId = techNPassToSampleId;
    }

    public Set<String> getAutoSuggestList() {
        return autoSuggestList;
    }

    public void setAutoSuggestList(Set<String> autoSuggestList) {
        this.autoSuggestList = autoSuggestList;
    }

    public Map<String, String> getPlatformsAndUrls() {
        return platformsAndUrls;
    }

    public void setPlatformsAndUrls(Map<String, String> platformsAndUrls) {
        this.platformsAndUrls = platformsAndUrls;
    }

    public void setDataSummary(List<Map> dataSummary) {
        this.dataSummary = dataSummary;
    }

    public List<Map> getDataSummary() {
        return dataSummary;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStageClassification() {
        return stageClassification;
    }

    public void setStageClassification(String stageClassification) {
        this.stageClassification = stageClassification;
    }

    public String getGradeClassification() {
        return gradeClassification;
    }

    public void setGradeClassification(String gradeClassification) {
        this.gradeClassification = gradeClassification;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public PatientDTO getPatient() {
        return patient;
    }
}
