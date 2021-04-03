package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;
import static logic.AccountLogic.NAME;

/**
 *
 * @author xiaod
 */
public class PersonLogic extends GenericLogic<Person, PersonDAL> {

    public static final String FIRST_NAME = "first_naame";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String BIRTH = "birth";
    public static final String ID = "id";
    
    PersonLogic() {
        super( new PersonDAL() );
    }
    
    @Override
    public List<Person> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public Person getWithId(int id) {
        return get( () -> dal().findById( id ) );
    }
    
    public List<Person> getPersonWithPhone(String phone) {
        return get( () -> dal().findByPhone(phone) );
    }
    
    public List<Person> getPersonWithFirstName(String firstName) {
        return get( () -> dal().findByFirstName(firstName) );
    }
    
    public List<Person> getPersonWithLastName(String lastName) {
        return get( () -> dal().findByLastName(lastName) );
    }
        
    public List<Person> getPersonWithAddress(String address) {
        return get( () -> dal().findByAddress(address) );
    }
            
    public List<Person> getPersonWithBirth(Date birth) {
        return get( () -> dal().findByBirth(birth) );
    }
    
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "first_name", "last_lame", "phone", "address", "birth", "id" );
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTH, ID );
    }

    @Override
    public List<?> extractDataAsList(Person e) {
        return Arrays.asList( e.getFirstName(), e.getLastName(), e.getPhone(), e.getAddress(), e.getBirth(), e.getId() );
    }

    @Override
    public Person createEntity(Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            throw new NullPointerException("parameterMap cannot be null");
        }
        Person entity = new Person();
        
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }
        ObjIntConsumer< String> validator = ( value, length ) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };
        String displayname = null;
        if(parameterMap.containsKey(NAME)){
            displayname = parameterMap.get(FIRST_NAME)[0] + parameterMap.get(LAST_NAME)[0];
            validator.accept(displayname, 100);
        }
      
        String firstName = parameterMap.get(FIRST_NAME)[0]; 
        String lastName = parameterMap.get(LAST_NAME)[0];
        String phone = parameterMap.get(PHONE)[0];
        String address = parameterMap.get(ADDRESS)[0];
        String birth = parameterMap.get(BIRTH)[0];
        
        validator.accept(firstName, 50);
        validator.accept(lastName, 50);
        validator.accept(phone, 15);
        validator.accept(address, 100);
        
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setPhone(phone);
        entity.setAddress(address);
        entity.setBirth(convertStringToDate(birth));

     
        return entity;
    }



    
}
