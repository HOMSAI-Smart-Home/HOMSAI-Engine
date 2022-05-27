package app.homsai.engine.common.domain.specifications;

/**
 * Created by Giacomo Agostini on 20/01/2017.
 */

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private String concat;

    public SearchCriteria(String key, String operation, Object value, String concat) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.concat = concat;
    }

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.concat = BaseEntitySpecificationsBuilder.andDivider;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat;
    }
}
