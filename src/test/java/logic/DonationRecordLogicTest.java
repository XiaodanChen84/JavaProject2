package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.DonationRecord;
import entity.Person;
import javax.persistence.EntityManager;

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
        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();        
    }
    
    
    
}
