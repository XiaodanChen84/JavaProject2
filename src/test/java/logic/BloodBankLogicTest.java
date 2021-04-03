package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import dal.DataAccessLayer;
import entity.BloodBank;
import entity.Person;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Admin
 */
public class BloodBankLogicTest {

    private BloodBankLogic logic;
    private BloodBank expectedEntity;

    public BloodBankLogicTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        TomcatStartUp.createTomcat("/simpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    public void setUp() {
        logic = LogicFactory.getFor("BloodBank");
        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();
        BloodBank entity = new BloodBank();

        entity.setOwner(new Person(Integer.parseInt("5")));
        entity.setName("bbank");
        entity.setEstablished(logic.convertStringToDate("2020-03-20 03:20:00"));

        entity.setEmplyeeCount(Integer.parseInt("111"));
       
        entity.setPrivatelyOwned(true);

        expectedEntity = em.merge(entity);
        em.getTransaction().commit();
        em.close();

    }

    @AfterEach
    public void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    @Test
    public void testGetAll() {
        List<BloodBank> list = logic.getAll();
        int size = list.size();
        assertNotNull(expectedEntity);
        logic.delete(expectedEntity);
        list = logic.getAll();
        assertEquals(size - 1, list.size());
    }

    //helper method
    private void assertBloodBankEquals(BloodBank expected, BloodBank actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrivatelyOwned(), actual.getPrivatelyOwned());
        assertEquals(expected.getEstablished(), actual.getEstablished());
        assertEquals(expected.getOwner(), actual.getOwner());
        assertEquals(expected.getEmplyeeCount(), actual.getEmplyeeCount());

    }

    @Test
    public void testGetWithId() {
        BloodBank returnedBloodBank = logic.getWithId(expectedEntity.getId());
        assertBloodBankEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void testGetBloodBankWithName() {
        BloodBank returnedBloodBank = logic.getBloodBankWithName(expectedEntity.getName());
        assertBloodBankEquals(expectedEntity, returnedBloodBank);

    }

    @Test
    final void testGetBloodBankWithPrivatelyOwned() {
        int foundFull = 0;
        boolean privatelyOwnedBoolean = expectedEntity.getPrivatelyOwned();
        List<BloodBank> returnedBloodBanks = logic.getBloodBankWithPrivatelyOwned(privatelyOwnedBoolean);

        for (BloodBank bloodBank : returnedBloodBanks) {
            assertEquals(privatelyOwnedBoolean, bloodBank.getPrivatelyOwned());

            if (bloodBank.getId().equals(expectedEntity.getId())) {
                assertBloodBankEquals(expectedEntity, bloodBank);
                foundFull++;
            }
            assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
        }
    }

    @Test
    final void testGetBloodBankWithEstablished() {
        int foundFull = 0;
        List<BloodBank> returnBloodBanks = logic.getBloodBankWithEstablished(expectedEntity.getEstablished());
        for (BloodBank bloodBank : returnBloodBanks) {
            assertEquals(expectedEntity.getEstablished(), bloodBank.getEstablished());
            if (bloodBank.getId().equals(expectedEntity.getId())) {
                assertBloodBankEquals(expectedEntity, bloodBank);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void testGetBloodBanksWithOwner() {
        BloodBank returnedBank = logic.getBloodBanksWithOwner(expectedEntity.getOwner().getId());
        assertBloodBankEquals(expectedEntity, returnedBank);
    }

    @Test
    final void testGetBloodBanksWithEmplyeeCount() {
        int foundFull = 0;
        List<BloodBank> returnBloodBanks = logic.getBloodBanksWithEmplyeeCount(expectedEntity.getEmplyeeCount());
        for (BloodBank bloodBank : returnBloodBanks) {
            assertEquals(expectedEntity.getEmplyeeCount(), bloodBank.getEmplyeeCount());
            if (bloodBank.getId().equals(expectedEntity.getId())) {
                assertBloodBankEquals(expectedEntity, bloodBank);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testCreateEntityAndAdd(){
         Map<String, String[]> sampleMap = new HashMap<>();
         sampleMap.put(BloodBankLogic.OWNER_ID, new String[]{"3"});
         sampleMap.put(BloodBankLogic.NAME, new String[]{"junit"});
         sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{"test5"});
         sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{"true"});
         sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{"test"});
         
         BloodBank returnedBloodBank = logic.createEntity(sampleMap);
         logic.add(returnedBloodBank);
         
         returnedBloodBank = logic.getWithId(returnedBloodBank.getId());
         assertEquals(sampleMap.get(BloodBankLogic.OWNER_ID)[0],returnedBloodBank.getOwner());
         assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
         assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], returnedBloodBank.getPrivatelyOwned());
         assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0],returnedBloodBank.getEstablished());
         assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], returnedBloodBank.getEmplyeeCount());
         
         logic.delete( returnedBloodBank);
    }
    
    @Test
    final void testCreateEntity(){

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.ID, new String[]{ Integer.toString(expectedEntity.getId())});
        sampleMap.put(BloodBankLogic.OWNER_ID, new String[]{ Integer.toString(expectedEntity.getOwner().getId())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{String.valueOf(expectedEntity.getPrivatelyOwned())});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{String.valueOf(expectedEntity.getEmplyeeCount())});
        
        BloodBank returnBloodBank = logic.createEntity(sampleMap);
        
        assertBloodBankEquals(expectedEntity, returnBloodBank);
        
    }
    
    @Test
    final void testCreateEntityNullAndEmptyValues(){
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String,String[]>> fillMap = (Map<String, String[]>map)->{
            map.clear();
            map.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodBankLogic.NAME, new String[]{ expectedEntity.getName() } );
            map.put(BloodBankLogic.OWNER_ID, new String[]{Integer.toString(expectedEntity.getOwner().getId()) } );
            map.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{ String.valueOf(expectedEntity.getPrivatelyOwned())} );
            map.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{ String.valueOf(expectedEntity.getEmplyeeCount())} );
            map.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
          };
        
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ID, null );
        assertThrows(NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.ID, new String[]{} );
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.OWNER_ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.OWNER_ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
                
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ESTABLISHED, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.ESTABLISHED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
                
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );     
        
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        }
    
    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put(BloodBankLogic.NAME, new String[]{ expectedEntity.getName() } );
           };
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };
        
        fillMap.accept( sampleMap );
        sampleMap.replace(BloodBankLogic.NAME, new String[]{""});
        assertThrows(ValidationException.class,()->logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.NAME, new String[]{ generateString.apply(100)});
        assertThrows(ValidationException.class, ()->logic.createEntity(sampleMap));
    }      
            
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> { 
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                .toString();
        };
       
        Map<String, String[]> sampleMap = new HashMap<>();
         sampleMap.put(BloodBankLogic.ID, new String[]{Integer.toString(1)});
         sampleMap.put(BloodBankLogic.OWNER_ID, new String[]{generateString.apply(1)});
         sampleMap.put(BloodBankLogic.NAME, new String[]{generateString.apply(1)});
         sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{generateString.apply(1)});
         sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{generateString.apply(1)});
         sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{generateString.apply(1)});
         
        BloodBank returnedBloodBank = logic.createEntity(sampleMap);
         assertEquals(Integer.parseInt(sampleMap.get(BloodBankLogic.ID)[0]), returnedBloodBank.getId());
         assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
         assertEquals(Integer.parseInt(sampleMap.get(BloodBankLogic.OWNER_ID)[0]), returnedBloodBank.getOwner().getId());
         assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], returnedBloodBank.getEmplyeeCount());
         assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], returnedBloodBank.getPrivatelyOwned());
         assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], returnedBloodBank.getEstablished());
         
        sampleMap = new HashMap<>();
        sampleMap.put( BloodBankLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( BloodBankLogic.NAME, new String[]{ generateString.apply( 100 ) } );
        sampleMap.put( BloodBankLogic.OWNER_ID, new String[]{Integer.toString( 1 ) } );
        sampleMap.put( BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString( 1 ) } );
        sampleMap.put( BloodBankLogic.PRIVATELY_OWNED, new String[]{Integer.toString( 1 ) } );
        sampleMap.put( BloodBankLogic.ESTABLISHED,new String[]{Integer.toString( 1 ) } );
        
        returnedBloodBank = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(BloodBankLogic.ID)[0]), returnedBloodBank.getId());
        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
        assertEquals(Integer.parseInt(sampleMap.get(BloodBankLogic.OWNER_ID)[0]), returnedBloodBank.getOwner().getId());
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], returnedBloodBank.getEmplyeeCount());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], returnedBloodBank.getPrivatelyOwned());
        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], returnedBloodBank.getEstablished());
    }
     
    @Test
    final void testGetColumnNames(){
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("bank_id", "owner", "name", "privately_owned", "established", "emplyee_count"), list);
    }
    
    @Test
    final void testGetColumnCodes(){
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodBankLogic.ID, BloodBankLogic.OWNER_ID, BloodBankLogic.NAME,
                BloodBankLogic.PRIVATELY_OWNED,BloodBankLogic.ESTABLISHED, BloodBankLogic.EMPLOYEE_COUNT), list);
    }
    
    @Test
    final void testExtractDataAsList(){
        List<?> list = logic.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getName(), list.get( 1 ) );
        assertEquals( expectedEntity.getOwner().getId(), list.get( 2 ) );
        assertEquals( expectedEntity.getPrivatelyOwned(), list.get( 3 ) );
        assertEquals( expectedEntity.getEstablished(), list.get( 4 ) );
        assertEquals( expectedEntity.getEmplyeeCount(), list.get( 5 ) );
    }
    
}
