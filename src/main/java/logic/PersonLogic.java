package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Xiaodan Chen
 */
public class PersonLogic extends GenericLogic<Person, PersonDAL> {

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String BIRTH = "birth";
    public static final String ID = "id";

    /**
     * constructor to call super class
     */
    PersonLogic() {
        super(new PersonDAL());
    }

    /**
     *
     * @return findAll
     */
    @Override
    public List<Person> getAll() {
        return get(() -> dal().findAll());
    }

    /**
     *
     * @param id int
     * @return get id from dal
     */
    @Override
    public Person getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    /**
     *
     * @param phone String
     * @return get phone from dal
     */
    public List<Person> getPersonWithPhone(String phone) {
        return get(() -> dal().findByPhone(phone));
    }

    /**
     *
     * @param firstName String
     * @return get first name from dal
     */
    public List<Person> getPersonWithFirstName(String firstName) {
        return get(() -> dal().findByFirstName(firstName));
    }

    /**
     *
     * @param lastName String
     * @return get lastName from dal
     */
    public List<Person> getPersonWithLastName(String lastName) {
        return get(() -> dal().findByLastName(lastName));
    }

    /**
     *
     * @param address String
     * @return get address from dal
     */
    public List<Person> getPersonWithAddress(String address) {
        return get(() -> dal().findByAddress(address));
    }

    /**
     *
     * @param birth Date
     * @return get birth from dal
     */
    public List<Person> getPersonWithBirth(Date birth) {
        return get(() -> dal().findByBirth(birth));
    }

    /**
     *
     * @param search String
     * @return get search from dal
     */
    @Override
    public List<Person> search(String search) {
        return get(() -> dal().findContaining(search));
    }

    /**
     *
     * @return array list
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "First_Name", "Last_Name", "Phone", "Address", "Birth");
    }

    /**
     *
     * @return array list
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTH);
    }

    /**
     *
     * @param e Person
     * @return array list
     */
    @Override
    public List<?> extractDataAsList(Person e) {
        return Arrays.asList(e.getId(), e.getFirstName(), e.getLastName(), e.getPhone(), e.getAddress(), convertDateToString(e.getBirth()));
    }

    /**
     *
     * @param parameterMap Map
     * @return entity
     */
    @Override
    public Person createEntity(Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            throw new NullPointerException("parameterMap cannot be null");
        }
        Person entity = new Person();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };

        String firstName = parameterMap.get(FIRST_NAME)[0];
        String lastName = parameterMap.get(LAST_NAME)[0];
        String phone = parameterMap.get(PHONE)[0];
        String address = parameterMap.get(ADDRESS)[0];
        String birth = parameterMap.get(BIRTH)[0];

        birth = birth.replaceAll("T", " ");

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
