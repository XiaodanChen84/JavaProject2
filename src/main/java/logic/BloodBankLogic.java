package logic;

import common.ValidationException;
import dal.BloodBankDAL;
import entity.BloodBank;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Jing Zhao
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL> {

    public static final String OWNER_ID = "owner_id";
    public static final String PRIVATELY_OWNED = "privately_owned";
    public static final String ESTABLISHED = "established";
    public static final String NAME = "name";
    public static final String EMPLOYEE_COUNT = "employee_count";
    public static final String ID = "id";

    /**
     * constructor to call super class
     */
    BloodBankLogic() {
        super(new BloodBankDAL());
    }

    /**
     *
     * @return findAll from dal
     */
    @Override
    public List<BloodBank> getAll() {
        return get(() -> dal().findAll());
    }

    /**
     *
     * @param id int
     * @return get id from dal
     */
    @Override
    public BloodBank getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    /**
     *
     * @param name String
     * @return get name from dal
     */
    public BloodBank getBloodBankWithName(String name) {
        return get(() -> dal().findByName(name));
    }

    /**
     *
     * @param privatelyOwned boolean
     * @return get privatelyOwned from dal
     */
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned) {
        return get(() -> dal().findByPrivatelyOwned(privatelyOwned));
    }

    /**
     *
     * @param established Date
     * @return get established from dal
     */
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get(() -> dal().findByEstablished(established));
    }

    /**
     *
     * @param ownerId int
     * @return get ownerId from dal
     */
    public BloodBank getBloodBanksWithOwner(int ownerId) {
        return get(() -> dal().findByOwner(ownerId));
    }

    /**
     *
     * @param count int
     * @return get count from dal
     */
    public List<BloodBank> getBloodBanksWithEmplyeeCount(int count) {
        return get(() -> dal().findByEmplyeeCount(count));
    }

    /**
     *
     * @param search String
     * @return get search from dal
     */
    @Override
    public List<BloodBank> search(String search) {
        return get(() -> dal().findContaining(search));
    }

    /**
     *
     * @param parameterMap Map
     * @return entity
     */
    @Override
    public BloodBank createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap can not be null");
        BloodBank entity = new BloodBank();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        ObjIntConsumer<String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + ">" + length;
                }
                throw new ValidationException(error);
            }
        };
        String displayName = null;
        if (parameterMap.containsKey(NAME)) {
            displayName = parameterMap.get(NAME)[0];
            validator.accept(displayName, 100);
        }

        String privatelyOwned = parameterMap.get(PRIVATELY_OWNED)[0];
        String established = parameterMap.get(ESTABLISHED)[0];
        established = established.replaceAll("T", " ");
        String employeeCount = parameterMap.get(EMPLOYEE_COUNT)[0];

        entity.setName(displayName);

        entity.setEstablished(convertStringToDate(established));

        entity.setEmplyeeCount(Integer.parseInt(employeeCount));

        entity.setPrivatelyOwned(Boolean.parseBoolean(privatelyOwned));

        return entity;
    }

    /**
     *
     * @return array list
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "OWNER_ID", "NAME", "PRIVATELY_OWNED", "ESTABLISHED",
                "EMPLOYEE_COUNT");
    }

    /**
     *
     * @return array list
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, OWNER_ID, NAME, PRIVATELY_OWNED, ESTABLISHED,
                EMPLOYEE_COUNT);
    }

    /**
     *
     * @param e List
     * @return array list
     */
    @Override
    public List<?> extractDataAsList(BloodBank e) {

        return Arrays.asList(e.getId(), e.getOwner() == null ? "null" : e.getOwner().getId(),
                e.getName(), e.getPrivatelyOwned(), convertDateToString(e.getEstablished()), e.getEmplyeeCount());

    }

}
