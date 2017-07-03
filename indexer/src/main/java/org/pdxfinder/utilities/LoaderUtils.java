/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pdxfinder.utilities;

import org.pdxfinder.dao.*;
import org.pdxfinder.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * The hope was to put a lot of reused repository actions into one place ie find
 * or create a node or create a node with that requires a number of 'child'
 * nodes that are terms
 *
 * @author sbn
 */
@Component
public class LoaderUtils {

    private TumorTypeRepository tumorTypeRepository;
    private BackgroundStrainRepository backgroundStrainRepository;
    private ImplantationTypeRepository implantationTypeRepository;
    private ImplantationSiteRepository implantationSiteRepository;
    private ExternalDataSourceRepository externalDataSourceRepository;
    private PatientRepository patientRepository;
    private ModelCreationRepository modelCreationRepository;
    private TissueRepository tissueRepository;
    private PatientSnapshotRepository patientSnapshotRepository;
    private SampleRepository sampleRepository;
    private MarkerRepository markerRepository;
    private MarkerAssociationRepository markerAssociationRepository;
    private MolecularCharacterizationRepository molecularCharacterizationRepository;
    private PdxPassageRepository pdxPassageRepository;
    private QualityAssuranceRepository qualityAssuranceRepository;
    private OntologyTermRepository ontologyTermRepository;
    private SpecimenRepository specimenRepository;

    private final static Logger log = LoggerFactory.getLogger(LoaderUtils.class);

    public LoaderUtils(TumorTypeRepository tumorTypeRepository,
                       BackgroundStrainRepository backgroundStrainRepository,
                       ImplantationTypeRepository implantationTypeRepository,
                       ImplantationSiteRepository implantationSiteRepository,
                       ExternalDataSourceRepository externalDataSourceRepository,
                       PatientRepository patientRepository,
                       ModelCreationRepository modelCreationRepository,
                       TissueRepository tissueRepository,
                       PatientSnapshotRepository patientSnapshotRepository,
                       SampleRepository sampleRepository,
                       MarkerRepository markerRepository,
                       MarkerAssociationRepository markerAssociationRepository,
                       MolecularCharacterizationRepository molecularCharacterizationRepository,
                       PdxPassageRepository pdxPassageRepository,
                       QualityAssuranceRepository qualityAssuranceRepository,
                       OntologyTermRepository ontologyTermRepository,
                       SpecimenRepository specimenRepository) {

        Assert.notNull(tumorTypeRepository);
        Assert.notNull(backgroundStrainRepository);
        Assert.notNull(implantationTypeRepository);
        Assert.notNull(implantationSiteRepository);
        Assert.notNull(externalDataSourceRepository);
        Assert.notNull(patientRepository);
        Assert.notNull(modelCreationRepository);
        Assert.notNull(tissueRepository);
        Assert.notNull(patientSnapshotRepository);
        Assert.notNull(sampleRepository);
        Assert.notNull(markerRepository);
        Assert.notNull(markerAssociationRepository);
        Assert.notNull(molecularCharacterizationRepository);

        this.tumorTypeRepository = tumorTypeRepository;
        this.backgroundStrainRepository = backgroundStrainRepository;
        this.implantationTypeRepository = implantationTypeRepository;
        this.implantationSiteRepository = implantationSiteRepository;
        this.externalDataSourceRepository = externalDataSourceRepository;
        this.patientRepository = patientRepository;
        this.modelCreationRepository = modelCreationRepository;
        this.tissueRepository = tissueRepository;
        this.patientSnapshotRepository = patientSnapshotRepository;
        this.sampleRepository = sampleRepository;
        this.markerRepository = markerRepository;
        this.markerAssociationRepository = markerAssociationRepository;
        this.molecularCharacterizationRepository = molecularCharacterizationRepository;
        this.pdxPassageRepository = pdxPassageRepository;
        this.qualityAssuranceRepository = qualityAssuranceRepository;
        this.ontologyTermRepository = ontologyTermRepository;
        this.specimenRepository = specimenRepository;

    }

    public ExternalDataSource getExternalDataSource(String abbr, String name, String description) {
        ExternalDataSource eDS = externalDataSourceRepository.findByAbbreviation(abbr);
        if (eDS == null) {
            log.info("External data source '{}' not found. Creating", abbr);
            eDS = new ExternalDataSource(
                    name,
                    abbr,
                    description,
                    Date.from(Instant.now()));
            externalDataSourceRepository.save(eDS);
        }

        return eDS;

    }

    public ModelCreation createModelCreation(String pdxId, ImplantationSite implantationSite, ImplantationType implantationType, Sample sample, BackgroundStrain backgroundStrain, QualityAssurance qa) {

        ModelCreation modelCreation = modelCreationRepository.findBySourcePdxId(pdxId);
        if (modelCreation != null) {
            log.info("Deleting existing ModelCreation " + pdxId);
            modelCreationRepository.delete(modelCreation);
        }

        modelCreation = new ModelCreation(pdxId, implantationSite, implantationType, sample, backgroundStrain, qa);
        modelCreationRepository.save(modelCreation);
        return modelCreation;
    }

    public ModelCreation createModelCreation(String pdxId, String implantationSiteStr, String implantationTypeStr, Sample sample, BackgroundStrain backgroundStrain, QualityAssurance qa) {

        ImplantationSite implantationSite = this.getImplantationSite(implantationSiteStr);
        ImplantationType implantationType = this.getImplantationType(implantationTypeStr);
        ModelCreation modelCreation = modelCreationRepository.findBySourcePdxId(pdxId);
        if (modelCreation != null) {
            log.info("Deleting existing ModelCreation " + pdxId);
            modelCreationRepository.delete(modelCreation);
        }
        modelCreation = new ModelCreation(pdxId, implantationSite, implantationType, sample, backgroundStrain, qa);
        modelCreationRepository.save(modelCreation);
        return modelCreation;
    }

    public PatientSnapshot getPatientSnapshot(String externalId, String sex, String race, String ethnicity, String age, ExternalDataSource externalDataSource) {

        Patient patient = patientRepository.findByExternalId(externalId);
        PatientSnapshot patientSnapshot = null;

        if (patient == null) {
            log.info("Patient '{}' not found. Creating", externalId);

            patient = this.getPatient(externalId, sex, race, ethnicity, externalDataSource);

            patientSnapshot = new PatientSnapshot(patient, age);
            patientSnapshotRepository.save(patientSnapshot);

        } else {
            patientSnapshot = this.getPatientSnapshot(patient, age);
        }
        return patientSnapshot;
    }

    public PatientSnapshot getPatientSnapshot(Patient patient, String age) {

        PatientSnapshot patientSnapshot = null;

        Set<PatientSnapshot> pSnaps = patientSnapshotRepository.findByPatient(patient);
        loop:
        for (PatientSnapshot ps : pSnaps) {
            if (ps.getAge().equals(age)) {
                patientSnapshot = ps;
                break loop;
            }
        }
        if (patientSnapshot == null) {
            log.info("PatientSnapshot for patient '{}' at age '{}' not found. Creating", patient.getExternalId(), age);
            patientSnapshot = new PatientSnapshot(patient, age);
            patientSnapshotRepository.save(patientSnapshot);
        }

        return patientSnapshot;
    }

    public Patient getPatient(String externalId, String sex, String race, String ethnicity, ExternalDataSource externalDataSource) {

        Patient patient = patientRepository.findByExternalId(externalId);

        if (patient == null) {
            log.info("Patient '{}' not found. Creating", externalId);

            patient = new Patient(externalId, sex, race, ethnicity, externalDataSource);

            patientRepository.save(patient);
        }

        return patient;
    }

    public Sample getSample(String sourceSampleId, String typeStr, String diagnosis, String originStr, String sampleSiteStr, String classification, Boolean normalTissue, ExternalDataSource externalDataSource) {

        TumorType type = this.getTumorType(typeStr);
        Tissue origin = this.getTissue(originStr);
        Tissue sampleSite = this.getTissue(sampleSiteStr);
        Sample sample = sampleRepository.findBySourceSampleId(sourceSampleId);
        if (sample == null) {

            sample = new Sample(sourceSampleId, type, diagnosis, origin, sampleSite, classification, normalTissue, externalDataSource);
            sampleRepository.save(sample);
        }

        return sample;
    }

    public Sample getSampleBySourceSampleId(String sourceSampleId){

        Sample sample = sampleRepository.findBySourceSampleId(sourceSampleId);
        return sample;
    }

    public void saveSample(Sample sample){
        sampleRepository.save(sample);
    }

    public ImplantationSite getImplantationSite(String iSite) {
        ImplantationSite site = implantationSiteRepository.findByName(iSite);
        if (site == null) {
            log.info("Implantation Site '{}' not found. Creating.", iSite);
            site = new ImplantationSite(iSite);
            implantationSiteRepository.save(site);
        }

        return site;
    }

    public ImplantationType getImplantationType(String iType) {
        ImplantationType type = implantationTypeRepository.findByName(iType);
        if (type == null) {
            log.info("Implantation Site '{}' not found. Creating.", iType);
            type = new ImplantationType(iType);
            implantationTypeRepository.save(type);
        }

        return type;
    }

    public Tissue getTissue(String t) {
        Tissue tissue = tissueRepository.findByName(t);
        if (tissue == null) {
            log.info("Tissue '{}' not found. Creating.", t);
            tissue = new Tissue(t);
            tissueRepository.save(tissue);
        }

        return tissue;
    }

    public TumorType getTumorType(String name) {
        TumorType tumorType = tumorTypeRepository.findByName(name);
        if (tumorType == null) {
            log.info("TumorType '{}' not found. Creating.", name);
            tumorType = new TumorType(name);
            tumorTypeRepository.save(tumorType);
        }

        return tumorType;
    }

    public BackgroundStrain getBackgroundStrain(String symbol, String name, String description, String url) {
        BackgroundStrain bgStrain = backgroundStrainRepository.findByName(name);
        if (bgStrain == null) {
            log.info("Background Strain '{}' not found. Creating", name);
            bgStrain = new BackgroundStrain(symbol, name, description, url);
            backgroundStrainRepository.save(bgStrain);
        }
        return bgStrain;
    }

    // is this bad? ... probably..
    public Marker getMarker(String symbol) {
        return this.getMarker(symbol, symbol);
    }

    public Marker getMarker(String symbol, String name) {

        Marker marker = markerRepository.findByName(name);
        if (marker == null && symbol != null) {
            marker = markerRepository.findBySymbol(symbol);
        }
        if (marker == null) {
            log.info("Marker '{}' not found. Creating", name);
            marker = new Marker(symbol, name);
            marker = markerRepository.save(marker);
        }
        return marker;
    }

    public MarkerAssociation getMarkerAssociation(String type, String markerSymbol, String markerName) {
        Marker m = this.getMarker(markerSymbol, markerName);
        MarkerAssociation ma = markerAssociationRepository.findByTypeAndMarkerName(type, m.getName());

        if (ma == null && m.getSymbol() != null) {
            ma = markerAssociationRepository.findByTypeAndMarkerSymbol(type, m.getSymbol());
        }

        if (ma == null) {
            ma = new MarkerAssociation(type, m);
            markerAssociationRepository.save(ma);
        }

        return ma;
    }

    // wow this is misleading it doesn't do anyting!
    public void deleteAllByEDSName(String edsName) throws Exception {
        throw new Exception("Nothing deleted. Method not implemented!");
    }

    public void savePatientSnapshot(PatientSnapshot ps) {
        patientSnapshotRepository.save(ps);
    }

    public void saveMolecularCharacterization(MolecularCharacterization mc) {
        molecularCharacterizationRepository.save(mc);
    }

    public void saveQualityAssurance(QualityAssurance qa) {
        if (qa != null) {
            if (null == qualityAssuranceRepository.findFirstByTechnologyAndDescriptionAndValidationTechniques(qa.getTechnology(), qa.getDescription(), qa.getValidationTechniques())) {
                qualityAssuranceRepository.save(qa);
            }
        }
    }
    
    public void savePdxPassage(PdxPassage pdxPassage){
        pdxPassageRepository.save(pdxPassage);
    }
    
    
    
    public Specimen getSpecimen(String id){
        Specimen specimen = specimenRepository.findByExternalId(id);
        if(specimen == null){
            specimen = new Specimen();
            specimen.setExternalId(id);
        }
             
        return specimen;
    }
    
    public void saveSpecimen(Specimen specimen){
        specimenRepository.save(specimen);
    }


    public OntologyTerm getOntologyTerm(String url, String label){

        OntologyTerm ot = ontologyTermRepository.findByUrl(url);

        if(ot == null){
            ot = new OntologyTerm(url, label);
            ontologyTermRepository.save(ot);
        }

        return ot;
    }

    public OntologyTerm getOntologyTerm(String url){

        OntologyTerm ot = ontologyTermRepository.findByUrl(url);
        return ot;
    }

    public OntologyTerm getOntologyTermByLabel(String label){

        OntologyTerm ot = ontologyTermRepository.findByLabel(label);
        return ot;
    }

    public void saveOntologyTerm(OntologyTerm ot){

        ontologyTermRepository.save(ot);
    }


    ////// TODO: Temporarily added these methods to fix load issue.
    //modified savemarker behavior
    public void saveMarker(Marker marker) {
        markerRepository.save(marker);
    }

    public Collection<Marker> getAllMarkers() {
        return Collections.EMPTY_LIST;
    }

}
