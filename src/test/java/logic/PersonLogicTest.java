package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.Person;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author xiaod
 */
class PersonLogicTest {
    private PersonLogic logic;
    private Person expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setUp() throws Exception {

        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();

        Person entity = new Person();
        entity.setFirstName( "Xiaodan" );
        entity.setLastName( "Chen" );
        entity.setPhone( "123456789" );
        entity.setAddress( "Ottawa" );
        entity.setBirth(logic.convertStringToDate("2020-03-20 03:20:00"));

        expectedEntity = em.merge( entity );
        em.getTransaction().commit();
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }
    
    @Test
    final void testGetAll() {
        List<Person> list = logic.getAll();
        int originalSize = list.size();

        assertNotNull( expectedEntity );
        logic.delete( expectedEntity );

        list = logic.getAll();
        assertEquals( originalSize - 1, list.size() );
    }

    /**
     * helper method for testing all account fields
     *
     * @param expected
     * @param actual
     */
    private void assertPersonEquals( Person expected, Person actual ) {
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getFirstName(), actual.getFirstName());
        assertEquals( expected.getLastName(), actual.getLastName());
        assertEquals( expected.getPhone(), actual.getPhone() );
        assertEquals( expected.getAddress(), actual.getAddress());
        assertEquals( expected.getBirth(), actual.getBirth());
    }
    
    @Test
    final void testGetWithId() {
        Person returnedPerson = logic.getWithId( expectedEntity.getId() );
        assertPersonEquals( expectedEntity, returnedPerson );
    }
//
//    @Test
//    final void testGetPersonWithFirstName() {
//        List<Person> returnedPerson = logic.getPersonWithFirstName( expectedEntity.getFirstName() );
//        for (Person person : returnedPerson) {
//            assertEquals(expectedEntity.getFirstName(), person.getFirstName());
//        }
//    }
//
//    @Test
//    final void testGetPersonWithLastName() {
//        List<Person> returnedPerson = logic.getPersonWithLastName( expectedEntity.getLastName() );
//        for (Person person : returnedPerson) {
//            assertEquals(expectedEntity.getLastName(), person.getLastName());
//        }
//    }

}
