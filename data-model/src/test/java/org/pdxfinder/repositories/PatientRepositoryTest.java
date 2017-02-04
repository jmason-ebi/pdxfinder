package org.pdxfinder.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdxfinder.TestConfig;
import org.pdxfinder.dao.ExternalDataSource;
import org.pdxfinder.dao.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;

/**
 * Tests for the Patient data repository
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(locations = {"classpath:ogm.properties"})
@Transactional
@SpringBootTest
public class PatientRepositoryTest {

    private final static Logger log = LoggerFactory.getLogger(PatientRepositoryTest.class);
    private String extDsName = "TEST_SOURCE";

    @Autowired
    private ExternalDataSourceRepository externalDataSourceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Before
    public void setupDb() {

        patientRepository.deleteAll();
        externalDataSourceRepository.deleteAll();

        ExternalDataSource ds = externalDataSourceRepository.findByName(extDsName);
        if (ds == null) {
            log.info("External data source ", extDsName, "not found. Creating");
            ds = new ExternalDataSource(extDsName, extDsName, extDsName, Date.from(Instant.now()));
            externalDataSourceRepository.save(ds);
        }
    }

    @Test
    public void persistedPatientShouldBeRetrievableFromGraphDb() throws Exception {

        ExternalDataSource externalDataSource = externalDataSourceRepository.findByAbbreviation(extDsName);

        Patient femalePatient = new Patient("-9999", "F", "65", null, null, externalDataSource);
        patientRepository.save(femalePatient);

        Patient foundFemalePatient = patientRepository.findBySexAndAge("F", "65").iterator().next();
        assert (foundFemalePatient != null);
        assert (foundFemalePatient.getSex().equals("F"));
        assert (foundFemalePatient.getAge().equals("65"));

        log.info(foundFemalePatient.toString());

        patientRepository.delete(patientRepository.findByExternalId("-9999"));
        Patient notFoundFemalePatient = patientRepository.findByExternalId("-9999");
        assert (notFoundFemalePatient == null);


    }

}