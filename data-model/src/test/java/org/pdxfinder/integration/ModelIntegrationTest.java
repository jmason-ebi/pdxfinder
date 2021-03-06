package org.pdxfinder.integration;

import org.junit.Before;
import org.junit.Test;
import org.pdxfinder.BaseTest;
import org.pdxfinder.dao.*;
import org.pdxfinder.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;

/**
 * Test the integration of tumor to patient
 */
public class ModelIntegrationTest extends BaseTest {

    private final static Logger log = LoggerFactory.getLogger(ModelIntegrationTest.class);
    private String tumorTypeName = "TEST_TUMORTYPE";
    private String extDsName = "TEST_SOURCE";
    private String extDsAbbrev = "TS";

    private String extDsNameAlternate = "ALTERNATE_TEST_SOURCE";
    private String extDsNameAlternateAbbrev = "ATS";

    private String tissueName = "TEST_TISSUE";
    private String markerSymbol = "TEST_MARKER";
    private String molChar = "TEST_MOLCHAR";
    private String modelCreationId = "TEST_MODEL";

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private TissueRepository tissueRepository;

    @Autowired
    private TumorTypeRepository tumorTypeRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientSnapshotRepository patientsnapshotRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MolecularCharacterizationRepository mcRepository;

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private MarkerAssociationRepository markerAssociationRepository;

    @Autowired
    private ModelCreationRepository modelCreationRepository;

    @Autowired
    private SpecimenRepository specimenRepository;


    @Before
    public void setupDb() {

        sampleRepository.deleteAll();
        patientRepository.deleteAll();
        groupRepository.deleteAll();
        tissueRepository.deleteAll();
        mcRepository.deleteAll();
        markerRepository.deleteAll();
        markerAssociationRepository.deleteAll();
        modelCreationRepository.deleteAll();

        MolecularCharacterization mc = mcRepository.findByTechnology(molChar);
        if (mc == null) {
            log.debug("Molecular characterization {} not found. Creating", molChar);
            mc = new MolecularCharacterization(molChar);
            mcRepository.save(mc);
        }

        MolecularCharacterization mcPassage = mcRepository.findByTechnology(molChar + "_PASSAGE");
        if (mcPassage == null) {
            log.debug("Molecular characterization {} not found. Creating", molChar);
            mcPassage = new MolecularCharacterization(molChar + "_PASSAGE");
            mcRepository.save(mcPassage);
        }

        Marker marker = markerRepository.findBySymbol(markerSymbol);
        if (marker == null) {
            log.debug("Marker {} not found. Creating", markerSymbol);
            marker = new Marker(markerSymbol, markerSymbol);
            markerRepository.save(marker);
        }

        Group ds = groupRepository.findByNameAndType(extDsName, "Provider");
        if(ds == null){
            log.info("Group not found. Creating", extDsName);

            ds = new Group(extDsName, extDsAbbrev, "Provider");
            groupRepository.save(ds);

        }


        Group dsAlt = groupRepository.findByNameAndType(extDsNameAlternate, "Provider");
        if (dsAlt == null) {
            log.debug("Group {} not found. Creating", extDsNameAlternate);
            dsAlt = new Group(extDsNameAlternate, extDsNameAlternateAbbrev, "Provider");
            groupRepository.save(dsAlt);
        }

        TumorType tumorType = tumorTypeRepository.findByName(tumorTypeName);
        if (tumorType == null) {
            log.debug("Sample type {} not found. Creating", tumorTypeName);
            tumorType = new TumorType(tumorTypeName);
            tumorTypeRepository.save(tumorType);
        }

        Tissue tissue = tissueRepository.findByName(tissueName);
        if (tissue == null) {
            log.debug("Tissue {} not found. Creating", extDsName);
            tissue = new Tissue(tissueName);
            tissueRepository.save(tissue);
        }
    }

    @Test
    public void persistModel() throws Exception {

        TumorType tumorType = tumorTypeRepository.findByName(tumorTypeName);
        Tissue tissue = tissueRepository.findByName(tissueName);
        Group group = groupRepository.findByNameAndType(extDsName, "Provider");
        MolecularCharacterization mc = mcRepository.findByTechnology(molChar);
        MolecularCharacterization mcPassage = mcRepository.findByTechnology(molChar + "_PASSAGE");
        Marker marker = markerRepository.findBySymbol(markerSymbol);

        MarkerAssociation ma = new MarkerAssociation();
        ma.setMarker(marker);
        mc.setMarkerAssociations(Collections.singletonList(ma));

        Sample sample = new Sample("sample-1", tumorType, "TEST_DIAGNOSIS", tissue, tissue, "Surgical Resection", "TEST_CLASSIFICATION", false, group.getAbbreviation());
        sampleRepository.save(sample);

        Patient patient = new Patient("patient_id_1", "F", null, null, group);
        PatientSnapshot ps = new PatientSnapshot(patient, "67");
        patient.hasSnapshot(ps);

        Sample s = new Sample("test", tumorType, "adinocarcinoma", tissue, null, "Surgical Resection", "F", false, group.getAbbreviation());
        s.setMolecularCharacterizations(new HashSet<>(Collections.singletonList(mc)));

        Sample specimenSample = new Sample("specimenSampleTest", tumorType, "adinocarcinoma", tissue, null, "", "", false, group.getAbbreviation());
        specimenSample.setMolecularCharacterizations(new HashSet<>(Collections.singletonList(mcPassage)));

        ps.setSamples(new HashSet<>(Collections.singletonList(s)));

        patientRepository.save(patient);

        Group groupAlternate = groupRepository.findByNameAndType(extDsNameAlternate, "Provider");


        /*
        ModelCreation modelCreation = new ModelCreation(
                modelCreationId,
                new EngraftmentSite(tissueName),
                new EngraftmentType("subcutis"),
                sample,
                new HostStrain("TEST_STRAIN"),
                new QualityAssurance("test", "Test description", ValidationTechniques.VALIDATION));

        PdxPassage pdxPassage = new PdxPassage(modelCreation, 0);
        PdxPassage pdxPassage1 = new PdxPassage(pdxPassage, 1);
        pdxPassage1.setPdxPassage(pdxPassage);

        Specimen specimen = new Specimen("TEST_SPECIMEN", null, specimenSample);
        specimen.setPdxPassage(pdxPassage1);


        specimenRepository.save(specimen);

        // Assert the nodes have been persisted in the correct graph
        Specimen found = specimenRepository.findByExternalIdAndDS("TEST_SPECIMEN");
        assert found.getPdxPassage().getPassage() == 1;
        assert found.getPdxPassage().getPdxPassage().getModelCreation().getSample().getSampleSite() == tissue;

*/
    }


}
