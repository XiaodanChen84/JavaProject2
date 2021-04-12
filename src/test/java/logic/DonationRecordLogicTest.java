package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.annotations.common.util.impl.Log;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gabriel Matte
 */
public class DonationRecordLogicTest {
    
    private DonationRecordLogic drLogic;
    private DonationRecord expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setup() throws Exception {
        
        // Create instances of logicfactory and entity manager. Begin transaction on entity manager
        // This is done only for test purposes. 
        drLogic = LogicFactory.getFor("DonationRecord");
        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();
        
        // Create Person and BloodDonation entities for the purposes of testing only
        // instance of BloodBank may also be required if BloodDonation dependency does not already exist in DB
        
        // Check if the Person dependency exists on DB already
        Person person = em.find(Person.class, 1);
        // If result is null, create the entity and persist it
        if (person == null) {
            person = new Person();
            person.setFirstName("first");
            person.setLastName("last");
            person.setPhone("9991234567");
            person.setAddress("123 somewhere");
            person.setBirth(drLogic.convertStringToDate("2000-01-01 12:00:00"));
            // Persist the dependency
            em.persist(person);
        }
        
        BloodBank bb = em.find(BloodBank.class, 1);
        // If result is null, create the entity and persist it
        if (bb == null) {
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(drLogic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            // Persist the dependency
            em.persist(bb);
            
            System.out.println(bb.toString());
        }
        
        // Check if the BloodDonation dependency exists on DB already
        BloodDonation bd = em.find(BloodDonation.class, 1);
        // If result is null, create the entity and persist it
        if (bd == null) {
            bd = new BloodDonation();
            bd.setBloodBank(bb);
            bd.setMilliliters(100);
            bd.setBloodGroup(BloodGroup.AB);
            bd.setRhd(RhesusFactor.Negative);
            bd.setCreated(drLogic.convertStringToDate("1111-11-11 11:11:11"));
            // Persist the dependency
            em.persist(bd);
        }
        
        // Finally, create the DonationRecord Entity to be tested        
        DonationRecord entity = new DonationRecord();
        // Set dependencies
        entity.setPerson(person);
        entity.setBloodDonation(bd);
        // Set remaining parameters
        entity.setTested(true);
        entity.setAdministrator("testadmin");
        entity.setHospital("testhospital");
        entity.setCreated(drLogic.convertStringToDate("1111-11-11 11:11:11"));
        
        // Add entity to Hibernate
        // Merge so that entity remains available for testing
        expectedEntity = em.merge(entity);
        // Commit changes and close EntityManager
        em.getTransaction().commit();
        em.close();
       
    }
    
    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            drLogic.delete(expectedEntity);
        }
    }
    
    // Tests the getAll() method - Should return a list with all DonationRecords in the DB
    @Test
    public void testGetAll() {
        List<DonationRecord> list = drLogic.getAll();
        // Get initial size of the list of DonationRecords
        int size = list.size();
        // Check that the entity was actually created
        assertNotNull(expectedEntity);
        // Remove the expected entity
        drLogic.delete(expectedEntity);
        list = drLogic.getAll();
        // Size of DonationRecords list should be one smaller after removing expected entity
        assertEquals(size -1, list.size());
    }
    
    @Test
    public void testGetWithId() {
        DonationRecord record = drLogic.getWithId(expectedEntity.getId());
        assertDonationRecordEquals(expectedEntity, record);
    }
    
    @Test 
    final void testGetColumnNames() {
        List<String> list = drLogic.getColumnNames();
        assertEquals(Arrays.asList("Record ID", "Person ID", "Donation ID", "Tested", "Administrator", "Hospital", "Created"), list);
    }
    
    @Test
    final void testGetColumnCodes() {
        List<String> list = drLogic.getColumnCodes();
        assertEquals(Arrays.asList(DonationRecordLogic.ID, DonationRecordLogic.PERSON_ID, DonationRecordLogic.DONATION_ID, 
                DonationRecordLogic.TESTED, DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED), list);
    }
    
    @Test
    final void testExtractDataAsList() {
        List<?> list = drLogic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getPerson().getId(), list.get(1));
        assertEquals(expectedEntity.getBloodDonation().getId(), list.get(2));
        assertEquals(expectedEntity.getTested(), list.get(3));
        assertEquals(expectedEntity.getAdministrator(), list.get(4));
        assertEquals(expectedEntity.getHospital(), list.get(5));
        assertEquals(expectedEntity.getCreated(), list.get(6));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Helper method to determin equality between expected entity and actual entity
    private void assertDonationRecordEquals(DonationRecord expected, DonationRecord actual) {
        assertEquals( expected.getId(), actual.getId());
        assertEquals( expected.getBloodDonation(), actual.getBloodDonation());
        assertEquals( expected.getTested(), actual.getTested());
        assertEquals( expected.getAdministrator(), actual.getAdministrator());
        assertEquals( expected.getHospital(), actual.getHospital());
        assertEquals( expected.getCreated(), actual.getCreated());
    }
}
